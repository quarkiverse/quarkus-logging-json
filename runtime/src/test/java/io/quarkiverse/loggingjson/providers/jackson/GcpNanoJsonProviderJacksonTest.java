package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpNanoJsonProviderTest;

public class GcpNanoJsonProviderJacksonTest extends GcpNanoJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
