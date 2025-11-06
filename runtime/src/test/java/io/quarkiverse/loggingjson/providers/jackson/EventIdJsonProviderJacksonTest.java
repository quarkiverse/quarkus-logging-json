package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.EventIdJsonProviderJsonbTest;

public class EventIdJsonProviderJacksonTest extends EventIdJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
