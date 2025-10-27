package io.quarkiverse.loggingjson;

class JsonFormatterJacksonTest extends JsonFormatterJsonbTest {

    public JsonFormatterJacksonTest() {
        expectedPrettyPrintMessage = "{\n  \"message\" : \"TestMessage\"\n}\n";
    }

    @Override
    protected Type type() {
        return Type.JACKSON;
    }

}
