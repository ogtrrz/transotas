package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Informacion;
import wf.transotas.repository.InformacionRepository;
import wf.transotas.repository.search.InformacionSearchRepository;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.mapper.InformacionMapper;

/**
 * Service Implementation for managing {@link Informacion}.
 */
@Service
@Transactional
public class InformacionService {

    private final Logger log = LoggerFactory.getLogger(InformacionService.class);

    private final InformacionRepository informacionRepository;

    private final InformacionMapper informacionMapper;

    private final InformacionSearchRepository informacionSearchRepository;

    public InformacionService(
        InformacionRepository informacionRepository,
        InformacionMapper informacionMapper,
        InformacionSearchRepository informacionSearchRepository
    ) {
        this.informacionRepository = informacionRepository;
        this.informacionMapper = informacionMapper;
        this.informacionSearchRepository = informacionSearchRepository;
    }

    /**
     * Save a informacion.
     *
     * @param informacionDTO the entity to save.
     * @return the persisted entity.
     */
    public InformacionDTO save(InformacionDTO informacionDTO) {
        log.debug("Request to save Informacion : {}", informacionDTO);
        Informacion informacion = informacionMapper.toEntity(informacionDTO);
        informacion = informacionRepository.save(informacion);
        InformacionDTO result = informacionMapper.toDto(informacion);
        informacionSearchRepository.index(informacion);
        return result;
    }

    /**
     * Update a informacion.
     *
     * @param informacionDTO the entity to save.
     * @return the persisted entity.
     */
    public InformacionDTO update(InformacionDTO informacionDTO) {
        log.debug("Request to update Informacion : {}", informacionDTO);
        Informacion informacion = informacionMapper.toEntity(informacionDTO);
        informacion = informacionRepository.save(informacion);
        InformacionDTO result = informacionMapper.toDto(informacion);
        informacionSearchRepository.index(informacion);
        return result;
    }

    /**
     * Partially update a informacion.
     *
     * @param informacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InformacionDTO> partialUpdate(InformacionDTO informacionDTO) {
        log.debug("Request to partially update Informacion : {}", informacionDTO);

        return informacionRepository
            .findById(informacionDTO.getId())
            .map(existingInformacion -> {
                informacionMapper.partialUpdate(existingInformacion, informacionDTO);

                return existingInformacion;
            })
            .map(informacionRepository::save)
            .map(savedInformacion -> {
                informacionSearchRepository.save(savedInformacion);

                return savedInformacion;
            })
            .map(informacionMapper::toDto);
    }

    /**
     * Get all the informacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InformacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Informacions");
        return informacionRepository.findAll(pageable).map(informacionMapper::toDto);
    }

    /**
     * Get one informacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InformacionDTO> findOne(Long id) {
        log.debug("Request to get Informacion : {}", id);
        return informacionRepository.findById(id).map(informacionMapper::toDto);
    }

    /**
     * Delete the informacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Informacion : {}", id);
        informacionRepository.deleteById(id);
        informacionSearchRepository.deleteById(id);
    }

    /**
     * Search for the informacion corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InformacionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Informacions for query {}", query);
        return informacionSearchRepository.search(query, pageable).map(informacionMapper::toDto);
    }
}
