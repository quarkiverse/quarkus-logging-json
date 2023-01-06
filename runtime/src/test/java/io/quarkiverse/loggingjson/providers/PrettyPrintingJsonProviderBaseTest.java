package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;

abstract class PrettyPrintingJsonProviderBaseTest extends JsonBaseTest {

    private final JsonFactory jsonb = new JsonbJsonFactory(true);
    private final JsonFactory jackson = new JacksonJsonFactory(true);

    @Override
    protected JsonFactory getJsonFactory() {
        switch (type()) {
            case JSONB:
                return jsonb;
            case JACKSON:
                return jackson;
            default:
                throw new RuntimeException("Unsupported type");
        }
    }

}
