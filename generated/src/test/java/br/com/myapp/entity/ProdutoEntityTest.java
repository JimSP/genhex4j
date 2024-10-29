
package br.com.myapp.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoEntityTest {

    @Test
    void testProdutoEntityGettersAndSetters() {
        final ProdutoEntity entity = new ProdutoEntity();
        
        final Long idValue = testValues.generateTestValue("Long");
        
        entity.setId(idValue);
        assertEquals(idValue, entity.getId());
        final String nomeValue = testValues.generateTestValue("String");
        
        entity.setNome(nomeValue);
        assertEquals(nomeValue, entity.getNome());
        final String descricaoValue = testValues.generateTestValue("String");
        
        entity.setDescricao(descricaoValue);
        assertEquals(descricaoValue, entity.getDescricao());
        final Double precoValue = testValues.generateTestValue("Double");
        
        entity.setPreco(precoValue);
        assertEquals(precoValue, entity.getPreco());
    }
}
