package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpSpanIdJsonProviderTest;

public class GcpSpanIdJsonProviderJacksonTest extends GcpSpanIdJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
