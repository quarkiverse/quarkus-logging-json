package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;

public class ProcessIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
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
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("pid");
        config.enabled = Optional.of(false);
        final ProcessIdJsonProvider provider = new ProcessIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setProcessId(23847);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long pid = result.findValue("pid").asLong();
        Assertions.assertEquals(23847, pid);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new ProcessIdJsonProvider(config).isEnabled());
    }
}
