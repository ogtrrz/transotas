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
import wf.transotas.repository.InformacionRepository;
import wf.transotas.service.InformacionQueryService;
import wf.transotas.service.InformacionService;
import wf.transotas.service.criteria.InformacionCriteria;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Informacion}.
 */
@RestController
@RequestMapping("/api")
public class InformacionResource {

    private final Logger log = LoggerFactory.getLogger(InformacionResource.class);

    private static final String ENTITY_NAME = "informacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformacionService informacionService;

    private final InformacionRepository informacionRepository;

    private final InformacionQueryService informacionQueryService;

    public InformacionResource(
        InformacionService informacionService,
        InformacionRepository informacionRepository,
        InformacionQueryService informacionQueryService
    ) {
        this.informacionService = informacionService;
        this.informacionRepository = informacionRepository;
        this.informacionQueryService = informacionQueryService;
    }

    /**
     * {@code POST  /informacions} : Create a new informacion.
     *
     * @param informacionDTO the informacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informacionDTO, or with status {@code 400 (Bad Request)} if the informacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/informacions")
    public ResponseEntity<InformacionDTO> createInformacion(@RequestBody InformacionDTO informacionDTO) throws URISyntaxException {
        log.debug("REST request to save Informacion : {}", informacionDTO);
        if (informacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new informacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InformacionDTO result = informacionService.save(informacionDTO);
        return ResponseEntity
            .created(new URI("/api/informacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /informacions/:id} : Updates an existing informacion.
     *
     * @param id the id of the informacionDTO to save.
     * @param informacionDTO the informacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informacionDTO,
     * or with status {@code 400 (Bad Request)} if the informacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/informacions/{id}")
    public ResponseEntity<InformacionDTO> updateInformacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InformacionDTO informacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Informacion : {}, {}", id, informacionDTO);
        if (informacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InformacionDTO result = informacionService.update(informacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /informacions/:id} : Partial updates given fields of an existing informacion, field will ignore if it is null
     *
     * @param id the id of the informacionDTO to save.
     * @param informacionDTO the informacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informacionDTO,
     * or with status {@code 400 (Bad Request)} if the informacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the informacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the informacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/informacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InformacionDTO> partialUpdateInformacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InformacionDTO informacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Informacion partially : {}, {}", id, informacionDTO);
        if (informacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InformacionDTO> result = informacionService.partialUpdate(informacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /informacions} : get all the informacions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informacions in body.
     */
    @GetMapping("/informacions")
    public ResponseEntity<List<InformacionDTO>> getAllInformacions(
        InformacionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Informacions by criteria: {}", criteria);
        Page<InformacionDTO> page = informacionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /informacions/count} : count all the informacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/informacions/count")
    public ResponseEntity<Long> countInformacions(InformacionCriteria criteria) {
        log.debug("REST request to count Informacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(informacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /informacions/:id} : get the "id" informacion.
     *
     * @param id the id of the informacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/informacions/{id}")
    public ResponseEntity<InformacionDTO> getInformacion(@PathVariable Long id) {
        log.debug("REST request to get Informacion : {}", id);
        Optional<InformacionDTO> informacionDTO = informacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informacionDTO);
    }

    /**
     * {@code DELETE  /informacions/:id} : delete the "id" informacion.
     *
     * @param id the id of the informacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/informacions/{id}")
    public ResponseEntity<Void> deleteInformacion(@PathVariable Long id) {
        log.debug("REST request to delete Informacion : {}", id);
        informacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/informacions?query=:query} : search for the informacion corresponding
     * to the query.
     *
     * @param query the query of the informacion search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/informacions")
    public ResponseEntity<List<InformacionDTO>> searchInformacions(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Informacions for query {}", query);
        Page<InformacionDTO> page = informacionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
