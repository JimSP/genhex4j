package ${packageName}.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.entities.${entityName}Entity;
import ${packageName}.converters.${entityName}DomainToEntityConverter;
import ${packageName}.converters.${entityName}EntityToDomainConverter;

@Component
@AllArgsConstructor
public class ${entityName}RepositoryAdapter implements ${entityName}RepositoryPort {

    private final ${entityName}Repository repository;
    private final ${entityName}DomainToEntityConverter domainToEntityConverter;
    private final ${entityName}EntityToDomainConverter entityToDomainConverter;

    @Override
    public List<${entityName}Domain> findAll() {
        return repository.findAll().stream()
                         .map(entityToDomainConverter::convert)
                         .collect(Collectors.toList());
    }

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
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
