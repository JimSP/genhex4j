package br.com.myapp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
public class ProdutoDomain {

    Long id;
    String nome;
    String descricao;
    Double preco;
}
