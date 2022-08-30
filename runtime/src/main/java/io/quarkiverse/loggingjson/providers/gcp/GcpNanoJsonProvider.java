package io.quarkiverse.loggingjson.providers.gcp;

import java.io.IOException;
import java.time.Instant;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class GcpNanoJsonProvider implements JsonProvider, Enabled {

    private final Config.FieldConfig config;

    /**
     * The JSON field name for the seconds of the timestamp.
     */
    public static final String TIMESTAMP_NANOS_ATTRIBUTE = "timestampNanos";
    private final String fieldName;

    public GcpNanoJsonProvider(Config.FieldConfig config) {
        this(config, TIMESTAMP_NANOS_ATTRIBUTE);
    }

    public GcpNanoJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        Instant time = Instant.ofEpochMilli(event.getMillis());
        JsonWritingUtils.writeNumberField(generator, fieldName, time.getNano());
    }
}
