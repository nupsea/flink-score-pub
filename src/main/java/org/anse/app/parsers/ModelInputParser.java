package org.anse.app.parsers;

import org.anse.app.schema.ModelInput;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class ModelInputParser extends ParseJson<ModelInput> {
    // This class can be used to parse ModelInput JSON strings

    @Override
    public ModelInput map(String value) throws Exception {
        return objectMapper.readValue(value, ModelInput.class);
    }

    @Override
    public TypeInformation<ModelInput> getProducedType() {
        return TypeInformation.of(ModelInput.class);
    }

    public String toJson(ModelInput modelInput) throws Exception {
        return objectMapper.writeValueAsString(modelInput);
    }

}