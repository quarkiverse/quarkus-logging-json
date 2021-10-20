package io.quarkiverse.loggingjson.deployment;

import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import org.jboss.logmanager.handlers.ConsoleHandler;
import org.jboss.logmanager.handlers.WriterHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import io.quarkiverse.loggingjson.JsonFormatter;
import io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument;
import io.quarkus.bootstrap.logging.InitialConfigurator;
import io.quarkus.bootstrap.logging.QuarkusDelayedHandler;

public abstract class JsonECSFormatterBaseTest {
    private static StringWriter writer = new StringWriter();
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
        Assertions.assertTrue(logManager instanceof org.jboss.logmanager.LogManager);

        QuarkusDelayedHandler delayedHandler = InitialConfigurator.DELAYED_HANDLER;
        Assertions.assertTrue(Arrays.asList(Logger.getLogger("").getHandlers()).contains(delayedHandler));
        Assertions.assertEquals(Level.ALL, delayedHandler.getLevel());

        Handler handler = Arrays.stream(delayedHandler.getHandlers())
                .filter(h -> (h instanceof ConsoleHandler))
                .findFirst().orElse(null);
        Assertions.assertNotNull(handler);
        Assertions.assertEquals(Level.WARNING, handler.getLevel());

        Formatter formatter = handler.getFormatter();
        Assertions.assertTrue(formatter instanceof JsonFormatter);
        return (JsonFormatter) formatter;
    }

    protected String[] logLines() {
        handler.flush();
        return writer.toString().split("\n");
    }

    protected void testLogOutput() throws Exception {
        JsonFormatter jsonFormatter = getJsonFormatter();

        org.slf4j.Logger log = LoggerFactory.getLogger("JsonStructuredTest");
        OffsetDateTime beforeFirstLog = OffsetDateTime.now().minusSeconds(1);

        try (MDC.MDCCloseable closeable = MDC.putCloseable("mdcKey", "mdcVal")) {
            log.error("Test {}", "message",
                    KeyValueStructuredArgument.kv("structuredKey", "structuredValue"),
                    new RuntimeException("Testing stackTrace"));
        }

        OffsetDateTime afterLastLog = OffsetDateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        String[] lines = logLines();

        Assertions.assertEquals(1, lines.length);
        JsonNode jsonNode = mapper.readValue(lines[0], JsonNode.class);
        Assertions.assertTrue(jsonNode.isObject());

        List<String> expectedFields = Arrays.asList(
                "@timestamp",
                "log.logger",
                "log.level",
                "process.thread.name",
                "process.thread.id",
                "mdc",
                "host.name",
                "error.stack_trace",
                "error.type",
                "error.message",
                "arg0",
                "structuredKey",
                "service.name",
                "message",
                "ecs.version");
        Assertions.assertEquals(expectedFields, ImmutableList.copyOf(jsonNode.fieldNames()));

        String timestamp = jsonNode.findValue("@timestamp").asText();
        Assertions.assertNotNull(timestamp);
        OffsetDateTime logTimestamp = OffsetDateTime.parse(timestamp);
        Assertions.assertTrue(beforeFirstLog.isBefore(logTimestamp) || beforeFirstLog.isEqual(logTimestamp));
        Assertions.assertTrue(afterLastLog.isAfter(logTimestamp) || afterLastLog.isEqual(logTimestamp));

        Assertions.assertTrue(jsonNode.findValue("log.logger").isTextual());
        Assertions.assertEquals("JsonStructuredTest", jsonNode.findValue("log.logger").asText());

        Assertions.assertTrue(jsonNode.findValue("log.level").isTextual());
        Assertions.assertEquals("ERROR", jsonNode.findValue("log.level").asText());

        Assertions.assertTrue(jsonNode.findValue("message").isTextual());
        Assertions.assertEquals("Test message", jsonNode.findValue("message").asText());

        Assertions.assertTrue(jsonNode.findValue("process.thread.name").isTextual());
        Assertions.assertEquals("main", jsonNode.findValue("process.thread.name").asText());

        Assertions.assertTrue(jsonNode.findValue("process.thread.id").isNumber());

        Assertions.assertTrue(jsonNode.findValue("mdc").isObject());
        Assertions.assertNotNull(jsonNode.findValue("mdc").findValue("mdcKey"));
        Assertions.assertEquals("mdcVal", jsonNode.findValue("mdc").findValue("mdcKey").asText());

        Assertions.assertTrue(jsonNode.findValue("host.name").isTextual());
        Assertions.assertNotEquals("", jsonNode.findValue("host.name").asText());

        Assertions.assertTrue(jsonNode.findValue("error.stack_trace").isTextual());
        Assertions.assertTrue(jsonNode.findValue("error.stack_trace").asText().length() > 100);

        Assertions.assertTrue(jsonNode.findValue("error.type").isTextual());
        Assertions.assertEquals("java.lang.RuntimeException", jsonNode.findValue("error.type").asText());

        Assertions.assertTrue(jsonNode.findValue("error.message").isTextual());
        Assertions.assertEquals("Testing stackTrace", jsonNode.findValue("error.message").asText());

        Assertions.assertTrue(jsonNode.findValue("arg0").isTextual());
        Assertions.assertEquals("message", jsonNode.findValue("arg0").asText());

        Assertions.assertTrue(jsonNode.findValue("structuredKey").isTextual());
        Assertions.assertEquals("structuredValue", jsonNode.findValue("structuredKey").asText());

        Assertions.assertTrue(jsonNode.findValue("service.name").isTextual());
        Assertions.assertEquals("deployment-test", jsonNode.findValue("service.name").asText());

        Assertions.assertTrue(jsonNode.findValue("ecs.version").isTextual());
        Assertions.assertTrue(jsonNode.findValue("ecs.version").asText().matches("^[0-9]*\\.[0-9]*\\.[0-9]*$"));
    }
}
