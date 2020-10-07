package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.SequenceJsonProviderJsonbTest;

public class SequenceJsonProviderJacksonTest extends SequenceJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
