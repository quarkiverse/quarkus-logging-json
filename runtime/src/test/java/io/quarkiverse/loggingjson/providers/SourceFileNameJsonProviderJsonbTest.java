package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SourceFileNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final SourceFileNameJsonProvider provider = new SourceFileNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceFileName("SourceFileNameJsonProviderTest.java");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String fileName = result.findValue("sourceFileName").asText();
        Assertions.assertNotNull(fileName);
        Assertions.assertFalse(fileName.isEmpty());
        Assertions.assertEquals("SourceFileNameJsonProviderTest.java", fileName);
        Assertions.assertFalse(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("file");
        config.enabled = Optional.of(false);
        final SourceFileNameJsonProvider provider = new SourceFileNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceFileName("SourceFileNameJsonProviderTest.java");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String fileName = result.findValue("file").asText();
        Assertions.assertNotNull(fileName);
        Assertions.assertFalse(fileName.isEmpty());
        Assertions.assertEquals("SourceFileNameJsonProviderTest.java", fileName);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new SourceFileNameJsonProvider(config).isEnabled());
    }
}
