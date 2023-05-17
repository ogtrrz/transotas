package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Caso;
import wf.transotas.service.dto.CasoDTO;

/**
 * Mapper for the entity {@link Caso} and its DTO {@link CasoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CasoMapper extends EntityMapper<CasoDTO, Caso> {}
