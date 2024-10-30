package br.com.myapp.converters;

import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.domains.ProdutoDomain;

import org.springframework.stereotype.Component;

@Component
public class ProdutoJPAToDomainConverter {

    public ProdutoDomain convert(final ProdutoEntity entity) {
        return ProdutoDomain.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
            .build();
    }
}