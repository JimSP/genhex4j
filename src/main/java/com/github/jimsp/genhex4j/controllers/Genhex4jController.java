package com.github.jimsp.genhex4j.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.dto.Genhex4jDTO;
import com.github.jimsp.genhex4j.dto.TemplateDTO;
import com.github.jimsp.genhex4j.session.SessionGenerator;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
public class Genhex4jController {
	
	private final SessionGenerator sessionGenerator;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> createProject(@Valid @RequestBody final Genhex4jDTO genhex4jDTO) {
		
		final List<TemplateDTO> templates = genhex4jDTO.getTemplates();
        final List<TemplateDescriptor> standardTemplates = genhex4jDTO.getStandardTemplates();
        final List<TemplateDescriptor> rulesTemplates = genhex4jDTO.getRulesTemplates();
		final EntityDescriptor entityDescriptor = genhex4jDTO.getEntityDescriptor();
		
		final byte[] genhex4j = sessionGenerator.execute(entityDescriptor, templates, standardTemplates, rulesTemplates);
		
		return ResponseEntity
				.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"genhex4j.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(genhex4j);
		
	}
}
