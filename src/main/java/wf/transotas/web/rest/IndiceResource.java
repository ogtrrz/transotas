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
import wf.transotas.repository.IndiceRepository;
import wf.transotas.service.IndiceQueryService;
import wf.transotas.service.IndiceService;
import wf.transotas.service.criteria.IndiceCriteria;
import wf.transotas.service.dto.IndiceDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Indice}.
 */
@RestController
@RequestMapping("/api")
public class IndiceResource {

    private final Logger log = LoggerFactory.getLogger(IndiceResource.class);

    private static final String ENTITY_NAME = "indice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndiceService indiceService;

    private final IndiceRepository indiceRepository;

    private final IndiceQueryService indiceQueryService;

    public IndiceResource(IndiceService indiceService, IndiceRepository indiceRepository, IndiceQueryService indiceQueryService) {
        this.indiceService = indiceService;
        this.indiceRepository = indiceRepository;
        this.indiceQueryService = indiceQueryService;
    }

    /**
     * {@code POST  /indices} : Create a new indice.
     *
     * @param indiceDTO the indiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indiceDTO, or with status {@code 400 (Bad Request)} if the indice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/indices")
    public ResponseEntity<IndiceDTO> createIndice(@RequestBody IndiceDTO indiceDTO) throws URISyntaxException {
        log.debug("REST request to save Indice : {}", indiceDTO);
        if (indiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new indice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndiceDTO result = indiceService.save(indiceDTO);
        return ResponseEntity
            .created(new URI("/api/indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /indices/:id} : Updates an existing indice.
     *
     * @param id the id of the indiceDTO to save.
     * @param indiceDTO the indiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indiceDTO,
     * or with status {@code 400 (Bad Request)} if the indiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/indices/{id}")
    public ResponseEntity<IndiceDTO> updateIndice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndiceDTO indiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Indice : {}, {}", id, indiceDTO);
        if (indiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IndiceDTO result = indiceService.update(indiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /indices/:id} : Partial updates given fields of an existing indice, field will ignore if it is null
     *
     * @param id the id of the indiceDTO to save.
     * @param indiceDTO the indiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indiceDTO,
     * or with status {@code 400 (Bad Request)} if the indiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the indiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the indiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/indices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndiceDTO> partialUpdateIndice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndiceDTO indiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Indice partially : {}, {}", id, indiceDTO);
        if (indiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndiceDTO> result = indiceService.partialUpdate(indiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /indices} : get all the indices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indices in body.
     */
    @GetMapping("/indices")
    public ResponseEntity<List<IndiceDTO>> getAllIndices(
        IndiceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Indices by criteria: {}", criteria);
        Page<IndiceDTO> page = indiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indices/count} : count all the indices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/indices/count")
    public ResponseEntity<Long> countIndices(IndiceCriteria criteria) {
        log.debug("REST request to count Indices by criteria: {}", criteria);
        return ResponseEntity.ok().body(indiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /indices/:id} : get the "id" indice.
     *
     * @param id the id of the indiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/indices/{id}")
    public ResponseEntity<IndiceDTO> getIndice(@PathVariable Long id) {
        log.debug("REST request to get Indice : {}", id);
        Optional<IndiceDTO> indiceDTO = indiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indiceDTO);
    }

    /**
     * {@code DELETE  /indices/:id} : delete the "id" indice.
     *
     * @param id the id of the indiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/indices/{id}")
    public ResponseEntity<Void> deleteIndice(@PathVariable Long id) {
        log.debug("REST request to delete Indice : {}", id);
        indiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/indices?query=:query} : search for the indice corresponding
     * to the query.
     *
     * @param query the query of the indice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/indices")
    public ResponseEntity<List<IndiceDTO>> searchIndices(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Indices for query {}", query);
        Page<IndiceDTO> page = indiceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
