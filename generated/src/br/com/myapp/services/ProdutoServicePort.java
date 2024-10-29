package br.com.myapp.services;

import java.util.List;
import br.com.myapp.domain.ProdutoDomain;

public interface ProdutoServicePort {

    Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable)
    ProdutoDomain findById(final Long id);
    ProdutoDomain save(final ProdutoDomain domain);
    void deleteById(final Long id);
}
