package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ThreadNameJsonProviderJsonbTest;

public class ThreadNameJsonProviderJacksonTest extends ThreadNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
