package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.LogLevelJsonProviderJsonbTest;

public class LogLevelJsonProviderJacksonTest extends LogLevelJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
