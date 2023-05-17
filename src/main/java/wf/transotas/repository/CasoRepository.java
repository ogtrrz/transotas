package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Caso;

/**
 * Spring Data JPA repository for the Caso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasoRepository extends JpaRepository<Caso, Long>, JpaSpecificationExecutor<Caso> {}
