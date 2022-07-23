package com.hypretail.vcart.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hypretail.vcart.web.rest.TestUtil;

public class PurchaserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaserDTO.class);
        PurchaserDTO purchaserDTO1 = new PurchaserDTO();
        purchaserDTO1.setId(1L);
        PurchaserDTO purchaserDTO2 = new PurchaserDTO();
        assertThat(purchaserDTO1).isNotEqualTo(purchaserDTO2);
        purchaserDTO2.setId(purchaserDTO1.getId());
        assertThat(purchaserDTO1).isEqualTo(purchaserDTO2);
        purchaserDTO2.setId(2L);
        assertThat(purchaserDTO1).isNotEqualTo(purchaserDTO2);
        purchaserDTO1.setId(null);
        assertThat(purchaserDTO1).isNotEqualTo(purchaserDTO2);
    }
}
