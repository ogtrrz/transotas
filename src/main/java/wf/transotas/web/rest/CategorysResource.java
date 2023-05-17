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
import wf.transotas.repository.CategorysRepository;
import wf.transotas.service.CategorysQueryService;
import wf.transotas.service.CategorysService;
import wf.transotas.service.criteria.CategorysCriteria;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link wf.transotas.domain.Categorys}.
 */
@RestController
@RequestMapping("/api")
public class CategorysResource {

    private final Logger log = LoggerFactory.getLogger(CategorysResource.class);

    private static final String ENTITY_NAME = "categorys";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategorysService categorysService;

    private final CategorysRepository categorysRepository;

    private final CategorysQueryService categorysQueryService;

    public CategorysResource(
        CategorysService categorysService,
        CategorysRepository categorysRepository,
        CategorysQueryService categorysQueryService
    ) {
        this.categorysService = categorysService;
        this.categorysRepository = categorysRepository;
        this.categorysQueryService = categorysQueryService;
    }

    /**
     * {@code POST  /categorys} : Create a new categorys.
     *
     * @param categorysDTO the categorysDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorysDTO, or with status {@code 400 (Bad Request)} if the categorys has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorys")
    public ResponseEntity<CategorysDTO> createCategorys(@RequestBody CategorysDTO categorysDTO) throws URISyntaxException {
        log.debug("REST request to save Categorys : {}", categorysDTO);
        if (categorysDTO.getId() != null) {
            throw new BadRequestAlertException("A new categorys cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorysDTO result = categorysService.save(categorysDTO);
        return ResponseEntity
            .created(new URI("/api/categorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorys/:id} : Updates an existing categorys.
     *
     * @param id the id of the categorysDTO to save.
     * @param categorysDTO the categorysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorysDTO,
     * or with status {@code 400 (Bad Request)} if the categorysDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorys/{id}")
    public ResponseEntity<CategorysDTO> updateCategorys(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategorysDTO categorysDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Categorys : {}, {}", id, categorysDTO);
        if (categorysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategorysDTO result = categorysService.update(categorysDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorysDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorys/:id} : Partial updates given fields of an existing categorys, field will ignore if it is null
     *
     * @param id the id of the categorysDTO to save.
     * @param categorysDTO the categorysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorysDTO,
     * or with status {@code 400 (Bad Request)} if the categorysDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categorysDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorys/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategorysDTO> partialUpdateCategorys(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategorysDTO categorysDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categorys partially : {}, {}", id, categorysDTO);
        if (categorysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorysDTO> result = categorysService.partialUpdate(categorysDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorysDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categorys} : get all the categorys.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorys in body.
     */
    @GetMapping("/categorys")
    public ResponseEntity<List<CategorysDTO>> getAllCategorys(
        CategorysCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Categorys by criteria: {}", criteria);
        Page<CategorysDTO> page = categorysQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categorys/count} : count all the categorys.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categorys/count")
    public ResponseEntity<Long> countCategorys(CategorysCriteria criteria) {
        log.debug("REST request to count Categorys by criteria: {}", criteria);
        return ResponseEntity.ok().body(categorysQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categorys/:id} : get the "id" categorys.
     *
     * @param id the id of the categorysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorys/{id}")
    public ResponseEntity<CategorysDTO> getCategorys(@PathVariable Long id) {
        log.debug("REST request to get Categorys : {}", id);
        Optional<CategorysDTO> categorysDTO = categorysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorysDTO);
    }

    /**
     * {@code DELETE  /categorys/:id} : delete the "id" categorys.
     *
     * @param id the id of the categorysDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorys/{id}")
    public ResponseEntity<Void> deleteCategorys(@PathVariable Long id) {
        log.debug("REST request to delete Categorys : {}", id);
        categorysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/categorys?query=:query} : search for the categorys corresponding
     * to the query.
     *
     * @param query the query of the categorys search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/categorys")
    public ResponseEntity<List<CategorysDTO>> searchCategorys(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Categorys for query {}", query);
        Page<CategorysDTO> page = categorysService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
