package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;
import io.quarkiverse.loggingjson.config.Config;

public class LoggerNameJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public LoggerNameJsonProvider(Config.FieldConfig config) {
        this(config, "loggerName");
    }

    public LoggerNameJsonProvider(Config.FieldConfig config, String defaultName) {
        this.fieldName = config.fieldName().orElse(defaultName);
        this.config = config;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, fieldName, event.getLoggerName());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled().orElse(true);
    }
}
