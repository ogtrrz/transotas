package wf.transotas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CategorysTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categorys.class);
        Categorys categorys1 = new Categorys();
        categorys1.setId(1L);
        Categorys categorys2 = new Categorys();
        categorys2.setId(categorys1.getId());
        assertThat(categorys1).isEqualTo(categorys2);
        categorys2.setId(2L);
        assertThat(categorys1).isNotEqualTo(categorys2);
        categorys1.setId(null);
        assertThat(categorys1).isNotEqualTo(categorys2);
    }
}
