package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Informacion;
import wf.transotas.service.dto.InformacionDTO;

/**
 * Mapper for the entity {@link Informacion} and its DTO {@link InformacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformacionMapper extends EntityMapper<InformacionDTO, Informacion> {}
