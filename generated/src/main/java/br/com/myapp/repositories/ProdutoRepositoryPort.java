package br.com.myapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import br.com.myapp.domain.ProdutoDomain;

public interface ProdutoRepositoryPort {

    Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable);
    Optional<ProdutoDomain> findById(final Long id);
    ProdutoDomain save(final ProdutoDomain domain);
    void deleteById(final Long id);
}
