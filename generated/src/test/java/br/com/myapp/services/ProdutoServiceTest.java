
package br.com.myapp.services;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepositoryPort repositoryPort;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(1L)
                    .nome("Example String")
                    .descricao("Example String")
                    .preco(10.5)
                .build();

        when(repositoryPort.findById(domain.getId())).thenReturn(Optional.of(domain));

        final ProdutoDomain result = produtoService.findById(domain.getId());

        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getNome(), result.getNome());
        assertEquals(domain.getDescricao(), result.getDescricao());
        assertEquals(domain.getPreco(), result.getPreco());
    }

    @Test
    void testSaveProduto() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(1L)
                    .nome("Example String")
                    .descricao("Example String")
                    .preco(10.5)
                .build();

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ProdutoDomain result = produtoService.save(domain);

        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getNome(), result.getNome());
        assertEquals(domain.getDescricao(), result.getDescricao());
        assertEquals(domain.getPreco(), result.getPreco());
    }

    @Test
    void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repositoryPort).deleteById(id);

        produtoService.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
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

        final ProdutoDomain domain1 = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        final ProdutoDomain domain2 = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        final List<ProdutoDomain> domainList = List.of(domain1, domain2);
        final Page<ProdutoDomain> domainPage = new PageImpl<>(domainList);

        when(repositoryPort.searchWithFilters(filterDomain, pageable)).thenReturn(domainPage);

        final Page<ProdutoDomain> results = produtoService.searchWithFilters(filterDomain, pageable);

        assertNotNull(results);
        assertEquals(2, results.getTotalElements());
        assertEquals(domain1, results.getContent().get(0));
        assertEquals(domain2, results.getContent().get(1));
    }
}
