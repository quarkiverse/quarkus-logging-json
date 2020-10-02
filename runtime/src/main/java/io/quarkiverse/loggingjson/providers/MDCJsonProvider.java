package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

public class MDCJsonProvider implements JsonProvider {

    public static final String FIELD_MDC = "mdc";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeMapStringFields(generator, FIELD_MDC, event.getMdcCopy());
    }
}