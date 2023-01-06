package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;

abstract class JsonProviderBaseTest extends JsonBaseTest {

    private static final JsonFactory jsonb = new JsonbJsonFactory();
    private static final JsonFactory jackson = new JacksonJsonFactory();

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
