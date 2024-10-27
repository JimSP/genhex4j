package br.com.myapp.repositories;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.converters.ProdutoDomainToEntityConverter;
import br.com.myapp.converters.ProdutoEntityToDomainConverter;
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

public class ProdutoRepositoryAdapterTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoDomainToEntityConverter domainToEntityConverter;

    @Mock
    private ProdutoEntityToDomainConverter entityToDomainConverter;

    @InjectMocks
    private ProdutoRepositoryAdapter produtoRepositoryAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        final ProdutoEntity entity1 = new ProdutoEntity(1L, "Example1", 0.0);
        final ProdutoEntity entity2 = new ProdutoEntity(2L, "Example2", 1.0);
        final List<ProdutoEntity> entities = Arrays.asList(entity1, entity2);
        final ProdutoDomain domain1 = new ProdutoDomain(1L, "Example1", 0.0);
        final ProdutoDomain domain2 = new ProdutoDomain(2L, "Example2", 1.0);

        when(repository.findAll()).thenReturn(entities);
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final List<ProdutoDomain> domains = produtoRepositoryAdapter.findAll();

        assertEquals(2, domains.size());
        assertEquals("Example1", domains.get(0).getNome());
        assertEquals("Example2", domains.get(1).getNome());
    }

    @Test
    public void testFindById() {
        final ProdutoEntity entity = new ProdutoEntity(1L, "Example", 0.0);
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<ProdutoDomain> result = produtoRepositoryAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Example", result.get().getNome());
    }

    @Test
    public void testSave() {
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);
        final ProdutoEntity entity = new ProdutoEntity(1L, "Example", 0.0);

        when(domainToEntityConverter.convert(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final ProdutoDomain result = produtoRepositoryAdapter.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repository).deleteById(id);

        final produtoRepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
