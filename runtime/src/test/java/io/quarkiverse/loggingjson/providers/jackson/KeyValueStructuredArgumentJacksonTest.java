package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.KeyValueStructuredArgumentJsonbTest;

class KeyValueStructuredArgumentJacksonTest extends KeyValueStructuredArgumentJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
