package br.com.myapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.*;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = ProdutoDTO.ProdutoDTOBuilder.class)
public class ProdutoDTO {


            @JsonProperty
            Long id;
            @NotNull(message = "nome é obrigatório")

            @Size(max = 100, message = "nome não pode ter mais de 100 caracteres")
            @JsonProperty
            String nome;
            @NotNull(message = "descricao é obrigatório")

            @JsonProperty
            String descricao;

            @JsonProperty
            Double preco;
}
