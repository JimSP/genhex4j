package br.com.myapp.converters;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;

import org.springframework.stereotype.Component;

@Component
public class ProdutoDTOToDomainConverter {

    public ProdutoDomain convert(final ProdutoDTO dto) {
        return ProdutoDomain.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
            .build();
    }
}
