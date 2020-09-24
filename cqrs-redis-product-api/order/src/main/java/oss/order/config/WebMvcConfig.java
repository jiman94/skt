package oss.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/api/orderlist")
                .allowedOrigins("http://localhost:8087");

    }
}
