package ${packageName}.integration;

import ${packageName}.Application;  // Substitua pelo nome da classe principal da sua aplicação
import ${packageName}.controller.${entityName}Controller;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.entity.${entityName}Entity;
import ${packageName}.repository.${entityName}Repository;
import ${packageName}.service.${entityName}Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ${entityName}EndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ${entityName}Repository ${entityName?uncap_first}Repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        ${entityName?uncap_first}Repository.deleteAll();
    }

    @Test
    public void testCreate${entityName}AndRetrieveById() throws Exception {
        ${entityName}DTO ${entityName?uncap_first}DTO = ${entityName}DTO.builder()
            <#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if></#if>)
            </#list>
            .build();

        // Cria a entidade via POST
        mockMvc.perform(post("/api/${entityName?lower_case}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(${entityName?uncap_first}DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Valida se foi salvo no banco de dados
        ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.findAll().get(0);
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
    public void testUpdate${entityName}AndVerifyChanges() throws Exception {
        ${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        ${entityName?uncap_first}Entity.set${attribute.name?cap_first}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Original"</#if><#if attribute.type == "Double">10.0</#if></#if>);
        </#list>
        ${entityName?uncap_first}Entity = ${entityName?uncap_first}Repository.save(${entityName?uncap_first}Entity);

        ${entityName}DTO updatedDTO = ${entityName}DTO.builder()
            .id(${entityName?uncap_first}Entity.getId())
            <#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "String">"Updated"</#if><#if attribute.type == "Double">20.0</#if><#if attribute.type == "Long">2L</#if></#if>)
            </#list>
            .build();

        // Atualiza a entidade via PUT
        mockMvc.perform(put("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}Entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        // Valida se a atualização foi realizada no banco de dados
        ${entityName}Entity updatedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}Entity.getId()).orElse(null);
        assertThat(updatedEntity).isNotNull();
        <#list dtoDescriptor.attributes as attribute>
        assertThat(updatedEntity.get${attribute.name?cap_first}()).isEqualTo(updatedDTO.get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testDelete${entityName}AndVerifyRemoval() throws Exception {
        ${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        ${entityName?uncap_first}Entity.set${attribute.name?cap_first}(<#if attribute.testValue??>${attribute.testValue}<#else><#if attribute.type == "Long">1L</#if><#if attribute.type == "String">"Example"</#if><#if attribute.type == "Double">0.0</#if></#if>);
        </#list>
        ${entityName?uncap_first}Entity = ${entityName?uncap_first}Repository.save(${entityName?uncap_first}Entity);

        // Deleta a entidade via DELETE
        mockMvc.perform(delete("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}Entity.getId()))
                .andExpect(status().isNoContent());

        // Verifica se foi removida do banco de dados
        Optional<${entityName}Entity> deletedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}Entity.getId());
        assertThat(deletedEntity).isEmpty();
    }
}
