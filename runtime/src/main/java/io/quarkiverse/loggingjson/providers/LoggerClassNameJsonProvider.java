package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class LoggerClassNameJsonProvider implements JsonProvider {

    public static final String FIELD_LOGGER_NAME = "loggerClassName";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, FIELD_LOGGER_NAME, event.getLoggerClassName());
    }
}
