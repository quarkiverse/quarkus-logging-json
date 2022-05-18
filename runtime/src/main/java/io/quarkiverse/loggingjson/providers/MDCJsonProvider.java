package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class MDCJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.MDCConfig config;

    public MDCJsonProvider(Config.MDCConfig config) {
        this(config, "mdc");
    }

    public MDCJsonProvider(Config.MDCConfig config, String defaultName) {
        this.config = config;
        if (config.flatFields) {
            this.fieldName = null;
        } else {
            this.fieldName = config.fieldName.orElse(defaultName);
        }
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeMapStringFields(generator, fieldName, event.getMdcCopy());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
