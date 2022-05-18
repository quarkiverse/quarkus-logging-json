package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.io.PrintWriter;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.StringBuilderWriter;

public class MessageWithErrorJsonProvider extends ExtFormatter implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public MessageWithErrorJsonProvider(Config.FieldConfig config) {
        this.config = config;
        this.fieldName = config.fieldName.orElse("message");
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        final StringBuilderWriter w = new StringBuilderWriter();
        w.append(formatMessage(event));
        if (event.getThrown() != null) {
            w.append("\n");
            event.getThrown().printStackTrace(new PrintWriter(w));
        }
        JsonWritingUtils.writeStringField(generator, fieldName, w.toString());
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
