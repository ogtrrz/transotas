package wf.transotas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasoTextMapperTest {

    private CasoTextMapper casoTextMapper;

    @BeforeEach
    public void setUp() {
        casoTextMapper = new CasoTextMapperImpl();
    }
}
