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
import wf.transotas.domain.Reportes;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ReportesRepositoryWithBagRelationshipsImpl implements ReportesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reportes> fetchBagRelationships(Optional<Reportes> reportes) {
        return reportes.map(this::fetchCategorys).map(this::fetchComentarios);
    }

    @Override
    public Page<Reportes> fetchBagRelationships(Page<Reportes> reportes) {
        return new PageImpl<>(fetchBagRelationships(reportes.getContent()), reportes.getPageable(), reportes.getTotalElements());
    }

    @Override
    public List<Reportes> fetchBagRelationships(List<Reportes> reportes) {
        return Optional.of(reportes).map(this::fetchCategorys).map(this::fetchComentarios).orElse(Collections.emptyList());
    }

    Reportes fetchCategorys(Reportes result) {
        return entityManager
            .createQuery(
                "select reportes from Reportes reportes left join fetch reportes.categorys where reportes is :reportes",
                Reportes.class
            )
            .setParameter("reportes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Reportes> fetchCategorys(List<Reportes> reportes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reportes.size()).forEach(index -> order.put(reportes.get(index).getId(), index));
        List<Reportes> result = entityManager
            .createQuery(
                "select distinct reportes from Reportes reportes left join fetch reportes.categorys where reportes in :reportes",
                Reportes.class
            )
            .setParameter("reportes", reportes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Reportes fetchComentarios(Reportes result) {
        return entityManager
            .createQuery(
                "select reportes from Reportes reportes left join fetch reportes.comentarios where reportes is :reportes",
                Reportes.class
            )
            .setParameter("reportes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Reportes> fetchComentarios(List<Reportes> reportes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reportes.size()).forEach(index -> order.put(reportes.get(index).getId(), index));
        List<Reportes> result = entityManager
            .createQuery(
                "select distinct reportes from Reportes reportes left join fetch reportes.comentarios where reportes in :reportes",
                Reportes.class
            )
            .setParameter("reportes", reportes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
