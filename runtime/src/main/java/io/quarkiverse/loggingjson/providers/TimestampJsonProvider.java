package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class TimestampJsonProvider implements JsonProvider {

    public static final String FIELD_TIMESTAMP = "timestamp";
    private final DateTimeFormatter dateTimeFormatter;

    public TimestampJsonProvider(String dateFormat) {
        if (!dateFormat.equals("default")) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        } else {
            dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());
        }
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        long millis = event.getMillis();
        JsonWritingUtils.writeStringField(generator, FIELD_TIMESTAMP, dateTimeFormatter.format(Instant.ofEpochMilli(millis)));
    }
}
