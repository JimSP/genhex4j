
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

        // Cria a entidade via POST
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Valida se foi salvo no banco de dados
        final ProdutoEntity savedEntity = produtoRepository.findAll().get(0);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(produtoDTO.getId());
        assertThat(savedEntity.getNome()).isEqualTo(produtoDTO.getNome());
        assertThat(savedEntity.getDescricao()).isEqualTo(produtoDTO.getDescricao());
        assertThat(savedEntity.getPreco()).isEqualTo(produtoDTO.getPreco());

        // Recupera a entidade pelo ID via GET
        mockMvc.perform(get("/api/produtos/" + savedEntity.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEntity.getId()))
                .andExpect(jsonPath("$.id").value(produtoDTO.getId()))
                                .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()))
                                .andExpect(jsonPath("$.descricao").value(produtoDTO.getDescricao()))
                                .andExpect(jsonPath("$.preco").value(produtoDTO.getPreco()))
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

        // Atualiza a entidade via PUT
        mockMvc.perform(put("/api/produtos/" + produtoSavedEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        // Valida se a atualização foi realizada no banco de dados
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

        // Deleta a entidade via DELETE
        mockMvc.perform(delete("/api/produtos/" + produtoSavedEntity.getId()))
                .andExpect(status().isNoContent());

        // Verifica se foi removida do banco de dados
        final Optional<ProdutoEntity> deletedEntity = produtoRepository.findById(produtoSavedEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }
}
