package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CasoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caso.class);
        Caso caso1 = new Caso();
        caso1.setId(1L);
        Caso caso2 = new Caso();
        caso2.setId(caso1.getId());
        assertThat(caso1).isEqualTo(caso2);
        caso2.setId(2L);
        assertThat(caso1).isNotEqualTo(caso2);
        caso1.setId(null);
        assertThat(caso1).isNotEqualTo(caso2);
    }
}
