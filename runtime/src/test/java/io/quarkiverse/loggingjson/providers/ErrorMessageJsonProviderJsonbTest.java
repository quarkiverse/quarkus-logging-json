package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ErrorMessageJsonProviderJsonbTest extends JsonProviderBaseTest {
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
        final ErrorMessageJsonProvider provider = new ErrorMessageJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing errorMessage");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String errorMessage = result.findValue("errorMessage").asText();
        Assertions.assertNotNull(errorMessage);
        Assertions.assertFalse(errorMessage.isEmpty());

        Assertions.assertEquals("Testing errorMessage", errorMessage);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.of("em");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(false);
            }
        };
        final ErrorMessageJsonProvider provider = new ErrorMessageJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing errorMessage");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String em = result.findValue("em").asText();
        Assertions.assertNotNull(em);
        Assertions.assertFalse(em.isEmpty());

        Assertions.assertEquals("Testing errorMessage", em);
        Assertions.assertFalse(provider.isEnabled());

        config = new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.of("em");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(true);
            }
        };
        Assertions.assertTrue(new ErrorMessageJsonProvider(config).isEnabled());
    }
}
