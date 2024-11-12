package com.github.jimsp.genhex4j.controllers;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.github.jimsp.genhex4j.descriptors.DescriptorReader;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.descriptors.RuleDescriptor;
import com.github.jimsp.genhex4j.dto.Genhex4jDTO;
import com.github.jimsp.genhex4j.dto.TemplateDTO;
import com.github.jimsp.genhex4j.session.Loader;
import com.github.jimsp.genhex4j.session.SessionGenerator;
import com.github.jimsp.genhex4j.templates.LLMCredencials;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateProcessor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
public class Genhex4jController {
	
	private final SessionGenerator sessionGenerator;
	private final Loader loader;
	private final DescriptorReader descriptorReader;
	private final TemplateProcessor templateProcessor;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Genhex4jDTO> getTemplateProject() {
		
		final List<TemplateDTO> templates = loader.execute();
		
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        final List<TemplateDescriptor> rulesTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
		
		return ResponseEntity
				.ok(Genhex4jDTO
						.builder()
						.entityDescriptor(entityDescriptor)
						.standardTemplates(standardTemplates)
						.rulesTemplates(rulesTemplates)
						.templates(templates)
						.build());
		
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> createProject(@RequestHeader final HttpHeaders headers, @Valid @RequestBody final Genhex4jDTO genhex4jDTO) {
		
		final LLMCredencials llmCredencials = headersToLLmCredencial(headers);
		
		final List<TemplateDTO> templates = genhex4jDTO.getTemplates();
		
        final List<TemplateDescriptor> standardTemplates = genhex4jDTO.getStandardTemplates();
        final List<TemplateDescriptor> rulesTemplates = genhex4jDTO.getRulesTemplates();
		final EntityDescriptor entityDescriptor = genhex4jDTO.getEntityDescriptor();
		
		final Pair<String, byte[]> genhex4j = generateCode(llmCredencials, templates, standardTemplates, rulesTemplates, entityDescriptor);
		
		return createResponse(genhex4j.getValue());
		
	}
	
	@PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> createProjectMin(@RequestHeader final HttpHeaders headers, @RequestBody final List<RuleDescriptor> rules) {
		
		final LLMCredencials llmCredencials = headersToLLmCredencial(headers);
		
		final List<TemplateDTO> templates = loader.execute();
		
		final EntityDescriptor entityDescriptor = descriptorReader.readDescriptor();
		final List<TemplateDescriptor> standardTemplates = templateProcessor.loadTemplates(template -> !template.getTemplateName().contains("rule"));
        final List<TemplateDescriptor> rulesTemplates = templateProcessor.loadTemplates(template -> template.getTemplateName().contains("rule"));
        
        final EntityDescriptor entityDescriptorUpdated = entityDescriptor.toBuilder().rulesDescriptor(rules).build();
		
		final Pair<String, byte[]> genhex4j = generateCode(llmCredencials, templates, standardTemplates, rulesTemplates, entityDescriptorUpdated);
		
		return createResponse(genhex4j.getValue());
		
	}
	
	private LLMCredencials headersToLLmCredencial(final HttpHeaders headers) {
		
		final String openAiApiKey =  extractHeaderVariable(headers, "OPENAI-API-KEY");
		final String openAiBaseUrl = extractHeaderVariable(headers, "OPENAI-BASE-URL");
		final String chatOptionsModel = extractHeaderVariable(headers, "CHAT-OPTIONS-MODEL");
		final Float chatOptionsTemperature = Float.parseFloat(extractHeaderVariable(headers, "CHAT-OPTIONS-TEMPERATURE"));
		
		return LLMCredencials
				.builder()
				.openAiApiKey(openAiApiKey)
				.openAiBaseUrl(openAiBaseUrl)
				.chatOptionsModel(chatOptionsModel)
				.chatOptionsTemperature(chatOptionsTemperature)
				.build();
	}

	private String extractHeaderVariable(final HttpHeaders headers, final String key) {
		
		if(headers.containsKey(key)) {
			final List<String> vars = headers.get(key);
			return vars != null ? vars.stream().findFirst().orElse(null) : null;
		}
		
		throw new InvalidParameterException(key + " not present in the header, the attribute is mandatory.");
	}
	
	private final Pair<String, byte[]> generateCode(final LLMCredencials llmCredencials, final List<TemplateDTO> templates, final List<TemplateDescriptor> standardTemplates,
			final List<TemplateDescriptor> rulesTemplates, final EntityDescriptor entityDescriptor) {
		
		return sessionGenerator.execute(llmCredencials, entityDescriptor, templates, standardTemplates, rulesTemplates);
	}
	
	private ResponseEntity<byte[]> createResponse(final byte[] genhex4j) {
		
		return ResponseEntity
				.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"genhex4j.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(genhex4j);
	}
}
