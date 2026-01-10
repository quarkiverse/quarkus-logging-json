[![Maven Central](https://img.shields.io/maven-central/v/io.quarkiverse.loggingjson/quarkus-logging-json?logo=apache-maven&style=for-the-badge)](https://search.maven.org/artifact/io.quarkiverse.loggingjson/quarkus-logging-json)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/quarkiverse_quarkus-logging-json?logo=sonarcloud&server=https%3A%2F%2Fsonarcloud.io&style=for-the-badge)](https://sonarcloud.io/dashboard?id=quarkiverse_quarkus-logging-json)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->
# Quarkus Logging JSON
Quarkus logging extension outputting the log messages in JSON.
It supports the following formats: default, [Elastic Common Schema (ECS)](https://www.elastic.co/guide/en/ecs-logging/overview/current/intro.html).

## Version to use
| Quarkus Version | Use version  |
|-----------------|--------------|
| 3.x.x           | 3.x.x        |
| 2.x.x           | 1.x.x, 2.x.x |

# Configuration
The extension is enabled by default for console and socket, when added to the project.
Console json logging can be disabled using configuration: `quarkus.log.json.console.enabled=false`
Socket json logging can be disabled using configuration: `quarkus.log.json.socket.enabled=false`

To see additional configuration options take a look at [Config](https://quarkiverse.github.io/quarkiverse-docs/quarkus-logging-json/dev/index.html)

## Elastic Common Scheme
```properties
quarkus.log.json.log-format=ecs
```

# Add additional fields to all log messages
If you want to add a static field to all the log message, that is possible using the configuration.
```properties
quarkus.log.json.additional-field.serviceName.value=service-a
# type is by default STRING - Other is INT, LONG, FLOAT, DOUBLE 
quarkus.log.json.additional-field.buildNumber.type=INT
quarkus.log.json.additional-field.buildNumber.value=42
```

# Structured argument
If you want to do structured logging of arguments, then the argument send with your logging, can implement `io.quarkiverse.loggingjson.providers.StructuredArgument`. Then it is possible to use the JsonGenerator to format the argument in json. 

## Simple usage
```java
import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.*;
...
log.info("Test log of structured arg", kv("key", "value"));
```

Using `io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv` within the message is supported by formating the key/value pair using `String#format()`. By default, the format string is `%s=%s` which can customized.

```java
import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.*;
...
log.info("Test log of structured arg {}", kv("key1", "value1")); // (1)
log.info("Test log of structured arg {}", kv("key2", "value2", "%s: %s")); // (2)
```

Message `(1)` is formatted as `"Test log of structured arg key1=value1"` and `(2)` as `"Test log of structured arg key2: value2"`.

# Custom log handler
If you want to add your own custom way to handle the LogRecords.
You can create your own implementations of `io.quarkiverse.loggingjson.JsonProvider`, and provide it using CDI.
Example implementation:
```java
import jakarta.inject.Singleton;
import java.io.IOException;

import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonGenerator;
import org.jboss.logmanager.ExtLogRecord;

@Singleton
public class MyJsonProvider implements JsonProvider {

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        generator.writeStringField("myCustomField", "and my custom value"); // Will be added to every log, as a field on the json.
    }
}
```

## Configuration Properties Relocation

In 3.2.0, two configuration properties were renamed to be more consistent with Quarkus configuration.

- Old: `quarkus.log.json.console.enable` → New: `quarkus.log.json.console.enabled`
- Old: `quarkus.log.json.file.enable` → New: `quarkus.log.json.file.enabled`

A relocation/fallback mechanism has been added so the old properties are still supported
but we recomment that you switch to the new ones
(`quarkus update` will do it for you).

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/SlyngDK"><img src="https://avatars2.githubusercontent.com/u/6666094?v=4" width="100px;" alt=""/><br /><sub><b>Simon Bengtsson</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-logging-json/commits?author=SlyngDK" title="Code">💻</a> <a href="#maintenance-SlyngDK" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

`
