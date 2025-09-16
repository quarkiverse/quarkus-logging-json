package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SpanIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final SpanIdJsonProvider provider = new SpanIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("spanId", "123456");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String traceId = result.findValue("spanId").asText();
        Assertions.assertNotNull(traceId);
        Assertions.assertFalse(traceId.isEmpty());
        Assertions.assertEquals("123456", traceId);
        Assertions.assertTrue(provider.isEnabled());

        config = fieldConfig(Optional.empty(), Optional.of(true));
        Assertions.assertTrue(new SpanIdJsonProvider(config).isEnabled());
    }
}
