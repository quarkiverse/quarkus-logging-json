package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class ProcessIdJsonProvider implements JsonProvider {

    public static final String FIELD_PROCESS_ID = "processId";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getProcessId() >= 0) {
            JsonWritingUtils.writeNumberField(generator, FIELD_PROCESS_ID, event.getProcessId());
        }
    }
}
