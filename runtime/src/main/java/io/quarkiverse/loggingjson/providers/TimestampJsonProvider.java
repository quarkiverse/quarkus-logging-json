package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class TimestampJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final DateTimeFormatter dateTimeFormatter;
    private final Config.TimestampField config;

    public TimestampJsonProvider(Config.TimestampField config) {
        this.config = config;
        fieldName = config.fieldName.orElse("timestamp");

        ZoneId zoneId;
        if (config.zoneId == null || "default".equals(config.zoneId)) {
            zoneId = ZoneId.systemDefault();
        } else {
            zoneId = ZoneId.of(config.zoneId);
        }

        if (config.dateFormat != null && !config.dateFormat.equals("default")) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(config.dateFormat).withZone(zoneId);
        } else {
            dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zoneId);
        }

    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        long millis = event.getMillis();
        JsonWritingUtils.writeStringField(generator, fieldName, dateTimeFormatter.format(Instant.ofEpochMilli(millis)));
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
