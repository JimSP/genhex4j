package br.com.myapp.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;
import br.com.myapp.entities.ProdutoEntity;

@SpringBootTest
class ProdutoConverterTest {

    @Autowired
    private ProdutoDTOToDomainConverter dtoToDomainConverter;
    
    @Autowired
    private ProdutoDomainToDTOConverter domainToDTOConverter;
    
    @Autowired
    private ProdutoJPAToDomainConverter jpaToDomainConverter;
    
    @Autowired
    private ProdutoDomainToJPAConverter domainToJPAConverter;

    @Test
    void testDtoToDomainConversion() {
        final ProdutoDTO dto = ProdutoDTO.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        final ProdutoDomain domain = dtoToDomainConverter.convert(dto);

        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getNome(), domain.getNome());
        assertEquals(dto.getDescricao(), domain.getDescricao());
        assertEquals(dto.getPreco(), domain.getPreco());
    }

    @Test
    void testDomainToDtoConversion() {
        final ProdutoDomain domain = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        final ProdutoDTO dto = domainToDTOConverter.convert(domain);

        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getNome(), dto.getNome());
        assertEquals(domain.getDescricao(), dto.getDescricao());
        assertEquals(domain.getPreco(), dto.getPreco());
    }

    @Test
    void testEntityToDomainConversion() {
        final ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);
        entity.setNome("Example String");
        entity.setDescricao("Example String");
        entity.setPreco(10.5);

        final ProdutoDomain domain = jpaToDomainConverter.convert(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
        assertEquals(entity.getDescricao(), domain.getDescricao());
        assertEquals(entity.getPreco(), domain.getPreco());
    }

    @Test
    void testDomainToEntityConversion() {
        final ProdutoDomain domain = ProdutoDomain.builder()
                .id(1L)
                .nome("Example String")
                .descricao("Example String")
                .preco(10.5)
            .build();

        final ProdutoEntity entity = domainToJPAConverter.convert(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getNome(), entity.getNome());
        assertEquals(domain.getDescricao(), entity.getDescricao());
        assertEquals(domain.getPreco(), entity.getPreco());
    }
}
