package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Write Timestamp as Unix Epoch milliseconds.
 */
public class UnixEpochTimestampJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.TimestampField config;

    public UnixEpochTimestampJsonProvider(Config.TimestampField config) {
        this(config, "timestamp");
    }

    public UnixEpochTimestampJsonProvider(Config.TimestampField config, String defaultName) {
        this.config = config;
        fieldName = config.fieldName.orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeNumberField(generator, fieldName, event.getMillis());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
