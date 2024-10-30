package br.com.myapp.converters;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.entities.ProdutoEntity;

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
