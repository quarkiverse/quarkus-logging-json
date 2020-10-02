# Quarkiverse Logging Json
Quarkus logging extension outputting the logging in json.

#Structured argument
If you want to do structured logging of arguments, then the argument send with your logging, can implement `io.quarkus.logging.json.structured.StructuredArgument`. Then it is possible to use the JsonGenerator to format the argument in json. 

## Simple usage
```java
import static io.quarkus.logging.json.structured.KeyValueStructuredArgument.*;
...
log.info("Test log of structured arg", kv("key", "value"));
```
