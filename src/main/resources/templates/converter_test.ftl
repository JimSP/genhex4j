package ${packageName}.converter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ${packageName}.domain.${entityName}Domain;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.entity.${entityName}Entity;
import ${packageName}.converter.${entityName}Converter;

public class ${entityName}ConverterTest {

    @Test
    public void testDtoToDomainConversion() {
        final ${entityName}DTO dto = ${entityName}DTO.builder()<#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>)</#list>
            .build();

        final ${entityName}Domain domain = ${entityName}Converter.dtoToDomain(dto);

        <#list dtoDescriptor.attributes as attribute>
        assertEquals(dto.get${attribute.name?cap_first}(), domain.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testDomainToDtoConversion() {
        final ${entityName}Domain domain = ${entityName}Domain.builder()<#list domainDescriptor.attributes as attribute>
            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>)</#list>
            .build();

        final ${entityName}DTO dto = ${entityName}Converter.domainToDto(domain);

        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), dto.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testEntityToDomainConversion() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        entity.set${attribute.name?cap_first}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>);
        </#list>

        final ${entityName}Domain domain = ${entityName}Converter.entityToDomain(entity);

        <#list jpaDescriptor.attributes as attribute>
        assertEquals(entity.get${attribute.name?cap_first}(), domain.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testDomainToEntityConversion() {
        ${entityName}Domain domain = ${entityName}Domain.builder()<#list domainDescriptor.attributes as attribute>
            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if><#if attribute.type == "Integer">1</#if></#if>)</#list>
            .build();

        ${entityName}Entity entity = ${entityName}Converter.domainToEntity(domain);

        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), entity.get${attribute.name?cap_first}());
        </#list>
    }
}
