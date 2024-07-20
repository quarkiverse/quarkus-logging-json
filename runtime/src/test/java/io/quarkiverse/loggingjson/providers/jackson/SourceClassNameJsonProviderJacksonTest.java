package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.SourceClassNameJsonProviderJsonbTest;

public class SourceClassNameJsonProviderJacksonTest extends SourceClassNameJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
