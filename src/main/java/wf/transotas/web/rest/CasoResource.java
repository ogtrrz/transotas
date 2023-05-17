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
import wf.transotas.repository.CasoRepository;
import wf.transotas.service.CasoQueryService;
import wf.transotas.service.CasoService;
import wf.transotas.service.criteria.CasoCriteria;
import wf.transotas.service.dto.CasoDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Caso}.
 */
@RestController
@RequestMapping("/api")
public class CasoResource {

    private final Logger log = LoggerFactory.getLogger(CasoResource.class);

    private static final String ENTITY_NAME = "caso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasoService casoService;

    private final CasoRepository casoRepository;

    private final CasoQueryService casoQueryService;

    public CasoResource(CasoService casoService, CasoRepository casoRepository, CasoQueryService casoQueryService) {
        this.casoService = casoService;
        this.casoRepository = casoRepository;
        this.casoQueryService = casoQueryService;
    }

    /**
     * {@code POST  /casos} : Create a new caso.
     *
     * @param casoDTO the casoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casoDTO, or with status {@code 400 (Bad Request)} if the caso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/casos")
    public ResponseEntity<CasoDTO> createCaso(@RequestBody CasoDTO casoDTO) throws URISyntaxException {
        log.debug("REST request to save Caso : {}", casoDTO);
        if (casoDTO.getId() != null) {
            throw new BadRequestAlertException("A new caso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasoDTO result = casoService.save(casoDTO);
        return ResponseEntity
            .created(new URI("/api/casos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /casos/:id} : Updates an existing caso.
     *
     * @param id the id of the casoDTO to save.
     * @param casoDTO the casoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casoDTO,
     * or with status {@code 400 (Bad Request)} if the casoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/casos/{id}")
    public ResponseEntity<CasoDTO> updateCaso(@PathVariable(value = "id", required = false) final Long id, @RequestBody CasoDTO casoDTO)
        throws URISyntaxException {
        log.debug("REST request to update Caso : {}, {}", id, casoDTO);
        if (casoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasoDTO result = casoService.update(casoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /casos/:id} : Partial updates given fields of an existing caso, field will ignore if it is null
     *
     * @param id the id of the casoDTO to save.
     * @param casoDTO the casoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casoDTO,
     * or with status {@code 400 (Bad Request)} if the casoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the casoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the casoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/casos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CasoDTO> partialUpdateCaso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CasoDTO casoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caso partially : {}, {}", id, casoDTO);
        if (casoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasoDTO> result = casoService.partialUpdate(casoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /casos} : get all the casos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casos in body.
     */
    @GetMapping("/casos")
    public ResponseEntity<List<CasoDTO>> getAllCasos(
        CasoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Casos by criteria: {}", criteria);
        Page<CasoDTO> page = casoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /casos/count} : count all the casos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/casos/count")
    public ResponseEntity<Long> countCasos(CasoCriteria criteria) {
        log.debug("REST request to count Casos by criteria: {}", criteria);
        return ResponseEntity.ok().body(casoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /casos/:id} : get the "id" caso.
     *
     * @param id the id of the casoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/casos/{id}")
    public ResponseEntity<CasoDTO> getCaso(@PathVariable Long id) {
        log.debug("REST request to get Caso : {}", id);
        Optional<CasoDTO> casoDTO = casoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casoDTO);
    }

    /**
     * {@code DELETE  /casos/:id} : delete the "id" caso.
     *
     * @param id the id of the casoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/casos/{id}")
    public ResponseEntity<Void> deleteCaso(@PathVariable Long id) {
        log.debug("REST request to delete Caso : {}", id);
        casoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/casos?query=:query} : search for the caso corresponding
     * to the query.
     *
     * @param query the query of the caso search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/casos")
    public ResponseEntity<List<CasoDTO>> searchCasos(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Casos for query {}", query);
        Page<CasoDTO> page = casoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
