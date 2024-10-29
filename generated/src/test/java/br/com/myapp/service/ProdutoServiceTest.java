
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        final ProdutoDomain domain = new ProdutoDomain(
                    1L
    
 ,
                    "Example"
    
 ,
                    "Example"
    
 ,
                    0.0
    
 
        );

        when(repositoryPort.findById(domain.getId())).thenReturn(Optional.of(domain));

        final ProdutoDomain result = produtoService.findById(domain.getId());

        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getNome(), result.getNome());
        assertEquals(domain.getDescricao(), result.getDescricao());
        assertEquals(domain.getPreco(), result.getPreco());
    }

    @Test
    public void testSaveProduto() {
        final ProdutoDomain domain = new ProdutoDomain(
                    1L
    
 ,
                    "Example"
    
 ,
                    "Example"
    
 ,
                    0.0
    
 
        );

        when(repositoryPort.save(domain)).thenReturn(domain);

        final ProdutoDomain result = produtoService.save(domain);

        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getNome(), result.getNome());
        assertEquals(domain.getDescricao(), result.getDescricao());
        assertEquals(domain.getPreco(), result.getPreco());
    }

    @Test
    public void testDeleteById() {
        final Long id =     1L
    
;

        doNothing().when(repositoryPort).deleteById(id);

        final produtoService.deleteById(id);

        verify(repositoryPort, times(1)).deleteById(id);
    }

    @Test
    public void testFindAll() {
        final ProdutoDomain domain1 = new ProdutoDomain(
                    1L
    
 ,
                    "Example"
    
 ,
                    "Example"
    
 ,
                    0.0
    
 
        );

        final ProdutoDomain domain2 = new ProdutoDomain(
                    1L
    
 ,
                    "Example"
    
 ,
                    "Example"
    
 ,
                    0.0
    
 
        );

        final List<ProdutoDomain> domainList = Arrays.asList(domain1, domain2);

        when(repositoryPort.findAll()).thenReturn(domainList);

        final List<ProdutoDomain> results = produtoService.findAll();

        assertEquals(2, results.size());
        assertEquals(domain1.getId(), results.get(0).getId());
        assertEquals(domain2.getId(), results.get(1).getId());
        assertEquals(domain1.getNome(), results.get(0).getNome());
        assertEquals(domain2.getNome(), results.get(1).getNome());
        assertEquals(domain1.getDescricao(), results.get(0).getDescricao());
        assertEquals(domain2.getDescricao(), results.get(1).getDescricao());
        assertEquals(domain1.getPreco(), results.get(0).getPreco());
        assertEquals(domain2.getPreco(), results.get(1).getPreco());
    }
}
