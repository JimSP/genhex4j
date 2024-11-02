package com.github.jimsp.genhex4j.descriptors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = RuleDescriptor.RuleDescriptorBuilder.class)
public class RuleDescriptor {

	@JsonProperty
	String ruleName;
	
	@JsonProperty
	String description;
	
	@JsonProperty
	String ruleInput;
	
	@JsonProperty 
	String ruleAdditionalInput;
	
	@JsonProperty
	String ruleOutput;
	
	@JsonProperty
	String llmGeneratedLogic;
	
	@JsonProperty
	String javaFunctionalInterface;
	
	@JsonProperty
	String javaFuncionalIntefaceMethodName;
}
