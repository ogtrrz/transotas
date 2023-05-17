package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Comentarios;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Comentarios} and its DTO {@link ComentariosDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComentariosMapper extends EntityMapper<ComentariosDTO, Comentarios> {
    @Mapping(target = "reportes", source = "reportes", qualifiedByName = "reportesId")
    ComentariosDTO toDto(Comentarios s);

    @Named("reportesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportesDTO toDtoReportesId(Reportes reportes);
}
