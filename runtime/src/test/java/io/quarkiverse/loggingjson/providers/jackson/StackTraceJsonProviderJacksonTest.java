package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.StackTraceJsonProviderJsonbTest;

public class StackTraceJsonProviderJacksonTest extends StackTraceJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
