package io.quarkiverse.loggingjson.providers.jackson;

import io.quarkiverse.loggingjson.providers.GcpMessageWithExceptionJsonProviderTest;

public class GcpMessageWithExceptionJsonProviderJacksonTest extends GcpMessageWithExceptionJsonProviderTest {
    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
