package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Informacion;

/**
 * Spring Data JPA repository for the Informacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformacionRepository extends JpaRepository<Informacion, Long>, JpaSpecificationExecutor<Informacion> {}
