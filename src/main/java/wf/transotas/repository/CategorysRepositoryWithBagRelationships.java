package wf.transotas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import wf.transotas.domain.Categorys;

public interface CategorysRepositoryWithBagRelationships {
    Optional<Categorys> fetchBagRelationships(Optional<Categorys> categorys);

    List<Categorys> fetchBagRelationships(List<Categorys> categorys);

    Page<Categorys> fetchBagRelationships(Page<Categorys> categorys);
}
