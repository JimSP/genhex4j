package br.com.myapp.services;

import lombok.AllArgsConstructor;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

@Service
@AllArgsConstructor
public class ProdutoServiceAdapter implements ProdutoServicePort {

    private final ProdutoRepositoryPort repositoryPort;

	@Override
	public Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, Pageable pageable) {
	    return repository.findAllByExample(domain, pageable);
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
