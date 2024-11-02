package com.github.jimsp.genhex4j.descriptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = AttributeDescriptor.AttributeDescriptorBuilder.class)
public class AttributeDescriptor {
	
	@JsonProperty
	@NotBlank
    String name;
	
	@JsonProperty
	@NotBlank
    String type;
	
	@JsonProperty
	@NotNull
    Boolean primaryKey;
	
	@JsonProperty
	@NotNull
    Boolean required;
	
	@JsonProperty
	@Positive
    Integer maxLength;
	
	@JsonProperty
	@NotNull
    Boolean generatedValue;
	
	@JsonProperty
	@NotBlank
    String columnDefinition;
}