package io.quarkiverse.loggingjson.providers;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;

import io.quarkiverse.loggingjson.config.Config;

public class MDCJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.MDCConfig config = additionalFieldConfig(Optional.empty(), Optional.empty(), false);
        final MDCJsonProvider provider = new MDCJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("mdcKey1", "mdcValue1");
        event.putMdc("mdcKey2", "mdcValue2");
        final JsonNode result = getResultAsJsonNode(provider, event);

        final JsonNode mdc = result.findValue("mdc");
        Assertions.assertNotNull(mdc);
        Assertions.assertTrue(mdc.isObject());
        Assertions.assertEquals(Arrays.asList("mdcKey1", "mdcKey2"), ImmutableList.copyOf(mdc.fieldNames()));
        Assertions.assertEquals("mdcValue1", mdc.get("mdcKey1").asText());
        Assertions.assertEquals("mdcValue2", mdc.get("mdcKey2").asText());
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.MDCConfig config = additionalFieldConfig(Optional.of("m"), Optional.of(false), false);
        final MDCJsonProvider provider = new MDCJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("mdcKey1", "mdcValue1");
        event.putMdc("mdcKey2", "mdcValue2");
        final JsonNode result = getResultAsJsonNode(provider, event);

        final JsonNode mdc = result.findValue("m");
        Assertions.assertNotNull(mdc);
        Assertions.assertTrue(mdc.isObject());
        Assertions.assertEquals(Arrays.asList("mdcKey1", "mdcKey2"), ImmutableList.copyOf(mdc.fieldNames()));
        Assertions.assertEquals("mdcValue1", mdc.get("mdcKey1").asText());
        Assertions.assertEquals("mdcValue2", mdc.get("mdcKey2").asText());

        Assertions.assertFalse(provider.isEnabled());

        config = additionalFieldConfig(Optional.of("m"), Optional.of(true), false);
        Assertions.assertTrue(new MDCJsonProvider(config).isEnabled());
    }

    @Test
    void testFlatCustomConfig() throws Exception {
        final Config.MDCConfig config = additionalFieldConfig(Optional.of("m"), Optional.empty(), true);
        final MDCJsonProvider provider = new MDCJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("mdcKey1", "mdcValue1");
        event.putMdc("mdcKey2", "mdcValue2");
        final JsonNode result = getResultAsJsonNode(provider, event);

        Assertions.assertFalse(result.has("m"));
        Assertions.assertEquals(Arrays.asList("mdcKey1", "mdcKey2"), ImmutableList.copyOf(result.fieldNames()));
        Assertions.assertEquals("mdcValue1", result.get("mdcKey1").asText());
        Assertions.assertEquals("mdcValue2", result.get("mdcKey2").asText());
    }

    private Config.MDCConfig additionalFieldConfig(Optional<String> fieldName, Optional<Boolean> enabled, boolean flatFields) {
        return new Config.MDCConfig() {

            @Override
            public Optional<String> fieldName() {
                return fieldName;
            }

            @Override
            public Optional<Boolean> enabled() {
                return enabled;
            }

            @Override
            public boolean flatFields() {
                return flatFields;
            }
        };
    }
}
