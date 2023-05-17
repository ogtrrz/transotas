package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.CasoText;
import wf.transotas.service.dto.CasoTextDTO;

/**
 * Mapper for the entity {@link CasoText} and its DTO {@link CasoTextDTO}.
 */
@Mapper(componentModel = "spring")
public interface CasoTextMapper extends EntityMapper<CasoTextDTO, CasoText> {}
