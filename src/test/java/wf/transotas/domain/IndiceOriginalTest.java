package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class IndiceOriginalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndiceOriginal.class);
        IndiceOriginal indiceOriginal1 = new IndiceOriginal();
        indiceOriginal1.setId(1L);
        IndiceOriginal indiceOriginal2 = new IndiceOriginal();
        indiceOriginal2.setId(indiceOriginal1.getId());
        assertThat(indiceOriginal1).isEqualTo(indiceOriginal2);
        indiceOriginal2.setId(2L);
        assertThat(indiceOriginal1).isNotEqualTo(indiceOriginal2);
        indiceOriginal1.setId(null);
        assertThat(indiceOriginal1).isNotEqualTo(indiceOriginal2);
    }
}
