package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.SourceLineNumberJsonProviderJsonbTest;

public class SourceLineNumberJsonProviderJacksonTest extends SourceLineNumberJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
