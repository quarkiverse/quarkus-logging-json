package io.quarkiverse.loggingjson.deployment;

import java.util.Collections;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.loggingjson.deployment.testutil.DefaultDependency;
import io.quarkus.test.QuarkusUnitTest;

class JsonECSFormatterJsonbTest extends JsonECSFormatterBaseTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .setForcedDependencies(Collections.singletonList(new DefaultDependency("quarkus-jsonb-deployment")))
            .withConfigurationResource("application-json-ecs.properties");

    @Test
    void testFormatterUsingJsonb() throws Exception {
        testLogOutput();
    }
}
