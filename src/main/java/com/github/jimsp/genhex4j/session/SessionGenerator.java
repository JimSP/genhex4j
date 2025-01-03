package com.github.jimsp.genhex4j.session;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.dto.TemplateDTO;
import com.github.jimsp.genhex4j.templates.LLMCredencials;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;
import com.github.jimsp.genhex4j.zip.ZipGenerator;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Service
@Slf4j
public class SessionGenerator {

	TemplateProcessor templateProcessor;
	FileSystem inMemoryFileSystem;
	ZipGenerator zipGenerator;

	@SneakyThrows
    public Pair<String, byte[]> execute(LLMCredencials llmCredencials, EntityDescriptor entityDescriptor, final List<TemplateDTO> templates, final List<TemplateDescriptor> standardTemplates, final List<TemplateDescriptor> ruleTemplates) {
		
		final String fileSystemIdentifierString = UUID.randomUUID().toString();
		final Path fileSystemIdentifier = inMemoryFileSystem.getPath(fileSystemIdentifierString);
		
		if(!Files.exists(fileSystemIdentifier)) {
	    	createDirectories(fileSystemIdentifier);
	    	log.info("m=generateFileFromTemplate, msg=\"created root directory {}\"", fileSystemIdentifier);
	    }
		
		final Configuration freemakerConfiguration = freemakerConfiguration(fileSystemIdentifierString, templates);
		
        templateProcessor.processStandardTemplates(freemakerConfiguration, standardTemplates, entityDescriptor, fileSystemIdentifier); 
        templateProcessor.processRuleTemplates(llmCredencials, freemakerConfiguration, ruleTemplates, entityDescriptor, fileSystemIdentifier);
        
        return Pair.of(fileSystemIdentifierString, zipGenerator.createZip(fileSystemIdentifier));
    }
	
	@SneakyThrows
	private Configuration freemakerConfiguration(final String fileSystemIdentifier, final List<TemplateDTO> templates) {
		
		final Path path = Paths.get(fileSystemIdentifier);
		
		if(!Files.exists(path)) {
			
			createDirectories(path);
		}
		
		templates.forEach(template->{
			
			final String fileName = template.getName();
			
			final Path name = path.resolve(fileName);
		    final Path parentDir = name.getParent();

		    if (!Files.exists(parentDir)) {
		        createDirectories(parentDir);
		    }
			
			final String content = template.getContent();
			
			write(name, content);
		});
		
		final Configuration freemakerConfiguration = new Configuration(Configuration.VERSION_2_3_33);

		freemakerConfiguration.setDirectoryForTemplateLoading(new File(fileSystemIdentifier + "/templates"));
		freemakerConfiguration.setDefaultEncoding("UTF-8");
		
		freemakerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		freemakerConfiguration.setLogTemplateExceptions(false);
		freemakerConfiguration.setWrapUncheckedExceptions(true);

		return freemakerConfiguration;
	}

	@SneakyThrows
	private Path createDirectories(final Path parentDir) {
		return Files.createDirectories(parentDir);
	}

	@SneakyThrows
	private Path write(final Path path, final String content) {
		
		if(!Files.exists(path)) {
			Files.createFile(path);
		}
		
		return Files.writeString(path, content);
	}
}
