package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ErrorTypeJsonProviderJsonbTest;

public class ErrorTypeJsonProviderJacksonTest extends ErrorTypeJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
