package wf.transotas.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import wf.transotas.domain.CasoText;
import wf.transotas.domain.Categorys;
import wf.transotas.domain.Comentarios;
import wf.transotas.domain.Informacion;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.CasoTextDTO;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Reportes} and its DTO {@link ReportesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportesMapper extends EntityMapper<ReportesDTO, Reportes> {
    @Mapping(target = "informacion", source = "informacion", qualifiedByName = "informacionId")
    @Mapping(target = "casoText", source = "casoText", qualifiedByName = "casoTextId")
    @Mapping(target = "categorys", source = "categorys", qualifiedByName = "categorysIdSet")
    @Mapping(target = "comentarios", source = "comentarios", qualifiedByName = "comentariosIdSet")
    ReportesDTO toDto(Reportes s);

    @Mapping(target = "removeCategorys", ignore = true)
    @Mapping(target = "removeComentarios", ignore = true)
    Reportes toEntity(ReportesDTO reportesDTO);

    @Named("informacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InformacionDTO toDtoInformacionId(Informacion informacion);

    @Named("casoTextId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CasoTextDTO toDtoCasoTextId(CasoText casoText);

    @Named("categorysId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategorysDTO toDtoCategorysId(Categorys categorys);

    @Named("categorysIdSet")
    default Set<CategorysDTO> toDtoCategorysIdSet(Set<Categorys> categorys) {
        return categorys.stream().map(this::toDtoCategorysId).collect(Collectors.toSet());
    }

    @Named("comentariosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ComentariosDTO toDtoComentariosId(Comentarios comentarios);

    @Named("comentariosIdSet")
    default Set<ComentariosDTO> toDtoComentariosIdSet(Set<Comentarios> comentarios) {
        return comentarios.stream().map(this::toDtoComentariosId).collect(Collectors.toSet());
    }
}
