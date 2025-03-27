package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SequenceJsonProviderJsonbTest extends JsonProviderBaseTest {
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
        final SequenceJsonProvider provider = new SequenceJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSequenceNumber(4356);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long sequence = result.findValue("sequence").asLong();
        Assertions.assertEquals(4356, sequence);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final var config = new Config.FieldConfig() {
            private Optional<Boolean> enabled = Optional.of(false);

            @Override
            public Optional<String> fieldName() {
                return Optional.of("seq");
            }

            @Override
            public Optional<Boolean> enabled() {
                return enabled;
            }
        };
        final SequenceJsonProvider provider = new SequenceJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSequenceNumber(4356);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long seq = result.findValue("seq").asLong();
        Assertions.assertEquals(4356, seq);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new SequenceJsonProvider(config).isEnabled());
    }
}
