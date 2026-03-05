package com.example.paymentgateway.paymentgateway;

import java.util.TimeZone;
import java.util.Base64;
import io.jsonwebtoken.security.Keys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootApplication
public class PaymentgatewayApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(PaymentgatewayApplication.class, args);
//		String key = Base64.getEncoder()
//            .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
//	
//	    System.out.println(key);
	
	
	}
	
	
	
	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	            	registry.addMapping("/**")
	                .allowedOriginPatterns("*") 
	                .allowCredentials(true) 
	                .allowedMethods("GET", "POST", "PUT", "DELETE") 
	                .allowedHeaders("*"); 
	            }
	        };
	    }

	 
}
