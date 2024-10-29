package br.com.myapp.converter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.dto.ProdutoDTO;
import br.com.myapp.entity.ProdutoEntity;
import br.com.myapp.converter.ProdutoConverter;

public class ProdutoConverterTest {

    @Test
    public void testDtoToDomainConversion() {
        final ProdutoDTO dto = ProdutoDTO.builder()
            .id(1L)
            .nome("Example")
            .preco(0.0)
            .build();

        final ProdutoDomain domain = ProdutoConverter.dtoToDomain(dto);

        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getNome(), domain.getNome());
        assertEquals(dto.getPreco(), domain.getPreco());
    }

    @Test
    public void testDomainToDtoConversion() {
        final ProdutoDomain domain = ProdutoDomain.builder()
            .id(1L)
            .nome("Example")
            .descricao("Example")
            .preco(0.0)
            .build();

        final ProdutoDTO dto = ProdutoConverter.domainToDto(domain);

        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getNome(), dto.getNome());
        assertEquals(domain.getDescricao(), dto.getDescricao());
        assertEquals(domain.getPreco(), dto.getPreco());
    }

    @Test
    public void testEntityToDomainConversion() {
        final ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);
        entity.setNome("Example");
        entity.setDescricao("Example");
        entity.setPreco(0.0);

        final ProdutoDomain domain = ProdutoConverter.entityToDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
        assertEquals(entity.getDescricao(), domain.getDescricao());
        assertEquals(entity.getPreco(), domain.getPreco());
    }

    @Test
    public void testDomainToEntityConversion() {
        ProdutoDomain domain = ProdutoDomain.builder()
            .id(1L)
            .nome("Example")
            .descricao("Example")
            .preco(0.0)
            .build();

        ProdutoEntity entity = ProdutoConverter.domainToEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getNome(), entity.getNome());
        assertEquals(domain.getDescricao(), entity.getDescricao());
        assertEquals(domain.getPreco(), entity.getPreco());
    }
}
