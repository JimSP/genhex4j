package ${packageName}.services;

import lombok.AllArgsConstructor;
import java.util.List;

import org.springframework.stereotype.Service;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.repositories.${entityName}RepositoryPort;

@Service
@AllArgsConstructor
public class ${entityName}ServiceAdapter implements ${entityName}ServicePort {

    private final ${entityName}RepositoryPort repositoryPort;

	@Override
	public Page<${entityName}Domain> searchWithFilters(final ${entityName}Domain domain, Pageable pageable) {
	    return repository.findAllByExample(domain, pageable);
	}

    @Override
    public ${entityName}Domain findById(final Long id) {
        return repositoryPort.findById(id).orElse(null);
    }

    @Override
    public ${entityName}Domain save(final ${entityName}Domain domain) {
        return repositoryPort.save(domain);
    }

    @Override
    public void deleteById(Long id) {
        repositoryPort.deleteById(id);
    }
}
