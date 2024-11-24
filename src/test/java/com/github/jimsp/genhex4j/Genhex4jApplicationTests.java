package com.github.jimsp.genhex4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "SPRING_APPLICATION_NAME=genhex4j",
		"SPRING_AI_OPENAI_API_KEY=0",
		"SPRING_AI_OPENAI_BASE_URL=https://api.groq.com/openai",
		"SPRING_AI_OPENAI_CHAT_OPTIONS_MODEL=llama-3.2-90b-text-preview",
		"SPRING_AI_OPENAI_CHAT_OPTIONS_TEMPERATURE=0.7", "SERVER_SSL_KEY_STORE=classpath:keystore.jks",
		"SERVER_SSL_KEY_STORE_PASSWORD=0", "SERVER_SSL_KEY_STORE_TYPE=JKS", "SERVER_SSL_KEY_ALIAS=backend-ssl" })
class Genhex4jApplicationTests {
	
	@Test
	void contextLoads() {
	}
}
