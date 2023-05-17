package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class ReportesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reportes.class);
        Reportes reportes1 = new Reportes();
        reportes1.setId(1L);
        Reportes reportes2 = new Reportes();
        reportes2.setId(reportes1.getId());
        assertThat(reportes1).isEqualTo(reportes2);
        reportes2.setId(2L);
        assertThat(reportes1).isNotEqualTo(reportes2);
        reportes1.setId(null);
        assertThat(reportes1).isNotEqualTo(reportes2);
    }
}
