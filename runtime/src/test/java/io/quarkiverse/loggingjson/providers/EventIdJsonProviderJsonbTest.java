package io.quarkiverse.loggingjson.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class EventIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.EventIdField config = new Config.EventIdField();
        config.fieldName = Optional.empty();
        config.enabled = true;
        final EventIdJsonProvider timestampJsonProvider = new EventIdJsonProvider(config);

        final JsonNode result = getResultAsJsonNode(timestampJsonProvider, new ExtLogRecord(Level.ALL, "", ""));

        String timestamp = result.findValue("eventId").asText();
        Assertions.assertNotNull(timestamp);
    }

    @Test
    void testConfigEnabled() {
        final Config.EventIdField config = new Config.EventIdField();
        config.fieldName = Optional.of("event.id");
        config.enabled = false;
        final EventIdJsonProvider eventIdJsonProvider = new EventIdJsonProvider(config, "eventId");
        assertFalse(eventIdJsonProvider.isEnabled());

        config.enabled = true;
        assertTrue(eventIdJsonProvider.isEnabled());
    }

    @Test
    void testCustomConfig() throws IOException {
        // Given
        final String customFieldName = "customEventIdFieldName";
        final Config.EventIdField config = new Config.EventIdField();
        config.fieldName = Optional.of(customFieldName);
        config.enabled = true;

        final ExtLogRecord record = new ExtLogRecord(Level.ALL, "", "");

        final UUID mockedUUID = UUID.fromString("201d870c-d9ee-4ed0-b209-620e017c2de9");
        final String encodedMockedUUID = "IB2HDNnuTtCyCWIOAXwt6Q";

        final EventIdJsonProvider eventIdJsonProvider = new EventIdJsonProvider(config, "eventId", () -> mockedUUID);

        // When
        final JsonNode result = getResultAsJsonNode(eventIdJsonProvider, record);

        // Then
        String eventId = result.findValue(customFieldName).asText();
        assertEquals(encodedMockedUUID, eventId);
    }
}
