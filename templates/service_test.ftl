<#import "/testValueModule.ftl" as testValues>

package ${packageName}.services;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.repositories.${entityName}RepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ${entityName}ServiceTest {

    @Mock
    private ${entityName}RepositoryPort repositoryPort;

    @InjectMocks
    private ${entityName}Service ${entityName?uncap_first}Service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        final ${entityName}Domain domain = new ${entityName}Domain(
            <#list domainDescriptor.attributes as attribute>
                <@testValues.generateTestValue attribute.type /> <#if attribute_has_next>,</#if>
            </#list>
        );

        when(repositoryPort.findById(domain.getId())).thenReturn(Optional.of(domain));

        final ${entityName}Domain result = ${entityName?uncap_first}Service.findById(domain.getId());

        assertNotNull(result);
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), result.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testSave${entityName}() {
        final ${entityName}Domain domain = new ${entityName}Domain(
            <#list domainDescriptor.attributes as attribute>
                <@testValues.generateTestValue attribute.type /> <#if attribute_has_next>,</#if>
            </#list>
        );

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ${entityName}Domain result = ${entityName?uncap_first}Service.save(domain);

        assertNotNull(result);
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), result.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testDeleteById() {
        final Long id = <@testValues.generateTestValue "Long" />;

        doNothing().when(repositoryPort).deleteById(id);

        final ${entityName?uncap_first}Service.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    public void testFindAll() {
        final ${entityName}Domain domain1 = new ${entityName}Domain(
            <#list domainDescriptor.attributes as attribute>
                <@testValues.generateTestValue attribute.type /> <#if attribute_has_next>,</#if>
            </#list>
        );

        final ${entityName}Domain domain2 = new ${entityName}Domain(
            <#list domainDescriptor.attributes as attribute>
                <@testValues.generateTestValue attribute.type /> <#if attribute_has_next>,</#if>
            </#list>
        );

        final List<${entityName}Domain> domainList = Arrays.asList(domain1, domain2);

        when(repositoryPort.findAll()).thenReturn(domainList);

        final List<${entityName}Domain> results = ${entityName?uncap_first}Service.findAll();

        assertEquals(2, results.size());
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain1.get${attribute.name?cap_first}(), results.get(0).get${attribute.name?cap_first}());
        assertEquals(domain2.get${attribute.name?cap_first}(), results.get(1).get${attribute.name?cap_first}());
        </#list>
    }
}