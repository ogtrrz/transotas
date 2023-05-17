package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class InformacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformacionDTO.class);
        InformacionDTO informacionDTO1 = new InformacionDTO();
        informacionDTO1.setId(1L);
        InformacionDTO informacionDTO2 = new InformacionDTO();
        assertThat(informacionDTO1).isNotEqualTo(informacionDTO2);
        informacionDTO2.setId(informacionDTO1.getId());
        assertThat(informacionDTO1).isEqualTo(informacionDTO2);
        informacionDTO2.setId(2L);
        assertThat(informacionDTO1).isNotEqualTo(informacionDTO2);
        informacionDTO1.setId(null);
        assertThat(informacionDTO1).isNotEqualTo(informacionDTO2);
    }
}
