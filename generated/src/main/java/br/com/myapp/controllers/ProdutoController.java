package br.com.myapp.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.dtos.ProdutoDTO;
import br.com.myapp.services.ProdutoServicePort;
import br.com.myapp.converters.ProdutoDTOToDomainConverter;
import br.com.myapp.converters.ProdutoDomainToDTOConverter;

@RestController
@RequestMapping("/api/produtos")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoServicePort service;
    private final ProdutoDTOToDomainConverter dtoToDomainConverter;
    private final ProdutoDomainToDTOConverter domainToDtoConverter;

	@GetMapping("/search")
	public ResponseEntity<Page<ProdutoDTO>> searchProduto(
	        @ModelAttribute final ProdutoDTO dto, final Pageable pageable) {
	    final ProdutoDomain domain = dtoToDomainConverter.convert(dto);
	    final Page<ProdutoDomain> domainsPage = service.searchWithFilters(domain, pageable);
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
