package io.quarkiverse.loggingjson;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.JsonProviderBaseTest;
import io.quarkiverse.loggingjson.providers.MessageJsonProvider;

class JsonFormatterJsonbTest extends JsonProviderBaseTest {

    protected String expectedPrettyPrintMessage = "{\n    \"message\": \"TestMessage\"\n}\n";

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void format() {
        final Config config = GetConfig(Map.of());

        final JsonFormatter formatter = new JsonFormatter(List.of(new MessageJsonProvider(config.fields().message())),
                getJsonFactory(config), config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "TestMessage", "");
        final String formatted = formatter.format(event);

        Assertions.assertNotNull(formatted);
        Assertions.assertFalse(formatted.isEmpty());
        Assertions.assertEquals("{\"message\":\"TestMessage\"}\n", formatted);
    }

    @Test
    void formatPrettyPrint() {
        final Config config = GetConfig(Map.of("quarkus.log.json.pretty-print", "true"));

        final JsonFormatter formatter = new JsonFormatter(List.of(new MessageJsonProvider(config.fields().message())),
                getJsonFactory(config), config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "TestMessage", "");
        final String formatted = formatter.format(event);

        Assertions.assertNotNull(formatted);
        Assertions.assertFalse(formatted.isEmpty());
        Assertions.assertEquals(expectedPrettyPrintMessage, formatted);
    }
}
