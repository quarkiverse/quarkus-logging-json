package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.StringBuilderWriter;

abstract class JsonBaseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    protected abstract Type type();

    protected abstract JsonFactory getJsonFactory();

    protected JsonGenerator getGenerator(StringBuilderWriter writer) throws IOException {
        return getJsonFactory().createGenerator(writer);
    }

    protected String getResult(JsonProvider jsonProvider, ExtLogRecord event) throws IOException {
        try (StringBuilderWriter writer = new StringBuilderWriter();
                JsonGenerator generator = getGenerator(writer)) {
            generator.writeStartObject();
            jsonProvider.writeTo(generator, event);
            generator.writeEndObject();
            generator.flush();
            return writer.toString();
        }
    }

    protected JsonNode getResultAsJsonNode(JsonProvider jsonProvider, ExtLogRecord event) throws IOException {
        return mapper.readValue(getResult(jsonProvider, event), JsonNode.class);
    }

    public enum Type {
        JSONB,
        JACKSON
    }
}
