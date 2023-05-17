package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class InformacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Informacion.class);
        Informacion informacion1 = new Informacion();
        informacion1.setId(1L);
        Informacion informacion2 = new Informacion();
        informacion2.setId(informacion1.getId());
        assertThat(informacion1).isEqualTo(informacion2);
        informacion2.setId(2L);
        assertThat(informacion1).isNotEqualTo(informacion2);
        informacion1.setId(null);
        assertThat(informacion1).isNotEqualTo(informacion2);
    }
}
