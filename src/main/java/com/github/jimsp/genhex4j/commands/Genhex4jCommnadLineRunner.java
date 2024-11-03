package com.github.jimsp.genhex4j.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.jimsp.genhex4j.descriptors.DescriptorReader;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.dto.TemplateDTO;
import com.github.jimsp.genhex4j.session.Loader;
import com.github.jimsp.genhex4j.session.SessionGenerator;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Component
@Value
@Slf4j
public class Genhex4jCommnadLineRunner implements CommandLineRunner {
	
	SessionGenerator sessionGenerator;
	DescriptorReader descriptorReader;
	TemplateProcessor templateProcessor;
	Loader loader;

	@Override
    public void run(String... args) throws Exception {
		
		final List<TemplateDTO> templates = loader.execute();
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        final List<TemplateDescriptor> rulesTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
		
		final Pair<String, byte[]> genhex4j = sessionGenerator.execute(entityDescriptor, templates, standardTemplates, rulesTemplates);
		
		saveZipFile(genhex4j.getValue(), Paths.get(genhex4j.getKey() + "-genhex4j.zip"));
    }

	
	@SneakyThrows
	private void saveZipFile(final byte[] zipContent, final Path destinationPath) {
		log.info("destinationPath: {}", destinationPath);
        Files.write(destinationPath, zipContent);
    }
}
