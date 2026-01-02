package io.quarkiverse.loggingjson.jsonb;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.yasson.YassonJsonb;

import io.quarkiverse.loggingjson.JsonGenerator;

public class JsonbJsonGenerator implements JsonGenerator {
    private final jakarta.json.stream.JsonGenerator generator;
    private final YassonJsonb jsonb;

    public JsonbJsonGenerator(jakarta.json.stream.JsonGenerator generator, YassonJsonb jsonb) {
        this.generator = generator;
        this.jsonb = jsonb;
    }

    @Override
    public void writeStartObject() {
        this.generator.writeStartObject();
    }

    @Override
    public void writeEndObject() {
        this.generator.writeEnd();
    }

    @Override
    public void flush() {
        this.generator.flush();
    }

    @Override
    public void close() {
        this.generator.close();
    }

    @Override
    public void writeFieldName(String name) {
        this.generator.writeKey(name);
    }

    @Override
    public void writeObject(Object pojo) {
        jsonb.toJson(pojo, this.generator);
    }

    @Override
    public void writeObjectFieldStart(String fieldName) {
        this.generator.writeStartObject(fieldName);
    }

    @Override
    public void writeObjectField(String fieldName, Object pojo) {
        this.generator.writeKey(fieldName);
        jsonb.toJson(pojo, this.generator);
    }

    @Override
    public void writeArrayFieldStart(String fieldName) {
        this.generator.writeStartArray(fieldName);
    }

    @Override
    public void writeEndArray() {
        this.generator.writeEnd();
    }

    @Override
    public void writeString(String text) {
        this.generator.write(text);
    }

    @Override
    public void writeStringField(String fieldName, String value) {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, short value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, int value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, long value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, BigInteger value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, float value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, double value) throws IOException {
        this.generator.write(fieldName, value);
    }

    @Override
    public void writeNumberField(String fieldName, BigDecimal value) throws IOException {
        this.generator.write(fieldName, value);
    }
}
