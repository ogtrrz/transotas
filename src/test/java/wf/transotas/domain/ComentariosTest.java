package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class ComentariosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comentarios.class);
        Comentarios comentarios1 = new Comentarios();
        comentarios1.setId(1L);
        Comentarios comentarios2 = new Comentarios();
        comentarios2.setId(comentarios1.getId());
        assertThat(comentarios1).isEqualTo(comentarios2);
        comentarios2.setId(2L);
        assertThat(comentarios1).isNotEqualTo(comentarios2);
        comentarios1.setId(null);
        assertThat(comentarios1).isNotEqualTo(comentarios2);
    }
}
