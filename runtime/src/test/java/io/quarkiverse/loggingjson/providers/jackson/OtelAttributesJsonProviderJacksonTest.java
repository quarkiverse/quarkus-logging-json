package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.OtelAttributesJsonProviderJsonbTest;

public class OtelAttributesJsonProviderJacksonTest extends OtelAttributesJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
