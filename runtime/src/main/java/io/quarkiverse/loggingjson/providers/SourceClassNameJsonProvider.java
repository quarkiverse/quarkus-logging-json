package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;

public class SourceClassNameJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public SourceClassNameJsonProvider(Config.FieldConfig config) {
        this(config, "sourceClassName");
    }

    public SourceClassNameJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, fieldName, event.getSourceClassName());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(false);
    }
}
