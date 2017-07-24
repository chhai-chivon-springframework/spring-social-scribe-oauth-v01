package com.phengtola.ui.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by tolapheng on 7/25/17.
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter{


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/home").setViewName("/login");
        registry.addViewController("/").setViewName("/login");
        registry.addViewController("/profile").setViewName("/facebook-profile");
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




}

