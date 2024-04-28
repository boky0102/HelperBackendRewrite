package com.example.helperbackend;

import com.example.helperbackend.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class HelperBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelperBackendApplication.class, args);

	}

}
