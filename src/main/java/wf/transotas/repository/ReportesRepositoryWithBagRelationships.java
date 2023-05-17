package wf.transotas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import wf.transotas.domain.Reportes;

public interface ReportesRepositoryWithBagRelationships {
    Optional<Reportes> fetchBagRelationships(Optional<Reportes> reportes);

    List<Reportes> fetchBagRelationships(List<Reportes> reportes);

    Page<Reportes> fetchBagRelationships(Page<Reportes> reportes);
}
