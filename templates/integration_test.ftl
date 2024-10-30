<#import "testValueModule.ftl" as testValues>

package ${packageName};

import ${packageName}.dtos.${entityName}DTO;
import ${packageName}.entities.${entityName}Entity;
import ${packageName}.repositories.${entityName}Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ${entityName}Application.class)
@AutoConfigureMockMvc
class ${entityName}EndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ${entityName}Repository ${entityName?uncap_first}Repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ${entityName?uncap_first}Repository.deleteAll();
    }

    @Test
    void testCreate${entityName}AndRetrieveById() throws Exception {
        final ${entityName}DTO ${entityName?uncap_first}DTO = ${entityName}DTO.builder()
            <#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<@testValues.generateTestValue attribute.type />)
            </#list>
            .build();

        // Cria a entidade via POST
        mockMvc.perform(post("/api/${entityName?lower_case}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(${entityName?uncap_first}DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Valida se foi salvo no banco de dados
        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.findAll().get(0);
        assertThat(savedEntity).isNotNull();
        <#list dtoDescriptor.attributes as attribute>
        assertThat(savedEntity.get${attribute.name?cap_first}()).isEqualTo(${entityName?uncap_first}DTO.get${attribute.name?cap_first}());
        </#list>

        // Recupera a entidade pelo ID via GET
        mockMvc.perform(get("/api/${entityName?lower_case}s/" + savedEntity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEntity.getId()))
                <#list dtoDescriptor.attributes as attribute>
                .andExpect(jsonPath("$.${attribute.name}").value(${entityName?uncap_first}DTO.get${attribute.name?cap_first}()))
                </#list>;
    }

    @Test
    void testUpdate${entityName}AndVerifyChanges() throws Exception {
        final ${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        ${entityName?uncap_first}Entity.set${attribute.name?cap_first}(<@testValues.generateTestValue attribute.type />);
        </#list>
        final ${entityName}Entity ${entityName?uncap_first}SavedEntity = ${entityName?uncap_first}Repository.save(${entityName?uncap_first}Entity);

        final ${entityName}DTO updatedDTO = ${entityName}DTO.builder()
            .id(${entityName?uncap_first}SavedEntity.getId())
            <#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<@testValues.generateTestValue attribute.type />)
            </#list>
            .build();

        // Atualiza a entidade via PUT
        mockMvc.perform(put("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}SavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        // Valida se a atualização foi realizada no banco de dados
        final ${entityName}Entity updatedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}SavedEntity.getId()).orElse(null);
        assertThat(updatedEntity).isNotNull();
        <#list dtoDescriptor.attributes as attribute>
        assertThat(updatedEntity.get${attribute.name?cap_first}()).isEqualTo(updatedDTO.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    void testDelete${entityName}AndVerifyRemoval() throws Exception {
        final ${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        ${entityName?uncap_first}Entity.set${attribute.name?cap_first}(<@testValues.generateTestValue attribute.type />);
        </#list>
        final ${entityName}Entity ${entityName?uncap_first}SavedEntity = ${entityName?uncap_first}Repository.save(${entityName?uncap_first}Entity);

        // Deleta a entidade via DELETE
        mockMvc.perform(delete("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}SavedEntity.getId()))
                .andExpect(status().isNoContent());

        // Verifica se foi removida do banco de dados
        final Optional<${entityName}Entity> deletedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}SavedEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }
}
