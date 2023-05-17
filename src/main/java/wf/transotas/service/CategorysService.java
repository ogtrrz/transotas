package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Categorys;
import wf.transotas.repository.CategorysRepository;
import wf.transotas.repository.search.CategorysSearchRepository;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.service.mapper.CategorysMapper;

/**
 * Service Implementation for managing {@link Categorys}.
 */
@Service
@Transactional
public class CategorysService {

    private final Logger log = LoggerFactory.getLogger(CategorysService.class);

    private final CategorysRepository categorysRepository;

    private final CategorysMapper categorysMapper;

    private final CategorysSearchRepository categorysSearchRepository;

    public CategorysService(
        CategorysRepository categorysRepository,
        CategorysMapper categorysMapper,
        CategorysSearchRepository categorysSearchRepository
    ) {
        this.categorysRepository = categorysRepository;
        this.categorysMapper = categorysMapper;
        this.categorysSearchRepository = categorysSearchRepository;
    }

    /**
     * Save a categorys.
     *
     * @param categorysDTO the entity to save.
     * @return the persisted entity.
     */
    public CategorysDTO save(CategorysDTO categorysDTO) {
        log.debug("Request to save Categorys : {}", categorysDTO);
        Categorys categorys = categorysMapper.toEntity(categorysDTO);
        categorys = categorysRepository.save(categorys);
        CategorysDTO result = categorysMapper.toDto(categorys);
        categorysSearchRepository.index(categorys);
        return result;
    }

    /**
     * Update a categorys.
     *
     * @param categorysDTO the entity to save.
     * @return the persisted entity.
     */
    public CategorysDTO update(CategorysDTO categorysDTO) {
        log.debug("Request to update Categorys : {}", categorysDTO);
        Categorys categorys = categorysMapper.toEntity(categorysDTO);
        categorys = categorysRepository.save(categorys);
        CategorysDTO result = categorysMapper.toDto(categorys);
        categorysSearchRepository.index(categorys);
        return result;
    }

    /**
     * Partially update a categorys.
     *
     * @param categorysDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategorysDTO> partialUpdate(CategorysDTO categorysDTO) {
        log.debug("Request to partially update Categorys : {}", categorysDTO);

        return categorysRepository
            .findById(categorysDTO.getId())
            .map(existingCategorys -> {
                categorysMapper.partialUpdate(existingCategorys, categorysDTO);

                return existingCategorys;
            })
            .map(categorysRepository::save)
            .map(savedCategorys -> {
                categorysSearchRepository.save(savedCategorys);

                return savedCategorys;
            })
            .map(categorysMapper::toDto);
    }

    /**
     * Get all the categorys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorysDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categorys");
        return categorysRepository.findAll(pageable).map(categorysMapper::toDto);
    }

    /**
     * Get one categorys by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategorysDTO> findOne(Long id) {
        log.debug("Request to get Categorys : {}", id);
        return categorysRepository.findById(id).map(categorysMapper::toDto);
    }

    /**
     * Delete the categorys by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Categorys : {}", id);
        categorysRepository.deleteById(id);
        categorysSearchRepository.deleteById(id);
    }

    /**
     * Search for the categorys corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorysDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categorys for query {}", query);
        return categorysSearchRepository.search(query, pageable).map(categorysMapper::toDto);
    }
}
