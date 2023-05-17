package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Comentarios;
import wf.transotas.service.dto.ComentariosDTO;

/**
 * Mapper for the entity {@link Comentarios} and its DTO {@link ComentariosDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComentariosMapper extends EntityMapper<ComentariosDTO, Comentarios> {}
