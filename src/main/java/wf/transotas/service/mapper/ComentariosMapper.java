package wf.transotas.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
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
    @Mapping(target = "reportes", source = "reportes", qualifiedByName = "reportesIdSet")
    ComentariosDTO toDto(Comentarios s);

    @Mapping(target = "removeReportes", ignore = true)
    Comentarios toEntity(ComentariosDTO comentariosDTO);

    @Named("reportesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportesDTO toDtoReportesId(Reportes reportes);

    @Named("reportesIdSet")
    default Set<ReportesDTO> toDtoReportesIdSet(Set<Reportes> reportes) {
        return reportes.stream().map(this::toDtoReportesId).collect(Collectors.toSet());
    }
}
