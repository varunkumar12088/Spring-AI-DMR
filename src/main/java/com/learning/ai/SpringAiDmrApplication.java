package com.learning.ai;

import com.learning.ai.properties.AIProperties;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({AIProperties.class})
public class SpringAiDmrApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringAiDmrApplication.class, args);
		String[] beans = context.getBeanNamesForType(OpenAiChatModel.class);
		for (String beanName : beans) {
			System.out.println("Bean OpenAiChatModel type: " + beanName);
		}

	}

}
