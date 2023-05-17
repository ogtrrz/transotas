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
import wf.transotas.repository.ComentariosRepository;
import wf.transotas.service.ComentariosQueryService;
import wf.transotas.service.ComentariosService;
import wf.transotas.service.criteria.ComentariosCriteria;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Comentarios}.
 */
@RestController
@RequestMapping("/api")
public class ComentariosResource {

    private final Logger log = LoggerFactory.getLogger(ComentariosResource.class);

    private static final String ENTITY_NAME = "comentarios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComentariosService comentariosService;

    private final ComentariosRepository comentariosRepository;

    private final ComentariosQueryService comentariosQueryService;

    public ComentariosResource(
        ComentariosService comentariosService,
        ComentariosRepository comentariosRepository,
        ComentariosQueryService comentariosQueryService
    ) {
        this.comentariosService = comentariosService;
        this.comentariosRepository = comentariosRepository;
        this.comentariosQueryService = comentariosQueryService;
    }

    /**
     * {@code POST  /comentarios} : Create a new comentarios.
     *
     * @param comentariosDTO the comentariosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comentariosDTO, or with status {@code 400 (Bad Request)} if the comentarios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<ComentariosDTO> createComentarios(@RequestBody ComentariosDTO comentariosDTO) throws URISyntaxException {
        log.debug("REST request to save Comentarios : {}", comentariosDTO);
        if (comentariosDTO.getId() != null) {
            throw new BadRequestAlertException("A new comentarios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentariosDTO result = comentariosService.save(comentariosDTO);
        return ResponseEntity
            .created(new URI("/api/comentarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comentarios/:id} : Updates an existing comentarios.
     *
     * @param id the id of the comentariosDTO to save.
     * @param comentariosDTO the comentariosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentariosDTO,
     * or with status {@code 400 (Bad Request)} if the comentariosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comentariosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comentarios/{id}")
    public ResponseEntity<ComentariosDTO> updateComentarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComentariosDTO comentariosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Comentarios : {}, {}", id, comentariosDTO);
        if (comentariosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentariosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComentariosDTO result = comentariosService.update(comentariosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comentariosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comentarios/:id} : Partial updates given fields of an existing comentarios, field will ignore if it is null
     *
     * @param id the id of the comentariosDTO to save.
     * @param comentariosDTO the comentariosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentariosDTO,
     * or with status {@code 400 (Bad Request)} if the comentariosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the comentariosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the comentariosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comentarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComentariosDTO> partialUpdateComentarios(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComentariosDTO comentariosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comentarios partially : {}, {}", id, comentariosDTO);
        if (comentariosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentariosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentariosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComentariosDTO> result = comentariosService.partialUpdate(comentariosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comentariosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comentarios} : get all the comentarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comentarios in body.
     */
    @GetMapping("/comentarios")
    public ResponseEntity<List<ComentariosDTO>> getAllComentarios(
        ComentariosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Comentarios by criteria: {}", criteria);
        Page<ComentariosDTO> page = comentariosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comentarios/count} : count all the comentarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comentarios/count")
    public ResponseEntity<Long> countComentarios(ComentariosCriteria criteria) {
        log.debug("REST request to count Comentarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(comentariosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comentarios/:id} : get the "id" comentarios.
     *
     * @param id the id of the comentariosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comentariosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comentarios/{id}")
    public ResponseEntity<ComentariosDTO> getComentarios(@PathVariable Long id) {
        log.debug("REST request to get Comentarios : {}", id);
        Optional<ComentariosDTO> comentariosDTO = comentariosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comentariosDTO);
    }

    /**
     * {@code DELETE  /comentarios/:id} : delete the "id" comentarios.
     *
     * @param id the id of the comentariosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> deleteComentarios(@PathVariable Long id) {
        log.debug("REST request to delete Comentarios : {}", id);
        comentariosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/comentarios?query=:query} : search for the comentarios corresponding
     * to the query.
     *
     * @param query the query of the comentarios search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/comentarios")
    public ResponseEntity<List<ComentariosDTO>> searchComentarios(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Comentarios for query {}", query);
        Page<ComentariosDTO> page = comentariosService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
