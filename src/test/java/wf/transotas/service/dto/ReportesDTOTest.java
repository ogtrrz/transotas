package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class ReportesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportesDTO.class);
        ReportesDTO reportesDTO1 = new ReportesDTO();
        reportesDTO1.setId(1L);
        ReportesDTO reportesDTO2 = new ReportesDTO();
        assertThat(reportesDTO1).isNotEqualTo(reportesDTO2);
        reportesDTO2.setId(reportesDTO1.getId());
        assertThat(reportesDTO1).isEqualTo(reportesDTO2);
        reportesDTO2.setId(2L);
        assertThat(reportesDTO1).isNotEqualTo(reportesDTO2);
        reportesDTO1.setId(null);
        assertThat(reportesDTO1).isNotEqualTo(reportesDTO2);
    }
}
