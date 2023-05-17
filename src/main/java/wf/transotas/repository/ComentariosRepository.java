package wf.transotas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Comentarios;

/**
 * Spring Data JPA repository for the Comentarios entity.
 *
 * When extending this class, extend ComentariosRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ComentariosRepository
    extends ComentariosRepositoryWithBagRelationships, JpaRepository<Comentarios, Long>, JpaSpecificationExecutor<Comentarios> {
    default Optional<Comentarios> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Comentarios> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Comentarios> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
