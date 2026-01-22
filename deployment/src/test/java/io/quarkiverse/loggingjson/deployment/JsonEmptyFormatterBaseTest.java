package io.quarkiverse.loggingjson.deployment;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import org.jboss.logmanager.handlers.ConsoleHandler;
import org.jboss.logmanager.handlers.WriterHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import io.quarkiverse.loggingjson.JsonFormatter;
import io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument;
import io.quarkus.bootstrap.logging.InitialConfigurator;
import io.quarkus.bootstrap.logging.QuarkusDelayedHandler;

public abstract class JsonEmptyFormatterBaseTest {
    private static final StringWriter writer = new StringWriter();
    private static WriterHandler handler;

    static {
        System.setProperty("java.util.logging.manager", org.jboss.logmanager.LogManager.class.getName());
    }

    @BeforeAll
    static void setUp() {
        Formatter formatter = InitialConfigurator.DELAYED_HANDLER.getHandlers()[0].getFormatter();
        handler = new WriterHandler();
        handler.setFormatter(formatter);
        handler.setWriter(writer);
        InitialConfigurator.DELAYED_HANDLER.addHandler(handler);
    }

    public static JsonFormatter getJsonFormatter() {
        LogManager logManager = LogManager.getLogManager();
        Assertions.assertInstanceOf(org.jboss.logmanager.LogManager.class, logManager);

        QuarkusDelayedHandler delayedHandler = InitialConfigurator.DELAYED_HANDLER;
        Assertions.assertTrue(Arrays.asList(Logger.getLogger("").getHandlers()).contains(delayedHandler));
        Assertions.assertEquals(Level.ALL, delayedHandler.getLevel());

        Handler handler = Arrays.stream(delayedHandler.getHandlers())
                .filter(h -> (h instanceof ConsoleHandler))
                .findFirst().orElse(null);
        Assertions.assertNotNull(handler);
        Assertions.assertEquals(Level.WARNING, handler.getLevel());

        Formatter formatter = handler.getFormatter();
        Assertions.assertInstanceOf(JsonFormatter.class, formatter);
        return (JsonFormatter) formatter;
    }

    protected String[] logLines() {
        handler.flush();
        return writer.toString().split("\n");
    }

    protected void testLogOutput() throws Exception {
        JsonFormatter jsonFormatter = getJsonFormatter();

        org.slf4j.Logger log = LoggerFactory.getLogger("JsonStructuredTest");

        log.error("Test {}", "message",
                KeyValueStructuredArgument.kv("structuredKey", "structuredValue"),
                new RuntimeException("Testing stackTrace"));

        String[] lines = logLines();

        Assertions.assertEquals(1, lines.length);
        JsonNode jsonNode = new ObjectMapper().readValue(lines[0], JsonNode.class);
        Assertions.assertTrue(jsonNode.isObject());

        List<String> expectedFields = Arrays.asList(
                "arg0",
                "structuredKey",
                "service.name");
        Assertions.assertEquals(expectedFields, ImmutableList.copyOf(jsonNode.fieldNames()));

        Assertions.assertTrue(jsonNode.findValue("arg0").isTextual());
        Assertions.assertEquals("message", jsonNode.findValue("arg0").asText());

        Assertions.assertTrue(jsonNode.findValue("structuredKey").isTextual());
        Assertions.assertEquals("structuredValue", jsonNode.findValue("structuredKey").asText());

        Assertions.assertTrue(jsonNode.findValue("service.name").isTextual());
        Assertions.assertEquals("deployment-test", jsonNode.findValue("service.name").asText());
    }
}
