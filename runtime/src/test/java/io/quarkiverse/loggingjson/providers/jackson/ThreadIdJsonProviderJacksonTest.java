package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.ThreadIdJsonProviderJsonbTest;

public class ThreadIdJsonProviderJacksonTest extends ThreadIdJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
