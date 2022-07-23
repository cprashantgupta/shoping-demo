package com.hypretail.vcart.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PurchaserMapperTest {

    private PurchaserMapper purchaserMapper;

    @BeforeEach
    public void setUp() {
        purchaserMapper = new PurchaserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(purchaserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(purchaserMapper.fromId(null)).isNull();
    }
}
