package org.dgika.massive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "massive.api")
public record MassiveProperties(
        String baseUrl,
        String key) {
}
