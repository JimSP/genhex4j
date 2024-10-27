package br.com.myapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

@Service
@AllArgsConstructor
public class ProdutoServiceAdapter implements ProdutoServicePort {

    private final ProdutoRepositoryPort repositoryPort;

	@Override
	public Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable) {
	    return repositoryPort.searchWithFilters(domain, pageable);
	}

    @Override
    public ProdutoDomain findById(final Long id) {
        return repositoryPort.findById(id).orElse(null);
    }

    @Override
    public ProdutoDomain save(final ProdutoDomain domain) {
        return repositoryPort.save(domain);
    }

    @Override
    public void deleteById(Long id) {
        repositoryPort.deleteById(id);
    }
}
