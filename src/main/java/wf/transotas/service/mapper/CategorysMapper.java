package wf.transotas.service.mapper;

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
    @Mapping(target = "reportes", source = "reportes", qualifiedByName = "reportesId")
    CategorysDTO toDto(Categorys s);

    @Named("reportesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportesDTO toDtoReportesId(Reportes reportes);
}
