package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import io.quarkiverse.loggingjson.JsonGenerator;

public class KeyValueStructuredArgument implements StructuredArgument {
    private final String key;
    private final Object value;
    private final String format;

    public KeyValueStructuredArgument(String key, Object value) {
        this(key, value, "%s=%s");
    }

    public KeyValueStructuredArgument(String key, Object value, String format) {
        this.key = key;
        this.value = value;
        this.format = format;
    }

    public static StructuredArgument kv(String key, Object value) {
        return new KeyValueStructuredArgument(key, value);
    }

    public static StructuredArgument kv(String key, Object value, String format) {
        return new KeyValueStructuredArgument(key, value, format);
    }

    @Override
    public void writeTo(JsonGenerator generator) throws IOException {
        generator.writeObjectField(key, value);
    }

    @Override
    public String toString() {
        return String.format(format, key, value);
    }
}
