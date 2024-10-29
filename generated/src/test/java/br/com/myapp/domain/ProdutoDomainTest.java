
package br.com.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoDomainTest {

    @Test
    void testProdutoDomainCreation() {
        final ProdutoDomain domain = ProdutoDomain
                .builder()
                    .id(testValues.generateTestValue("Long"))
                    .nome(testValues.generateTestValue("String"))
                    .descricao(testValues.generateTestValue("String"))
                    .preco(testValues.generateTestValue("Double"))
                .build();

        assertEquals(
            testValues.generateTestValue("Long"), 
            domain.getId()
        );
        assertEquals(
            testValues.generateTestValue("String"), 
            domain.getNome()
        );
        assertEquals(
            testValues.generateTestValue("String"), 
            domain.getDescricao()
        );
        assertEquals(
            testValues.generateTestValue("Double"), 
            domain.getPreco()
        );
    }
}
