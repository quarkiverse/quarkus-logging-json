package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class MessageJsonProvider extends ExtFormatter implements JsonProvider {

    public static final String FIELD_MESSAGE = "message";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, FIELD_MESSAGE, formatMessage(event));
    }

    @Override
    public String format(ExtLogRecord record) {
        return null;
    }
}
