package io.quarkiverse.loggingjson.deployment;

import java.util.Collections;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.bootstrap.model.AppArtifact;
import io.quarkus.test.QuarkusUnitTest;

class JsonDefaultFormatterSourceFieldsJacksonTest extends JsonDefaultFormatterBaseTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .setForcedDependencies(Collections.singletonList(
                    new AppArtifact("io.quarkus", "quarkus-jackson", System.getProperty("test.quarkus.version"))))
            .withConfigurationResource("application-json-source-fields.properties");

    @Test
    void testFormatterUsingJackson() throws Exception {
        testLogOutputWithSourceFields();
    }
}
