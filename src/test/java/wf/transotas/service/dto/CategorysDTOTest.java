package wf.transotas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import wf.transotas.web.rest.TestUtil;

class CategorysDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorysDTO.class);
        CategorysDTO categorysDTO1 = new CategorysDTO();
        categorysDTO1.setId(1L);
        CategorysDTO categorysDTO2 = new CategorysDTO();
        assertThat(categorysDTO1).isNotEqualTo(categorysDTO2);
        categorysDTO2.setId(categorysDTO1.getId());
        assertThat(categorysDTO1).isEqualTo(categorysDTO2);
        categorysDTO2.setId(2L);
        assertThat(categorysDTO1).isNotEqualTo(categorysDTO2);
        categorysDTO1.setId(null);
        assertThat(categorysDTO1).isNotEqualTo(categorysDTO2);
    }
}
