package wf.transotas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasoMapperTest {

    private CasoMapper casoMapper;

    @BeforeEach
    public void setUp() {
        casoMapper = new CasoMapperImpl();
    }
}
