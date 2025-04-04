package org.anse.app.sink;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;

public class RedisSink extends RichSinkFunction<String> {

    private transient Jedis jedis;
    private transient ObjectMapper objectMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        this.jedis = new Jedis("localhost", 6379);
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public void invoke(String value, Context context) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(value);

        String matchId = jsonNode.get("match_id").asText();
        if (matchId == null) {
            return;
        }

        String key = String.format("model-input-json:%s", matchId);
        jedis.set(key, value);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (this.jedis != null) {
            this.jedis.close();
        }
    }
}
