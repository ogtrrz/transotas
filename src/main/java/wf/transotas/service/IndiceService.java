package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Indice;
import wf.transotas.repository.IndiceRepository;
import wf.transotas.repository.search.IndiceSearchRepository;
import wf.transotas.service.dto.IndiceDTO;
import wf.transotas.service.mapper.IndiceMapper;

/**
 * Service Implementation for managing {@link Indice}.
 */
@Service
@Transactional
public class IndiceService {

    private final Logger log = LoggerFactory.getLogger(IndiceService.class);

    private final IndiceRepository indiceRepository;

    private final IndiceMapper indiceMapper;

    private final IndiceSearchRepository indiceSearchRepository;

    public IndiceService(IndiceRepository indiceRepository, IndiceMapper indiceMapper, IndiceSearchRepository indiceSearchRepository) {
        this.indiceRepository = indiceRepository;
        this.indiceMapper = indiceMapper;
        this.indiceSearchRepository = indiceSearchRepository;
    }

    /**
     * Save a indice.
     *
     * @param indiceDTO the entity to save.
     * @return the persisted entity.
     */
    public IndiceDTO save(IndiceDTO indiceDTO) {
        log.debug("Request to save Indice : {}", indiceDTO);
        Indice indice = indiceMapper.toEntity(indiceDTO);
        indice = indiceRepository.save(indice);
        IndiceDTO result = indiceMapper.toDto(indice);
        indiceSearchRepository.index(indice);
        return result;
    }

    /**
     * Update a indice.
     *
     * @param indiceDTO the entity to save.
     * @return the persisted entity.
     */
    public IndiceDTO update(IndiceDTO indiceDTO) {
        log.debug("Request to update Indice : {}", indiceDTO);
        Indice indice = indiceMapper.toEntity(indiceDTO);
        indice = indiceRepository.save(indice);
        IndiceDTO result = indiceMapper.toDto(indice);
        indiceSearchRepository.index(indice);
        return result;
    }

    /**
     * Partially update a indice.
     *
     * @param indiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndiceDTO> partialUpdate(IndiceDTO indiceDTO) {
        log.debug("Request to partially update Indice : {}", indiceDTO);

        return indiceRepository
            .findById(indiceDTO.getId())
            .map(existingIndice -> {
                indiceMapper.partialUpdate(existingIndice, indiceDTO);

                return existingIndice;
            })
            .map(indiceRepository::save)
            .map(savedIndice -> {
                indiceSearchRepository.save(savedIndice);

                return savedIndice;
            })
            .map(indiceMapper::toDto);
    }

    /**
     * Get all the indices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Indices");
        return indiceRepository.findAll(pageable).map(indiceMapper::toDto);
    }

    /**
     * Get one indice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndiceDTO> findOne(Long id) {
        log.debug("Request to get Indice : {}", id);
        return indiceRepository.findById(id).map(indiceMapper::toDto);
    }

    /**
     * Delete the indice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Indice : {}", id);
        indiceRepository.deleteById(id);
        indiceSearchRepository.deleteById(id);
    }

    /**
     * Search for the indice corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndiceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Indices for query {}", query);
        return indiceSearchRepository.search(query, pageable).map(indiceMapper::toDto);
    }
}
