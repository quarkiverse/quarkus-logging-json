package io.quarkiverse.loggingjson.providers;

public class PrettyPrintingJsonProviderJsonbTest extends KeyValueStructuredArgumentJsonbTest {

    @Override
    protected boolean prettyPrint() {
        return true;
    }

}
