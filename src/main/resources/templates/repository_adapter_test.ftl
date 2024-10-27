package ${packageName}.repositories;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.entities.${entityName}Entity;
import ${packageName}.converters.${entityName}DomainToEntityConverter;
import ${packageName}.converters.${entityName}EntityToDomainConverter;
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
        final ${entityName}Entity entity1 = new ${entityName}Entity(1L, "Example1", 0.0);
        final ${entityName}Entity entity2 = new ${entityName}Entity(2L, "Example2", 1.0);
        final List<${entityName}Entity> entities = Arrays.asList(entity1, entity2);
        final ${entityName}Domain domain1 = new ${entityName}Domain(1L, "Example1", 0.0);
        final ${entityName}Domain domain2 = new ${entityName}Domain(2L, "Example2", 1.0);

        when(repository.findAll()).thenReturn(entities);
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final List<${entityName}Domain> domains = ${entityName?uncap_first}RepositoryAdapter.findAll();

        assertEquals(2, domains.size());
        assertEquals("Example1", domains.get(0).getNome());
        assertEquals("Example2", domains.get(1).getNome());
    }

    @Test
    public void testFindById() {
        final ${entityName}Entity entity = new ${entityName}Entity(1L, "Example", 0.0);
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<${entityName}Domain> result = ${entityName?uncap_first}RepositoryAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Example", result.get().getNome());
    }

    @Test
    public void testSave() {
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);
        final ${entityName}Entity entity = new ${entityName}Entity(1L, "Example", 0.0);

        when(domainToEntityConverter.convert(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final ${entityName}Domain result = ${entityName?uncap_first}RepositoryAdapter.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repository).deleteById(id);

        final ${entityName?uncap_first}RepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
