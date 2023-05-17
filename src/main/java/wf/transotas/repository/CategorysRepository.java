package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Categorys;

/**
 * Spring Data JPA repository for the Categorys entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorysRepository extends JpaRepository<Categorys, Long>, JpaSpecificationExecutor<Categorys> {}
