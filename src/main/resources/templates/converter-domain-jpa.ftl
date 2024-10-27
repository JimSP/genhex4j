package ${packageName}.entity;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.entity.${entityName}Entity;

import org.springframework.stereotype.Component;

@Component
public class ${entityName}DomainToJPAConverter {

    public ${entityName}Entity convert(final ${entityName}Domain domain) {
        return ${entityName}Entity.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(domain.get${attribute.name?cap_first}())
            </#list>
            .build();
    }
}
