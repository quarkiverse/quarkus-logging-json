package io.quarkiverse.loggingjson.jsonb;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.stream.JsonGeneratorFactory;

import org.eclipse.yasson.YassonJsonb;
import org.eclipse.yasson.internal.JsonBindingBuilder;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.config.Config;

public class JsonbJsonFactory implements JsonFactory {

    private JsonGeneratorFactory factory;
    private YassonJsonb jsonb;

    public JsonbJsonFactory() {
        setConfig(null);
    }

    @Override
    public void setConfig(Config config) {
        Map<String, Object> jsonConfig = new HashMap<>();
        if (config != null && config.prettyPrint()) {
            jsonConfig.put(jakarta.json.stream.JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        }
        factory = Json.createGeneratorFactory(jsonConfig);
        jsonb = (YassonJsonb) new JsonBindingBuilder().build();
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) {
        return new JsonbJsonGenerator(factory.createGenerator(writer), jsonb);
    }
}
