package io.quarkiverse.loggingjson.providers;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpNanoJsonProvider;

public class GcpNanoJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final GcpNanoJsonProvider provider = new GcpNanoJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        long timestamp = result.findValue(GcpNanoJsonProvider.TIMESTAMP_NANOS_ATTRIBUTE).asLong();
        Assertions.assertEquals(
                Instant.ofEpochMilli(event.getMillis()).getNano(),
                timestamp);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("nanoTs"), Optional.of(false));
        final GcpNanoJsonProvider provider = new GcpNanoJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        long nanoTs = result.findValue("nanoTs").asLong();
        Assertions.assertEquals(
                Instant.ofEpochMilli(event.getMillis()).getNano(),
                nanoTs);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("nanoTs"), Optional.of(true));
        Assertions.assertTrue(new GcpNanoJsonProvider(config).isEnabled());
    }
}
