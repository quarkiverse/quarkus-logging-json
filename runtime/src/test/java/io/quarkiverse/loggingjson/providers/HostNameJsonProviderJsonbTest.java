package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class HostNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final HostNameJsonProvider provider = new HostNameJsonProvider(config);

        final JsonNode result = getResultAsJsonNode(provider, new ExtLogRecord(Level.ALL, "", ""));

        String hostName = result.findValue("hostName").asText();
        Assertions.assertNotNull(hostName);
        Assertions.assertFalse(hostName.isEmpty());
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("host"), Optional.of(false));
        final HostNameJsonProvider provider = new HostNameJsonProvider(config);

        final JsonNode result = getResultAsJsonNode(provider, new ExtLogRecord(Level.ALL, "", ""));

        String hostName = result.findValue("host").asText();
        Assertions.assertNotNull(hostName);
        Assertions.assertFalse(hostName.isEmpty());
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("host"), Optional.of(true));
        Assertions.assertTrue(new HostNameJsonProvider(config).isEnabled());
    }
}
