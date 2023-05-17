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
import wf.transotas.domain.Comentarios;
import wf.transotas.repository.ComentariosRepository;
import wf.transotas.repository.search.ComentariosSearchRepository;
import wf.transotas.service.criteria.ComentariosCriteria;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.service.mapper.ComentariosMapper;

/**
 * Service for executing complex queries for {@link Comentarios} entities in the database.
 * The main input is a {@link ComentariosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComentariosDTO} or a {@link Page} of {@link ComentariosDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComentariosQueryService extends QueryService<Comentarios> {

    private final Logger log = LoggerFactory.getLogger(ComentariosQueryService.class);

    private final ComentariosRepository comentariosRepository;

    private final ComentariosMapper comentariosMapper;

    private final ComentariosSearchRepository comentariosSearchRepository;

    public ComentariosQueryService(
        ComentariosRepository comentariosRepository,
        ComentariosMapper comentariosMapper,
        ComentariosSearchRepository comentariosSearchRepository
    ) {
        this.comentariosRepository = comentariosRepository;
        this.comentariosMapper = comentariosMapper;
        this.comentariosSearchRepository = comentariosSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ComentariosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComentariosDTO> findByCriteria(ComentariosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comentarios> specification = createSpecification(criteria);
        return comentariosMapper.toDto(comentariosRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComentariosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComentariosDTO> findByCriteria(ComentariosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comentarios> specification = createSpecification(criteria);
        return comentariosRepository.findAll(specification, page).map(comentariosMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComentariosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comentarios> specification = createSpecification(criteria);
        return comentariosRepository.count(specification);
    }

    /**
     * Function to convert {@link ComentariosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comentarios> createSpecification(ComentariosCriteria criteria) {
        Specification<Comentarios> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comentarios_.id));
            }
            if (criteria.getAutor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutor(), Comentarios_.autor));
            }
            if (criteria.getComentario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComentario(), Comentarios_.comentario));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Comentarios_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Comentarios_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Comentarios_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Comentarios_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Comentarios_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Comentarios_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Comentarios_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Comentarios_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Comentarios_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Comentarios_.extra10));
            }
            if (criteria.getReportesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportesId(),
                            root -> root.join(Comentarios_.reportes, JoinType.LEFT).get(Reportes_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
