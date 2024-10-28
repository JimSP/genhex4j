package ${packageName}.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.*;

@Value
@Builder(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = ${entityName}DTO.${entityName}DTOBuilder.class)
public class ${entityName}DTO {

    <#-- Verifica se dtoDescriptor e seus atributos estão definidos -->
    <#if dtoDescriptor?? && dtoDescriptor.attributes?? && (dtoDescriptor.attributes?size > 0)>
        <#list dtoDescriptor.attributes as attribute>
            <#-- Validação para NotNull se o atributo for requerido -->
            <#if attribute.required?? && attribute.required>
            @NotNull(message = "${attribute.name} é obrigatório")
            </#if>

            <#-- Validação para o tamanho máximo se maxLength estiver definido -->
            <#if attribute.maxLength??>
            @Size(max = ${attribute.maxLength}, message = "${attribute.name} não pode ter mais de ${attribute.maxLength} caracteres")
            </#if>
            @JsonProperty
            ${attribute.type} ${attribute.name};
        </#list>
    </#if>
}
