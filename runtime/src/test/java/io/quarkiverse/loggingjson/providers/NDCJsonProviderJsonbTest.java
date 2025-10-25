package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class NDCJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final NDCJsonProvider provider = new NDCJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setNdc("NDCTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String ndc = result.findValue("ndc").asText();
        Assertions.assertNotNull(ndc);
        Assertions.assertFalse(ndc.isEmpty());
        Assertions.assertEquals("NDCTest", ndc);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("n"), Optional.of(false));
        final NDCJsonProvider provider = new NDCJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setNdc("NDCTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String n = result.findValue("n").asText();
        Assertions.assertNotNull(n);
        Assertions.assertFalse(n.isEmpty());
        Assertions.assertEquals("NDCTest", n);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("n"), Optional.of(true));
        Assertions.assertTrue(new NDCJsonProvider(config).isEnabled());
    }
}
