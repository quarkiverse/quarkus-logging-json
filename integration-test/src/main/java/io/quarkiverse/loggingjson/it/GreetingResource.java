package io.quarkiverse.loggingjson.it;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/hello")
public class GreetingResource {

    private static final Logger logger = Logger.getLogger(GreetingResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("Hello to \"Quarkus\"");
        return "hello";
    }
}
