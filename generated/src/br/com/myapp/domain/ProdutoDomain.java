package br.com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = ProdutoDomain.ProdutoDomainBuilder.class)
public class ProdutoDomain {

            Long id;
            String nome;
            String descricao;
            Double preco;
}
