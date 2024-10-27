package com.github.jimsp.genhex4j.configuration;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

@Configuration
public class FreemarkerConfiguration {

	@Bean
	@SneakyThrows
	public freemarker.template.Configuration freemarkerConfig() {

		final freemarker.template.Configuration configuration = new freemarker.template.Configuration(
				freemarker.template.Configuration.VERSION_2_3_33);
		
		configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		return configuration;
	}
}