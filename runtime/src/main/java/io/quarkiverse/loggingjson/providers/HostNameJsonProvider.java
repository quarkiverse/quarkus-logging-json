package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;

public class HostNameJsonProvider implements JsonProvider {

    public static final String FIELD_HOST_NAME = "hostName";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (JsonWritingUtils.isNotNullOrEmpty(event.getHostName())) {
            JsonWritingUtils.writeStringField(generator, FIELD_HOST_NAME, event.getHostName());
        }
    }
}
