package io.quarkiverse.loggingjson.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    void testNoRemovedJsonProviders() throws Exception {
        final Path logFile = Paths.get("target/quarkus.log");

        Matcher m = Pattern.compile("Removed beans:").matcher("");
        try (Stream<String> lines = Files.lines(logFile)) {
            final long count = lines.filter(line -> m.reset(line).matches()).count();
            Assertions.assertFalse(count > 0);
        }
    }

}
