package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.HostNameJsonProviderJsonbTest;

public class HostNameJsonProviderJacksonTest extends HostNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
