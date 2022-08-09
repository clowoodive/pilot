package clowoodive.pilot.cloud.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ConfigClientApplication {
    @Value("${pilot.lang}")
    private String lang;

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    @GetMapping(value = "/{profile}/lang", produces = MediaType.TEXT_PLAIN_VALUE)
    public String profileLang(@PathVariable("profile") String profile) {
        return String.format("%s profile lang is %s...\n", profile, lang);
    }
}
