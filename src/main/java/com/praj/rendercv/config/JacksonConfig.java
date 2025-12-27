package com.praj.rendercv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    /**
     * Configures the YAML mapper used for parsing the user's Resume YAML input.
     */
    @Bean
    public YAMLMapper yamlMapper() {
        YAMLMapper mapper = new YAMLMapper();
        // Support Java 8 Date/Time API in YAML if needed
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Configures the default JSON mapper used for API responses.
     * The @Primary annotation ensures Spring uses this for your RestControllers.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Register the module that handles LocalDateTime
        mapper.registerModule(new JavaTimeModule());

        // 2. Ensure dates are written as ISO strings (e.g., "2025-12-27T...")
        // instead of numeric timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
