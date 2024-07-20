package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.SourceFileNameJsonProviderJsonbTest;

public class SourceFileNameJsonProviderJacksonTest extends SourceFileNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
