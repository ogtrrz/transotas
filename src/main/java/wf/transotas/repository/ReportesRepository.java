package wf.transotas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Reportes;

/**
 * Spring Data JPA repository for the Reportes entity.
 *
 * When extending this class, extend ReportesRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ReportesRepository
    extends ReportesRepositoryWithBagRelationships, JpaRepository<Reportes, Long>, JpaSpecificationExecutor<Reportes> {
    default Optional<Reportes> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Reportes> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Reportes> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
