package br.com.myapp.domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
public class ProdutoDomain {

    java.lang.Long id;
    java.lang.String nome;
    java.lang.String descricao;
    java.lang.Double preco;
}
