package ${packageName}.converters;

import ${packageName}.domains.${entityName}Domain;
import ${packageName}.dtos.${entityName}DTO;

import org.springframework.stereotype.Component;

@Component
public class ${entityName}DomainToDTOConverter {

    public ${entityName}DTO convert(final ${entityName}Domain domain) {
        return ${entityName}DTO.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(domain.get${attribute.name?cap_first}())
            </#list>
            .build();
    }
}