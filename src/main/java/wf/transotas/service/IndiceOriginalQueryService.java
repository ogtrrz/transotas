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
import wf.transotas.domain.IndiceOriginal;
import wf.transotas.repository.IndiceOriginalRepository;
import wf.transotas.repository.search.IndiceOriginalSearchRepository;
import wf.transotas.service.criteria.IndiceOriginalCriteria;
import wf.transotas.service.dto.IndiceOriginalDTO;
import wf.transotas.service.mapper.IndiceOriginalMapper;

/**
 * Service for executing complex queries for {@link IndiceOriginal} entities in the database.
 * The main input is a {@link IndiceOriginalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IndiceOriginalDTO} or a {@link Page} of {@link IndiceOriginalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndiceOriginalQueryService extends QueryService<IndiceOriginal> {

    private final Logger log = LoggerFactory.getLogger(IndiceOriginalQueryService.class);

    private final IndiceOriginalRepository indiceOriginalRepository;

    private final IndiceOriginalMapper indiceOriginalMapper;

    private final IndiceOriginalSearchRepository indiceOriginalSearchRepository;

    public IndiceOriginalQueryService(
        IndiceOriginalRepository indiceOriginalRepository,
        IndiceOriginalMapper indiceOriginalMapper,
        IndiceOriginalSearchRepository indiceOriginalSearchRepository
    ) {
        this.indiceOriginalRepository = indiceOriginalRepository;
        this.indiceOriginalMapper = indiceOriginalMapper;
        this.indiceOriginalSearchRepository = indiceOriginalSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IndiceOriginalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IndiceOriginalDTO> findByCriteria(IndiceOriginalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IndiceOriginal> specification = createSpecification(criteria);
        return indiceOriginalMapper.toDto(indiceOriginalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IndiceOriginalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceOriginalDTO> findByCriteria(IndiceOriginalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndiceOriginal> specification = createSpecification(criteria);
        return indiceOriginalRepository.findAll(specification, page).map(indiceOriginalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndiceOriginalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IndiceOriginal> specification = createSpecification(criteria);
        return indiceOriginalRepository.count(specification);
    }

    /**
     * Function to convert {@link IndiceOriginalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndiceOriginal> createSpecification(IndiceOriginalCriteria criteria) {
        Specification<IndiceOriginal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IndiceOriginal_.id));
            }
            if (criteria.getImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg(), IndiceOriginal_.img));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), IndiceOriginal_.titulo));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), IndiceOriginal_.url));
            }
            if (criteria.getAutor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutor(), IndiceOriginal_.autor));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), IndiceOriginal_.fecha));
            }
            if (criteria.getCiudad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCiudad(), IndiceOriginal_.ciudad));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), IndiceOriginal_.estado));
            }
            if (criteria.getPais() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPais(), IndiceOriginal_.pais));
            }
            if (criteria.getComentarios() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComentarios(), IndiceOriginal_.comentarios));
            }
            if (criteria.getVistas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVistas(), IndiceOriginal_.vistas));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), IndiceOriginal_.rating));
            }
        }
        return specification;
    }
}
