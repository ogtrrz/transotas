package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CasoTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasoTextDTO.class);
        CasoTextDTO casoTextDTO1 = new CasoTextDTO();
        casoTextDTO1.setId(1L);
        CasoTextDTO casoTextDTO2 = new CasoTextDTO();
        assertThat(casoTextDTO1).isNotEqualTo(casoTextDTO2);
        casoTextDTO2.setId(casoTextDTO1.getId());
        assertThat(casoTextDTO1).isEqualTo(casoTextDTO2);
        casoTextDTO2.setId(2L);
        assertThat(casoTextDTO1).isNotEqualTo(casoTextDTO2);
        casoTextDTO1.setId(null);
        assertThat(casoTextDTO1).isNotEqualTo(casoTextDTO2);
    }
}
