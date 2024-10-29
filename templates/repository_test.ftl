<#import "/testValueModule.ftl" as testValues>

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
        <#list attributes as attribute>
        entity.set${attribute.name?cap_first}(testValues.generateTestValue("${attribute.type}"));
        </#list>

        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.save(entity);
        final Optional<${entityName}Entity> foundEntity = ${entityName?uncap_first}Repository.findById(savedEntity.getId());

        assertTrue(foundEntity.isPresent());
        assertEquals(savedEntity.getId(), foundEntity.get().getId());
        <#list attributes as attribute>
        assertEquals(entity.get${attribute.name?cap_first}(), foundEntity.get().get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testFindAll() {
        final ${entityName}Entity entity1 = new ${entityName}Entity();
        <#list attributes as attribute>
        entity1.set${attribute.name?cap_first}(testValues.generateTestValue("${attribute.type}"));
        </#list>

        final ${entityName}Entity entity2 = new ${entityName}Entity();
        <#list attributes as attribute>
        entity2.set${attribute.name?cap_first}(testValues.generateTestValue("${attribute.type}"));
        </#list>

        final ${entityName?uncap_first}Repository.save(entity1);
        final ${entityName?uncap_first}Repository.save(entity2);

        final List<${entityName}Entity> entities = ${entityName?uncap_first}Repository.findAll();

        assertEquals(2, entities.size());
        <#list attributes as attribute>
        assertEquals(entity1.get${attribute.name?cap_first}(), entities.get(0).get${attribute.name?cap_first}());
        assertEquals(entity2.get${attribute.name?cap_first}(), entities.get(1).get${attribute.name?cap_first}());
        </#list>
    }

    @Test
    public void testDeleteById() {
        final ${entityName}Entity entity = new ${entityName}Entity();
        <#list attributes as attribute>
        entity.set${attribute.name?cap_first}(testValues.generateTestValue("${attribute.type}"));
        </#list>

        final ${entityName}Entity savedEntity = ${entityName?uncap_first}Repository.save(entity);
        final Long id = savedEntity.getId();

        ${entityName?uncap_first}Repository.deleteById(id);
        final Optional<${entityName}Entity> deletedEntity = ${entityName?uncap_first}Repository.findById(id);

        assertFalse(deletedEntity.isPresent());
    }

    @Test
    public void testFindByNonExistentId() {
        final Optional<${entityName}Entity> entity = ${entityName?uncap_first}Repository.findById(999L);
        assertFalse(entity.isPresent(), "Expected no entity to be found with ID 999");
    }
}
