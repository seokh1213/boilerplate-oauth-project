package com.boilerplate.oauth.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class BoilerplateOauthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoilerplateOauthBackendApplication.class, args);
    }

}
