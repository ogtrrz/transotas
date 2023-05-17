package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.IndiceOriginal;
import wf.transotas.repository.IndiceOriginalRepository;
import wf.transotas.repository.search.IndiceOriginalSearchRepository;
import wf.transotas.service.dto.IndiceOriginalDTO;
import wf.transotas.service.mapper.IndiceOriginalMapper;

/**
 * Service Implementation for managing {@link IndiceOriginal}.
 */
@Service
@Transactional
public class IndiceOriginalService {

    private final Logger log = LoggerFactory.getLogger(IndiceOriginalService.class);

    private final IndiceOriginalRepository indiceOriginalRepository;

    private final IndiceOriginalMapper indiceOriginalMapper;

    private final IndiceOriginalSearchRepository indiceOriginalSearchRepository;

    public IndiceOriginalService(
        IndiceOriginalRepository indiceOriginalRepository,
        IndiceOriginalMapper indiceOriginalMapper,
        IndiceOriginalSearchRepository indiceOriginalSearchRepository
    ) {
        this.indiceOriginalRepository = indiceOriginalRepository;
        this.indiceOriginalMapper = indiceOriginalMapper;
        this.indiceOriginalSearchRepository = indiceOriginalSearchRepository;
    }

    /**
     * Save a indiceOriginal.
     *
     * @param indiceOriginalDTO the entity to save.
     * @return the persisted entity.
     */
    public IndiceOriginalDTO save(IndiceOriginalDTO indiceOriginalDTO) {
        log.debug("Request to save IndiceOriginal : {}", indiceOriginalDTO);
        IndiceOriginal indiceOriginal = indiceOriginalMapper.toEntity(indiceOriginalDTO);
        indiceOriginal = indiceOriginalRepository.save(indiceOriginal);
        IndiceOriginalDTO result = indiceOriginalMapper.toDto(indiceOriginal);
        indiceOriginalSearchRepository.index(indiceOriginal);
        return result;
    }

    /**
     * Update a indiceOriginal.
     *
     * @param indiceOriginalDTO the entity to save.
     * @return the persisted entity.
     */
    public IndiceOriginalDTO update(IndiceOriginalDTO indiceOriginalDTO) {
        log.debug("Request to update IndiceOriginal : {}", indiceOriginalDTO);
        IndiceOriginal indiceOriginal = indiceOriginalMapper.toEntity(indiceOriginalDTO);
        indiceOriginal = indiceOriginalRepository.save(indiceOriginal);
        IndiceOriginalDTO result = indiceOriginalMapper.toDto(indiceOriginal);
        indiceOriginalSearchRepository.index(indiceOriginal);
        return result;
    }

    /**
     * Partially update a indiceOriginal.
     *
     * @param indiceOriginalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndiceOriginalDTO> partialUpdate(IndiceOriginalDTO indiceOriginalDTO) {
        log.debug("Request to partially update IndiceOriginal : {}", indiceOriginalDTO);

        return indiceOriginalRepository
            .findById(indiceOriginalDTO.getId())
            .map(existingIndiceOriginal -> {
                indiceOriginalMapper.partialUpdate(existingIndiceOriginal, indiceOriginalDTO);

                return existingIndiceOriginal;
            })
            .map(indiceOriginalRepository::save)
            .map(savedIndiceOriginal -> {
                indiceOriginalSearchRepository.save(savedIndiceOriginal);

                return savedIndiceOriginal;
            })
            .map(indiceOriginalMapper::toDto);
    }

    /**
     * Get all the indiceOriginals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceOriginalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IndiceOriginals");
        return indiceOriginalRepository.findAll(pageable).map(indiceOriginalMapper::toDto);
    }

    /**
     * Get one indiceOriginal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndiceOriginalDTO> findOne(Long id) {
        log.debug("Request to get IndiceOriginal : {}", id);
        return indiceOriginalRepository.findById(id).map(indiceOriginalMapper::toDto);
    }

    /**
     * Delete the indiceOriginal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IndiceOriginal : {}", id);
        indiceOriginalRepository.deleteById(id);
        indiceOriginalSearchRepository.deleteById(id);
    }

    /**
     * Search for the indiceOriginal corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceOriginalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IndiceOriginals for query {}", query);
        return indiceOriginalSearchRepository.search(query, pageable).map(indiceOriginalMapper::toDto);
    }
}
