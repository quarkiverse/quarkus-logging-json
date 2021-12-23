package io.quarkiverse.loggingjson.providers.gcp;

import java.io.IOException;
import java.io.PrintWriter;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class GcpMessageWithExceptionJsonProvider extends ExtFormatter implements JsonProvider, Enabled {
    private final String fieldName;
    private final Config.FieldConfig config;

    public GcpMessageWithExceptionJsonProvider(Config.FieldConfig config) {
        this.config = config;
        this.fieldName = config.fieldName.orElse("message");
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        StringBuilderWriter message = new StringBuilderWriter();
        message.write(formatMessage(event));
        if (event.getThrown() != null) {
            message.write("\n");
            event.getThrown().printStackTrace(new PrintWriter(message));
        }
        JsonWritingUtils.writeStringField(generator, fieldName, message.toString());
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
