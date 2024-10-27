package br.com.myapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoEntityTest {

    @Test
    public void testProdutoEntityGettersAndSetters() {
        final ProdutoEntity entity = new ProdutoEntity();
        
        final Long idValue = 1L;
        
        entity.setId(idValue);
        assertEquals(idValue, entity.getId());
        final String nomeValue = "Exemplo";
        
        entity.setNome(nomeValue);
        assertEquals(nomeValue, entity.getNome());
        final String descricaoValue = "Exemplo";
        
        entity.setDescricao(descricaoValue);
        assertEquals(descricaoValue, entity.getDescricao());
        final Double precoValue = 0.0;
        
        entity.setPreco(precoValue);
        assertEquals(precoValue, entity.getPreco());
    }
}
