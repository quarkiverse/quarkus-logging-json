package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.MDCJsonProviderJsonbTest;

public class MDCJsonProviderJacksonTest extends MDCJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
