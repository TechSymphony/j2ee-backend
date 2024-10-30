package com.tech_symfony.resource_server.system.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static String MY_TIME_ZONE= "Asia/Ho_Chi_Minh";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule to handle Java 8 date/time types
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        // Add a default deserializer for Instant (no specific format)
        module.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        // Set the default time zone to UTC+7 (Vietnam)
        objectMapper.configOverride(java.time.Instant.class)
                .setFormat(JsonFormat.Value.forPattern("dd-MM-yyyy HH:mm:ss")
                        .withTimeZone(TimeZone.getTimeZone(ZoneId.of(MY_TIME_ZONE))));


        objectMapper.registerModule(module);
        TimeZone.setDefault(TimeZone.getTimeZone(MY_TIME_ZONE));
        return objectMapper;
    }
}
