package wf.transotas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import wf.transotas.domain.Comentarios;

public interface ComentariosRepositoryWithBagRelationships {
    Optional<Comentarios> fetchBagRelationships(Optional<Comentarios> comentarios);

    List<Comentarios> fetchBagRelationships(List<Comentarios> comentarios);

    Page<Comentarios> fetchBagRelationships(Page<Comentarios> comentarios);
}
