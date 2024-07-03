package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SourceMethodNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final SourceMethodNameJsonProvider provider = new SourceMethodNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceMethodName("foo");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String methodName = result.findValue("sourceMethodName").asText();
        Assertions.assertNotNull(methodName);
        Assertions.assertFalse(methodName.isEmpty());
        Assertions.assertEquals("foo", methodName);
        Assertions.assertFalse(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("method");
        config.enabled = Optional.of(false);
        final SourceMethodNameJsonProvider provider = new SourceMethodNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceMethodName("foo");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String methodName = result.findValue("method").asText();
        Assertions.assertNotNull(methodName);
        Assertions.assertFalse(methodName.isEmpty());
        Assertions.assertEquals("foo", methodName);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new SourceMethodNameJsonProvider(config).isEnabled());
    }
}
