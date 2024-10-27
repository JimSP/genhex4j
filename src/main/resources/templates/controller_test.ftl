package ${packageName}.controllers;

import ${packageName}.services.${entityName}ServicePort;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.converters.${entityName}DTOToDomainConverter;
import ${packageName}.converters.${entityName}DomainToDTOConverter;
import ${packageName}.domain.${entityName}Domain;
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
public class ${entityName}ControllerTest {

    @Mock
    private ${entityName}ServicePort service;

    @Mock
    private ${entityName}DTOToDomainConverter dtoToDomainConverter;

    @Mock
    private ${entityName}DomainToDTOConverter domainToDtoConverter;

    @InjectMocks
    private ${entityName}Controller controller;

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
    public void testCreate${entityName}() throws Exception {
        final ${entityName}DTO dto = new ${entityName}DTO(1L, "Example", 0.0);
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);

        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/${entityName?lower_case}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Example"));
    }

    @Test
    public void testGet${entityName}ById() throws Exception {
        final ${entityName}DTO dto = new ${entityName}DTO(1L, "Example", 0.0);
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);

        when(service.findById(1L)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/${entityName?lower_case}s/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Example"));
    }

    @Test
    public void testUpdate${entityName}() throws Exception {
        final ${entityName}DTO dto = new ${entityName}DTO(1L, "Updated Example", 1.0);
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Updated Example", 1.0);

        when(service.findById(1L)).thenReturn(domain);
        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/${entityName?lower_case}s/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Updated Example"));
    }

    @Test
    public void testDelete${entityName}() throws Exception {
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);
        when(service.findById(1L)).thenReturn(domain);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/${entityName?lower_case}s/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    public void testSearch${entityName}() throws Exception {
        final ${entityName}DTO dto = new ${entityName}DTO(1L, "Example", 0.0);
        final ${entityName}Domain domain = new ${entityName}Domain(1L, "Example", 0.0);
        final List<${entityName}Domain> domainList = Arrays.asList(domain);
        final Page<${entityName}Domain> page = new PageImpl<>(domainList);
        final Pageable pageable = PageRequest.of(0, 10);

        when(dtoToDomainConverter.convertToDomain(dto)).thenReturn(domain);
        when(service.searchWithFilters(domain, pageable)).thenReturn(page);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/${entityName?lower_case}s/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Example"));
    }
}
