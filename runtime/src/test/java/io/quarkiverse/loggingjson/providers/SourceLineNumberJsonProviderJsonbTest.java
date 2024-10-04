package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SourceLineNumberJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final SourceLineNumberJsonProvider provider = new SourceLineNumberJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceLineNumber(42);
        final JsonNode result = getResultAsJsonNode(provider, event);

        int lineNumber = result.findValue("sourceLineNumber").asInt();
        Assertions.assertEquals(42, lineNumber);
        Assertions.assertFalse(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("line");
        config.enabled = Optional.of(false);
        final SourceLineNumberJsonProvider provider = new SourceLineNumberJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceLineNumber(42);
        final JsonNode result = getResultAsJsonNode(provider, event);

        int lineNumber = result.findValue("line").asInt();
        Assertions.assertEquals(42, lineNumber);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new SourceLineNumberJsonProvider(config).isEnabled());
    }
}
