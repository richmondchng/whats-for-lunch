package com.richmond.whatsforlunch.users.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Load configuration.
 * @param secret
 */
@ConfigurationProperties(prefix = "whatsforlunch")
public record UserSecurityProperties(String secret) { }
