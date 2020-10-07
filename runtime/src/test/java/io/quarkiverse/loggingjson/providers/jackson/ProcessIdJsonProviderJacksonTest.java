package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ProcessIdJsonProviderJsonbTest;

public class ProcessIdJsonProviderJacksonTest extends ProcessIdJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
