package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.AdditionalFieldsJsonProviderJsonbTest;

public class AdditionalFieldsJsonProviderJacksonTest extends AdditionalFieldsJsonProviderJsonbTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }
}
