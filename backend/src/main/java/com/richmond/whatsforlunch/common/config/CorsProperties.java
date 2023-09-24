package com.richmond.whatsforlunch.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Load configuration.
 * @param allowed list of allowed origins
 */
@ConfigurationProperties(prefix = "whatsforlunch.cors")
public record CorsProperties(List<String> allowed) {
}
