package com.hypretail.vcart.service.mapper;


import com.hypretail.vcart.domain.*;
import com.hypretail.vcart.service.dto.VendorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface VendorMapper extends EntityMapper<VendorDTO, Vendor> {


    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Vendor toEntity(VendorDTO vendorDTO);

    default Vendor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vendor vendor = new Vendor();
        vendor.setId(id);
        return vendor;
    }
}
