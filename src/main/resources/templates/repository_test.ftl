package ${packageName}.repository;

import ${packageName}.entity.${entityName}Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class ${entityName}RepositoryTest {

    @Autowired
    private ${entityName}Repository ${entityName?uncap_first}Repository;

    @Test
    public void testSaveAndFindById() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        entity.setNome("Example");

        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.save(entity);
        Optional<${entityName}Entity> foundEntity = ${entityName?uncap_first}Repository.findById(savedEntity.getId());

        assertTrue(foundEntity.isPresent());
        assertEquals(savedEntity.getId(), foundEntity.get().getId());
        assertEquals("Example", foundEntity.get().getNome());
    }

    @Test
    public void testFindAll() {
        final ${entityName}Entity entity1 = new ${entityName}Entity();
        entity1.setNome("Example1");
        final ${entityName}Entity entity2 = new ${entityName}Entity();
        entity2.setNome("Example2");

        ${entityName?uncap_first}Repository.save(entity1);
        ${entityName?uncap_first}Repository.save(entity2);

        final List<${entityName}Entity> entities = ${entityName?uncap_first}Repository.findAll();

        assertEquals(2, entities.size());
        assertEquals("Example1", entities.get(0).getNome());
        assertEquals("Example2", entities.get(1).getNome());
    }

    @Test
    public void testDeleteById() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        entity.setNome("Example");

        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.save(entity);
        Long id = savedEntity.getId();
        
        final ${entityName?uncap_first}Repository.deleteById(id);
        Optional<${entityName}Entity> deletedEntity = ${entityName?uncap_first}Repository.findById(id);

        assertFalse(deletedEntity.isPresent());
    }

    @Test
    public void testFindByNonExistentId() {
        final Optional<${entityName}Entity> entity = ${entityName?uncap_first}Repository.findById(999L);
        assertFalse(entity.isPresent(), "Expected no entity to be found with ID 999");
    }
}
