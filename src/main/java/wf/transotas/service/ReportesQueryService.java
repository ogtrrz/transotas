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
import wf.transotas.domain.Reportes;
import wf.transotas.repository.ReportesRepository;
import wf.transotas.repository.search.ReportesSearchRepository;
import wf.transotas.service.criteria.ReportesCriteria;
import wf.transotas.service.dto.ReportesDTO;
import wf.transotas.service.mapper.ReportesMapper;

/**
 * Service for executing complex queries for {@link Reportes} entities in the database.
 * The main input is a {@link ReportesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportesDTO} or a {@link Page} of {@link ReportesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportesQueryService extends QueryService<Reportes> {

    private final Logger log = LoggerFactory.getLogger(ReportesQueryService.class);

    private final ReportesRepository reportesRepository;

    private final ReportesMapper reportesMapper;

    private final ReportesSearchRepository reportesSearchRepository;

    public ReportesQueryService(
        ReportesRepository reportesRepository,
        ReportesMapper reportesMapper,
        ReportesSearchRepository reportesSearchRepository
    ) {
        this.reportesRepository = reportesRepository;
        this.reportesMapper = reportesMapper;
        this.reportesSearchRepository = reportesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportesDTO> findByCriteria(ReportesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reportes> specification = createSpecification(criteria);
        return reportesMapper.toDto(reportesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportesDTO> findByCriteria(ReportesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reportes> specification = createSpecification(criteria);
        return reportesRepository.findAll(specification, page).map(reportesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reportes> specification = createSpecification(criteria);
        return reportesRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reportes> createSpecification(ReportesCriteria criteria) {
        Specification<Reportes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reportes_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Reportes_.titulo));
            }
            if (criteria.getCaso() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaso(), Reportes_.caso));
            }
            if (criteria.getImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg(), Reportes_.img));
            }
            if (criteria.getAutor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutor(), Reportes_.autor));
            }
            if (criteria.getTituloix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTituloix(), Reportes_.tituloix));
            }
            if (criteria.getAutorix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAutorix(), Reportes_.autorix));
            }
            if (criteria.getFechaix() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaix(), Reportes_.fechaix));
            }
            if (criteria.getImgix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgix(), Reportes_.imgix));
            }
            if (criteria.getCiudad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCiudad(), Reportes_.ciudad));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Reportes_.estado));
            }
            if (criteria.getPais() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPais(), Reportes_.pais));
            }
            if (criteria.getExtra1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra1(), Reportes_.extra1));
            }
            if (criteria.getExtra2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra2(), Reportes_.extra2));
            }
            if (criteria.getExtra3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra3(), Reportes_.extra3));
            }
            if (criteria.getExtra4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra4(), Reportes_.extra4));
            }
            if (criteria.getExtra5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra5(), Reportes_.extra5));
            }
            if (criteria.getExtra6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra6(), Reportes_.extra6));
            }
            if (criteria.getExtra7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra7(), Reportes_.extra7));
            }
            if (criteria.getExtra8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra8(), Reportes_.extra8));
            }
            if (criteria.getExtra9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra9(), Reportes_.extra9));
            }
            if (criteria.getExtra10() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtra10(), Reportes_.extra10));
            }
            if (criteria.getInformacionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInformacionId(),
                            root -> root.join(Reportes_.informacion, JoinType.LEFT).get(Informacion_.id)
                        )
                    );
            }
            if (criteria.getCategorysId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategorysId(),
                            root -> root.join(Reportes_.categorys, JoinType.LEFT).get(Categorys_.id)
                        )
                    );
            }
            if (criteria.getComentariosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComentariosId(),
                            root -> root.join(Reportes_.comentarios, JoinType.LEFT).get(Comentarios_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
