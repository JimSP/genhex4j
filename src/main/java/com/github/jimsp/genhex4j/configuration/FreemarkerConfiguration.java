package com.github.jimsp.genhex4j.configuration;

import java.io.File;

import org.springframework.context.annotation.Bean;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfiguration {

	@Bean
	@SneakyThrows
	public Configuration freeMarkerConfig() {
		final Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);

		cfg.setDirectoryForTemplateLoading(new File("Z:\\genhex4j\\templates"));
		cfg.setDefaultEncoding("UTF-8");
		
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);

		return cfg;
	}
}