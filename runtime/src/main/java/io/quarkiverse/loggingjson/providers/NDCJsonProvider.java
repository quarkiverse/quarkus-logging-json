package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

public class NDCJsonProvider implements JsonProvider {

    public static final String FIELD_NDC = "ndc";

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getNdc() != null && !"".equals(event.getNdc())) {
            JsonWritingUtils.writeStringField(generator, FIELD_NDC, event.getNdc());
        }
    }
}
