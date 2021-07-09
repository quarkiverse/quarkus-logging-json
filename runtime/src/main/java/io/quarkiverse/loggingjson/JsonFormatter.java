package io.quarkiverse.loggingjson;

import java.util.List;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

public class JsonFormatter extends ExtFormatter {
    private final List<JsonProvider> providers;
    private final JsonFactory jsonFactory;
    private final String recordDelimiter;

    public JsonFormatter(List<JsonProvider> providers, JsonFactory jsonFactory, Config config) {
        this.providers = providers;
        this.jsonFactory = jsonFactory;
        this.recordDelimiter = config.recordDelimiter;
    }

    @Override
    public String format(ExtLogRecord record) {
        try {
            final StringBuilderWriter writer = new StringBuilderWriter();
            try (JsonGenerator generator = this.jsonFactory.createGenerator(writer)) {
                generator.writeStartObject();
                for (JsonProvider provider : this.providers) {
                    provider.writeTo(generator, record);
                }
                generator.writeEndObject();
                generator.flush();
                if (recordDelimiter != null) {
                    writer.write(recordDelimiter);
                }
            }
            return writer.toString();
        } catch (Exception e) {
            // Wrap and rethrow
            throw new RuntimeException(e);
        }
    }
}
