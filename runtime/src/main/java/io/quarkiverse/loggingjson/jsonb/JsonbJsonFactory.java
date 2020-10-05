package io.quarkiverse.loggingjson.jsonb;

import java.util.HashMap;

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

    public JsonbJsonFactory() {
        factory = Json.createGeneratorFactory(new HashMap<>());
        jsonb = (YassonJsonb) new JsonBindingBuilder().build();
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) {
        return new JsonbJsonGenerator(factory.createGenerator(writer), jsonb);
    }
}
