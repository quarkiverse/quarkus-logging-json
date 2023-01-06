package io.quarkiverse.loggingjson.providers;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;

public class PrettyPrintingKeyValueStructuredArgumentJsonbTest extends PrettyPrintingJsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testKeyValues() throws IOException {
        assertEquals("\n"
                + "{\n"
                + "    \"key\": null\n"
                + "}",
                run("key", null));
        assertEquals("\n"
                + "{\n"
                + "    \"anotherKey\": null\n"
                + "}",
                run("anotherKey", null));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324\n"
                + "}",
                run("key", (short) 324));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324\n"
                + "}",
                run("key", 324));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324\n"
                + "}",
                run("key", 324L));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324.348\n"
                + "}",
                run("key", 324.348));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324.348\n"
                + "}",
                run("key", 324.348d));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324\n"
                + "}",
                run("key", BigInteger.valueOf(324)));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": 324.348\n"
                + "}",
                run("key", BigDecimal.valueOf(324.348d)));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": \"value\"\n"
                + "}",
                run("key", "value"));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": [\n"
                + "        \"value\",\n"
                + "        \"value2\"\n"
                + "    ]\n"
                + "}",
                run("key", new String[] { "value", "value2" }));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": [\n"
                + "        \"value\",\n"
                + "        \"value2\"\n"
                + "    ]\n"
                + "}",
                run("key", Arrays.asList("value", "value2")));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": {\n"
                + "    }\n"
                + "}",
                run("key", new Object()));
        assertEquals("\n"
                + "{\n"
                + "    \"key\": {\n"
                + "        \"field1\": \"field1\",\n"
                + "        \"field2\": 2389472389\n"
                + "    }\n"
                + "}",
                run("key", new TestPojo()));
    }

    protected String run(String key, Object value) throws IOException {
        StringBuilderWriter w = new StringBuilderWriter();
        try (JsonGenerator generator = getGenerator(w)) {
            generator.writeStartObject();

            kv(key, value).writeTo(generator);
            generator.writeEndObject();
            generator.flush();
        }
        return w.toString();
    }

    public static class TestPojo {
        private final String field1 = "field1";
        private final Long field2 = 2389472389L;

        public String getField1() {
            return field1;
        }

        public Long getField2() {
            return field2;
        }
    }
}
