package wf.transotas.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import wf.transotas.domain.Comentarios;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ComentariosRepositoryWithBagRelationshipsImpl implements ComentariosRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Comentarios> fetchBagRelationships(Optional<Comentarios> comentarios) {
        return comentarios.map(this::fetchReportes);
    }

    @Override
    public Page<Comentarios> fetchBagRelationships(Page<Comentarios> comentarios) {
        return new PageImpl<>(fetchBagRelationships(comentarios.getContent()), comentarios.getPageable(), comentarios.getTotalElements());
    }

    @Override
    public List<Comentarios> fetchBagRelationships(List<Comentarios> comentarios) {
        return Optional.of(comentarios).map(this::fetchReportes).orElse(Collections.emptyList());
    }

    Comentarios fetchReportes(Comentarios result) {
        return entityManager
            .createQuery(
                "select comentarios from Comentarios comentarios left join fetch comentarios.reportes where comentarios is :comentarios",
                Comentarios.class
            )
            .setParameter("comentarios", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Comentarios> fetchReportes(List<Comentarios> comentarios) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, comentarios.size()).forEach(index -> order.put(comentarios.get(index).getId(), index));
        List<Comentarios> result = entityManager
            .createQuery(
                "select distinct comentarios from Comentarios comentarios left join fetch comentarios.reportes where comentarios in :comentarios",
                Comentarios.class
            )
            .setParameter("comentarios", comentarios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
