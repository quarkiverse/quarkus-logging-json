package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.LoggerClassNameJsonProviderJsonbTest;

public class LoggerClassNameJsonProviderJacksonTest extends LoggerClassNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
