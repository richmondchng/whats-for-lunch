package com.richmond.whatsforlunch;

import com.richmond.whatsforlunch.common.config.CorsProperties;
import com.richmond.whatsforlunch.users.config.UserSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ UserSecurityProperties.class, CorsProperties.class })
public class WhatsforlunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhatsforlunchApplication.class, args);
	}

}
