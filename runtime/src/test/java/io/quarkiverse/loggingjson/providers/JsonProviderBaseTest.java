package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jboss.logmanager.ExtLogRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;

abstract class JsonProviderBaseTest {

    private final JsonFactory jsonb = new JsonbJsonFactory(prettyPrint());
    private final JsonFactory jackson = new JacksonJsonFactory(prettyPrint());

    private final JsonWriterFactory writerFactory;
    private final Function<String, String> jsonbFormatter;

    private final ObjectMapper mapper = new ObjectMapper();
    private final Function<String, String> jacksonFormatter;

    public JsonProviderBaseTest() {
        //Set-up JSONB formatter for tests
        Map<String, Object> config = new HashMap<>();
        config.put(javax.json.stream.JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        writerFactory = Json.createWriterFactory(config);

        jsonbFormatter=(String raw)-> {
            StringWriter stringWriter = new StringWriter();
            StringReader stringReader = new StringReader(raw);
            JsonReader jr = Json.createReader(stringReader);
            JsonObject jo = jr.readObject();

            JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

            jsonWriter.writeObject(jo);
            jsonWriter.close();

            return stringWriter.toString();
        };

        //Set-up Jackson formatter for tests
        jacksonFormatter=(String raw)-> {
            try {
                Object o = mapper.readValue(raw, Object.class);
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            } catch(JsonProcessingException e) {
                return raw;
            }
        };
    }

    protected abstract Type type();

    protected boolean prettyPrint() {
        return false;
    }

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

    protected String format(String raw) {
        if(prettyPrint()) {
            switch (type()) {
                case JSONB:
                    return jsonbFormatter.apply(raw);
                case JACKSON:
                    return jacksonFormatter.apply(raw);
            }
        }
        return raw;
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
