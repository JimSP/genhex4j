<#import "testValueModule.ftl" as testValues>

package ${packageName}.repositories;

import ${packageName}.domains.${entityName}Domain;
import ${packageName}.entities.${entityName}Entity;
import ${packageName}.converters.${entityName}DomainToJPAConverter;
import ${packageName}.converters.${entityName}JPAToDomainConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ${entityName}RepositoryAdapterTest {

    @Mock
    private ${entityName}Repository repository;

    @Mock
    private ${entityName}DomainToJPAConverter domainToEntityConverter;

    @Mock
    private ${entityName}JPAToDomainConverter entityToDomainConverter;

    @InjectMocks
    private ${entityName}RepositoryAdapter ${entityName?uncap_first}RepositoryAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        final Long id = 1L;
        final ${entityName}Entity entity = new ${entityName}Entity(
            <#list attributes as attribute>
                <@testValues.generateTestValue attribute.type!'String' /> <#if attribute_has_next>,</#if>
            </#list>
        );
        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(<@testValues.generateTestValue attribute.type!'String' />)
            </#list>
            .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<${entityName}Domain> result = ${entityName?uncap_first}RepositoryAdapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void testSave() {
        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(<@testValues.generateTestValue attribute.type!'String' />)
            </#list>
            .build();
        final ${entityName}Entity entity = new ${entityName}Entity(
            <#list attributes as attribute>
                <@testValues.generateTestValue attribute.type!'String' /> <#if attribute_has_next>,</#if>
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
    void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repository).deleteById(id);

        ${entityName?uncap_first}RepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testSearchWithFilters() {
        final ${entityName}Domain filterDomain = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(<@testValues.generateTestValue attribute.type!'String' />)
            </#list>
            .build();
        final Pageable pageable = Pageable.unpaged();

        final ${entityName}Entity entity1 = new ${entityName}Entity(
            <#list attributes as attribute>
                <@testValues.generateTestValue attribute.type!'String' /> <#if attribute_has_next>,</#if>
            </#list>
        );
        final ${entityName}Entity entity2 = new ${entityName}Entity(
            <#list attributes as attribute>
                <@testValues.generateTestValue attribute.type!'String' /> <#if attribute_has_next>,</#if>
            </#list>
        );

        final List<${entityName}Entity> entities = List.of(entity1, entity2);
        final Page<${entityName}Entity> entityPage = new PageImpl<>(entities);

        final ${entityName}Domain domain1 = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(<@testValues.generateTestValue attribute.type!'String' />)
            </#list>
            .build();
        final ${entityName}Domain domain2 = ${entityName}Domain.builder()
            <#list domainDescriptor.attributes as attribute>
                .${attribute.name}(<@testValues.generateTestValue attribute.type!'String' />)
            </#list>
            .build();

        when(repository.findAll(any(), eq(pageable))).thenReturn(entityPage);
        
        when(domainToEntityConverter.convert(domain1)).thenReturn(entity1);
        when(domainToEntityConverter.convert(domain2)).thenReturn(entity2);
        
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final Page<${entityName}Domain> result = ${entityName?uncap_first}RepositoryAdapter.searchWithFilters(filterDomain, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(domain1, result.getContent().get(0));
        assertEquals(domain2, result.getContent().get(1));
    }
}
