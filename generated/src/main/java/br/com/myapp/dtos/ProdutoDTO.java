package br.com.myapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.*;

@Value
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
@JsonDeserialize(builder = ProdutoDTO.ProdutoDTOBuilder.class)
public class ProdutoDTO {


            @JsonProperty
            java.lang.Long id;
            @NotNull(message = "nome é obrigatório")

            @Size(max = 100, message = "nome não pode ter mais de 100 caracteres")
            @JsonProperty
            java.lang.String nome;
            @NotNull(message = "descricao é obrigatório")

            @JsonProperty
            java.lang.String descricao;

            @JsonProperty
            java.lang.Double preco;
}
