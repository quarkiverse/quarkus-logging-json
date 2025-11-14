package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class TraceIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final TraceIdJsonProvider provider = new TraceIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("traceId", "123456");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String traceId = result.findValue("traceId").asText();
        Assertions.assertNotNull(traceId);
        Assertions.assertFalse(traceId.isEmpty());
        Assertions.assertEquals("123456", traceId);
        Assertions.assertTrue(provider.isEnabled());

        config = fieldConfig(Optional.empty(), Optional.of(true));
        Assertions.assertTrue(new TraceIdJsonProvider(config).isEnabled());
    }
}
