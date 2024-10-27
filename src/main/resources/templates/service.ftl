package ${packageName}.services;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import ${packageName}.domain.${entityName}Domain;
import ${packageName}.repositories.${entityName}RepositoryPort;

@Service
@AllArgsConstructor
public class ${entityName}Service {

    private final ${entityName}RepositoryPort repositoryPort;

    public List<${entityName}Domain> findAll() {
        return repositoryPort.findAll();
    }

    public ${entityName}Domain findById(final Long id) {
        return repositoryPort.findById(id).orElse(null);
    }

    public ${entityName}Domain save(final ${entityName}Domain domain) {
        return repositoryPort.save(domain);
    }

    public void deleteById(final Long id) {
        repositoryPort.deleteById(id);
    }
}
