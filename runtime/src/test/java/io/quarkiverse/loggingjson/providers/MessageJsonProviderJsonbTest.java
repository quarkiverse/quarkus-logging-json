package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class MessageJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final MessageJsonProvider provider = new MessageJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "TestMessage {0}", "");
        event.setParameters(new Object[] { "param0" });
        final JsonNode result = getResultAsJsonNode(provider, event);

        String message = result.findValue("message").asText();
        Assertions.assertNotNull(message);
        Assertions.assertFalse(message.isEmpty());
        Assertions.assertEquals("TestMessage param0", message);
        Assertions.assertTrue(provider.isEnabled());
        Assertions.assertNull(provider.format(event));
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("msg");
        config.enabled = Optional.of(false);
        final MessageJsonProvider provider = new MessageJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "TestMessage {0}", "");
        event.setParameters(new Object[] { "param0" });
        final JsonNode result = getResultAsJsonNode(provider, event);

        String msg = result.findValue("msg").asText();
        Assertions.assertNotNull(msg);
        Assertions.assertFalse(msg.isEmpty());
        Assertions.assertEquals("TestMessage param0", msg);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new MessageJsonProvider(config).isEnabled());
    }
}
