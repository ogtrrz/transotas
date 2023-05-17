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
import wf.transotas.domain.Categorys;
import wf.transotas.repository.CategorysRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Categorys} entity.
 */
public interface CategorysSearchRepository extends ElasticsearchRepository<Categorys, Long>, CategorysSearchRepositoryInternal {}

interface CategorysSearchRepositoryInternal {
    Page<Categorys> search(String query, Pageable pageable);

    Page<Categorys> search(Query query);

    void index(Categorys entity);
}

class CategorysSearchRepositoryInternalImpl implements CategorysSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final CategorysRepository repository;

    CategorysSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, CategorysRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Categorys> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Categorys> search(Query query) {
        SearchHits<Categorys> searchHits = elasticsearchTemplate.search(query, Categorys.class);
        List<Categorys> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Categorys entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
