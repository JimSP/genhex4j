package br.com.myapp.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import br.com.myapp.domain.ProdutoDomain;
import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.converters.ProdutoDomainToEntityConverter;
import br.com.myapp.converters.ProdutoEntityToDomainConverter;

@Component
@AllArgsConstructor
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {

    private final ProdutoRepository repository;
    private final ProdutoDomainToEntityConverter domainToEntityConverter;
    private final ProdutoEntityToDomainConverter entityToDomainConverter;

    @Override
    public List<ProdutoDomain> findAll() {
        return repository.findAll().stream()
                         .map(entityToDomainConverter::convert)
                         .collect(Collectors.toList());
    }

    @Override
    public Optional<ProdutoDomain> findById(final Long id) {
        return repository.findById(id)
                         .map(entityToDomainConverter::convert);
    }

    @Override
    public ProdutoDomain save(final ProdutoDomain domain) {
        final ProdutoEntity entity = domainToEntityConverter.convert(domain);
        final ProdutoEntity savedEntity = repository.save(entity);
        return entityToDomainConverter.convert(savedEntity);
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
