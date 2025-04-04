package org.anse.app.parsers;

import org.apache.flink.api.common.functions.MapFunction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public abstract class ParseJson<T> implements MapFunction<String, T> {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract T map(String value) throws Exception;

    public abstract TypeInformation<T> getProducedType();
}



