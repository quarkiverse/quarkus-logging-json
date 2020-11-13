package io.quarkiverse.loggingjson.providers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.Config;

public class TimestampJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.TimestampField config = new Config.TimestampField();
        config.fieldName = Optional.empty();
        config.dateFormat = "default";
        final TimestampJsonProvider timestampJsonProvider = new TimestampJsonProvider(config);

        OffsetDateTime beforeLog = OffsetDateTime.now().minusSeconds(1);
        final JsonNode result = getResultAsJsonNode(timestampJsonProvider, new ExtLogRecord(Level.ALL, "", ""));
        OffsetDateTime afterLog = OffsetDateTime.now();

        String timestamp = result.findValue("timestamp").asText();
        Assertions.assertNotNull(timestamp);
        OffsetDateTime logTimestamp = OffsetDateTime.parse(timestamp);
        Assertions.assertTrue(beforeLog.isBefore(logTimestamp) || beforeLog.isEqual(logTimestamp));
        Assertions.assertTrue(afterLog.isAfter(logTimestamp) || afterLog.isEqual(logTimestamp));
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.TimestampField config = new Config.TimestampField();
        config.fieldName = Optional.of("@timestamp");
        config.dateFormat = "dd-MM-yyyy HH:mm:ss.SSS";
        final TimestampJsonProvider timestampJsonProvider = new TimestampJsonProvider(config);

        LocalDateTime beforeLog = LocalDateTime.now().minusSeconds(1);
        final JsonNode result = getResultAsJsonNode(timestampJsonProvider, new ExtLogRecord(Level.ALL, "", ""));
        LocalDateTime afterLog = LocalDateTime.now();

        String timestamp = result.findValue("@timestamp").asText();
        Assertions.assertNotNull(timestamp);
        LocalDateTime logTimestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
        Assertions.assertTrue(beforeLog.isBefore(logTimestamp) || beforeLog.isEqual(logTimestamp));
        Assertions.assertTrue(afterLog.isAfter(logTimestamp) || afterLog.isEqual(logTimestamp));
    }

    @Test
    void testCustomDateFormatConfigFail() {
        final Config.TimestampField config = new Config.TimestampField();
        config.fieldName = Optional.of("@timestamp");
        config.dateFormat = "sdkfjl";

        try {
            new TimestampJsonProvider(config);
            Assertions.fail("Expected the format to be invalid");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    void testCustomZoneIdConfig() throws Exception {
        final Config.TimestampField config = new Config.TimestampField();
        config.fieldName = Optional.of("timestamp");
        config.zoneId = "Antarctica/Davis";
        final TimestampJsonProvider timestampJsonProvider = new TimestampJsonProvider(config);

        ZonedDateTime beforeLog = ZonedDateTime.now().minusSeconds(1).withZoneSameInstant(ZoneId.of("+7"));
        final JsonNode result = getResultAsJsonNode(timestampJsonProvider, new ExtLogRecord(Level.ALL, "", ""));
        ZonedDateTime afterLog = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("+7"));

        String timestamp = result.findValue("timestamp").asText();
        Assertions.assertNotNull(timestamp);
        ZonedDateTime logTimestamp = ZonedDateTime.parse(timestamp);
        Assertions.assertEquals(ZoneId.of("+7"), logTimestamp.getZone());
        Assertions.assertTrue(beforeLog.isBefore(logTimestamp) || beforeLog.isEqual(logTimestamp));
        Assertions.assertTrue(afterLog.isAfter(logTimestamp) || afterLog.isEqual(logTimestamp));
    }

    @Test
    void testCustomZoneIdConfigFail() {
        final Config.TimestampField config = new Config.TimestampField();
        config.fieldName = Optional.of("timestamp");
        config.zoneId = "sdkfjl";

        try {
            new TimestampJsonProvider(config);
            Assertions.fail("Expected the zoneId to be invalid");
        } catch (ZoneRulesException ignored) {
        }
    }
}
