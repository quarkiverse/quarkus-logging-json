package io.quarkiverse.loggingjson.deployment.providers.custom;

import java.util.Collections;

import javax.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import io.quarkiverse.loggingjson.deployment.JsonDefaultFormatterBaseTest;
import io.quarkus.bootstrap.model.AppArtifact;
import io.quarkus.test.QuarkusUnitTest;

class CustomJsonProviderJsonbTest extends JsonDefaultFormatterBaseTest {
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(FirstCustomJsonProvider.class, SecondCustomJsonProvider.class, ThirdCustomJsonProvider.class))
            .setForcedDependencies(Collections.singletonList(
                    new AppArtifact("io.quarkus", "quarkus-jsonb-deployment", System.getProperty("test.quarkus.version"))))
            .withConfigurationResource("application-json.properties");

    @Inject
    FirstCustomJsonProvider firstCustomJsonProvider;

    @Inject
    SecondCustomJsonProvider secondCustomJsonProvider;

    @Inject
    ThirdCustomJsonProvider thirdCustomJsonProvider;

    @Test
    void testCustomJsonProvider() throws Exception {
        org.slf4j.Logger log = LoggerFactory.getLogger("JsonStructuredTest");

        log.info("testCustomJsonProvider");

        String[] lines = logLines();
        Assertions.assertTrue(lines.length > 0);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(lines[lines.length - 1], JsonNode.class);
        Assertions.assertTrue(jsonNode.isObject());

        ImmutableList<String> fields = ImmutableList.copyOf(jsonNode.fieldNames());
        Assertions.assertTrue(fields.contains("first"));
        Assertions.assertTrue(firstCustomJsonProvider.getWriteToNumberOfCalls() > 0);

        Assertions.assertTrue(fields.contains("second"));
        Assertions.assertEquals(1, secondCustomJsonProvider.getIsEnabledNumberOfCalls());
        Assertions.assertTrue(secondCustomJsonProvider.getWriteToNumberOfCalls() > 0);

        Assertions.assertFalse(fields.contains("third"));
        Assertions.assertEquals(1, thirdCustomJsonProvider.getIsEnabledNumberOfCalls());
        Assertions.assertEquals(0, thirdCustomJsonProvider.getWriteToNumberOfCalls());
    }
}
