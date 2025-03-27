package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
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
        final Config.FieldConfig config = new Config.FieldConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.empty();
            }
        };
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
        final var config = new Config.FieldConfig() {
            private Optional<Boolean> enabled = Optional.of(false);

            @Override
            public Optional<String> fieldName() {
                return Optional.of("pid");
            }

            @Override
            public Optional<Boolean> enabled() {
                return enabled;
            }
        };
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
