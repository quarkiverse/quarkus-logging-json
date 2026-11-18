package io.quarkiverse.loggingjson.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import io.quarkiverse.loggingjson.JsonGenerator;

public class JacksonJsonGenerator implements JsonGenerator {
    private final tools.jackson.core.JsonGenerator generator;

    JacksonJsonGenerator(tools.jackson.core.JsonGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void writeStartObject() throws IOException {
        this.generator.writeStartObject();
    }

    @Override
    public void writeEndObject() throws IOException {
        this.generator.writeEndObject();
    }

    @Override
    public void flush() throws IOException {
        this.generator.flush();
    }

    @Override
    public void close() throws IOException {
        this.generator.close();
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        this.generator.writeName(name);
    }

    @Override
    public void writeObject(Object pojo) throws IOException {
        this.generator.writePOJO(pojo);
    }

    @Override
    public void writeObjectFieldStart(String fieldName) throws IOException {
        this.generator.writeObjectPropertyStart(fieldName);
    }

    @Override
    public void writeObjectField(String fieldName, Object pojo) throws IOException {
        this.generator.writePOJOProperty(fieldName, pojo);
    }

    @Override
    public void writeArrayFieldStart(String fieldName) throws IOException {
        this.generator.writeArrayPropertyStart(fieldName);
    }

    @Override
    public void writeEndArray() throws IOException {
        this.generator.writeEndArray();
    }

    @Override
    public void writeString(String text) throws IOException {
        this.generator.writeString(text);
    }

    @Override
    public void writeStringField(String fieldName, String value) throws IOException {
        this.generator.writeStringProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, short value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, int value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, long value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, BigInteger value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, float value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, double value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, BigDecimal value) throws IOException {
        this.generator.writeNumberProperty(fieldName, value);
    }
}
