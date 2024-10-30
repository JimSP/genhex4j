
package br.com.myapp;

import br.com.myapp.dtos.ProdutoDTO;
import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.repositories.ProdutoRepository;

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

@SpringBootTest(classes = ProdutoApplication.class)
@AutoConfigureMockMvc
class ProdutoEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
    }

    @Test
    void testCreateProdutoAndRetrieveById() throws Exception {
        final ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .id(1L)
            .nome("Example String")
            .descricao("Example String")
            .preco(10.5)
            .build();

        final String response = mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
		
		final ProdutoDTO produtoResponseDTO = objectMapper.readValue(response, ProdutoDTO.class);
		
        final ProdutoEntity savedEntity = produtoRepository.findById(produtoResponseDTO.getId()).get();
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(produtoResponseDTO.getId());
        assertThat(savedEntity.getNome()).isEqualTo(produtoResponseDTO.getNome());
        assertThat(savedEntity.getDescricao()).isEqualTo(produtoResponseDTO.getDescricao());
        assertThat(savedEntity.getPreco()).isEqualTo(produtoResponseDTO.getPreco());

        mockMvc.perform(get("/api/produtos/" + savedEntity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEntity.getId()))
                .andExpect(jsonPath("$.id").value(produtoResponseDTO.getId()))
                                .andExpect(jsonPath("$.nome").value(produtoResponseDTO.getNome()))
                                .andExpect(jsonPath("$.descricao").value(produtoResponseDTO.getDescricao()))
                                .andExpect(jsonPath("$.preco").value(produtoResponseDTO.getPreco()))
                ;
    }

    @Test
    void testUpdateProdutoAndVerifyChanges() throws Exception {
        final ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Example String");
        produtoEntity.setDescricao("Example String");
        produtoEntity.setPreco(10.5);
        final ProdutoEntity produtoSavedEntity = produtoRepository.save(produtoEntity);

        final ProdutoDTO updatedDTO = ProdutoDTO.builder()
            .id(produtoSavedEntity.getId())
            .id(1L)
            .nome("Example String")
            .descricao("Example String")
            .preco(10.5)
            .build();

        mockMvc.perform(put("/api/produtos/" + produtoSavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        final ProdutoEntity updatedEntity = produtoRepository.findById(produtoSavedEntity.getId()).orElse(null);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getId()).isEqualTo(updatedDTO.getId());
        assertThat(updatedEntity.getNome()).isEqualTo(updatedDTO.getNome());
        assertThat(updatedEntity.getDescricao()).isEqualTo(updatedDTO.getDescricao());
        assertThat(updatedEntity.getPreco()).isEqualTo(updatedDTO.getPreco());
    }

    @Test
    void testDeleteProdutoAndVerifyRemoval() throws Exception {
        final ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Example String");
        produtoEntity.setDescricao("Example String");
        produtoEntity.setPreco(10.5);
        final ProdutoEntity produtoSavedEntity = produtoRepository.save(produtoEntity);

        mockMvc.perform(delete("/api/produtos/" + produtoSavedEntity.getId()))
                .andExpect(status().isNoContent());

        final Optional<ProdutoEntity> deletedEntity = produtoRepository.findById(produtoSavedEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    void testSearchProdutoWithFilters() throws Exception {
        final ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(1L);
        produtoEntity.setNome("Example String");
        produtoEntity.setDescricao("Example String");
        produtoEntity.setPreco(10.5);
        
        final ProdutoEntity produtoSavedEntity = produtoRepository.save(produtoEntity);

        mockMvc.perform(get("/api/produtos/search?id="+produtoSavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(produtoSavedEntity.getId()))
                                .andExpect(jsonPath("$.content[0].nome").value(produtoSavedEntity.getNome()))
                                .andExpect(jsonPath("$.content[0].descricao").value(produtoSavedEntity.getDescricao()))
                                .andExpect(jsonPath("$.content[0].preco").value(produtoSavedEntity.getPreco()))
                ;
    }
    
    @Test
    void testGetProdutoNotFound() throws Exception {
    
    	mockMvc.perform(get("/api/produtos/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteProdutoAndVerifyNotFound() throws Exception {

        mockMvc.perform(delete("/api/produtos/-1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateProdutoAndVerifyNotFound() throws Exception {

    	 final ProdutoDTO updatedDTO = ProdutoDTO.builder()
            .id(1L)
            .nome("Example String")
            .descricao("Example String")
            .preco(10.5)
            .build();

        mockMvc.perform(put("/api/produtos/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isNotFound());
    }
}
