package io.quarkiverse.loggingjson.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpSpanIdJsonProvider;

public class GcpSpanIdJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.GcpSpanIdConfig config = new Config.GcpSpanIdConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.empty();
            }

            @Override
            public Optional<String> mdcKey() {
                return Optional.empty();
            }
        };

        final GcpSpanIdJsonProvider provider = new GcpSpanIdJsonProvider(config);

        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map<String, String> mdc = new HashMap<>();
        mdc.put("x-b3-spanid", "261dcf2aa53fd2b8");
        event.setMdc(mdc);

        final JsonNode result = getResultAsJsonNode(provider, event);
        String spanId = result.findValue(GcpSpanIdJsonProvider.SPAN_ID_ATTRIBUTE).asText();

        Assertions.assertEquals("261dcf2aa53fd2b8", spanId);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.GcpSpanIdConfig config = new Config.GcpSpanIdConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.of("span");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(true);
            }

            @Override
            public Optional<String> mdcKey() {
                return Optional.of("my-span-id");
            }
        };

        final GcpSpanIdJsonProvider provider = new GcpSpanIdJsonProvider(config);

        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map<String, String> mdc = new HashMap<>();
        mdc.put("my-span-id", "abcdef1234567890");
        event.setMdc(mdc);

        final JsonNode result = getResultAsJsonNode(provider, event);
        String span = result.findValue("span").asText();

        Assertions.assertEquals("abcdef1234567890", span);
        Assertions.assertTrue(provider.isEnabled());

        // Another config instance to test default enabled behavior
        Config.GcpSpanIdConfig defaultConfig = new Config.GcpSpanIdConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.empty();
            }

            @Override
            public Optional<String> mdcKey() {
                return Optional.empty();
            }
        };

        Assertions.assertTrue(new GcpSpanIdJsonProvider(defaultConfig).isEnabled());
    }
}
