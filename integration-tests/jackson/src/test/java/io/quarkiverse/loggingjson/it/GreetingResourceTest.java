package io.quarkiverse.loggingjson.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Tag("integration")
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    void testMapperEndpoint() throws IOException {
        final Path logFile = Paths.get("target/quarkus.log");
        try (FileInputStream is = new FileInputStream(logFile.toFile())) {
            while (is.available() > 0) {
                is.skip(is.available());
            }

            given()
                    .when().get("/hello/mapper")
                    .then()
                    .statusCode(200)
                    .body(is("hello2"));

            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                while (is.available() > 0) {
                    bytes.write(is.readNBytes(is.available()));
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new ByteArrayInputStream(bytes.toByteArray()), StandardCharsets.UTF_8))) {

                    final List<String> lines = reader.lines().collect(Collectors.toList());
                    final List<String> matchingLines = lines.stream()
                            .filter(line -> line.startsWith("{") && line.contains("\"Mapper test\""))
                            .collect(Collectors.toList());
                    Assertions.assertEquals(1, matchingLines.size());

                    JsonNode jsonNode = new ObjectMapper().readTree(lines.get(0));
                    Assertions.assertEquals("Mapper test", jsonNode.get("message").asText());
                    Assertions.assertEquals("2026-01-01T01:00:00Z", jsonNode.get("OffsetDateTime").asText());

                    JsonNode testObj = jsonNode.get("testObj");
                    Assertions.assertEquals("asdf", testObj.get("name").asText());
                    Assertions.assertTrue(testObj.has("time"));
                    Assertions.assertTrue(testObj.get("time").isTextual());
                }

            }
        }
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
