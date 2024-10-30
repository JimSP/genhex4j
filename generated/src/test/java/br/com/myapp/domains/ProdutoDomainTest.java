
package br.com.myapp.domains;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDomainTest {

    @Test
    void testProdutoDomainCreation() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(1L)
                    .nome("Example String")
                    .descricao("Example String")
                    .preco(10.5)
                .build();

        assertEquals(
            1L, 
            domain.getId()
        );
        assertEquals(
            "Example String", 
            domain.getNome()
        );
        assertEquals(
            "Example String", 
            domain.getDescricao()
        );
        assertEquals(
            10.5, 
            domain.getPreco()
        );
    }
}
