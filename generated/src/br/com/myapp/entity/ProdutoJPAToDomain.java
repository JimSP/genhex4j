package br.com.myapp.entity;

import br.com.myapp.entity.ProdutoEntity;
import br.com.myapp.domain.ProdutoDomain;

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