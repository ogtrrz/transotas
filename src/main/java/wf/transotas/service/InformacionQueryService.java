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
import wf.transotas.domain.Informacion;
import wf.transotas.repository.InformacionRepository;
import wf.transotas.repository.search.InformacionSearchRepository;
import wf.transotas.service.criteria.InformacionCriteria;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.mapper.InformacionMapper;

/**
 * Service for executing complex queries for {@link Informacion} entities in the database.
 * The main input is a {@link InformacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InformacionDTO} or a {@link Page} of {@link InformacionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InformacionQueryService extends QueryService<Informacion> {

    private final Logger log = LoggerFactory.getLogger(InformacionQueryService.class);

    private final InformacionRepository informacionRepository;

    private final InformacionMapper informacionMapper;

    private final InformacionSearchRepository informacionSearchRepository;

    public InformacionQueryService(
        InformacionRepository informacionRepository,
        InformacionMapper informacionMapper,
        InformacionSearchRepository informacionSearchRepository
    ) {
        this.informacionRepository = informacionRepository;
        this.informacionMapper = informacionMapper;
        this.informacionSearchRepository = informacionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InformacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InformacionDTO> findByCriteria(InformacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Informacion> specification = createSpecification(criteria);
        return informacionMapper.toDto(informacionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InformacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InformacionDTO> findByCriteria(InformacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Informacion> specification = createSpecification(criteria);
        return informacionRepository.findAll(specification, page).map(informacionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InformacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Informacion> specification = createSpecification(criteria);
        return informacionRepository.count(specification);
    }

    /**
     * Function to convert {@link InformacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Informacion> createSpecification(InformacionCriteria criteria) {
        Specification<Informacion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Informacion_.id));
            }
            if (criteria.getComentarios() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComentarios(), Informacion_.comentarios));
            }
            if (criteria.getVistas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVistas(), Informacion_.vistas));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Informacion_.rating));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Informacion_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Informacion_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Informacion_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Informacion_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Informacion_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Informacion_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Informacion_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Informacion_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Informacion_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Informacion_.extra10));
            }
            if (criteria.getReportesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportesId(),
                            root -> root.join(Informacion_.reportes, JoinType.LEFT).get(Reportes_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
