package ${packageName}.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import lombok.AllArgsConstructor;

import ${packageName}.domains.${entityName}Domain;
import ${packageName}.entities.${entityName}Entity;
import ${packageName}.converters.${entityName}DomainToJPAConverter;
import ${packageName}.converters.${entityName}JPAToDomainConverter;

@Component
@AllArgsConstructor
public class ${entityName}RepositoryAdapter implements ${entityName}RepositoryPort {

    private final ${entityName}Repository repository;
    private final ${entityName}DomainToJPAConverter domainToEntityConverter;
    private final ${entityName}JPAToDomainConverter entityToDomainConverter;

    @Override
    public Optional<${entityName}Domain> findById(final Long id) {
        return repository.findById(id)
                         .map(entityToDomainConverter::convert);
    }

    @Override
    public ${entityName}Domain save(final ${entityName}Domain domain) {
        final ${entityName}Entity entity = domainToEntityConverter.convert(domain);
        final ${entityName}Entity savedEntity = repository.save(entity);
        return entityToDomainConverter.convert(savedEntity);
    }

    @Override
    public Page<${entityName}Domain> searchWithFilters(final ${entityName}Domain domain, final Pageable pageable){
    	
    	final ${entityName}Entity entity = domainToEntityConverter.convert(domain);
    	final Example<${entityName}Entity> example = Example.of(entity, ExampleMatcher.matchingAll());
    	
        return repository.findAll(example, pageable)
                         .map(entityToDomainConverter::convert);
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
