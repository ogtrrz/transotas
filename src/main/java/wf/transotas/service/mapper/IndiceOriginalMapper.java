package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.IndiceOriginal;
import wf.transotas.service.dto.IndiceOriginalDTO;

/**
 * Mapper for the entity {@link IndiceOriginal} and its DTO {@link IndiceOriginalDTO}.
 */
@Mapper(componentModel = "spring")
public interface IndiceOriginalMapper extends EntityMapper<IndiceOriginalDTO, IndiceOriginal> {}
