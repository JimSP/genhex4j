
package br.com.myapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.dto.ProdutoDTO;
import br.com.myapp.dto.ProdutoDTOToDomainConverter;
import br.com.myapp.dto.ProdutoDomainToDTOConverter;
import br.com.myapp.entity.ProdutoEntity;
import br.com.myapp.entity.ProdutoJPAToDomainConverter;
import br.com.myapp.entity.ProdutoDomainToJPAConverter;

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
                .id(testValues.generateTestValue(attribute.type))
                .nome(testValues.generateTestValue(attribute.type))
                .descricao(testValues.generateTestValue(attribute.type))
                .preco(testValues.generateTestValue(attribute.type))
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
                .id(testValues.generateTestValue(attribute.type))
                .nome(testValues.generateTestValue(attribute.type))
                .descricao(testValues.generateTestValue(attribute.type))
                .preco(testValues.generateTestValue(attribute.type))
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
        entity.setId(testValues.generateTestValue(attribute.type));
        entity.setNome(testValues.generateTestValue(attribute.type));
        entity.setDescricao(testValues.generateTestValue(attribute.type));
        entity.setPreco(testValues.generateTestValue(attribute.type));

        final ProdutoDomain domain = jpaToDomainConverter.convert(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
        assertEquals(entity.getDescricao(), domain.getDescricao());
        assertEquals(entity.getPreco(), domain.getPreco());
    }

    @Test
    void testDomainToEntityConversion() {
        final ProdutoDomain domain = ProdutoDomain.builder()
                .id(testValues.generateTestValue(attribute.type))
                .nome(testValues.generateTestValue(attribute.type))
                .descricao(testValues.generateTestValue(attribute.type))
                .preco(testValues.generateTestValue(attribute.type))
            .build();

        final ProdutoEntity entity = domainToJPAConverter.convert(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getNome(), entity.getNome());
        assertEquals(domain.getDescricao(), entity.getDescricao());
        assertEquals(domain.getPreco(), entity.getPreco());
    }
}
