package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ProcessNameJsonProviderJsonbTest;

public class ProcessNameJsonProviderJacksonTest extends ProcessNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
