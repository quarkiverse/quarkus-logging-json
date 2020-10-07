package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;
import java.io.PrintWriter;

public class StackTraceJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public StackTraceJsonProvider(Config.FieldConfig config) {
        this.config = config;
        this.fieldName = config.fieldName.orElse("stackTrace");
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getThrown() != null) {
            final StringBuilderWriter w = new StringBuilderWriter();
            event.getThrown().printStackTrace(new PrintWriter(w));
            JsonWritingUtils.writeStringField(generator, fieldName, w.toString());
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
