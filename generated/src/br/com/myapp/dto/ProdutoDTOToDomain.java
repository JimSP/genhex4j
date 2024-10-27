package br.com.myapp.dto;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.dto.ProdutoDTO;

import org.springframework.stereotype.Component;

@Component
public class ProdutoDTOToDomainConverter {

    public ProdutoDomain convert(final ProdutoDTO dto) {
        return ProdutoDomain.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .preco(dto.getPreco())
            .build();
    }
}
