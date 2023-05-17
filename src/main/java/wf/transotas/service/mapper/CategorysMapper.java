package wf.transotas.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import wf.transotas.domain.Categorys;
import wf.transotas.domain.Reportes;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.service.dto.ReportesDTO;

/**
 * Mapper for the entity {@link Categorys} and its DTO {@link CategorysDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategorysMapper extends EntityMapper<CategorysDTO, Categorys> {
    @Mapping(target = "reportes", source = "reportes", qualifiedByName = "reportesIdSet")
    CategorysDTO toDto(Categorys s);

    @Mapping(target = "removeReportes", ignore = true)
    Categorys toEntity(CategorysDTO categorysDTO);

    @Named("reportesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportesDTO toDtoReportesId(Reportes reportes);

    @Named("reportesIdSet")
    default Set<ReportesDTO> toDtoReportesIdSet(Set<Reportes> reportes) {
        return reportes.stream().map(this::toDtoReportesId).collect(Collectors.toSet());
    }
}
