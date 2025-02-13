package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;

public class MDCJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.MDCConfig config;

    public MDCJsonProvider(Config.MDCConfig config) {
        this.config = config;
        if (config.flatFields()) {
            this.fieldName = null;
        } else {
            this.fieldName = config.fieldName().orElse("mdc");
        }
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeMapStringFields(generator, fieldName, event.getMdcCopy());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled().orElse(true);
    }
}
