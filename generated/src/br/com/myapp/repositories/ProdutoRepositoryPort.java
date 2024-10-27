package br.com.myapp.repositories;

import java.util.List;
import java.util.Optional;
import br.com.myapp.domain.ProdutoDomain;

public interface ProdutoRepositoryPort {

    Page<ProdutoDomain> searchWithFilters(final ProdutoDTO dto, final Pageable pageable);
    Optional<ProdutoDomain> findById(final Long id);
    ProdutoDomain save(final ProdutoDomain domain);
    void deleteById(final Long id);
}
