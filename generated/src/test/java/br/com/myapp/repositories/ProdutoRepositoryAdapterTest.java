
package br.com.myapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.myapp.converters.ProdutoDomainToDTOConverter;
import br.com.myapp.converters.ProdutoDomainToJPAConverter;
import br.com.myapp.converters.ProdutoJPAToDomainConverter;
import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.entities.ProdutoEntity;

class ProdutoRepositoryAdapterTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoDomainToJPAConverter domainToEntityConverter;

    @Mock
    private ProdutoJPAToDomainConverter entityToDomainConverter;
    
    @Mock
    private ProdutoDomainToDTOConverter domainToDTOConverter;

    @InjectMocks
    private ProdutoRepositoryAdapter produtoRepositoryAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        final Long id = 1L;
        final ProdutoEntity entity = new ProdutoEntity(
        );
        final ProdutoDomain domain = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(entityToDomainConverter.convert(entity)).thenReturn(domain);

        final Optional<ProdutoDomain> result = produtoRepositoryAdapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void testSave() {
        final ProdutoDomain domain = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();
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
    void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repository).deleteById(id);

        produtoRepositoryAdapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testSearchWithFilters() {
        final ProdutoDomain filterDomain = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();
        
        final Pageable pageable = Pageable.unpaged();

        final ProdutoEntity entity1 = new ProdutoEntity();
        entity1.setId(1L);
        
        final ProdutoEntity entity2 = new ProdutoEntity();
        entity2.setId(2L);

        final List<ProdutoEntity> entities = List.of(entity1, entity2);
        final Page<ProdutoEntity> entityPage = new PageImpl<>(entities);

        final ProdutoDomain domain1 = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();
        
        final ProdutoDomain domain2 = ProdutoDomain.builder()
                .id(2L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        when(repository.findAll(any(), eq(pageable))).thenReturn(entityPage);
        
        when(domainToEntityConverter.convert(domain1)).thenReturn(entity1);
        when(domainToEntityConverter.convert(domain2)).thenReturn(entity2);
        
        when(entityToDomainConverter.convert(entity1)).thenReturn(domain1);
        when(entityToDomainConverter.convert(entity2)).thenReturn(domain2);

        final Page<ProdutoDomain> result = produtoRepositoryAdapter.searchWithFilters(filterDomain, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(domain1, result.getContent().get(0));
        assertEquals(domain2, result.getContent().get(1));
    }
}
