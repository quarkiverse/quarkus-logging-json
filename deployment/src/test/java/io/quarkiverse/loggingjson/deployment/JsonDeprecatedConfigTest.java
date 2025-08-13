package io.quarkiverse.loggingjson.deployment;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jboss.logmanager.handlers.ConsoleHandler;
import org.jboss.logmanager.handlers.FileHandler;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.loggingjson.JsonFormatter;
import io.quarkiverse.loggingjson.deployment.testutil.DefaultDependency;
import io.quarkus.bootstrap.logging.InitialConfigurator;
import io.quarkus.bootstrap.logging.QuarkusDelayedHandler;
import io.quarkus.test.QuarkusUnitTest;

/**
 * In this test, we check that the feature is disabled as it is the only way to check the relocation/fallback works.
 */
class JsonDeprecatedConfigTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class))
            .setForcedDependencies(Collections.singletonList(new DefaultDependency("quarkus-jackson")))
            .withConfigurationResource("application-json-deprecated-config.properties");

    static {
        System.setProperty("java.util.logging.manager", org.jboss.logmanager.LogManager.class.getName());
    }

    @Test
    void testJsonLoggingDisabled() {
        LogManager logManager = LogManager.getLogManager();
        Assertions.assertInstanceOf(org.jboss.logmanager.LogManager.class, logManager);

        QuarkusDelayedHandler delayedHandler = InitialConfigurator.DELAYED_HANDLER;
        Assertions.assertTrue(Arrays.asList(Logger.getLogger("").getHandlers()).contains(delayedHandler));
        Assertions.assertEquals(Level.ALL, delayedHandler.getLevel());

        Handler consoleHandler = Arrays.stream(delayedHandler.getHandlers())
                .filter(h -> (h instanceof ConsoleHandler))
                .findFirst().orElse(null);
        Assertions.assertNotNull(consoleHandler);

        Assertions.assertFalse(consoleHandler.getFormatter() instanceof JsonFormatter);

        Handler fileHandler = Arrays.stream(delayedHandler.getHandlers())
                .filter(h -> (h instanceof FileHandler))
                .findFirst().orElse(null);
        Assertions.assertNotNull(fileHandler);

        Assertions.assertFalse(fileHandler.getFormatter() instanceof JsonFormatter);
    }
}
