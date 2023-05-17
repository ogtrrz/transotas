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
import wf.transotas.domain.Indice;
import wf.transotas.repository.IndiceRepository;
import wf.transotas.repository.search.IndiceSearchRepository;
import wf.transotas.service.criteria.IndiceCriteria;
import wf.transotas.service.dto.IndiceDTO;
import wf.transotas.service.mapper.IndiceMapper;

/**
 * Service for executing complex queries for {@link Indice} entities in the database.
 * The main input is a {@link IndiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IndiceDTO} or a {@link Page} of {@link IndiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndiceQueryService extends QueryService<Indice> {

    private final Logger log = LoggerFactory.getLogger(IndiceQueryService.class);

    private final IndiceRepository indiceRepository;

    private final IndiceMapper indiceMapper;

    private final IndiceSearchRepository indiceSearchRepository;

    public IndiceQueryService(IndiceRepository indiceRepository, IndiceMapper indiceMapper, IndiceSearchRepository indiceSearchRepository) {
        this.indiceRepository = indiceRepository;
        this.indiceMapper = indiceMapper;
        this.indiceSearchRepository = indiceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IndiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IndiceDTO> findByCriteria(IndiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Indice> specification = createSpecification(criteria);
        return indiceMapper.toDto(indiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IndiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceDTO> findByCriteria(IndiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Indice> specification = createSpecification(criteria);
        return indiceRepository.findAll(specification, page).map(indiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Indice> specification = createSpecification(criteria);
        return indiceRepository.count(specification);
    }

    /**
     * Function to convert {@link IndiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Indice> createSpecification(IndiceCriteria criteria) {
        Specification<Indice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Indice_.id));
            }
            if (criteria.getImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg(), Indice_.img));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Indice_.titulo));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Indice_.url));
            }
            if (criteria.getAutor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutor(), Indice_.autor));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Indice_.fecha));
            }
            if (criteria.getCiudad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCiudad(), Indice_.ciudad));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Indice_.estado));
            }
            if (criteria.getPais() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPais(), Indice_.pais));
            }
            if (criteria.getComentarios() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComentarios(), Indice_.comentarios));
            }
            if (criteria.getVistas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVistas(), Indice_.vistas));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Indice_.rating));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Indice_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Indice_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Indice_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Indice_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Indice_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Indice_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Indice_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Indice_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Indice_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Indice_.extra10));
            }
        }
        return specification;
    }
}
