package com.github.jimsp.genhex4j.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.jimsp.genhex4j.descriptors.EntityDescriptor;
import com.github.jimsp.genhex4j.templates.TemplateDescriptor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
@JsonDeserialize(builder = Genhex4jDTO.Genhex4jDTOBuilder.class)
public class Genhex4jDTO {

	@JsonProperty
	@NotNull
	EntityDescriptor entityDescriptor;
	
	@JsonProperty
	@NotNull
	@NotEmpty
	List<@NotNull TemplateDescriptor> standardTemplates;
	
	@JsonProperty
	@NotNull
	@NotEmpty
    List<@NotNull TemplateDescriptor> rulesTemplates;
	
	@JsonProperty
	@NotNull
	@NotEmpty
	List<@NotNull TemplateDTO> templates;
	
	
}
