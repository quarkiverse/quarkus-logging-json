package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

abstract class JsonProviderBaseTest {

    private static final JsonFactory jsonb = new JsonbJsonFactory();
    private static final JsonFactory jackson = new JacksonJsonFactory();
    private static final ObjectMapper mapper = new ObjectMapper();

    protected abstract Type type();

    private JsonFactory getJsonFactory() {
        switch (type()) {
            case JSONB:
                return jsonb;
            case JACKSON:
                return jackson;
            default:
                throw new RuntimeException("Unsupported type");
        }
    }

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
