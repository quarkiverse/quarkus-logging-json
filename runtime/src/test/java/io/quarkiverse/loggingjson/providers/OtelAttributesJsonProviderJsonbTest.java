package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkiverse.loggingjson.config.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;

public class OtelAttributesJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.MDCConfig config = new Config.MDCConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final OtelAttributesJsonProvider provider = new OtelAttributesJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("traceId", "traceValue");
        event.putMdc("spanId", "spanValue");
        event.putMdc("anotherKey", "anotherValue");
        final JsonNode result = getResultAsJsonNode(provider, event);

        Assertions.assertEquals("traceValue", result.get("traceId").asText());
        Assertions.assertEquals("spanValue", result.get("spanId").asText());
        final JsonNode attributes = result.findValue("Attributes");
        Assertions.assertNotNull(attributes);
        Assertions.assertTrue(attributes.isObject());
        Assertions.assertEquals("anotherValue", attributes.get("anotherKey").asText());
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomFieldNameConfig() throws Exception {
        final Config.MDCConfig config = new Config.MDCConfig();
        config.fieldName = Optional.of("customAttributes");
        config.enabled = Optional.empty();
        final OtelAttributesJsonProvider provider = new OtelAttributesJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.putMdc("traceId", "customTraceValue");
        event.putMdc("spanId", "customSpanValue");
        event.putMdc("customKey", "customValue");
        final JsonNode result = getResultAsJsonNode(provider, event);

        Assertions.assertEquals("customTraceValue", result.get("traceId").asText());
        Assertions.assertEquals("customSpanValue", result.get("spanId").asText());
        final JsonNode attributes = result.findValue("customAttributes");
        Assertions.assertNotNull(attributes);
        Assertions.assertTrue(attributes.isObject());
        Assertions.assertEquals("customValue", attributes.get("customKey").asText());
    }

    @Test
    void testDisabledProvider() throws Exception {
        final Config.MDCConfig config = new Config.MDCConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.of(false);
        final OtelAttributesJsonProvider provider = new OtelAttributesJsonProvider(config);

        Assertions.assertFalse(provider.isEnabled());
    }

}
