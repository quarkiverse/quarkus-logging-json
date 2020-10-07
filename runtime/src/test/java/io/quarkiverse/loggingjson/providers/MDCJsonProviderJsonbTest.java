package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

public class MDCJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
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
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("m");
        config.enabled = Optional.of(false);
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

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new MDCJsonProvider(config).isEnabled());
    }

}
