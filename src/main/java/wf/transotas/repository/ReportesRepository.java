package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Reportes;

/**
 * Spring Data JPA repository for the Reportes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportesRepository extends JpaRepository<Reportes, Long>, JpaSpecificationExecutor<Reportes> {}
