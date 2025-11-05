package com.portfolio.bknd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins(
								"https://portfolio-2-0-eight-sage.vercel.app",
								"http://localhost:3000"
								)
						.allowedMethods("Get", "Post", "Put", "Delete")
						.allowedHeaders("*")
						.allowCredentials(false);
			}
		};
	}
	
}
