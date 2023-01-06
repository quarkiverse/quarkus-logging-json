package io.quarkiverse.loggingjson.providers.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.quarkiverse.loggingjson.providers.PrettyPrintingKeyValueStructuredArgumentJsonbTest;

class PrettyPrintingKeyValueStructuredArgumentJacksonTest extends PrettyPrintingKeyValueStructuredArgumentJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

    @Test
    void testKeyValues() throws IOException {
        assertEquals("{\n"
                + "  \"key\" : null\n"
                + "}",
                run("key", null));
        assertEquals("{\n"
                + "  \"anotherKey\" : null\n"
                + "}",
                run("anotherKey", null));
        assertEquals("{\n"
                + "  \"key\" : 324\n"
                + "}",
                run("key", (short) 324));
        assertEquals("{\n"
                + "  \"key\" : 324\n"
                + "}",
                run("key", 324));
        assertEquals("{\n"
                + "  \"key\" : 324\n"
                + "}",
                run("key", 324L));
        assertEquals("{\n"
                + "  \"key\" : 324.348\n"
                + "}",
                run("key", 324.348));
        assertEquals("{\n"
                + "  \"key\" : 324.348\n"
                + "}",
                run("key", 324.348d));
        assertEquals("{\n"
                + "  \"key\" : 324\n"
                + "}",
                run("key", BigInteger.valueOf(324)));
        assertEquals("{\n"
                + "  \"key\" : 324.348\n"
                + "}",
                run("key", BigDecimal.valueOf(324.348d)));
        assertEquals("{\n"
                + "  \"key\" : \"value\"\n"
                + "}",
                run("key", "value"));
        assertEquals("{\n"
                + "  \"key\" : [ \"value\", \"value2\" ]\n"
                + "}",
                run("key", new String[] { "value", "value2" }));
        assertEquals("{\n"
                + "  \"key\" : [ \"value\", \"value2\" ]\n"
                + "}",
                run("key", Arrays.asList("value", "value2")));
        assertEquals("{\n"
                + "  \"key\" : { }\n"
                + "}",
                run("key", new Object()));
        assertEquals("{\n"
                + "  \"key\" : {\n"
                + "    \"field1\" : \"field1\",\n"
                + "    \"field2\" : 2389472389\n"
                + "  }\n"
                + "}",
                run("key", new TestPojo()));
    }

}
