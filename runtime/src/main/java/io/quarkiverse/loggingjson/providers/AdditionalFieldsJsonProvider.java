package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.util.Map;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.config.Config;

public class AdditionalFieldsJsonProvider implements JsonProvider, Enabled {
    private final boolean enabled;
    private final Map<String, Config.AdditionalFieldConfig> fields;

    public AdditionalFieldsJsonProvider(Map<String, Config.AdditionalFieldConfig> fields) {
        this.enabled = fields != null && !fields.isEmpty();
        this.fields = fields;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        for (Map.Entry<String, Config.AdditionalFieldConfig> entry : fields.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldValue = entry.getValue().value;
            switch (entry.getValue().type) {
                case STRING:
                    generator.writeStringField(fieldName, fieldValue);
                    break;
                case INT:
                    generator.writeNumberField(fieldName, Integer.parseInt(fieldValue));
                    break;
                case LONG:
                    generator.writeNumberField(fieldName, Long.parseLong(fieldValue));
                    break;
                case FLOAT:
                    generator.writeNumberField(fieldName, Float.parseFloat(fieldValue));
                    break;
                case DOUBLE:
                    generator.writeNumberField(fieldName, Double.parseDouble(fieldValue));
                    break;
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
