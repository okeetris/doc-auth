package com.raiseGreen.rg_auth_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class RgAuthSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RgAuthSvcApplication.class, args);
	}

}
