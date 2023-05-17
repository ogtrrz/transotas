package wf.transotas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategorysMapperTest {

    private CategorysMapper categorysMapper;

    @BeforeEach
    public void setUp() {
        categorysMapper = new CategorysMapperImpl();
    }
}
