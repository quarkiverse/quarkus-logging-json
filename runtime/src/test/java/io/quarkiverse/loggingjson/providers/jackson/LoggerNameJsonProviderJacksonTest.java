package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.LoggerNameJsonProviderJsonbTest;

public class LoggerNameJsonProviderJacksonTest extends LoggerNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
