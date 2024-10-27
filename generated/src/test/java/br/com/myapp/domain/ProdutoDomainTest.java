package br.com.myapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDomainTest {

    @Test
    public void testProdutoDomainCreation() {
        final ProdutoDomain domain = ProdutoDomain
		        	.builder()
		            .id(1L)
		            .nome("Exemplo")
		            .descricao("Exemplo")
		            .preco(0.0)
		            .build();

        assertEquals(1L, domain.getId());
        assertEquals("Exemplo", domain.getNome());
        assertEquals("Exemplo", domain.getDescricao());
        assertEquals(0.0, domain.getPreco());
    }
}
