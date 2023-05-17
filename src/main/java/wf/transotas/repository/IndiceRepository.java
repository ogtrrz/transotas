package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Indice;

/**
 * Spring Data JPA repository for the Indice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndiceRepository extends JpaRepository<Indice, Long>, JpaSpecificationExecutor<Indice> {}
