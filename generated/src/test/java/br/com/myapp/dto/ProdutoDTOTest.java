package br.com.myapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDTOTest {

    @Test
    public void testProdutoDTOCreation() {
        final ProdutoDTO dto = ProdutoDTO
        			.builder()
        			.id(1L)
        			.nome("Example")
        			.descricao("Example")
        			.preco(0.0)
        			.build();

        assertEquals(1L, dto.getId());
        assertEquals("Example", dto.getNome());
        assertEquals("Example", dto.getDescricao());
        assertEquals(0.0, dto.getPreco());
    }
}
