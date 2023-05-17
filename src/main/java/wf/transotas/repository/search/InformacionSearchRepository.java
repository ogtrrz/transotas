package wf.transotas.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Informacion;
import wf.transotas.repository.InformacionRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Informacion} entity.
 */
public interface InformacionSearchRepository extends ElasticsearchRepository<Informacion, Long>, InformacionSearchRepositoryInternal {}

interface InformacionSearchRepositoryInternal {
    Page<Informacion> search(String query, Pageable pageable);

    Page<Informacion> search(Query query);

    void index(Informacion entity);
}

class InformacionSearchRepositoryInternalImpl implements InformacionSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final InformacionRepository repository;

    InformacionSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, InformacionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Informacion> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Informacion> search(Query query) {
        SearchHits<Informacion> searchHits = elasticsearchTemplate.search(query, Informacion.class);
        List<Informacion> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Informacion entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
