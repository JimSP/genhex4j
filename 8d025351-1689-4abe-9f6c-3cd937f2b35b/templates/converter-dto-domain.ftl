package ${packageName}.converters;

import ${packageName}.domains.${entityName}Domain;
import ${packageName}.dtos.${entityName}DTO;

import org.springframework.stereotype.Component;

@Component
public class ${entityName}DTOToDomainConverter {

    public ${entityName}Domain convert(final ${entityName}DTO dto) {
        return ${entityName}Domain.builder()
            <#list dtoDescriptor.attributes as attribute>
                .${attribute.name}(dto.get${attribute.name?cap_first}())
            </#list>
            .build();
    }
}
