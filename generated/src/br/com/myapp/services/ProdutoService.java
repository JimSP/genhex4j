package br.com.myapp.services;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.repositories.ProdutoRepositoryPort;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final ProdutoRepositoryPort repositoryPort;

    public List<ProdutoDomain> findAll() {
        return repositoryPort.findAll();
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
