package com.github.jimsp.genhex4j.descriptors;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = DomainDescriptor.DomainDescriptorBuilder.class)
public class DomainDescriptor {
	
	@JsonProperty
	List<AttributeDescriptor> attributes;
}