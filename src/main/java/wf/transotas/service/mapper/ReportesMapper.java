package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Informacion;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Reportes} and its DTO {@link ReportesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportesMapper extends EntityMapper<ReportesDTO, Reportes> {
    @Mapping(target = "informacion", source = "informacion", qualifiedByName = "informacionId")
    ReportesDTO toDto(Reportes s);

    @Named("informacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InformacionDTO toDtoInformacionId(Informacion informacion);
}
