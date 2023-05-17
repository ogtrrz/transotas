package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Informacion;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Informacion} and its DTO {@link InformacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformacionMapper extends EntityMapper<InformacionDTO, Informacion> {
    @Mapping(target = "reportes", source = "reportes", qualifiedByName = "reportesId")
    InformacionDTO toDto(Informacion s);

    @Named("reportesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportesDTO toDtoReportesId(Reportes reportes);
}
