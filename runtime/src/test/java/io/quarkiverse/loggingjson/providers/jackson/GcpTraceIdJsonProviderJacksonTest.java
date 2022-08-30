package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpTraceIdJsonProviderTest;

public class GcpTraceIdJsonProviderJacksonTest extends GcpTraceIdJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
