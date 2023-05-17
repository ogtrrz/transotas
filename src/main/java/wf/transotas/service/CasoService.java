package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Caso;
import wf.transotas.repository.CasoRepository;
import wf.transotas.repository.search.CasoSearchRepository;
import wf.transotas.service.dto.CasoDTO;
import wf.transotas.service.mapper.CasoMapper;

/**
 * Service Implementation for managing {@link Caso}.
 */
@Service
@Transactional
public class CasoService {

    private final Logger log = LoggerFactory.getLogger(CasoService.class);

    private final CasoRepository casoRepository;

    private final CasoMapper casoMapper;

    private final CasoSearchRepository casoSearchRepository;

    public CasoService(CasoRepository casoRepository, CasoMapper casoMapper, CasoSearchRepository casoSearchRepository) {
        this.casoRepository = casoRepository;
        this.casoMapper = casoMapper;
        this.casoSearchRepository = casoSearchRepository;
    }

    /**
     * Save a caso.
     *
     * @param casoDTO the entity to save.
     * @return the persisted entity.
     */
    public CasoDTO save(CasoDTO casoDTO) {
        log.debug("Request to save Caso : {}", casoDTO);
        Caso caso = casoMapper.toEntity(casoDTO);
        caso = casoRepository.save(caso);
        CasoDTO result = casoMapper.toDto(caso);
        casoSearchRepository.index(caso);
        return result;
    }

    /**
     * Update a caso.
     *
     * @param casoDTO the entity to save.
     * @return the persisted entity.
     */
    public CasoDTO update(CasoDTO casoDTO) {
        log.debug("Request to update Caso : {}", casoDTO);
        Caso caso = casoMapper.toEntity(casoDTO);
        caso = casoRepository.save(caso);
        CasoDTO result = casoMapper.toDto(caso);
        casoSearchRepository.index(caso);
        return result;
    }

    /**
     * Partially update a caso.
     *
     * @param casoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CasoDTO> partialUpdate(CasoDTO casoDTO) {
        log.debug("Request to partially update Caso : {}", casoDTO);

        return casoRepository
            .findById(casoDTO.getId())
            .map(existingCaso -> {
                casoMapper.partialUpdate(existingCaso, casoDTO);

                return existingCaso;
            })
            .map(casoRepository::save)
            .map(savedCaso -> {
                casoSearchRepository.save(savedCaso);

                return savedCaso;
            })
            .map(casoMapper::toDto);
    }

    /**
     * Get all the casos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CasoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Casos");
        return casoRepository.findAll(pageable).map(casoMapper::toDto);
    }

    /**
     * Get one caso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CasoDTO> findOne(Long id) {
        log.debug("Request to get Caso : {}", id);
        return casoRepository.findById(id).map(casoMapper::toDto);
    }

    /**
     * Delete the caso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Caso : {}", id);
        casoRepository.deleteById(id);
        casoSearchRepository.deleteById(id);
    }

    /**
     * Search for the caso corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CasoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Casos for query {}", query);
        return casoSearchRepository.search(query, pageable).map(casoMapper::toDto);
    }
}
