package br.com.myapp.controllers;

import br.com.myapp.services.ProdutoServicePort;
import br.com.myapp.dto.ProdutoDTO;
import br.com.myapp.converters.ProdutoDTOToDomainConverter;
import br.com.myapp.converters.ProdutoDomainToDTOConverter;
import br.com.myapp.domain.ProdutoDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutoControllerTest {

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void testCreateProduto() throws Exception {
        final ProdutoDTO dto = new ProdutoDTO(1L, "Example", 0.0);
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);

        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Example"));
    }

    @Test
    public void testGetProdutoById() throws Exception {
        final ProdutoDTO dto = new ProdutoDTO(1L, "Example", 0.0);
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);

        when(service.findById(1L)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Example"));
    }

    @Test
    public void testUpdateProduto() throws Exception {
        final ProdutoDTO dto = new ProdutoDTO(1L, "Updated Example", 1.0);
        final ProdutoDomain domain = new ProdutoDomain(1L, "Updated Example", 1.0);

        when(service.findById(1L)).thenReturn(domain);
        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produtos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Updated Example"));
    }

    @Test
    public void testDeleteProduto() throws Exception {
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);
        when(service.findById(1L)).thenReturn(domain);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/produtos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    public void testSearchProduto() throws Exception {
        final ProdutoDTO dto = new ProdutoDTO(1L, "Example", 0.0);
        final ProdutoDomain domain = new ProdutoDomain(1L, "Example", 0.0);
        final List<ProdutoDomain> domainList = Arrays.asList(domain);
        final Page<ProdutoDomain> page = new PageImpl<>(domainList);
        final Pageable pageable = PageRequest.of(0, 10);

        when(dtoToDomainConverter.convertToDomain(dto)).thenReturn(domain);
        when(service.searchWithFilters(domain, pageable)).thenReturn(page);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produtos/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Example"));
    }
}
