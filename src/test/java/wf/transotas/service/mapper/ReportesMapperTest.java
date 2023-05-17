package wf.transotas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportesMapperTest {

    private ReportesMapper reportesMapper;

    @BeforeEach
    public void setUp() {
        reportesMapper = new ReportesMapperImpl();
    }
}
