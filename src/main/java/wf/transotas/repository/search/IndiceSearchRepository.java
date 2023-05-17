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
import wf.transotas.domain.Indice;
import wf.transotas.repository.IndiceRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Indice} entity.
 */
public interface IndiceSearchRepository extends ElasticsearchRepository<Indice, Long>, IndiceSearchRepositoryInternal {}

interface IndiceSearchRepositoryInternal {
    Page<Indice> search(String query, Pageable pageable);

    Page<Indice> search(Query query);

    void index(Indice entity);
}

class IndiceSearchRepositoryInternalImpl implements IndiceSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final IndiceRepository repository;

    IndiceSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, IndiceRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Indice> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Indice> search(Query query) {
        SearchHits<Indice> searchHits = elasticsearchTemplate.search(query, Indice.class);
        List<Indice> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Indice entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
