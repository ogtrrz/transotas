package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.CasoText;

/**
 * Spring Data JPA repository for the CasoText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasoTextRepository extends JpaRepository<CasoText, Long> {}
