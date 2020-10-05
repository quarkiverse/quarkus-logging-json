package io.quarkiverse.loggingjson.deployment;

import java.util.Arrays;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.bootstrap.model.AppArtifact;
import io.quarkus.test.QuarkusUnitTest;

public class JsonFormatterJsonbTest extends JsonFormatterBaseTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .setForcedDependencies(Arrays.asList(
                    new AppArtifact("io.quarkus", "quarkus-jsonb-deployment", System.getProperty("test.quarkus.version")),
                    new AppArtifact("org.jboss.slf4j", "slf4j-jboss-logging", "1.2.1.Final") // FIXME Remove when quarkus is updated
            ))
            .withConfigurationResource("application-json.properties");

    @Test
    void testFormaterUsingJsonb() throws Exception {
        testLogOutput();
    }
}
