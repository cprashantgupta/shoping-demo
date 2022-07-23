package com.hypretail.vcart.service.mapper;


import com.hypretail.vcart.domain.*;
import com.hypretail.vcart.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {VendorMapper.class, SubCategoryMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "subCategory.id", target = "subCategoryId")
    ProductDTO toDto(Product product);

    @Mapping(target = "removeVendor", ignore = true)
    @Mapping(source = "subCategoryId", target = "subCategory")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
