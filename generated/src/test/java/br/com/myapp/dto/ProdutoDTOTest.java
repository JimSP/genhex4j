
package br.com.myapp.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDTOTest {

    @Test
    void testProdutoDTOCreation() {
        final ProdutoDTO dto = ProdutoDTO
                .builder()
                    .id(testValues.generateTestValue("Long"))
                    .nome(testValues.generateTestValue("String"))
                    .descricao(testValues.generateTestValue("String"))
                    .preco(testValues.generateTestValue("Double"))
                .build();

        assertEquals(
            testValues.generateTestValue("Long"), 
            dto.getId()
        );
        assertEquals(
            testValues.generateTestValue("String"), 
            dto.getNome()
        );
        assertEquals(
            testValues.generateTestValue("String"), 
            dto.getDescricao()
        );
        assertEquals(
            testValues.generateTestValue("Double"), 
            dto.getPreco()
        );
    }
}
