package io.quarkiverse.loggingjson.providers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;

import io.quarkiverse.loggingjson.config.Config;

public class ArgumentsJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.ArgumentsConfig config = new Config.ArgumentsConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public boolean includeStructuredArguments() {
                return true;
            }

            @Override
            public boolean includeNonStructuredArguments() {
                return false;
            }

            @Override
            public String nonStructuredArgumentsFieldPrefix() {
                return "arg";
            }
        };
        final ArgumentsJsonProvider provider = new ArgumentsJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);
        Assertions.assertEquals(0, ImmutableList.copyOf(result.fieldNames()).size());

        event.setParameters(new Object[] {
                "nonIncluded",
                KeyValueStructuredArgument.kv("key", "value"),
                new RuntimeException("Should not be included")
        });
        final JsonNode result2 = getResultAsJsonNode(provider, event);

        Assertions.assertEquals(Collections.singletonList("key"), ImmutableList.copyOf(result2.fieldNames()));

        String key = result2.findValue("key").asText();
        Assertions.assertNotNull(key);
        Assertions.assertEquals("value", key);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfigShortCircuit() throws Exception {
        final Config.ArgumentsConfig config = new Config.ArgumentsConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public boolean includeStructuredArguments() {
                return false;
            }

            @Override
            public boolean includeNonStructuredArguments() {
                return false;
            }

            @Override
            public String nonStructuredArgumentsFieldPrefix() {
                return "arg";
            }
        };
        final ArgumentsJsonProvider provider = new ArgumentsJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setParameters(new Object[] {
                "nonIncluded",
                KeyValueStructuredArgument.kv("key", "value"),
                new RuntimeException("Should not be included")
        });
        final JsonNode result = getResultAsJsonNode(provider, event);
        Assertions.assertEquals(0, ImmutableList.copyOf(result.fieldNames()).size());
        Assertions.assertFalse(provider.isEnabled());
    }

    @Test
    void testCustomConfigWrapInObject() throws Exception {
        final Config.ArgumentsConfig config = new Config.ArgumentsConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.of("arguments");
            }

            @Override
            public boolean includeStructuredArguments() {
                return true;
            }

            @Override
            public boolean includeNonStructuredArguments() {
                return true;
            }

            @Override
            public String nonStructuredArgumentsFieldPrefix() {
                return "arg";
            }
        };
        final ArgumentsJsonProvider provider = new ArgumentsJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setParameters(new Object[] {
                "included",
                KeyValueStructuredArgument.kv("key", "value"),
                new RuntimeException("Should not be included")
        });
        final JsonNode result = getResultAsJsonNode(provider, event);
        Assertions.assertEquals(Collections.singletonList("arguments"), ImmutableList.copyOf(result.fieldNames()));
        Assertions.assertEquals(Arrays.asList("arg0", "key"), ImmutableList.copyOf(result.get("arguments").fieldNames()));
        Assertions.assertEquals("included", result.get("arguments").get("arg0").asText());
        Assertions.assertEquals("value", result.get("arguments").get("key").asText());

        event.setParameters(new Object[] {
                KeyValueStructuredArgument.kv("key", "value"),
                "included",
                new RuntimeException("Should not be included")
        });
        final JsonNode result2 = getResultAsJsonNode(provider, event);
        Assertions.assertEquals(Collections.singletonList("arguments"), ImmutableList.copyOf(result2.fieldNames()));
        Assertions.assertEquals(Arrays.asList("key", "arg1"), ImmutableList.copyOf(result2.get("arguments").fieldNames()));
        Assertions.assertEquals("included", result2.get("arguments").get("arg1").asText());
        Assertions.assertEquals("value", result2.get("arguments").get("key").asText());

        Assertions.assertTrue(provider.isEnabled());
    }
}
