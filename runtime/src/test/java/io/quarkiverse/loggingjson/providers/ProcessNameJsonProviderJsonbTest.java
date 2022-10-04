package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ProcessNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final ProcessNameJsonProvider provider = new ProcessNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setProcessName("TestProcess");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String processName = result.findValue("processName").asText();
        Assertions.assertNotNull(processName);
        Assertions.assertFalse(processName.isEmpty());
        Assertions.assertEquals("TestProcess", processName);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("pn");
        config.enabled = Optional.of(false);
        final ProcessNameJsonProvider provider = new ProcessNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setProcessName("TestProcess");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String pn = result.findValue("pn").asText();
        Assertions.assertNotNull(pn);
        Assertions.assertFalse(pn.isEmpty());
        Assertions.assertEquals("TestProcess", pn);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new ProcessNameJsonProvider(config).isEnabled());
    }
}
