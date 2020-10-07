package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.TimestampJsonProviderJsonbTest;

public class TimestampJsonProviderJacksonTest extends TimestampJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
