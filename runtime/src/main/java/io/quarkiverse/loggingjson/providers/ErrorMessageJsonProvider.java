package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;
import io.quarkiverse.loggingjson.config.Config;

public class ErrorMessageJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public ErrorMessageJsonProvider(Config.FieldConfig config) {
        this(config, "errorMessage");
    }

    public ErrorMessageJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName().orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getThrown() != null) {
            JsonWritingUtils.writeStringField(generator, fieldName, event.getThrown().getMessage());
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled().orElse(true);
    }
}
