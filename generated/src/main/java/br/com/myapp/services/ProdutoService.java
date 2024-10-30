package br.com.myapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final ProdutoRepositoryPort repositoryPort;

    Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable){
        return repositoryPort.searchWithFilters(domain, pageable);
    }

    public ProdutoDomain findById(final Long id) {
        return repositoryPort.findById(id).orElse(null);
    }

    public ProdutoDomain save(final ProdutoDomain domain) {
        return repositoryPort.save(domain);
    }

    public void deleteById(final Long id) {
        repositoryPort.deleteById(id);
    }
}
