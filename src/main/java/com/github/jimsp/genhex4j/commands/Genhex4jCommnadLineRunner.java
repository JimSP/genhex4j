package com.github.jimsp.genhex4j.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.jimsp.genhex4j.descriptors.DescriptorReader;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.session.SessionGenerator;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Component
@Value
@Slf4j
public class Genhex4jCommnadLineRunner implements CommandLineRunner {
	
	SessionGenerator sessionGenerator;
	DescriptorReader descriptorReader;
	TemplateProcessor templateProcessor;

	@Override
    public void run(String... args) throws Exception {
		
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		
		final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        final List<TemplateDescriptor> rulesTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
		
		final byte[] genhex4j = sessionGenerator.execute(entityDescriptor, standardTemplates, rulesTemplates);
		
		saveZipFile(genhex4j, Paths.get("genhex4j.zip"));
    }
	
	private void saveZipFile(final byte[] zipContent, final Path destinationPath) throws IOException {
		log.info("destinationPath: {}", destinationPath);
        Files.write(destinationPath, zipContent);
    }

}
