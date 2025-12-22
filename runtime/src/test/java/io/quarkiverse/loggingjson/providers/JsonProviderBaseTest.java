package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.jboss.logmanager.ExtLogRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.common.MapBackedConfigSource;

public abstract class JsonProviderBaseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    protected abstract Type type();

    protected static Config GetConfig(Map<String, String> values) {
        return new SmallRyeConfigBuilder()
                .withMapping(Config.class)
                .withSources(new MapBackedConfigSource("test", values) {
                })
                .build()
                .getConfigMapping(Config.class);
    }

    protected JsonFactory getJsonFactory(Config config) {
        JsonFactory jsonFactory;
        switch (type()) {
            case JSONB:
                jsonFactory = new JsonbJsonFactory();
                break;
            case JACKSON:
                jsonFactory = new JacksonJsonFactory();
                break;
            default:
                throw new RuntimeException("Unsupported type");
        }
        jsonFactory.setConfig(config);
        return jsonFactory;
    }

    protected JsonGenerator getGenerator(StringBuilderWriter writer) throws IOException {
        return getJsonFactory(GetConfig(Map.of())).createGenerator(writer);
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

    protected Config.FieldConfig fieldConfig(Optional<String> fieldName, Optional<Boolean> enabled) {
        return new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return fieldName;
            }

            @Override
            public Optional<Boolean> enabled() {
                return enabled;
            }
        };
    }

    public enum Type {
        JSONB,
        JACKSON
    }
}
