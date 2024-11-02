package com.github.jimsp.genhex4j.descriptors;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = EntityDescriptor.EntityDescriptorBuilder.class) 
public class EntityDescriptor {
	
	@JsonProperty
	String packageName;
	
	@JsonProperty
    String entityName;
	
	@JsonProperty
    Map<AttributeType, List<AttributeDescriptor>> attributesMap;
	
	@JsonProperty
    DomainDescriptor domainDescriptor;
	
	@JsonProperty
    DtoDescriptor dtoDescriptor;
    
    @JsonProperty
    JpaDescriptor jpaDescriptor;
    
    @JsonProperty
    List<RuleDescriptor> rulesDescriptor;

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
