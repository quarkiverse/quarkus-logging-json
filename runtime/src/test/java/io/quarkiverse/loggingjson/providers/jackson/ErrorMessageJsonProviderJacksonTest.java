package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ErrorMessageJsonProviderJsonbTest;

public class ErrorMessageJsonProviderJacksonTest extends ErrorMessageJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
