package com.github.jimsp.genhex4j.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.session.SessionGenerator;

import lombok.Value;

@Value
@RestController
public class Genhex4jController {
	
	SessionGenerator sessionGenerator;

	@PostMapping()
	public ResponseEntity<byte[]> createProject(@RequestBody final EntityDescriptor entityDescriptor) {
		
		final byte[] genhex4j = sessionGenerator.execute(entityDescriptor);
		
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"genhex4j.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(genhex4j);
		
	}
}
