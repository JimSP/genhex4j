<#import "/testValueModule.ftl" as testValues>

package ${packageName}.controllers;

import ${packageName}.services.${entityName}ServicePort;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.dto.${entityName}DTOToDomainConverter;
import ${packageName}.dto.${entityName}DomainToDTOConverter;
import ${packageName}.domain.${entityName}Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ${entityName}ControllerTest {

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
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void testCreate${entityName}() throws Exception {
        final ${entityName}DTO dto = ${entityName}DTO.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/${entityName?lower_case}s")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testGet${entityName}ById() throws Exception {
        final ${entityName}DTO dto = ${entityName}DTO.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        when(service.findById(dto.getId())).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/${entityName?lower_case}s/{id}", dto.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testUpdate${entityName}() throws Exception {
        final ${entityName}DTO dto = ${entityName}DTO.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        when(service.findById(dto.getId())).thenReturn(domain);
        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.save(domain)).thenReturn(domain);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/${entityName?lower_case}s/{id}", dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void testDelete${entityName}() throws Exception {
        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        when(service.findById(domain.getId())).thenReturn(domain);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/${entityName?lower_case}s/{id}", domain.getId()))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(domain.getId());
    }

    @Test
    void testSearch${entityName}() throws Exception {
        final ${entityName}DTO dto = ${entityName}DTO.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final ${entityName}Domain domain = ${entityName}Domain.builder()
            <#list attributes as attribute>
                .${attribute.name}(testValues.generateTestValue(attribute.type))
            </#list>
            .build();

        final List<${entityName}Domain> domainList = Arrays.asList(domain);
        final Page<${entityName}Domain> page = new PageImpl<>(domainList);
        final Pageable pageable = PageRequest.of(0, 10);

        when(dtoToDomainConverter.convert(dto)).thenReturn(domain);
        when(service.searchWithFilters(domain, pageable)).thenReturn(page);
        when(domainToDtoConverter.convert(domain)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/${entityName?lower_case}s/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.getId()));
    }
}
