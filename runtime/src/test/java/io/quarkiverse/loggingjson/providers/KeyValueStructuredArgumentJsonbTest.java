package io.quarkiverse.loggingjson.providers;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;

public class KeyValueStructuredArgumentJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testKeyValues() throws IOException {
        assertEquals(format("{\"key\":null}"), run("key", null));
        assertEquals(format("{\"anotherKey\":null}"), run("anotherKey", null));
        assertEquals(format("{\"key\":324}"), run("key", (short) 324));
        assertEquals(format("{\"key\":324}"), run("key", 324));
        assertEquals(format("{\"key\":324}"), run("key", 324L));
        assertEquals(format("{\"key\":324.348}"), run("key", 324.348));
        assertEquals(format("{\"key\":324.348}"), run("key", 324.348d));
        assertEquals(format("{\"key\":324}"), run("key", BigInteger.valueOf(324)));
        assertEquals(format("{\"key\":324.348}"), run("key", BigDecimal.valueOf(324.348d)));
        assertEquals(format("{\"key\":\"value\"}"), run("key", "value"));
        assertEquals(format("{\"key\":[\"value\",\"value2\"]}"), run("key", new String[] { "value", "value2" }));
        assertEquals(format("{\"key\":[\"value\",\"value2\"]}"), run("key", Arrays.asList("value", "value2")));
        assertEquals(format("{\"key\":{}}"), run("key", new Object()));
        assertEquals(format("{\"key\":{\"field1\":\"field1\",\"field2\":2389472389}}"), run("key", new TestPojo()));
    }

    private String run(String key, Object value) throws IOException {
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
