package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.io.PrintWriter;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class StackTraceJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public StackTraceJsonProvider(Config.FieldConfig config) {
        this(config, "stackTrace");
    }

    public StackTraceJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
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
