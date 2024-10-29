
package br.com.myapp.repository;

import br.com.myapp.entity.ProdutoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    public void testSaveAndFindById() {
        final ProdutoEntity entity = new ProdutoEntity();

        final ProdutoEntity savedEntity = produtoRepository.save(entity);
        final Optional<ProdutoEntity> foundEntity = produtoRepository.findById(savedEntity.getId());

        assertTrue(foundEntity.isPresent());
        assertEquals(savedEntity.getId(), foundEntity.get().getId());
    }

    @Test
    public void testFindAll() {
        final ProdutoEntity entity1 = new ProdutoEntity();

        final ProdutoEntity entity2 = new ProdutoEntity();

        final produtoRepository.save(entity1);
        final produtoRepository.save(entity2);

        final List<ProdutoEntity> entities = produtoRepository.findAll();

        assertEquals(2, entities.size());
    }

    @Test
    public void testDeleteById() {
        final ProdutoEntity entity = new ProdutoEntity();

        final ProdutoEntity savedEntity = produtoRepository.save(entity);
        final Long id = savedEntity.getId();

        produtoRepository.deleteById(id);
        final Optional<ProdutoEntity> deletedEntity = produtoRepository.findById(id);

        assertFalse(deletedEntity.isPresent());
    }

    @Test
    public void testFindByNonExistentId() {
        final Optional<ProdutoEntity> entity = produtoRepository.findById(999L);
        assertFalse(entity.isPresent(), "Expected no entity to be found with ID 999");
    }
}
