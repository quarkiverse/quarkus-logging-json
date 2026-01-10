package io.quarkiverse.loggingjson.providers;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KeyValueStructuredArgumentToStringTest {

    static class Pojo {
        private final String key;
        private final String value;

        public Pojo(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Pojo{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    @Test
    void testFormat() {
        Assertions.assertEquals("key=value", kv("key", "value").toString());
        Assertions.assertEquals("key=1", kv("key", 1).toString());
        Assertions.assertEquals("key=1.0", kv("key", 1.0).toString());
        Assertions.assertEquals("key=true", kv("key", true).toString());
        Assertions.assertEquals("key=Pojo{key='k', value='v'}", kv("key", new Pojo("k", "v")).toString());
        Assertions.assertEquals("key:value", kv("key", "value", "%s:%s").toString());
        Assertions.assertEquals("key=[value, 1, 1.0, true]", kv("key", List.of("value", 1, 1.0, true)).toString());
    }

}
