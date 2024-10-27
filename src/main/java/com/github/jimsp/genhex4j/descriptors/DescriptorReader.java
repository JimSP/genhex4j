package com.github.jimsp.genhex4j.descriptors;
import java.io.File;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component
public class DescriptorReader {

	@SneakyThrows
    public EntityDescriptor readDescriptor() {
    	
    	final ObjectMapper mapper = new ObjectMapper();
    	
    	final EntityDescriptor entityDescriptor = mapper.readValue(new File("config/model.json"), EntityDescriptor.class);
    	
    	entityDescriptor.addAttributes(AttributeType.DOMAIN, entityDescriptor.getDomainDescriptor().getAttributes());
        entityDescriptor.addAttributes(AttributeType.DTO, entityDescriptor.getDtoDescriptor().getAttributes());
        entityDescriptor.addAttributes(AttributeType.JPA, entityDescriptor.getJpaDescriptor().getAttributes());
    	
        return entityDescriptor;
    }
}