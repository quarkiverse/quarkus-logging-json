package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpLogLevelJsonProvider;

public class GcpLogLevelJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final GcpLogLevelJsonProvider provider = new GcpLogLevelJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String level = result.findValue(GcpLogLevelJsonProvider.SEVERITY_ATTRIBUTE).asText();
        Assertions.assertEquals("DEFAULT", level);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("lvl"), Optional.of(false));
        final GcpLogLevelJsonProvider provider = new GcpLogLevelJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.SEVERE, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String lvl = result.findValue("lvl").asText();
        Assertions.assertEquals("ERROR", lvl); // maps Level.SEVERE to "ERROR"
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("lvl"), Optional.of(true));
        Assertions.assertTrue(new GcpLogLevelJsonProvider(config).isEnabled());
    }
}
