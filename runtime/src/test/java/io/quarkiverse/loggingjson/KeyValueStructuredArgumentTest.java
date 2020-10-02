package io.quarkiverse.loggingjson;

import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyValueStructuredArgumentTest {

    @Test
    void testUsingJackson() throws Exception {
        testWithFactory(new JacksonJsonFactory());
    }

    @Test
    void testUsingJsonb() throws Exception {
        testWithFactory(new JsonbJsonFactory());
    }

    private void testWithFactory(JsonFactory factory) throws IOException {
        assertEquals("{\"key\":null}", run(factory, "key", null));
        assertEquals("{\"anotherKey\":null}", run(factory, "anotherKey", null));
        assertEquals("{\"key\":324}", run(factory, "key", (short) 324));
        assertEquals("{\"key\":324}", run(factory, "key", 324));
        assertEquals("{\"key\":324}", run(factory, "key", 324L));
        assertEquals("{\"key\":324.348}", run(factory, "key", 324.348));
        assertEquals("{\"key\":324.348}", run(factory, "key", 324.348d));
        assertEquals("{\"key\":324}", run(factory, "key", BigInteger.valueOf(324)));
        assertEquals("{\"key\":324.348}", run(factory, "key", BigDecimal.valueOf(324.348d)));
        assertEquals("{\"key\":\"value\"}", run(factory, "key", "value"));
        assertEquals("{\"key\":[\"value\",\"value2\"]}", run(factory, "key", new String[]{"value", "value2"}));
        assertEquals("{\"key\":[\"value\",\"value2\"]}", run(factory, "key", Arrays.asList("value", "value2")));
        assertEquals("{\"key\":{}}", run(factory, "key", new Object()));
        assertEquals("{\"key\":{\"field1\":\"field1\",\"field2\":2389472389}}", run(factory, "key", new TestPojo()));
    }

    private String run(JsonFactory factory, String key, Object value) throws IOException {
        StringBuilderWriter w = new StringBuilderWriter();
        try (JsonGenerator generator = factory.createGenerator(w)) {
            generator.writeStartObject();

            new KeyValueStructuredArgument(key, value).writeTo(generator);
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
