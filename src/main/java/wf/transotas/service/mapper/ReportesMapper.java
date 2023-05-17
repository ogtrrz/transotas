package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Reportes} and its DTO {@link ReportesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportesMapper extends EntityMapper<ReportesDTO, Reportes> {}
