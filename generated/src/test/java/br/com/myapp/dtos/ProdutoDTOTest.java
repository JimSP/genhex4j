
package br.com.myapp.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDTOTest {

    @Test
    void testProdutoDTOCreation() {
        final ProdutoDTO dto = ProdutoDTO
                .builder()
                    .id(null)
                    .nome(null)
                    .descricao(null)
                    .preco(null)
                .build();

        assertEquals(null, dto.getId());
        assertEquals(null, dto.getNome());
        assertEquals(null, dto.getDescricao());
        assertEquals(null, dto.getPreco());
    }
}
