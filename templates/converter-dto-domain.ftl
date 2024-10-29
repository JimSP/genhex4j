package ${packageName}.dto;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.dto.${entityName}DTO;

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
