package br.com.myapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = ProdutoDTO.ProdutoDTOBuilder.class)
public class ProdutoDTO {


            
            Long id;
            @NotNull(message = "nome é obrigatório")

            @Size(max = 100, message = "nome não pode ter mais de 100 caracteres")
            
            String nome;

            
            Double preco;
}
