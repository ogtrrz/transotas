package wf.transotas.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.transotas.domain.Comentarios;
import wf.transotas.repository.ComentariosRepository;
import wf.transotas.repository.search.ComentariosSearchRepository;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.service.mapper.ComentariosMapper;

/**
 * Service Implementation for managing {@link Comentarios}.
 */
@Service
@Transactional
public class ComentariosService {

    private final Logger log = LoggerFactory.getLogger(ComentariosService.class);

    private final ComentariosRepository comentariosRepository;

    private final ComentariosMapper comentariosMapper;

    private final ComentariosSearchRepository comentariosSearchRepository;

    public ComentariosService(
        ComentariosRepository comentariosRepository,
        ComentariosMapper comentariosMapper,
        ComentariosSearchRepository comentariosSearchRepository
    ) {
        this.comentariosRepository = comentariosRepository;
        this.comentariosMapper = comentariosMapper;
        this.comentariosSearchRepository = comentariosSearchRepository;
    }

    /**
     * Save a comentarios.
     *
     * @param comentariosDTO the entity to save.
     * @return the persisted entity.
     */
    public ComentariosDTO save(ComentariosDTO comentariosDTO) {
        log.debug("Request to save Comentarios : {}", comentariosDTO);
        Comentarios comentarios = comentariosMapper.toEntity(comentariosDTO);
        comentarios = comentariosRepository.save(comentarios);
        ComentariosDTO result = comentariosMapper.toDto(comentarios);
        comentariosSearchRepository.index(comentarios);
        return result;
    }

    /**
     * Update a comentarios.
     *
     * @param comentariosDTO the entity to save.
     * @return the persisted entity.
     */
    public ComentariosDTO update(ComentariosDTO comentariosDTO) {
        log.debug("Request to update Comentarios : {}", comentariosDTO);
        Comentarios comentarios = comentariosMapper.toEntity(comentariosDTO);
        comentarios = comentariosRepository.save(comentarios);
        ComentariosDTO result = comentariosMapper.toDto(comentarios);
        comentariosSearchRepository.index(comentarios);
        return result;
    }

    /**
     * Partially update a comentarios.
     *
     * @param comentariosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComentariosDTO> partialUpdate(ComentariosDTO comentariosDTO) {
        log.debug("Request to partially update Comentarios : {}", comentariosDTO);

        return comentariosRepository
            .findById(comentariosDTO.getId())
            .map(existingComentarios -> {
                comentariosMapper.partialUpdate(existingComentarios, comentariosDTO);

                return existingComentarios;
            })
            .map(comentariosRepository::save)
            .map(savedComentarios -> {
                comentariosSearchRepository.save(savedComentarios);

                return savedComentarios;
            })
            .map(comentariosMapper::toDto);
    }

    /**
     * Get all the comentarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComentariosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comentarios");
        return comentariosRepository.findAll(pageable).map(comentariosMapper::toDto);
    }

    /**
     * Get one comentarios by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComentariosDTO> findOne(Long id) {
        log.debug("Request to get Comentarios : {}", id);
        return comentariosRepository.findById(id).map(comentariosMapper::toDto);
    }

    /**
     * Delete the comentarios by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comentarios : {}", id);
        comentariosRepository.deleteById(id);
        comentariosSearchRepository.deleteById(id);
    }

    /**
     * Search for the comentarios corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComentariosDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comentarios for query {}", query);
        return comentariosSearchRepository.search(query, pageable).map(comentariosMapper::toDto);
    }
}
