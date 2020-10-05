package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class ProcessNameJsonProvider implements JsonProvider {

    public static final String FIELD_PROCESS_NAME = "processName";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (JsonWritingUtils.isNotNullOrEmpty(event.getProcessName())) {
            JsonWritingUtils.writeStringField(generator, FIELD_PROCESS_NAME, event.getProcessName());
        }
    }
}
