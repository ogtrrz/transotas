package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.CasoText;
import wf.transotas.repository.CasoTextRepository;
import wf.transotas.repository.search.CasoTextSearchRepository;
import wf.transotas.service.dto.CasoTextDTO;
import wf.transotas.service.mapper.CasoTextMapper;

/**
 * Service Implementation for managing {@link CasoText}.
 */
@Service
@Transactional
public class CasoTextService {

    private final Logger log = LoggerFactory.getLogger(CasoTextService.class);

    private final CasoTextRepository casoTextRepository;

    private final CasoTextMapper casoTextMapper;

    private final CasoTextSearchRepository casoTextSearchRepository;

    public CasoTextService(
        CasoTextRepository casoTextRepository,
        CasoTextMapper casoTextMapper,
        CasoTextSearchRepository casoTextSearchRepository
    ) {
        this.casoTextRepository = casoTextRepository;
        this.casoTextMapper = casoTextMapper;
        this.casoTextSearchRepository = casoTextSearchRepository;
    }

    /**
     * Save a casoText.
     *
     * @param casoTextDTO the entity to save.
     * @return the persisted entity.
     */
    public CasoTextDTO save(CasoTextDTO casoTextDTO) {
        log.debug("Request to save CasoText : {}", casoTextDTO);
        CasoText casoText = casoTextMapper.toEntity(casoTextDTO);
        casoText = casoTextRepository.save(casoText);
        CasoTextDTO result = casoTextMapper.toDto(casoText);
        casoTextSearchRepository.index(casoText);
        return result;
    }

    /**
     * Update a casoText.
     *
     * @param casoTextDTO the entity to save.
     * @return the persisted entity.
     */
    public CasoTextDTO update(CasoTextDTO casoTextDTO) {
        log.debug("Request to update CasoText : {}", casoTextDTO);
        CasoText casoText = casoTextMapper.toEntity(casoTextDTO);
        casoText = casoTextRepository.save(casoText);
        CasoTextDTO result = casoTextMapper.toDto(casoText);
        casoTextSearchRepository.index(casoText);
        return result;
    }

    /**
     * Partially update a casoText.
     *
     * @param casoTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CasoTextDTO> partialUpdate(CasoTextDTO casoTextDTO) {
        log.debug("Request to partially update CasoText : {}", casoTextDTO);

        return casoTextRepository
            .findById(casoTextDTO.getId())
            .map(existingCasoText -> {
                casoTextMapper.partialUpdate(existingCasoText, casoTextDTO);

                return existingCasoText;
            })
            .map(casoTextRepository::save)
            .map(savedCasoText -> {
                casoTextSearchRepository.save(savedCasoText);

                return savedCasoText;
            })
            .map(casoTextMapper::toDto);
    }

    /**
     * Get all the casoTexts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CasoTextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CasoTexts");
        return casoTextRepository.findAll(pageable).map(casoTextMapper::toDto);
    }

    /**
     * Get one casoText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CasoTextDTO> findOne(Long id) {
        log.debug("Request to get CasoText : {}", id);
        return casoTextRepository.findById(id).map(casoTextMapper::toDto);
    }

    /**
     * Delete the casoText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CasoText : {}", id);
        casoTextRepository.deleteById(id);
        casoTextSearchRepository.deleteById(id);
    }

    /**
     * Search for the casoText corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CasoTextDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CasoTexts for query {}", query);
        return casoTextSearchRepository.search(query, pageable).map(casoTextMapper::toDto);
    }
}
