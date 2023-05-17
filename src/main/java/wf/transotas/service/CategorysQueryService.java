package wf.transotas.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import wf.transotas.domain.*; // for static metamodels
import wf.transotas.domain.Categorys;
import wf.transotas.repository.CategorysRepository;
import wf.transotas.repository.search.CategorysSearchRepository;
import wf.transotas.service.criteria.CategorysCriteria;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.service.mapper.CategorysMapper;

/**
 * Service for executing complex queries for {@link Categorys} entities in the database.
 * The main input is a {@link CategorysCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategorysDTO} or a {@link Page} of {@link CategorysDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategorysQueryService extends QueryService<Categorys> {

    private final Logger log = LoggerFactory.getLogger(CategorysQueryService.class);

    private final CategorysRepository categorysRepository;

    private final CategorysMapper categorysMapper;

    private final CategorysSearchRepository categorysSearchRepository;

    public CategorysQueryService(
        CategorysRepository categorysRepository,
        CategorysMapper categorysMapper,
        CategorysSearchRepository categorysSearchRepository
    ) {
        this.categorysRepository = categorysRepository;
        this.categorysMapper = categorysMapper;
        this.categorysSearchRepository = categorysSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CategorysDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategorysDTO> findByCriteria(CategorysCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Categorys> specification = createSpecification(criteria);
        return categorysMapper.toDto(categorysRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategorysDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorysDTO> findByCriteria(CategorysCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Categorys> specification = createSpecification(criteria);
        return categorysRepository.findAll(specification, page).map(categorysMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategorysCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Categorys> specification = createSpecification(criteria);
        return categorysRepository.count(specification);
    }

    /**
     * Function to convert {@link CategorysCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Categorys> createSpecification(CategorysCriteria criteria) {
        Specification<Categorys> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Categorys_.id));
            }
            if (criteria.getCategoria() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoria(), Categorys_.categoria));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Categorys_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Categorys_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Categorys_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Categorys_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Categorys_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Categorys_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Categorys_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Categorys_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Categorys_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Categorys_.extra10));
            }
            if (criteria.getReportesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportesId(),
                            root -> root.join(Categorys_.reportes, JoinType.LEFT).get(Reportes_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
