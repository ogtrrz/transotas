package wf.transotas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import wf.transotas.domain.Comentarios;

/**
 * Spring Data JPA repository for the Comentarios entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentariosRepository extends JpaRepository<Comentarios, Long>, JpaSpecificationExecutor<Comentarios> {}
