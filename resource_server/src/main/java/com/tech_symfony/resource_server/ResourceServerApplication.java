package com.tech_symfony.resource_server;

import com.tech_symfony.resource_server.system.config.JacksonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;


@SpringBootApplication
public class ResourceServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone(JacksonConfig.MY_TIME_ZONE));
    }

}
