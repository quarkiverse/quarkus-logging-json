package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class MessageJsonProvider extends ExtFormatter implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public MessageJsonProvider(Config.FieldConfig config) {
        this.config = config;
        this.fieldName = config.fieldName.orElse("message");
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, fieldName, formatMessage(event));
    }

    @Override
    public String format(ExtLogRecord record) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
