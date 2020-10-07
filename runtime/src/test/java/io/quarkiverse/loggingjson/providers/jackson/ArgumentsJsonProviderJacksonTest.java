package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ArgumentsJsonProviderJsonbTest;

public class ArgumentsJsonProviderJacksonTest extends ArgumentsJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
