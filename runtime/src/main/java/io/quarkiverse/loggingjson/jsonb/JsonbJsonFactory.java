package io.quarkiverse.loggingjson.jsonb;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGeneratorFactory;

import org.eclipse.yasson.YassonJsonb;
import org.eclipse.yasson.internal.JsonBindingBuilder;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;

public class JsonbJsonFactory implements JsonFactory {

    private final JsonGeneratorFactory factory;
    private final YassonJsonb jsonb;

    public JsonbJsonFactory(boolean prettyPrint) {
        Map<String, Object> config = new HashMap<>();
        if (prettyPrint) {
            // It doesn't matter what value this key has, as long as it's present
            config.put(javax.json.stream.JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        }
        factory = Json.createGeneratorFactory(config);
        jsonb = (YassonJsonb) new JsonBindingBuilder().build();
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) {
        return new JsonbJsonGenerator(factory.createGenerator(writer), jsonb);
    }
}
