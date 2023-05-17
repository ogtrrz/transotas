package wf.transotas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InformacionMapperTest {

    private InformacionMapper informacionMapper;

    @BeforeEach
    public void setUp() {
        informacionMapper = new InformacionMapperImpl();
    }
}
