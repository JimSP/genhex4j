
package br.com.myapp.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.myapp.converters.ProdutoDTOToDomainConverter;
import br.com.myapp.converters.ProdutoDomainToDTOConverter;
import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;
import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.services.ProdutoServicePort;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Mock
    private ProdutoServicePort service;

    @Mock
    private ProdutoDTOToDomainConverter dtoToDomainConverter;

    @Mock
    private ProdutoDomainToDTOConverter domainToDtoConverter;

    @InjectMocks
    private ProdutoController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void testCreateProduto() throws Exception {
        final ProdutoDTO dto = ProdutoDTO.builder()
            .build();

        final ProdutoDomain domain = ProdutoDomain.builder()
            .build();

        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testGetProdutoById() throws Exception {
        final ProdutoDTO dto = ProdutoDTO.builder()
            .build();

        final ProdutoDomain domain = ProdutoDomain.builder()
            .build();

        when(service.findById(dto.getId())).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/{id}", dto.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testUpdateProduto() throws Exception {
        final ProdutoDTO dto = ProdutoDTO.builder()
            .build();

        final ProdutoDomain domain = ProdutoDomain.builder()
            .build();

        when(service.findById(dto.getId())).thenReturn(domain);
        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{id}", dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testDeleteProduto() throws Exception {
        final ProdutoDomain domain = ProdutoDomain.builder()
            .build();

        when(service.findById(domain.getId())).thenReturn(domain);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{id}", domain.getId()))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(domain.getId());
    }

    @Test
    void testSearchProduto() throws Exception {
    	
    	final ProdutoDomain domain = ProdutoDomain.builder()
    			.id(1L)
                .build();
    	
        final Pageable pageable = PageRequest.of(0, 10);

        when(service.searchWithFilters(domain, pageable)).thenReturn(new PageImpl<>(Arrays.asList(domain)));

        mockMvc.perform(get("/api/produtos/search?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(domain.getId()))
                                .andExpect(jsonPath("$.content[0].nome").value(domain.getNome()))
                                .andExpect(jsonPath("$.content[0].descricao").value(domain.getDescricao()))
                                .andExpect(jsonPath("$.content[0].preco").value(domain.getPreco()))
                ;
    }
}
