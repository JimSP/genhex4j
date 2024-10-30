package br.com.myapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.myapp.domains.ProdutoDomain;

public interface ProdutoServicePort {

    Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable);
    ProdutoDomain findById(final Long id);
    ProdutoDomain save(final ProdutoDomain domain);
    void deleteById(final Long id);
}
