package ${packageName}.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ${packageName}.entities.${entityName}Entity;

@Repository
public interface ${entityName}Repository extends JpaRepository<${entityName}Entity, Long> {

}