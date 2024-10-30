<#import "testValueModule.ftl" as testValues>
package ${packageName}.services;

import ${packageName}.domains.${entityName}Domain;
import ${packageName}.repositories.${entityName}RepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class ${entityName}ServiceAdapterTest {

    @Mock
    private ${entityName}RepositoryPort repositoryPort;

    @InjectMocks
    private ${entityName}ServiceAdapter ${entityName?uncap_first}ServiceAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        final ${entityName}Domain domain = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        when(repositoryPort.findById(domain.getId())).thenReturn(Optional.of(domain));

        final ${entityName}Domain result = ${entityName?uncap_first}ServiceAdapter.findById(domain.getId());

        assertNotNull(result);
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), result.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testSave${entityName}() {
        final ${entityName}Domain domain = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ${entityName}Domain result = ${entityName?uncap_first}ServiceAdapter.save(domain);

        assertNotNull(result);
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain.get${attribute.name?cap_first}(), result.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repositoryPort).deleteById(id);

        ${entityName?uncap_first}ServiceAdapter.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    void testFindAllWithFilters() {
        final ${entityName}Domain filterDomain = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        final ${entityName}Domain domain1 = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        final ${entityName}Domain domain2 = ${entityName}Domain
                .builder()
                <#list domainDescriptor.attributes as attribute>
                    .${attribute.name}(<@testValues.generateTestValue attribute.type />)
                </#list>
                .build();

        final List<${entityName}Domain> domainList = Arrays.asList(domain1, domain2);
        final Page<${entityName}Domain> domainPage = new PageImpl<>(domainList);

        when(repositoryPort.searchWithFilters(filterDomain, Pageable.unpaged())).thenReturn(domainPage);

        final Page<${entityName}Domain> results = ${entityName?uncap_first}ServiceAdapter.searchWithFilters(filterDomain, Pageable.unpaged());

        assertEquals(2, results.getTotalElements());
        <#list domainDescriptor.attributes as attribute>
        assertEquals(domain1.get${attribute.name?cap_first}(), results.getContent().get(0).get${attribute.name?cap_first}());
        assertEquals(domain2.get${attribute.name?cap_first}(), results.getContent().get(1).get${attribute.name?cap_first}());
        </#list>
    }
}
