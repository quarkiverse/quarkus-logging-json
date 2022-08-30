package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpEpochSecondJsonProviderTest;

public class GcpEpochSecondJsonProviderJacksonTest extends GcpEpochSecondJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
