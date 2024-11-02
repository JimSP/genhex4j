
package br.com.myapp.domains;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDomainTest {

    @Test
    void testProdutoDomainCreation() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        assertEquals(
            null, 
            domain.getId()
        );
        assertEquals(
            null, 
            domain.getNome()
        );
        assertEquals(
            null, 
            domain.getDescricao()
        );
        assertEquals(
            null, 
            domain.getPreco()
        );
    }
}
