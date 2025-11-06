package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Supplier;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;

/**
 * JsonProvider for the <code>event.id</code> field: Unique ID to describe the event.
 *
 * @see <a href="https://www.elastic.co/guide/en/ecs/current/ecs-event.html#field-event-id"><abbr
 *      title="Elastic Common Schema">ECS</abbr> Field Reference - <code>event.id</code></a>
 */
public class EventIdJsonProvider implements JsonProvider, Enabled {
    /**
     * Base64 URL encoder without padding.
     */
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final String fieldName;
    private final Config.EventIdField config;
    private final Supplier<UUID> uuidGenerator;

    public EventIdJsonProvider(Config.EventIdField config) {
        this(config, "eventId", UUID::randomUUID);
    }

    public EventIdJsonProvider(Config.EventIdField config, String defaultName) {
        this(config, defaultName, UUID::randomUUID);
    }

    EventIdJsonProvider(Config.EventIdField config, String defaultName, Supplier<UUID> uuidGenerator) {
        this.config = config;
        this.fieldName = config.fieldName().orElse(defaultName);

        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        JsonWritingUtils.writeStringField(generator, this.fieldName, generateEncodedUUID());
    }

    private String generateEncodedUUID() {
        UUID uuid = this.uuidGenerator.get();
        byte[] uuidBytes = asByteArray(uuid);
        return encode(uuidBytes);
    }

    /**
     * Converts a UUID to a byte array.
     *
     * @param uuid the UUID to convert
     * @return the byte array representation of the UUID
     */
    private byte[] asByteArray(UUID uuid) {
        byte[] bytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (mostSigBits >>> (8 * (7 - i)));
        }
        for (int i = 0; i < 8; i++) {
            bytes[i + 8] = (byte) (leastSigBits >>> (8 * (7 - i)));
        }
        return bytes;
    }

    /**
     * Encodes a byte array to a Base64 string.
     *
     * @param data the byte array to encode
     * @return the Base64 encoded string
     */
    private String encode(byte[] data) {
        return ENCODER.encodeToString(data);
    }

    @Override
    public boolean isEnabled() {
        return config.enabled();
    }
}
