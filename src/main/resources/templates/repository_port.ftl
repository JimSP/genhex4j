package ${packageName}.repositories;

import java.util.List;
import java.util.Optional;
import ${packageName}.domain.${entityName}Domain;

public interface ${entityName}RepositoryPort {

    Page<${entityName}Domain> searchWithFilters(final ${entityName}DTO dto, final Pageable pageable);
    Optional<${entityName}Domain> findById(final Long id);
    ${entityName}Domain save(final ${entityName}Domain domain);
    void deleteById(final Long id);
}
