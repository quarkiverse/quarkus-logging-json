package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

public class ProcessNameJsonProvider implements JsonProvider {

    public static final String FIELD_PROCESS_NAME = "processName";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (JsonWritingUtils.isNotNullOrEmpty(event.getProcessName())) {
            JsonWritingUtils.writeStringField(generator, FIELD_PROCESS_NAME, event.getProcessName());
        }
    }
}
