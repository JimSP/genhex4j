package com.github.jimsp.genhex4j.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.jimsp.genhex4j.descriptors.DescriptorReader;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.dto.TemplateDTO;
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

	@Override
    public void run(String... args) throws Exception {
		
		final List<TemplateDTO> templates = list()
				.map(path->TemplateDTO
						.builder()
						.name(path.toString())
						.content(read(path))
						.build())
				.toList();
		
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        final List<TemplateDescriptor> rulesTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
		
		final byte[] genhex4j = sessionGenerator.execute(entityDescriptor, templates, standardTemplates, rulesTemplates);
		
		saveZipFile(genhex4j, Paths.get("genhex4j.zip"));
    }

	@SneakyThrows
	private String read(Path path) {
		return Files.readString(path);
	}

	@SneakyThrows
	private Stream<Path> list() {
		return Files.list(Paths.get("templates"));
	}
	
	@SneakyThrows
	private void saveZipFile(final byte[] zipContent, final Path destinationPath) {
		log.info("destinationPath: {}", destinationPath);
        Files.write(destinationPath, zipContent);
    }
}
