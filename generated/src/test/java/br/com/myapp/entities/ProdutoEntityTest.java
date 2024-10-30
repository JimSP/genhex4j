
package br.com.myapp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoEntityTest {

    @Test
    void testProdutoEntityGettersAndSetters() {
        final ProdutoEntity entity = new ProdutoEntity();
        
        final Long idValue = 1L;
        
        entity.setId(idValue);
        assertEquals(idValue, entity.getId());
        final String nomeValue = "Example String";
        
        entity.setNome(nomeValue);
        assertEquals(nomeValue, entity.getNome());
        final String descricaoValue = "Example String";
        
        entity.setDescricao(descricaoValue);
        assertEquals(descricaoValue, entity.getDescricao());
        final Double precoValue = 10.5;
        
        entity.setPreco(precoValue);
        assertEquals(precoValue, entity.getPreco());
    }
}
