package com.learning.ai;

import com.learning.ai.properties.AIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AIProperties.class})
public class SpringAiDmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiDmrApplication.class, args);
	}

}
