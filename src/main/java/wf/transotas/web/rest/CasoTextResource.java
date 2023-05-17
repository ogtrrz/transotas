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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import wf.transotas.repository.CasoTextRepository;
import wf.transotas.service.CasoTextService;
import wf.transotas.service.dto.CasoTextDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.CasoText}.
 */
@RestController
@RequestMapping("/api")
public class CasoTextResource {

    private final Logger log = LoggerFactory.getLogger(CasoTextResource.class);

    private static final String ENTITY_NAME = "casoText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasoTextService casoTextService;

    private final CasoTextRepository casoTextRepository;

    public CasoTextResource(CasoTextService casoTextService, CasoTextRepository casoTextRepository) {
        this.casoTextService = casoTextService;
        this.casoTextRepository = casoTextRepository;
    }

    /**
     * {@code POST  /caso-texts} : Create a new casoText.
     *
     * @param casoTextDTO the casoTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casoTextDTO, or with status {@code 400 (Bad Request)} if the casoText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caso-texts")
    public ResponseEntity<CasoTextDTO> createCasoText(@RequestBody CasoTextDTO casoTextDTO) throws URISyntaxException {
        log.debug("REST request to save CasoText : {}", casoTextDTO);
        if (casoTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new casoText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasoTextDTO result = casoTextService.save(casoTextDTO);
        return ResponseEntity
            .created(new URI("/api/caso-texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caso-texts/:id} : Updates an existing casoText.
     *
     * @param id the id of the casoTextDTO to save.
     * @param casoTextDTO the casoTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casoTextDTO,
     * or with status {@code 400 (Bad Request)} if the casoTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casoTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caso-texts/{id}")
    public ResponseEntity<CasoTextDTO> updateCasoText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CasoTextDTO casoTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CasoText : {}, {}", id, casoTextDTO);
        if (casoTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casoTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casoTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasoTextDTO result = casoTextService.update(casoTextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casoTextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caso-texts/:id} : Partial updates given fields of an existing casoText, field will ignore if it is null
     *
     * @param id the id of the casoTextDTO to save.
     * @param casoTextDTO the casoTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casoTextDTO,
     * or with status {@code 400 (Bad Request)} if the casoTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the casoTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the casoTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caso-texts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CasoTextDTO> partialUpdateCasoText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CasoTextDTO casoTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CasoText partially : {}, {}", id, casoTextDTO);
        if (casoTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casoTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casoTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasoTextDTO> result = casoTextService.partialUpdate(casoTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casoTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caso-texts} : get all the casoTexts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casoTexts in body.
     */
    @GetMapping("/caso-texts")
    public ResponseEntity<List<CasoTextDTO>> getAllCasoTexts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CasoTexts");
        Page<CasoTextDTO> page = casoTextService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caso-texts/:id} : get the "id" casoText.
     *
     * @param id the id of the casoTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casoTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caso-texts/{id}")
    public ResponseEntity<CasoTextDTO> getCasoText(@PathVariable Long id) {
        log.debug("REST request to get CasoText : {}", id);
        Optional<CasoTextDTO> casoTextDTO = casoTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casoTextDTO);
    }

    /**
     * {@code DELETE  /caso-texts/:id} : delete the "id" casoText.
     *
     * @param id the id of the casoTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caso-texts/{id}")
    public ResponseEntity<Void> deleteCasoText(@PathVariable Long id) {
        log.debug("REST request to delete CasoText : {}", id);
        casoTextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/caso-texts?query=:query} : search for the casoText corresponding
     * to the query.
     *
     * @param query the query of the casoText search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/caso-texts")
    public ResponseEntity<List<CasoTextDTO>> searchCasoTexts(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of CasoTexts for query {}", query);
        Page<CasoTextDTO> page = casoTextService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
