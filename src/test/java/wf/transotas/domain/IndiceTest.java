package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class IndiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Indice.class);
        Indice indice1 = new Indice();
        indice1.setId(1L);
        Indice indice2 = new Indice();
        indice2.setId(indice1.getId());
        assertThat(indice1).isEqualTo(indice2);
        indice2.setId(2L);
        assertThat(indice1).isNotEqualTo(indice2);
        indice1.setId(null);
        assertThat(indice1).isNotEqualTo(indice2);
    }
}
