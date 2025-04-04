package org.anse.app;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestFlinkApp {
    public static void main(String[] args) throws Exception {
        // Parse command-line arguments
        final ParameterTool params = ParameterTool.fromArgs(args);

        // Create the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Set parallelism
        // env.setParallelism(2);

        DataStream<String> textStream = env.socketTextStream("localhost", 9999);
        textStream
            .map(value -> "Processed: " + value)
            .print();


        // Execute program
        env.execute("My Flink Job");
    }
}

