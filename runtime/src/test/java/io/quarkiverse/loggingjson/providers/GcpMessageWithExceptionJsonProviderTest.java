package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpMessageWithExceptionJsonProvider;

public class GcpMessageWithExceptionJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final GcpMessageWithExceptionJsonProvider provider = new GcpMessageWithExceptionJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing stackTrace");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);

        final JsonNode result = getResultAsJsonNode(provider, event);

        String message = result.findValue("message").asText();
        Assertions.assertNotNull(message);
        Assertions.assertFalse(message.isEmpty());

        // Check that the exception's class name appears in the message
        Assertions.assertTrue(message.contains(t.toString()));
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("msg"), Optional.of(false));
        final GcpMessageWithExceptionJsonProvider provider = new GcpMessageWithExceptionJsonProvider(config);

        final RuntimeException t = new RuntimeException("Custom field test");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);

        final JsonNode result = getResultAsJsonNode(provider, event);

        String msg = result.findValue("msg").asText();
        Assertions.assertNotNull(msg);
        Assertions.assertFalse(msg.isEmpty());
        Assertions.assertTrue(msg.contains(t.toString()));

        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("msg"), Optional.of(true));
        Assertions.assertTrue(new GcpMessageWithExceptionJsonProvider(config).isEnabled());
    }
}
