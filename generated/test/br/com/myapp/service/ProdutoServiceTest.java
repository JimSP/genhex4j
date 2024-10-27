package br.com.myapp.services;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;
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

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepositoryPort repositoryPort;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(domain));

        final ProdutoDomain result = produtoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testSaveProduto() {
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ProdutoDomain result = produtoService.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Example", result.getNome());
    }

    @Test
    public void testDeleteById() {
        final Long id = 1L;

        doNothing().when(repositoryPort).deleteById(id);

        final produtoService.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    public void testFindAll() {
        final ProdutoDomain domain1 = new ProdutoDomain(1L, "Example1", 0.0);
        final ProdutoDomain domain2 = new ProdutoDomain(2L, "Example2", 1.0);
        final List<ProdutoDomain> domainList = Arrays.asList(domain1, domain2);

        when(repositoryPort.findAll()).thenReturn(domainList);

        final List<ProdutoDomain> results = produtoService.findAll();

        assertEquals(2, results.size());
        assertEquals("Example1", results.get(0).getNome());
        assertEquals("Example2", results.get(1).getNome());
    }
}
