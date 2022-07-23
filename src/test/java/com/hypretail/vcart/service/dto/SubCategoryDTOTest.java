package com.hypretail.vcart.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hypretail.vcart.web.rest.TestUtil;

public class SubCategoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategoryDTO.class);
        SubCategoryDTO subCategoryDTO1 = new SubCategoryDTO();
        subCategoryDTO1.setId(1L);
        SubCategoryDTO subCategoryDTO2 = new SubCategoryDTO();
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
        subCategoryDTO2.setId(subCategoryDTO1.getId());
        assertThat(subCategoryDTO1).isEqualTo(subCategoryDTO2);
        subCategoryDTO2.setId(2L);
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
        subCategoryDTO1.setId(null);
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
    }
}
