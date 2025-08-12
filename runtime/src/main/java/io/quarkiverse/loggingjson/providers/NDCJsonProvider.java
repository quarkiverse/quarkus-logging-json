package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;

public class NDCJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.FieldConfig config;

    public NDCJsonProvider(Config.FieldConfig config) {
        this.config = config;
        this.fieldName = config.fieldName().orElse("ndc");
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        if (event.getNdc() != null && !"".equals(event.getNdc())) {
            JsonWritingUtils.writeStringField(generator, fieldName, event.getNdc());
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled().orElse(true);
    }
}
