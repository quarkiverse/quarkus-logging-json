package io.quarkiverse.loggingjson.providers;

import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ProcessIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(null, null);
        final ProcessIdJsonProvider provider = new ProcessIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setProcessId(23847);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long processId = result.findValue("processId").asLong();
        Assertions.assertEquals(23847, processId);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig("pid", false);
        final ProcessIdJsonProvider provider = new ProcessIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setProcessId(23847);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long pid = result.findValue("pid").asLong();
        Assertions.assertEquals(23847, pid);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig("pid", true);
        Assertions.assertTrue(new ProcessIdJsonProvider(config).isEnabled());
    }
}
