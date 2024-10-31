package br.com.myapp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.myapp.converters.ProdutoDTOToDomainConverter;
import br.com.myapp.converters.ProdutoDomainToDTOConverter;
import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;
import br.com.myapp.services.ProdutoServicePort;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoServicePort service;
    private final ProdutoDTOToDomainConverter dtoToDomainConverter;
    private final ProdutoDomainToDTOConverter domainToDtoConverter;

	@GetMapping("/search")
	public ResponseEntity<Page<ProdutoDTO>> searchProduto(final ProdutoDTO dto, @RequestParam(name = "page", defaultValue = "0") final Integer page, @RequestParam(name = "size", defaultValue = "10") final Integer size) {
	    final ProdutoDomain domain = dtoToDomainConverter.convert(dto);
	    final Page<ProdutoDomain> domainsPage = service.searchWithFilters(domain, PageRequest.of(page, size));
	    final Page<ProdutoDTO> dtosPage = domainsPage.map(domainToDtoConverter::convert);
	    return ResponseEntity.ok(dtosPage);
	}

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> getById(final @PathVariable Long id) {
        final ProdutoDomain domain = service.findById(id);
        return domain != null ? ResponseEntity.ok(domainToDtoConverter.convert(domain)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> create(final @RequestBody ProdutoDTO dto) {
        final ProdutoDomain domain = dtoToDomainConverter.convert(dto);
        final ProdutoDomain createdDomain = service.save(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(domainToDtoConverter.convert(createdDomain));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> update(final @PathVariable Long id, final @RequestBody ProdutoDTO dto) {
        if (service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        final ProdutoDomain domain = dtoToDomainConverter.convert(dto);
        final ProdutoDomain updatedDomain = service.save(domain);
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
