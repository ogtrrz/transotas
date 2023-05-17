package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class IndiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndiceDTO.class);
        IndiceDTO indiceDTO1 = new IndiceDTO();
        indiceDTO1.setId(1L);
        IndiceDTO indiceDTO2 = new IndiceDTO();
        assertThat(indiceDTO1).isNotEqualTo(indiceDTO2);
        indiceDTO2.setId(indiceDTO1.getId());
        assertThat(indiceDTO1).isEqualTo(indiceDTO2);
        indiceDTO2.setId(2L);
        assertThat(indiceDTO1).isNotEqualTo(indiceDTO2);
        indiceDTO1.setId(null);
        assertThat(indiceDTO1).isNotEqualTo(indiceDTO2);
    }
}
