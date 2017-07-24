package com.phengtola.ui;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.phengtola")
public class WebUiSpringBootWebApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebUiSpringBootWebApplication.class);
    }

    public static void main(String[] args){
        SpringApplication.run(WebUiSpringBootWebApplication.class , args);
    }

}
