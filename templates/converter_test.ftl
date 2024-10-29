<#-- Importa os módulos de templates necessários -->
<#import "testValueModule.ftl" as testValues>
<#import "dtoToEntityConverter.ftl" as dtoToEntityConverter>
<#import "entityToDtoConverter.ftl" as entityToDtoConverter>
<#import "generateValidations.ftl" as validations>
<#import "generateErrorMessage.ftl" as errorMessage>
<#import "generateResponse.ftl" as response>

package ${packageName}.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.dto.${entityName}DTOToDomainConverter;
import ${packageName}.dto.${entityName}DomainToDTOConverter;
import ${packageName}.entity.${entityName}Entity;
import ${packageName}.entity.${entityName}JPAToDomainConverter;
import ${packageName}.entity.${entityName}DomainToJPAConverter;

@SpringBootTest
class ${entityName}ConverterTest {

    @Autowired
    private ${entityName}DTOToDomainConverter dtoToDomainConverter;
    
    @Autowired
    private ${entityName}DomainToDTOConverter domainToDTOConverter;
    
    @Autowired
    private ${entityName}JPAToDomainConverter jpaToDomainConverter;
    
    @Autowired
    private ${entityName}DomainToJPAConverter domainToJPAConverter;

    @Test
    void testDtoToDomainConversion() {
        final ${entityName}DTO dto = ${entityName}DTO.builder()
            <#list dtoDescriptor.attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Domain domain = dtoToDomainConverter.convert(dto);

        <#list dtoDescriptor.attributes as attribute>
        assertEquals(dto.get${attribute.name?cap_first}(), domain.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testDomainToDtoConversion() {
        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}DTO dto = domainToDTOConverter.convert(domain);

        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), dto.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testEntityToDomainConversion() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        entity.set${attribute.name?cap_first}(testValues.generateTestValue(attribute.type));
        </#list>

        final ${entityName}Domain domain = jpaToDomainConverter.convert(entity);

        <#list jpaDescriptor.attributes as attribute>
        assertEquals(entity.get${attribute.name?cap_first}(), domain.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testDomainToEntityConversion() {
        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Entity entity = domainToJPAConverter.convert(domain);

        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), entity.get${attribute.name?cap_first}());
        </#list>
    }
}
