package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ErrorTypeJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final ErrorTypeJsonProvider provider = new ErrorTypeJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing errorType");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String errorType = result.findValue("errorType").asText();
        Assertions.assertNotNull(errorType);
        Assertions.assertFalse(errorType.isEmpty());

        Assertions.assertEquals("java.lang.RuntimeException", errorType);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("et"), Optional.of(false));
        final ErrorTypeJsonProvider provider = new ErrorTypeJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing errorType");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String et = result.findValue("et").asText();
        Assertions.assertNotNull(et);
        Assertions.assertFalse(et.isEmpty());

        Assertions.assertEquals("java.lang.RuntimeException", et);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("et"), Optional.of(true));
        Assertions.assertTrue(new ErrorTypeJsonProvider(config).isEnabled());
    }
}
