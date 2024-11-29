package com.tech_symfony.resource_server.system.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static String MY_TIME_ZONE= "Asia/Ho_Chi_Minh";
    public static ZoneId MY_ZONE_ID= TimeZone.getTimeZone(JacksonConfig.MY_TIME_ZONE).toZoneId();


    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        // Create a new ObjectMapper from the builder
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // Register JavaTimeModule for handling Java 8 date/time types
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);

        // Register the JavaTimeModule
        objectMapper.registerModule(javaTimeModule);

        // Set the default time zone to UTC+7
        TimeZone.setDefault(TimeZone.getTimeZone(MY_TIME_ZONE));

        // Optionally, you can set the default time zone for ObjectMapper
        objectMapper.configOverride(Instant.class)
                .setFormat(JsonFormat.Value.forPattern("dd-MM-yyyy HH:mm:ss")
                        .withTimeZone(TimeZone.getTimeZone(ZoneId.of(MY_TIME_ZONE))));

        return objectMapper;
    }
}
