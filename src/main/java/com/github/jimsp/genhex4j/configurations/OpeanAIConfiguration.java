package com.github.jimsp.genhex4j.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.jimsp.genhex4j.templates.LLMCredencials;

@Configuration
public class OpeanAIConfiguration {

	@org.springframework.beans.factory.annotation.Value("${spring.ai.openai.api-key}")
	private String openAiApiKey;
	
	@org.springframework.beans.factory.annotation.Value("${spring.ai.openai.base-url}")
	private String openAiBaseUrl;
	
	@org.springframework.beans.factory.annotation.Value("${spring.ai.openai.chat.options.model}")
	private String chatOptionsModel;
	
	@org.springframework.beans.factory.annotation.Value("${spring.ai.openai.chat.options.temperature}")
	private Float chatOptionsTemperature;
	
	@Bean
	public LLMCredencials llmCredencials() {
		
		return LLMCredencials
				.builder()
				.openAiApiKey(openAiApiKey)
				.openAiBaseUrl(openAiBaseUrl)
				.chatOptionsModel(chatOptionsModel)
				.chatOptionsTemperature(Float.valueOf(chatOptionsTemperature))
				.build();
	}
}
