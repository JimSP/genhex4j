
package br.com.myapp.integration;

import br.com.myapp.Application;  // Substitua pelo nome da classe principal da sua aplicação
import br.com.myapp.controller.ProdutoController;
import br.com.myapp.dto.ProdutoDTO;
import br.com.myapp.entity.ProdutoEntity;
import br.com.myapp.repository.ProdutoRepository;
import br.com.myapp.service.ProdutoService;
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
public class ProdutoEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        produtoRepository.deleteAll();
    }

    @Test
    public void testCreateProdutoAndRetrieveById() throws Exception {
        final ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .id(testValues.generateTestValue("Long"))
            .nome(testValues.generateTestValue("String"))
            .descricao(testValues.generateTestValue("String"))
            .preco(testValues.generateTestValue("Double"))
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
    public void testUpdateProdutoAndVerifyChanges() throws Exception {
        final ProdutoEntity produtoEntity = new ProdutoEntity();
        final produtoEntity.setId(testValues.generateTestValue("Long"));
        final produtoEntity.setNome(testValues.generateTestValue("String"));
        final produtoEntity.setDescricao(testValues.generateTestValue("String"));
        final produtoEntity.setPreco(testValues.generateTestValue("Double"));
        final produtoEntity = produtoRepository.save(produtoEntity);

        final ProdutoDTO updatedDTO = ProdutoDTO.builder()
            .id(produtoEntity.getId())
            .id(testValues.generateTestValue("Long"))
            .nome(testValues.generateTestValue("String"))
            .descricao(testValues.generateTestValue("String"))
            .preco(testValues.generateTestValue("Double"))
            .build();

        // Atualiza a entidade via PUT
        mockMvc.perform(put("/api/produtos/" + produtoEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        // Valida se a atualização foi realizada no banco de dados
        final ProdutoEntity updatedEntity = produtoRepository.findById(produtoEntity.getId()).orElse(null);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getId()).isEqualTo(updatedDTO.getId());
        assertThat(updatedEntity.getNome()).isEqualTo(updatedDTO.getNome());
        assertThat(updatedEntity.getDescricao()).isEqualTo(updatedDTO.getDescricao());
        assertThat(updatedEntity.getPreco()).isEqualTo(updatedDTO.getPreco());
    }

    @Test
    public void testDeleteProdutoAndVerifyRemoval() throws Exception {
        final ProdutoEntity produtoEntity = new ProdutoEntity();
        final produtoEntity.setId(testValues.generateTestValue("Long"));
        final produtoEntity.setNome(testValues.generateTestValue("String"));
        final produtoEntity.setDescricao(testValues.generateTestValue("String"));
        final produtoEntity.setPreco(testValues.generateTestValue("Double"));
        final produtoEntity = produtoRepository.save(produtoEntity);

        // Deleta a entidade via DELETE
        mockMvc.perform(delete("/api/produtos/" + produtoEntity.getId()))
                .andExpect(status().isNoContent());

        // Verifica se foi removida do banco de dados
        final Optional<ProdutoEntity> deletedEntity = produtoRepository.findById(produtoEntity.getId());
        assertThat(deletedEntity).isEmpty();
    }
}
