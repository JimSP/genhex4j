package com.github.jimsp.genhex4j.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
@JsonDeserialize(builder = TemplateDTO.TemplateDTOBuilder.class)
public class TemplateDTO {
	
	@JsonProperty
	@NotBlank
	String name;
	
	@JsonProperty
	@NotBlank
	String content;

}
