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
import wf.transotas.domain.Categorys;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CategorysRepositoryWithBagRelationshipsImpl implements CategorysRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Categorys> fetchBagRelationships(Optional<Categorys> categorys) {
        return categorys.map(this::fetchReportes);
    }

    @Override
    public Page<Categorys> fetchBagRelationships(Page<Categorys> categorys) {
        return new PageImpl<>(fetchBagRelationships(categorys.getContent()), categorys.getPageable(), categorys.getTotalElements());
    }

    @Override
    public List<Categorys> fetchBagRelationships(List<Categorys> categorys) {
        return Optional.of(categorys).map(this::fetchReportes).orElse(Collections.emptyList());
    }

    Categorys fetchReportes(Categorys result) {
        return entityManager
            .createQuery(
                "select categorys from Categorys categorys left join fetch categorys.reportes where categorys is :categorys",
                Categorys.class
            )
            .setParameter("categorys", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Categorys> fetchReportes(List<Categorys> categorys) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, categorys.size()).forEach(index -> order.put(categorys.get(index).getId(), index));
        List<Categorys> result = entityManager
            .createQuery(
                "select distinct categorys from Categorys categorys left join fetch categorys.reportes where categorys in :categorys",
                Categorys.class
            )
            .setParameter("categorys", categorys)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
