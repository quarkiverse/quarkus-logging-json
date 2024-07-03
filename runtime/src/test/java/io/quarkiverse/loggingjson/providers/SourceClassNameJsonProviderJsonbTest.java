package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SourceClassNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final SourceClassNameJsonProvider provider = new SourceClassNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceClassName("SourceClassNameJsonProviderTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String className = result.findValue("sourceClassName").asText();
        Assertions.assertNotNull(className);
        Assertions.assertFalse(className.isEmpty());
        Assertions.assertEquals("SourceClassNameJsonProviderTest", className);
        Assertions.assertFalse(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("class");
        config.enabled = Optional.of(false);
        final SourceClassNameJsonProvider provider = new SourceClassNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSourceClassName("SourceClassNameJsonProviderTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String className = result.findValue("class").asText();
        Assertions.assertNotNull(className);
        Assertions.assertFalse(className.isEmpty());
        Assertions.assertEquals("SourceClassNameJsonProviderTest", className);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new SourceClassNameJsonProvider(config).isEnabled());
    }
}
