package com.github.jimsp.genhex4j.templates;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = TemplateConfig.TemplateConfigBuilder.class)
public class TemplateConfig {
	
	@JsonProperty
	List<TemplateDescriptor> templates;
}
