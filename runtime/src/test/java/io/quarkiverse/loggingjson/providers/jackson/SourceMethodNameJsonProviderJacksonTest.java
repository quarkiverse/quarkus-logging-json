package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.SourceMethodNameJsonProviderJsonbTest;

public class SourceMethodNameJsonProviderJacksonTest extends SourceMethodNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
