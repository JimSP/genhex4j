package com.github.jimsp.genhex4j.descriptors;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = DtoDescriptor.DtoDescriptorBuilder.class)
public class DtoDescriptor {
	
	@JsonProperty
	@NotNull
	@NotEmpty
	List<@NotNull AttributeDescriptor> attributes;
}