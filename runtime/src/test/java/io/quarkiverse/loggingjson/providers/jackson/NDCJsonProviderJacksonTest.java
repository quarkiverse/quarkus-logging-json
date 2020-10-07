package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.NDCJsonProviderJsonbTest;

public class NDCJsonProviderJacksonTest extends NDCJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
