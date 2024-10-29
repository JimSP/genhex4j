package br.com.myapp.dto;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.dto.ProdutoDTO;

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