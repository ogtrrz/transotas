package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Indice;
import wf.transotas.service.dto.IndiceDTO;

/**
 * Mapper for the entity {@link Indice} and its DTO {@link IndiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface IndiceMapper extends EntityMapper<IndiceDTO, Indice> {}
