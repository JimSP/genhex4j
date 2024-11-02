package com.github.jimsp.genhex4j.session;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;
import com.github.jimsp.genhex4j.zip.ZipGenerator;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Component
@Slf4j
public class SessionGenerator {

	TemplateProcessor templateProcessor;
	FileSystem inMemoryFileSystem;
	ZipGenerator zipGenerator;

	@SneakyThrows
    public byte[] execute(final EntityDescriptor entityDescriptor) {
		
		final Path fileSystemIdentifier = inMemoryFileSystem.getPath(UUID.randomUUID().toString());
		
		if(!Files.exists(fileSystemIdentifier)) {
	    	Files.createDirectories(fileSystemIdentifier);
	    	log.info("m=generateFileFromTemplate, msg=\"created root directory {}\"", fileSystemIdentifier);
	    }
        
        final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        templateProcessor.processStandardTemplates(standardTemplates, entityDescriptor, fileSystemIdentifier);
        
        final List<TemplateDescriptor> ruleTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
        templateProcessor.processRuleTemplates(ruleTemplates, entityDescriptor, fileSystemIdentifier);
        
        return zipGenerator.createZip(fileSystemIdentifier);
    }
}
