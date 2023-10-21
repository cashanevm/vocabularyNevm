package com.nevm.vocabulary.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.rapid")
@Data
public class RapidProperties {
    private String key;
}
