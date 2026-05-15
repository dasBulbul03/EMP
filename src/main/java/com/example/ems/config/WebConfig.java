package com.example.ems.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Basic web configuration.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Redirect the root path to the employees view.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/employees");
    }
}


