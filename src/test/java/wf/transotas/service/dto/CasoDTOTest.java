package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CasoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasoDTO.class);
        CasoDTO casoDTO1 = new CasoDTO();
        casoDTO1.setId(1L);
        CasoDTO casoDTO2 = new CasoDTO();
        assertThat(casoDTO1).isNotEqualTo(casoDTO2);
        casoDTO2.setId(casoDTO1.getId());
        assertThat(casoDTO1).isEqualTo(casoDTO2);
        casoDTO2.setId(2L);
        assertThat(casoDTO1).isNotEqualTo(casoDTO2);
        casoDTO1.setId(null);
        assertThat(casoDTO1).isNotEqualTo(casoDTO2);
    }
}
