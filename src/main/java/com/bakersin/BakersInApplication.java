package com.bakersin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BakersInApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BakersInApplication.class, args);
    }

    // Required so the external Tomcat container can bootstrap the app from web.xml/WAR
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BakersInApplication.class);
    }
}
