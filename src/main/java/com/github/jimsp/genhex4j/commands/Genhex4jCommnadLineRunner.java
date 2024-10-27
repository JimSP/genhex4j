package com.github.jimsp.genhex4j.commands;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.jimsp.genhex4j.descriptors.DescriptorReader;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;

import lombok.Value;

@Component
@Value
public class Genhex4jCommnadLineRunner implements CommandLineRunner {
	
	
	DescriptorReader descriptorReader;
	TemplateProcessor templateProcessor;

	@Override
	public void run(String... args) throws Exception {
		
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		templateProcessor.processTemplates(entityDescriptor);
		
	}

}
