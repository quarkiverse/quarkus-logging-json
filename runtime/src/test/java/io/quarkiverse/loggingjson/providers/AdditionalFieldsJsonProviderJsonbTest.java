package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AdditionalFieldsJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Map<String, Config.AdditionalFieldConfig> additionalFields = new HashMap<>();
        final AdditionalFieldsJsonProvider provider = new AdditionalFieldsJsonProvider(additionalFields);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        Assertions.assertTrue(result.isObject());
        Assertions.assertEquals(0, ImmutableList.copyOf(result.fieldNames()).size());
        Assertions.assertFalse(provider.isEnabled());
        Assertions.assertFalse(new AdditionalFieldsJsonProvider(null).isEnabled());
    }

    @Test
    void testCustomConfigWithFields() throws Exception {
        final Map<String, Config.AdditionalFieldConfig> additionalFields = new HashMap<>();
        additionalFields.put("string", additionalFieldConfig("stringType", Config.AdditionalFieldType.STRING));
        additionalFields.put("int", additionalFieldConfig("123", Config.AdditionalFieldType.INT));
        additionalFields.put("long", additionalFieldConfig("2147483700", Config.AdditionalFieldType.LONG));
        additionalFields.put("float", additionalFieldConfig("3248.23847f", Config.AdditionalFieldType.FLOAT));
        additionalFields.put("double", additionalFieldConfig("93485.02394850d", Config.AdditionalFieldType.DOUBLE));
        final AdditionalFieldsJsonProvider provider = new AdditionalFieldsJsonProvider(additionalFields);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        Assertions.assertTrue(result.isObject());
        Assertions.assertEquals(Arrays.asList("string", "double", "float", "int", "long"),
                ImmutableList.copyOf(result.fieldNames()));
        Assertions.assertTrue(provider.isEnabled());
        Assertions.assertEquals("stringType", result.get("string").asText());
        Assertions.assertEquals(123, result.get("int").asInt());
        Assertions.assertEquals(2147483700L, result.get("long").asLong());
        Assertions.assertEquals(3248.23847f, result.get("float").floatValue());
        Assertions.assertEquals(93485.02394850d, result.get("double").doubleValue());
    }

    private Config.AdditionalFieldConfig additionalFieldConfig(String value, Config.AdditionalFieldType type) {
        final Config.AdditionalFieldConfig config = new Config.AdditionalFieldConfig();
        config.value = value;
        config.type = type;
        return config;
    }
}
