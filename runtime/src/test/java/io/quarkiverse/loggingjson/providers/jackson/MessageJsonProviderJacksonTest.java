package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.MessageJsonProviderJsonbTest;

public class MessageJsonProviderJacksonTest extends MessageJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
