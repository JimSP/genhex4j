package br.com.myapp.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;

import lombok.AllArgsConstructor;

import br.com.myapp.domains.ProdutoDomain;
import br.com.myapp.entities.ProdutoEntity;
import br.com.myapp.converters.ProdutoDomainToJPAConverter;
import br.com.myapp.converters.ProdutoJPAToDomainConverter;

@Component
@AllArgsConstructor
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {

    private final ProdutoRepository repository;
    private final ProdutoDomainToJPAConverter domainToEntityConverter;
    private final ProdutoJPAToDomainConverter entityToDomainConverter;

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
    public Page<ProdutoDomain> searchWithFilters(final ProdutoDomain domain, final Pageable pageable){
        return repository.findAll(Example.of(domainToEntityConverter.convert(domain)), pageable)
                         .map(entityToDomainConverter::convert);
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
