package io.quarkiverse.loggingjson.providers.jackson;

public class PrettyPrintingJsonProviderJacksonTest extends KeyValueStructuredArgumentJacksonTest {

    @Override
    protected boolean prettyPrint() {
        return true;
    }

}
