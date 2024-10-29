package ${packageName}.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.dto.${entityName}DTO;
import ${packageName}.services.${entityName}ServicePort;
import ${packageName}.dto.${entityName}DTOToDomainConverter;
import ${packageName}.dto.${entityName}DomainToDTOConverter;

@RestController
@RequestMapping("/api/${entityName?lower_case}s")
@AllArgsConstructor
public class ${entityName}Controller {

    private final ${entityName}ServicePort service;
    private final ${entityName}DTOToDomainConverter dtoToDomainConverter;
    private final ${entityName}DomainToDTOConverter domainToDtoConverter;

	@GetMapping("/search")
	public ResponseEntity<Page<${entityName}DTO>> search${entityName}(
	        @ModelAttribute final ${entityName}DTO dto, final Pageable pageable) {
	    final ${entityName}Domain domain = dtoToDomainConverter.convert(dto);
	    final Page<${entityName}Domain> domainsPage = service.searchWithFilters(domain, pageable);
	    final Page<${entityName}DTO> dtosPage = domainsPage.map(domainToDtoConverter::convert);
	    return ResponseEntity.ok(dtosPage);
	}

    @GetMapping("/{id}")
    public ResponseEntity<${entityName}DTO> getById(final @PathVariable Long id) {
        final ${entityName}Domain domain = service.findById(id);
        return domain != null ? ResponseEntity.ok(domainToDtoConverter.convert(domain)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<${entityName}DTO> create(final @RequestBody ${entityName}DTO dto) {
        final ${entityName}Domain domain = dtoToDomainConverter.convert(dto);
        final ${entityName}Domain createdDomain = service.save(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(domainToDtoConverter.convert(createdDomain));
    }

    @PutMapping("/{id}")
    public ResponseEntity<${entityName}DTO> update(final @PathVariable Long id, final @RequestBody ${entityName}DTO dto) {
        if (service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        final ${entityName}Domain domain = dtoToDomainConverter.convert(dto);
        final ${entityName}Domain updatedDomain = service.save(domain);
        return ResponseEntity.ok(domainToDtoConverter.convert(updatedDomain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(final @PathVariable Long id) {
        if (service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
