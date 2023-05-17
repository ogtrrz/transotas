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
import wf.transotas.repository.IndiceOriginalRepository;
import wf.transotas.service.IndiceOriginalQueryService;
import wf.transotas.service.IndiceOriginalService;
import wf.transotas.service.criteria.IndiceOriginalCriteria;
import wf.transotas.service.dto.IndiceOriginalDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.IndiceOriginal}.
 */
@RestController
@RequestMapping("/api")
public class IndiceOriginalResource {

    private final Logger log = LoggerFactory.getLogger(IndiceOriginalResource.class);

    private static final String ENTITY_NAME = "indiceOriginal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndiceOriginalService indiceOriginalService;

    private final IndiceOriginalRepository indiceOriginalRepository;

    private final IndiceOriginalQueryService indiceOriginalQueryService;

    public IndiceOriginalResource(
        IndiceOriginalService indiceOriginalService,
        IndiceOriginalRepository indiceOriginalRepository,
        IndiceOriginalQueryService indiceOriginalQueryService
    ) {
        this.indiceOriginalService = indiceOriginalService;
        this.indiceOriginalRepository = indiceOriginalRepository;
        this.indiceOriginalQueryService = indiceOriginalQueryService;
    }

    /**
     * {@code POST  /indice-originals} : Create a new indiceOriginal.
     *
     * @param indiceOriginalDTO the indiceOriginalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indiceOriginalDTO, or with status {@code 400 (Bad Request)} if the indiceOriginal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/indice-originals")
    public ResponseEntity<IndiceOriginalDTO> createIndiceOriginal(@RequestBody IndiceOriginalDTO indiceOriginalDTO)
        throws URISyntaxException {
        log.debug("REST request to save IndiceOriginal : {}", indiceOriginalDTO);
        if (indiceOriginalDTO.getId() != null) {
            throw new BadRequestAlertException("A new indiceOriginal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndiceOriginalDTO result = indiceOriginalService.save(indiceOriginalDTO);
        return ResponseEntity
            .created(new URI("/api/indice-originals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /indice-originals/:id} : Updates an existing indiceOriginal.
     *
     * @param id the id of the indiceOriginalDTO to save.
     * @param indiceOriginalDTO the indiceOriginalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indiceOriginalDTO,
     * or with status {@code 400 (Bad Request)} if the indiceOriginalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indiceOriginalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/indice-originals/{id}")
    public ResponseEntity<IndiceOriginalDTO> updateIndiceOriginal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndiceOriginalDTO indiceOriginalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IndiceOriginal : {}, {}", id, indiceOriginalDTO);
        if (indiceOriginalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indiceOriginalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indiceOriginalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IndiceOriginalDTO result = indiceOriginalService.update(indiceOriginalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indiceOriginalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /indice-originals/:id} : Partial updates given fields of an existing indiceOriginal, field will ignore if it is null
     *
     * @param id the id of the indiceOriginalDTO to save.
     * @param indiceOriginalDTO the indiceOriginalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indiceOriginalDTO,
     * or with status {@code 400 (Bad Request)} if the indiceOriginalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the indiceOriginalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the indiceOriginalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/indice-originals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndiceOriginalDTO> partialUpdateIndiceOriginal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndiceOriginalDTO indiceOriginalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IndiceOriginal partially : {}, {}", id, indiceOriginalDTO);
        if (indiceOriginalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indiceOriginalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indiceOriginalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndiceOriginalDTO> result = indiceOriginalService.partialUpdate(indiceOriginalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indiceOriginalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /indice-originals} : get all the indiceOriginals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indiceOriginals in body.
     */
    @GetMapping("/indice-originals")
    public ResponseEntity<List<IndiceOriginalDTO>> getAllIndiceOriginals(
        IndiceOriginalCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get IndiceOriginals by criteria: {}", criteria);
        Page<IndiceOriginalDTO> page = indiceOriginalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indice-originals/count} : count all the indiceOriginals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/indice-originals/count")
    public ResponseEntity<Long> countIndiceOriginals(IndiceOriginalCriteria criteria) {
        log.debug("REST request to count IndiceOriginals by criteria: {}", criteria);
        return ResponseEntity.ok().body(indiceOriginalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /indice-originals/:id} : get the "id" indiceOriginal.
     *
     * @param id the id of the indiceOriginalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indiceOriginalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/indice-originals/{id}")
    public ResponseEntity<IndiceOriginalDTO> getIndiceOriginal(@PathVariable Long id) {
        log.debug("REST request to get IndiceOriginal : {}", id);
        Optional<IndiceOriginalDTO> indiceOriginalDTO = indiceOriginalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indiceOriginalDTO);
    }

    /**
     * {@code DELETE  /indice-originals/:id} : delete the "id" indiceOriginal.
     *
     * @param id the id of the indiceOriginalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/indice-originals/{id}")
    public ResponseEntity<Void> deleteIndiceOriginal(@PathVariable Long id) {
        log.debug("REST request to delete IndiceOriginal : {}", id);
        indiceOriginalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/indice-originals?query=:query} : search for the indiceOriginal corresponding
     * to the query.
     *
     * @param query the query of the indiceOriginal search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/indice-originals")
    public ResponseEntity<List<IndiceOriginalDTO>> searchIndiceOriginals(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of IndiceOriginals for query {}", query);
        Page<IndiceOriginalDTO> page = indiceOriginalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
