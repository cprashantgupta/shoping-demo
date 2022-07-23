package com.hypretail.vcart.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hypretail.vcart.web.rest.TestUtil;

public class PurchaserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchaser.class);
        Purchaser purchaser1 = new Purchaser();
        purchaser1.setId(1L);
        Purchaser purchaser2 = new Purchaser();
        purchaser2.setId(purchaser1.getId());
        assertThat(purchaser1).isEqualTo(purchaser2);
        purchaser2.setId(2L);
        assertThat(purchaser1).isNotEqualTo(purchaser2);
        purchaser1.setId(null);
        assertThat(purchaser1).isNotEqualTo(purchaser2);
    }
}
