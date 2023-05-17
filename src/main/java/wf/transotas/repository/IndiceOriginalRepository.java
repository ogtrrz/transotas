package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.IndiceOriginal;

/**
 * Spring Data JPA repository for the IndiceOriginal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndiceOriginalRepository extends JpaRepository<IndiceOriginal, Long>, JpaSpecificationExecutor<IndiceOriginal> {}
