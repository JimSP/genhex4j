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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(domain));

        final ${entityName}Domain result = ${entityName?uncap_first}Service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testSave${entityName}() {
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ${entityName}Domain result = ${entityName?uncap_first}Service.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repositoryPort).deleteById(id);

        final ${entityName?uncap_first}Service.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    public void testFindAll() {
        final ${entityName}Domain domain1 = new ${entityName}Domain(1L, "Example1", 0.0);
        final ${entityName}Domain domain2 = new ${entityName}Domain(2L, "Example2", 1.0);
        final List<${entityName}Domain> domainList = Arrays.asList(domain1, domain2);

        when(repositoryPort.findAll()).thenReturn(domainList);

        final List<${entityName}Domain> results = ${entityName?uncap_first}Service.findAll();

        assertEquals(2, results.size());
        assertEquals("Example1", results.get(0).getNome());
        assertEquals("Example2", results.get(1).getNome());
    }
}
