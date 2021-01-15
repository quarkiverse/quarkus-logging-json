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
    private final Boolean flatFields;
    private final Config.MDCConfig config;

    public MDCJsonProvider(Config.MDCConfig config) {
        this.config = config;
        this.fieldName = config.fieldName.orElse("mdc");
        this.flatFields = config.flatFields.orElse(false);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (flatFields) {
            JsonWritingUtils.writeMapEntries(generator, event.getMdcCopy());
        } else {
            JsonWritingUtils.writeMapStringFields(generator, fieldName, event.getMdcCopy());
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
