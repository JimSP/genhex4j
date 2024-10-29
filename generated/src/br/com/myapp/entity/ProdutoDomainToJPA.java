package br.com.myapp.entity;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.entity.ProdutoEntity;

import org.springframework.stereotype.Component;

@Component
public class ProdutoDomainToJPAConverter {

    public ProdutoEntity convert(final ProdutoDomain domain) {
        return ProdutoEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
            .build();
    }
}
