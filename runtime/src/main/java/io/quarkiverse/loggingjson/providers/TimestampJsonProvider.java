package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class TimestampJsonProvider implements JsonProvider {

    private final String fieldName;
    private final DateTimeFormatter dateTimeFormatter;

    public TimestampJsonProvider(Config.TimestampField config) {
        fieldName = config.fieldName.orElse("timestamp");
        if (!config.dateFormat.equals("default")) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(config.dateFormat).withZone(ZoneId.systemDefault());
        } else {
            dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());
        }

    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        long millis = event.getMillis();
        JsonWritingUtils.writeStringField(generator, fieldName, dateTimeFormatter.format(Instant.ofEpochMilli(millis)));
    }
}
