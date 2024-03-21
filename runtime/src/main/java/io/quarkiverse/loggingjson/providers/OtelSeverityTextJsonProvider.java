package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

/**
 * Map as best we can from the java.util.logging levels to the OTel severity number
 */
public class OtelSeverityTextJsonProvider implements JsonProvider, Enabled {
    private final String fieldName;
    private final Config.FieldConfig config;

    public OtelSeverityTextJsonProvider(Config.FieldConfig config) {
        this(config, "severity_text");
    }

    public OtelSeverityTextJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        String severityText = event.getLevel().toString();
        switch (event.getLevel().toString()) {
            case "SEVERE":
                severityText = "ERROR";
                break;
            case "WARNING":
                severityText = "WARN";
                break;
            case "INFO":
                severityText = "INFO";
                break;
            case "CONFIG":
            case "FINE":
                severityText = "DEBUG";
                break;
            default:
                severityText = "TRACE";
                break;

        }
        JsonWritingUtils.writeStringField(generator, fieldName, severityText);
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
