
package br.com.myapp.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDTOTest {

    @Test
    void testProdutoDTOCreation() {
        final ProdutoDTO dto = ProdutoDTO
                .builder()
                    .id(1L)
                    .nome("Example String")
                    .descricao("Example String")
                    .preco(10.5)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("Example String", dto.getNome());
        assertEquals("Example String", dto.getDescricao());
        assertEquals(10.5, dto.getPreco());
    }
}
