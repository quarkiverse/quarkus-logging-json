package io.quarkiverse.loggingjson.deployment;

import java.util.Collections;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.maven.dependency.Dependency;
import io.quarkus.test.QuarkusExtensionTest;

class JsonDefaultFormatterJsonbTest extends JsonDefaultFormatterBaseTest {

    @RegisterExtension
    static final QuarkusExtensionTest config = new QuarkusExtensionTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .setForcedDependencies(Collections
                    .singletonList(Dependency.of("io.quarkus", "quarkus-jsonb", System.getProperty("test.quarkus.version"))))
            .withConfigurationResource("application-json.properties");

    @Test
    void testFormatterUsingJsonb() throws Exception {
        testLogOutput();
    }
}
