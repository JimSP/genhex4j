package com.github.jimsp.genhex4j.descriptors;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
@JsonDeserialize(builder = EntityDescriptor.EntityDescriptorBuilder.class) 
public class EntityDescriptor {
	
	@JsonProperty
	@NotBlank
	String packageName;
	
	@JsonProperty
	@NotBlank
    String entityName;
	
	@JsonProperty
	@NotNull
	@NotEmpty
    Map<AttributeType, @NotEmpty List<@NotNull AttributeDescriptor>> attributesMap;
	
	@JsonProperty
	@NotNull
    DomainDescriptor domainDescriptor;
	
	@JsonProperty
	@NotNull
    DtoDescriptor dtoDescriptor;
    
    @JsonProperty
    @NotNull
    JpaDescriptor jpaDescriptor;
    
    @JsonProperty
    @NotBlank
    String systemPrompt;
    
    @JsonProperty
    @NotNull
    @NotEmpty
    List<@NotNull RuleDescriptor> rulesDescriptor;

    public void addAttributes(final AttributeType key, final List<AttributeDescriptor> attributes) {
    	
    	if(this.attributesMap != null) {
    		this.attributesMap.put(key, attributes);
    	}
    }

    public List<AttributeDescriptor> getAttributesByKey(final AttributeType key) {
    	
    	if(this.attributesMap != null) {
    		return this.attributesMap.getOrDefault(key, Collections.emptyList());
    	}
        return Collections.emptyList();
    }
}
