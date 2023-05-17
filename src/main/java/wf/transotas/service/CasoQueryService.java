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
import wf.transotas.domain.Caso;
import wf.transotas.repository.CasoRepository;
import wf.transotas.repository.search.CasoSearchRepository;
import wf.transotas.service.criteria.CasoCriteria;
import wf.transotas.service.dto.CasoDTO;
import wf.transotas.service.mapper.CasoMapper;

/**
 * Service for executing complex queries for {@link Caso} entities in the database.
 * The main input is a {@link CasoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CasoDTO} or a {@link Page} of {@link CasoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CasoQueryService extends QueryService<Caso> {

    private final Logger log = LoggerFactory.getLogger(CasoQueryService.class);

    private final CasoRepository casoRepository;

    private final CasoMapper casoMapper;

    private final CasoSearchRepository casoSearchRepository;

    public CasoQueryService(CasoRepository casoRepository, CasoMapper casoMapper, CasoSearchRepository casoSearchRepository) {
        this.casoRepository = casoRepository;
        this.casoMapper = casoMapper;
        this.casoSearchRepository = casoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CasoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CasoDTO> findByCriteria(CasoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Caso> specification = createSpecification(criteria);
        return casoMapper.toDto(casoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CasoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CasoDTO> findByCriteria(CasoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Caso> specification = createSpecification(criteria);
        return casoRepository.findAll(specification, page).map(casoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CasoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Caso> specification = createSpecification(criteria);
        return casoRepository.count(specification);
    }

    /**
     * Function to convert {@link CasoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Caso> createSpecification(CasoCriteria criteria) {
        Specification<Caso> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Caso_.id));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Caso_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Caso_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Caso_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Caso_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Caso_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Caso_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Caso_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Caso_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Caso_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Caso_.extra10));
            }
        }
        return specification;
    }
}
