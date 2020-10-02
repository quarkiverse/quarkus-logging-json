package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

public class LoggerNameJsonProvider implements JsonProvider {

    public static final String FIELD_LOGGER_NAME = "loggerName";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, FIELD_LOGGER_NAME, event.getLoggerName());
    }
}
