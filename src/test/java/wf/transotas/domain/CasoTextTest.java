package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CasoTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasoText.class);
        CasoText casoText1 = new CasoText();
        casoText1.setId(1L);
        CasoText casoText2 = new CasoText();
        casoText2.setId(casoText1.getId());
        assertThat(casoText1).isEqualTo(casoText2);
        casoText2.setId(2L);
        assertThat(casoText1).isNotEqualTo(casoText2);
        casoText1.setId(null);
        assertThat(casoText1).isNotEqualTo(casoText2);
    }
}
