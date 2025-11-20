package com.smalldogg.config;

import com.smalldogg.config.properties.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(KafkaProperties.class)
@Configuration
public class PropertiesConfig {}
