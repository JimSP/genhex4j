
package br.com.myapp.repositories;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.entity.ProdutoEntity;
import br.com.myapp.entity.ProdutoDomainToJPAConverter;
import br.com.myapp.entity.ProdutoJPAToDomainConverter;

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
        final ProdutoEntity entity1 = new ProdutoEntity(
        );
        final ProdutoEntity entity2 = new ProdutoEntity(
        );

        final List<ProdutoEntity> entities = Arrays.asList(entity1, entity2);

        final ProdutoDomain domain1 = new ProdutoDomain(
        );
        final ProdutoDomain domain2 = new ProdutoDomain(
        );

        when(repository.findAll()).thenReturn(entities);
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final List<ProdutoDomain> domains = produtoRepositoryAdapter.findAll();

        assertEquals(2, domains.size());
        assertEquals(domain1, domains.get(0));
        assertEquals(domain2, domains.get(1));
    }

    @Test
    public void testFindById() {
        final ProdutoEntity entity = new ProdutoEntity(
        );

        final ProdutoDomain domain = new ProdutoDomain(
        );

        when(repository.findById(testValues.generateTestValue("Long"))).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<ProdutoDomain> result = produtoRepositoryAdapter.findById(testValues.generateTestValue("Long"));

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    public void testSave() {
        final ProdutoDomain domain = new ProdutoDomain(
        );
        
        final ProdutoEntity entity = new ProdutoEntity(
        );

        when(domainToEntityConverter.convert(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final ProdutoDomain result = produtoRepositoryAdapter.save(domain);

        assertNotNull(result);
        assertEquals(domain, result);
    }

    @Test
    public void testDeleteById() {
        final Long id = testValues.generateTestValue("Long");

        doNothing().when(repository).deleteById(id);

        produtoRepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
