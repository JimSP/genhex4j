package br.com.myapp.converters;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;

import org.springframework.stereotype.Component;

@Component
public class ProdutoDomainToDTOConverter {

    public ProdutoDTO convert(final ProdutoDomain domain) {
        return ProdutoDTO.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
            .build();
    }
}