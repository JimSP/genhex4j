package ${packageName}.converters;

import ${packageName}.entities.${entityName}Entity;
import ${packageName}.domains.${entityName}Domain;

import org.springframework.stereotype.Component;

@Component
public class ${entityName}JPAToDomainConverter {

    public ${entityName}Domain convert(final ${entityName}Entity entity) {
        return ${entityName}Domain.builder()
            <#list jpaDescriptor.attributes as attribute>
                .${attribute.name}(entity.get${attribute.name?cap_first}())
            </#list>
            .build();
    }
}