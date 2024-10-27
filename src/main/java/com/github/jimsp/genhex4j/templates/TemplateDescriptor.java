package com.github.jimsp.genhex4j.templates;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = TemplateDescriptor.TemplateDescriptorBuilder.class)
public class TemplateDescriptor {
	
	@JsonProperty
	String templateName;
	
	@JsonProperty
	String outputPathPattern;
	
	@JsonProperty
	String dataModelKey;
	
	@JsonProperty
	Map<String, Object> additionalData;
}
