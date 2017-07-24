package com.phengtola.webservices;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.phengtola")
public class WebServiceSpringBootWebApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebServiceSpringBootWebApplication.class);
    }

    public static void main(String[] args){
        SpringApplication.run(WebServiceSpringBootWebApplication.class , args);
    }

}
