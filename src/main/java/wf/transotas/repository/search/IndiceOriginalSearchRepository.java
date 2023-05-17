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
import wf.transotas.domain.IndiceOriginal;
import wf.transotas.repository.IndiceOriginalRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IndiceOriginal} entity.
 */
public interface IndiceOriginalSearchRepository
    extends ElasticsearchRepository<IndiceOriginal, Long>, IndiceOriginalSearchRepositoryInternal {}

interface IndiceOriginalSearchRepositoryInternal {
    Page<IndiceOriginal> search(String query, Pageable pageable);

    Page<IndiceOriginal> search(Query query);

    void index(IndiceOriginal entity);
}

class IndiceOriginalSearchRepositoryInternalImpl implements IndiceOriginalSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final IndiceOriginalRepository repository;

    IndiceOriginalSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, IndiceOriginalRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<IndiceOriginal> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<IndiceOriginal> search(Query query) {
        SearchHits<IndiceOriginal> searchHits = elasticsearchTemplate.search(query, IndiceOriginal.class);
        List<IndiceOriginal> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(IndiceOriginal entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
