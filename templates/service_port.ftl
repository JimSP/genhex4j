package ${packageName}.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ${packageName}.domain.${entityName}Domain;

public interface ${entityName}ServicePort {

    Page<${entityName}Domain> searchWithFilters(final ${entityName}Domain domain, final Pageable pageable);
    ${entityName}Domain findById(final Long id);
    ${entityName}Domain save(final ${entityName}Domain domain);
    void deleteById(final Long id);
}
