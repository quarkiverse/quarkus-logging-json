package io.quarkiverse.loggingjson.providers;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpEpochSecondJsonProvider;

public class GcpEpochSecondJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final GcpEpochSecondJsonProvider provider = new GcpEpochSecondJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        long timestamp = result.findValue(GcpEpochSecondJsonProvider.TIMESTAMP_SECONDS_ATTRIBUTE).asLong();

        Assertions.assertEquals(
                Instant.ofEpochMilli(event.getMillis()).getEpochSecond(),
                timestamp);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("ts"), Optional.of(false));
        final GcpEpochSecondJsonProvider provider = new GcpEpochSecondJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);

        long ts = result.findValue("ts").asLong();

        Assertions.assertEquals(
                Instant.ofEpochMilli(event.getMillis()).getEpochSecond(),
                ts);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("ts"), Optional.of(true));
        Assertions.assertTrue(new GcpEpochSecondJsonProvider(config).isEnabled());
    }
}
