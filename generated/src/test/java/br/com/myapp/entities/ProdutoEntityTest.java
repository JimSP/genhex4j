
package br.com.myapp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoEntityTest {

    @Test
    void testProdutoEntityGettersAndSetters() {
        final ProdutoEntity entity = new ProdutoEntity();
        
        final java.lang.Long idValue = null;
        
        entity.setId(idValue);
        assertEquals(idValue, entity.getId());
        final java.lang.String nomeValue = null;
        
        entity.setNome(nomeValue);
        assertEquals(nomeValue, entity.getNome());
        final java.lang.String descricaoValue = null;
        
        entity.setDescricao(descricaoValue);
        assertEquals(descricaoValue, entity.getDescricao());
        final java.lang.Double precoValue = null;
        
        entity.setPreco(precoValue);
        assertEquals(precoValue, entity.getPreco());
    }
}
