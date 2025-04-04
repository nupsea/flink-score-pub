package org.anse.app.parsers;

import org.anse.app.schema.DeliveryEvent;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class DeliveryEventParser extends ParseJson<DeliveryEvent> {
    // This class can be used to parse DeliveryEvent JSON strings

    @Override
    public DeliveryEvent map(String value) throws Exception {
        return objectMapper.readValue(value, DeliveryEvent.class);
    }

    @Override
    public TypeInformation<DeliveryEvent> getProducedType() {
        return TypeInformation.of(DeliveryEvent.class);
    }

}