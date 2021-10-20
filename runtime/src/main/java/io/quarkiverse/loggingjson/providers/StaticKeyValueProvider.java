package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;

public class StaticKeyValueProvider implements JsonProvider {

    private final String key;
    private final Object value;

    public StaticKeyValueProvider(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        generator.writeObjectField(key, value);
    }
}
