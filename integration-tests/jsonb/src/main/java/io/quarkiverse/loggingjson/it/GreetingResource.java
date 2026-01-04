package io.quarkiverse.loggingjson.it;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

import java.time.OffsetDateTime;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.quarkus.runtime.annotations.RegisterForReflection;

@Path("/hello")
public class GreetingResource {

    private static final Logger logger = Logger.getLogger(GreetingResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("Hello to \"Quarkus\"");
        return "hello";
    }

    @GET()
    @Path("/mapper")
    @Produces(MediaType.TEXT_PLAIN)
    public String mapper() {
        logger.infov("Mapper test",
                kv("OffsetDateTime", OffsetDateTime.parse("2026-01-01T01:00:00.000Z")),
                kv("empty", new Object()),
                kv("testObj", new Testing()));
        return "hello2";
    }

    @RegisterForReflection
    public static class Testing {
        private String name;
        private OffsetDateTime time;

        public Testing() {
            this.name = "asdf";
            this.time = OffsetDateTime.now();
        }

        public String getName() {
            return name;
        }

        public OffsetDateTime getTime() {
            return time;
        }
    }
}
