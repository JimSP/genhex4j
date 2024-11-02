package br.com.myapp.services;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

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

class ProdutoRuleAdapterTest {

    @Mock
    private ProdutoRepositoryPort repositoryPort;

    @InjectMocks
    private ProdutoServiceAdapter produtoServiceAdapter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        when(repositoryPort.findById(domain.getId())).thenReturn(Optional.of(domain));

        final ProdutoDomain result = produtoServiceAdapter.findById(domain.getId());

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
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ProdutoDomain result = produtoServiceAdapter.save(domain);

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

        produtoServiceAdapter.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    void testFindAllWithFilters() {
        final ProdutoDomain filterDomain = ProdutoDomain
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        final ProdutoDomain domain1 = ProdutoDomain
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        final ProdutoDomain domain2 = ProdutoDomain
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        final List<ProdutoDomain> domainList = Arrays.asList(domain1, domain2);
        final Page<ProdutoDomain> domainPage = new PageImpl<>(domainList);

        when(repositoryPort.searchWithFilters(filterDomain, Pageable.unpaged())).thenReturn(domainPage);

        final Page<ProdutoDomain> results = produtoServiceAdapter.searchWithFilters(filterDomain, Pageable.unpaged());

        assertEquals(2, results.getTotalElements());
        assertEquals(domain1.getId(), results.getContent().get(0).getId());
        assertEquals(domain2.getId(), results.getContent().get(1).getId());
        assertEquals(domain1.getNome(), results.getContent().get(0).getNome());
        assertEquals(domain2.getNome(), results.getContent().get(1).getNome());
        assertEquals(domain1.getDescricao(), results.getContent().get(0).getDescricao());
        assertEquals(domain2.getDescricao(), results.getContent().get(1).getDescricao());
        assertEquals(domain1.getPreco(), results.getContent().get(0).getPreco());
        assertEquals(domain2.getPreco(), results.getContent().get(1).getPreco());
    }
}
