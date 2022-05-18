package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;

public class WrappedSourceLocation implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;
    private final JsonProvider[] providers;

    public WrappedSourceLocation(Config.FieldConfig config, JsonProvider[] providers) {
        this(config, "error", providers);
    }

    public WrappedSourceLocation(Config.FieldConfig config, String defaultName, JsonProvider[] providers) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
        this.providers = providers;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        generator.writeObjectFieldStart(fieldName);
        for (JsonProvider p : providers) {
            p.writeTo(generator, event);
        }
        generator.writeEndObject();
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }

}
