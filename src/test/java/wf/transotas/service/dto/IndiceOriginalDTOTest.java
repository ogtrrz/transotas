package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class IndiceOriginalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndiceOriginalDTO.class);
        IndiceOriginalDTO indiceOriginalDTO1 = new IndiceOriginalDTO();
        indiceOriginalDTO1.setId(1L);
        IndiceOriginalDTO indiceOriginalDTO2 = new IndiceOriginalDTO();
        assertThat(indiceOriginalDTO1).isNotEqualTo(indiceOriginalDTO2);
        indiceOriginalDTO2.setId(indiceOriginalDTO1.getId());
        assertThat(indiceOriginalDTO1).isEqualTo(indiceOriginalDTO2);
        indiceOriginalDTO2.setId(2L);
        assertThat(indiceOriginalDTO1).isNotEqualTo(indiceOriginalDTO2);
        indiceOriginalDTO1.setId(null);
        assertThat(indiceOriginalDTO1).isNotEqualTo(indiceOriginalDTO2);
    }
}
