package com.github.jimsp.genhex4j.descriptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = RuleDescriptor.RuleDescriptorBuilder.class)
public class RuleDescriptor {

	@JsonProperty
	@NotBlank
	String ruleName;
	
	@JsonProperty
	@NotBlank
	String description;
	
	@JsonProperty
	@NotBlank
	String ruleInput;
	
	@JsonProperty
	@NotBlank
	String ruleAdditionalInput;
	
	@JsonProperty
	@NotBlank
	String ruleOutput;
	
	@JsonProperty
	@NotBlank
	String llmGeneratedLogic;
	
	@JsonProperty
	@NotBlank
	String javaFunctionalInterface;
	
	@JsonProperty
	@NotBlank
	String javaFuncionalIntefaceMethodName;
}
