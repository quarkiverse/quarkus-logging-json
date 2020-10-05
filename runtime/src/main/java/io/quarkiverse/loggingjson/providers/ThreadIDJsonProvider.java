package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class ThreadIDJsonProvider implements JsonProvider {

    public static final String FIELD_THREAD_ID = "threadId";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeNumberField(generator, FIELD_THREAD_ID, event.getThreadID());
    }
}
