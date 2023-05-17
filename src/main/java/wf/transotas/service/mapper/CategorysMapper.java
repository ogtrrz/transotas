package wf.transotas.service.mapper;

import org.mapstruct.*;
import wf.transotas.domain.Categorys;
import wf.transotas.service.dto.CategorysDTO;

/**
 * Mapper for the entity {@link Categorys} and its DTO {@link CategorysDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategorysMapper extends EntityMapper<CategorysDTO, Categorys> {}
