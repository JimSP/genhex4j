package ${packageName}.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import ${packageName}.domain.${entityName}Domain;

public interface ${entityName}RepositoryPort {

    Page<${entityName}Domain> searchWithFilters(final ${entityName}Domain domain, final Pageable pageable);
    Optional<${entityName}Domain> findById(final Long id);
    ${entityName}Domain save(final ${entityName}Domain domain);
    void deleteById(final Long id);
}
