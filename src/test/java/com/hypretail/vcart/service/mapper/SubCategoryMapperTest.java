package com.hypretail.vcart.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubCategoryMapperTest {

    private SubCategoryMapper subCategoryMapper;

    @BeforeEach
    public void setUp() {
        subCategoryMapper = new SubCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subCategoryMapper.fromId(null)).isNull();
    }
}
