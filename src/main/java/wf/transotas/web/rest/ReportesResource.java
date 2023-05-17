package wf.transotas.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import wf.transotas.repository.ReportesRepository;
import wf.transotas.service.ReportesQueryService;
import wf.transotas.service.ReportesService;
import wf.transotas.service.criteria.ReportesCriteria;
import wf.transotas.service.dto.ReportesDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Reportes}.
 */
@RestController
@RequestMapping("/api")
public class ReportesResource {

    private final Logger log = LoggerFactory.getLogger(ReportesResource.class);

    private static final String ENTITY_NAME = "reportes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportesService reportesService;

    private final ReportesRepository reportesRepository;

    private final ReportesQueryService reportesQueryService;

    public ReportesResource(
        ReportesService reportesService,
        ReportesRepository reportesRepository,
        ReportesQueryService reportesQueryService
    ) {
        this.reportesService = reportesService;
        this.reportesRepository = reportesRepository;
        this.reportesQueryService = reportesQueryService;
    }

    /**
     * {@code POST  /reportes} : Create a new reportes.
     *
     * @param reportesDTO the reportesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportesDTO, or with status {@code 400 (Bad Request)} if the reportes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reportes")
    public ResponseEntity<ReportesDTO> createReportes(@RequestBody ReportesDTO reportesDTO) throws URISyntaxException {
        log.debug("REST request to save Reportes : {}", reportesDTO);
        if (reportesDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportesDTO result = reportesService.save(reportesDTO);
        return ResponseEntity
            .created(new URI("/api/reportes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reportes/:id} : Updates an existing reportes.
     *
     * @param id the id of the reportesDTO to save.
     * @param reportesDTO the reportesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportesDTO,
     * or with status {@code 400 (Bad Request)} if the reportesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reportes/{id}")
    public ResponseEntity<ReportesDTO> updateReportes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportesDTO reportesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reportes : {}, {}", id, reportesDTO);
        if (reportesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportesDTO result = reportesService.update(reportesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reportes/:id} : Partial updates given fields of an existing reportes, field will ignore if it is null
     *
     * @param id the id of the reportesDTO to save.
     * @param reportesDTO the reportesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportesDTO,
     * or with status {@code 400 (Bad Request)} if the reportesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reportes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportesDTO> partialUpdateReportes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportesDTO reportesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reportes partially : {}, {}", id, reportesDTO);
        if (reportesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportesDTO> result = reportesService.partialUpdate(reportesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reportes} : get all the reportes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportes in body.
     */
    @GetMapping("/reportes")
    public ResponseEntity<List<ReportesDTO>> getAllReportes(
        ReportesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Reportes by criteria: {}", criteria);
        Page<ReportesDTO> page = reportesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reportes/count} : count all the reportes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reportes/count")
    public ResponseEntity<Long> countReportes(ReportesCriteria criteria) {
        log.debug("REST request to count Reportes by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reportes/:id} : get the "id" reportes.
     *
     * @param id the id of the reportesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReportesDTO> getReportes(@PathVariable Long id) {
        log.debug("REST request to get Reportes : {}", id);
        Optional<ReportesDTO> reportesDTO = reportesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportesDTO);
    }

    /**
     * {@code DELETE  /reportes/:id} : delete the "id" reportes.
     *
     * @param id the id of the reportesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> deleteReportes(@PathVariable Long id) {
        log.debug("REST request to delete Reportes : {}", id);
        reportesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/reportes?query=:query} : search for the reportes corresponding
     * to the query.
     *
     * @param query the query of the reportes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/reportes")
    public ResponseEntity<List<ReportesDTO>> searchReportes(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Reportes for query {}", query);
        Page<ReportesDTO> page = reportesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
