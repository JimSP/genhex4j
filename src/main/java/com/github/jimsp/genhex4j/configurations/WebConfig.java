package com.github.jimsp.genhex4j.configurations;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
	
	@Bean
	public WebApplicationType webApplicationType() {
		return WebApplicationType.SERVLET;
	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        
    	return new WebMvcConfigurer() {
            
        	@Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
