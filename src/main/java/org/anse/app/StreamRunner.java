package org.anse.app;

import org.anse.app.parsers.DeliveryEventParser;
import org.anse.app.parsers.ModelInputParser;
import org.anse.app.process.ComputeChaseState;
import org.anse.app.process.ComputeFirstInnings;
import org.anse.app.schema.DeliveryEvent;
import org.anse.app.schema.ModelInput;
import org.anse.app.sink.RedisSink;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;

import java.time.Duration;
import java.util.Objects;

public class StreamRunner {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setString("execution.checkpointing.interval", "60000");
        config.setString("state.checkpoints.dir", "file:///Users/sethurama/DEV/LM/cric-pred/flink-score-pub/checkpoints/c105");
        config.setString("parallelism.default", "2");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);
        env.configure(config);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("t20-deliveries")
                .setGroupId("t20-deliveries-consumer")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> stream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<DeliveryEvent> parsedStream = stream.map(new DeliveryEventParser())
                .returns(Types.POJO(DeliveryEvent.class))
                .filter(Objects::nonNull);

        KeyedStream<DeliveryEvent, String> firstInningsStream = parsedStream.filter(event -> event.getInning() == 1).keyBy(DeliveryEvent::getMatchId);
        KeyedStream<DeliveryEvent, String> secondInningsStream = parsedStream.filter(event -> event.getInning() == 2).keyBy(DeliveryEvent::getMatchId);

        SingleOutputStreamOperator<Tuple2<String, Integer>> processedFirstInningsStream = firstInningsStream
                .window(TumblingProcessingTimeWindows.of(Duration.ofSeconds(30)))
                .process(new ComputeFirstInnings());
        processedFirstInningsStream.print("First Innings");

        MapStateDescriptor<String, Integer> targetStateDesc = new MapStateDescriptor<>("first_innings_target", Types.STRING, Types.INT);
        BroadcastStream<Tuple2<String, Integer>> broadcastFirstInnings = processedFirstInningsStream.broadcast(targetStateDesc);

        SingleOutputStreamOperator<ModelInput> enrichedStream = secondInningsStream
                .connect(broadcastFirstInnings)
                .process(new ComputeChaseState())
                .filter(modelInput -> modelInput.getBallsRemaining() != 0);

        enrichedStream.print("Enriched Stream");

        DataStream<String> modelInputJsonStream = enrichedStream
                .map(value -> {
                    ModelInputParser parser = new ModelInputParser();
                    return parser.toJson(value);
                });

/*

       KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("t20-model-input")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .build();
        modelInputJsonStream.sinkTo(kafkaSink);

*/

        // Redis Sink
        modelInputJsonStream.addSink(new RedisSink());
        modelInputJsonStream.print("output Stream");

//        String plan = env.getExecutionPlan();
//        System.out.println(plan);

        env.execute("T20 Match Chase");
    }
}
