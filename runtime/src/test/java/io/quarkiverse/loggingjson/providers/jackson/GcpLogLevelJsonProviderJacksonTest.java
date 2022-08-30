package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpLogLevelJsonProviderTest;

public class GcpLogLevelJsonProviderJacksonTest extends GcpLogLevelJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
