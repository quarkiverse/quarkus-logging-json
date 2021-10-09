package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class ErrorTypeJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public ErrorTypeJsonProvider(Config.FieldConfig config) {
        this(config, "errorType");
    }

    public ErrorTypeJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getThrown() != null) {
            JsonWritingUtils.writeStringField(generator, fieldName, event.getThrown().getClass().getName());
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
