package com.phengtola.webservices.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by tolapheng on 7/24/17.
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Swagger
        registry.addViewController("/docs/api/v1").setViewName("/swagger/swagger-ui");
        registry.addViewController("/swagger-ui.html").setViewName("/swagger/swagger-ui");


        registry.addViewController("/error/403").setViewName("/error/access-denied");
        registry.addViewController("/404").setViewName("/error/404");


    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * Static Resources store in the project
		 */
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/");
		/*
		 * Static Resources store outside the project
		 */
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/opt/FILES_MANAGEMENT/images/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET","POST","DELETE","PUT","OPTIONS","PATCH")
                //.allowedOrigins("*");
                .allowedOrigins("*");
    }



}
