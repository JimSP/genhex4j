<#import "/testValueModule.ftl" as testValues>

package ${packageName}.repositories;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.entity.${entityName}Entity;
import ${packageName}.entity.${entityName}DomainToJPAConverter;
import ${packageName}.entity.${entityName}JPAToDomainConverter;

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

public class ${entityName}RepositoryAdapterTest {

    @Mock
    private ${entityName}Repository repository;

    @Mock
    private ${entityName}DomainToEntityConverter domainToEntityConverter;

    @Mock
    private ${entityName}EntityToDomainConverter entityToDomainConverter;

    @InjectMocks
    private ${entityName}RepositoryAdapter ${entityName?uncap_first}RepositoryAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        final ${entityName}Entity entity1 = new ${entityName}Entity(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );
        final ${entityName}Entity entity2 = new ${entityName}Entity(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );

        final List<${entityName}Entity> entities = Arrays.asList(entity1, entity2);

        final ${entityName}Domain domain1 = new ${entityName}Domain(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );
        final ${entityName}Domain domain2 = new ${entityName}Domain(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );

        when(repository.findAll()).thenReturn(entities);
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final List<${entityName}Domain> domains = ${entityName?uncap_first}RepositoryAdapter.findAll();

        assertEquals(2, domains.size());
        assertEquals(domain1, domains.get(0));
        assertEquals(domain2, domains.get(1));
    }

    @Test
    public void testFindById() {
        final ${entityName}Entity entity = new ${entityName}Entity(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );

        final ${entityName}Domain domain = new ${entityName}Domain(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );

        when(repository.findById(testValues.generateTestValue("Long"))).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<${entityName}Domain> result = ${entityName?uncap_first}RepositoryAdapter.findById(testValues.generateTestValue("Long"));

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    public void testSave() {
        final ${entityName}Domain domain = new ${entityName}Domain(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );
        
        final ${entityName}Entity entity = new ${entityName}Entity(
            <#list attributes as attribute>
                testValues.generateTestValue("${attribute.type}") <#if attribute_has_next>,</#if>
            </#list>
        );

        when(domainToEntityConverter.convert(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final ${entityName}Domain result = ${entityName?uncap_first}RepositoryAdapter.save(domain);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    public void testDeleteById() {
        final Long id = testValues.generateTestValue("Long");

        doNothing().when(repository).deleteById(id);

        ${entityName?uncap_first}RepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
