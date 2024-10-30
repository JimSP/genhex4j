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

        final String response = mockMvc.perform(post("/api/${entityName?lower_case}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(${entityName?uncap_first}DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
		
		final ${entityName}DTO ${entityName?uncap_first}ResponseDTO = objectMapper.readValue(response, ${entityName}DTO.class);
		
        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}ResponseDTO.getId()).get();
        assertThat(savedEntity).isNotNull();
        <#list dtoDescriptor.attributes as attribute>
        assertThat(savedEntity.get${attribute.name?cap_first}()).isEqualTo(${entityName?uncap_first}ResponseDTO.get${attribute.name?cap_first}());
        </#list>

        mockMvc.perform(get("/api/${entityName?lower_case}s/" + savedEntity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEntity.getId()))
                <#list dtoDescriptor.attributes as attribute>
                .andExpect(jsonPath("$.${attribute.name}").value(${entityName?uncap_first}ResponseDTO.get${attribute.name?cap_first}()))
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

        mockMvc.perform(put("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}SavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

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

        mockMvc.perform(delete("/api/${entityName?lower_case}s/" + ${entityName?uncap_first}SavedEntity.getId()))
                .andExpect(status().isNoContent());

        final Optional<${entityName}Entity> deletedEntity = ${entityName?uncap_first}Repository.findById(${entityName?uncap_first}SavedEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void testSearch${entityName}WithFilters() throws Exception {
        final ${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
        <#list jpaDescriptor.attributes as attribute>
        ${entityName?uncap_first}Entity.set${attribute.name?cap_first}(<@testValues.generateTestValue attribute.type />);
        </#list>
        
        final ${entityName}Entity ${entityName?uncap_first}SavedEntity = ${entityName?uncap_first}Repository.save(${entityName?uncap_first}Entity);

        mockMvc.perform(get("/api/${entityName?lower_case}s/search?id="+${entityName?uncap_first}SavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                <#list dtoDescriptor.attributes as attribute>
                .andExpect(jsonPath("$.content[0].${attribute.name}").value(${entityName?uncap_first}SavedEntity.get${attribute.name?cap_first}()))
                </#list>;
    }
    
    @Test
    void testGetProdutoNotFound() throws Exception {
    
    	mockMvc.perform(get("/api/${entityName?lower_case}s/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteProdutoAndVerifyNotFound() throws Exception {

        mockMvc.perform(delete("/api/${entityName?lower_case}s/-1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateProdutoAndVerifyNotFound() throws Exception {

    	 final ${entityName}DTO updatedDTO = ${entityName}DTO.builder()
            <#list dtoDescriptor.attributes as attribute>
            .${attribute.name}(<@testValues.generateTestValue attribute.type />)
            </#list>
            .build();

        mockMvc.perform(put("/api/${entityName?lower_case}s/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isNotFound());
    }
}
