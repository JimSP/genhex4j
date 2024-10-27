package com.github.jimsp.genhex4j.descriptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = AttributeDescriptor.AttributeDescriptorBuilder.class)
public class AttributeDescriptor {
	
	@JsonProperty
    String name;
	
	@JsonProperty
    String type;
	
	@JsonProperty
    Boolean primaryKey;
	
	@JsonProperty
    Boolean required;
	
	@JsonProperty
    Integer maxLength;
	
	@JsonProperty
    Boolean generatedValue;
	
	@JsonProperty
    String columnDefinition;
}