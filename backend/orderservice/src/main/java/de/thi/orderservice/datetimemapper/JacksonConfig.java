package de.thi.orderservice.datetimemapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JacksonConfig {

    public JacksonConfig(ObjectMapper objectMapper) {
        // Registrierung des JavaTimeModules für die Unterstützung von LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
    }
}