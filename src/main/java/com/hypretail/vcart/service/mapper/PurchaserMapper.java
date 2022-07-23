package com.hypretail.vcart.service.mapper;


import com.hypretail.vcart.domain.*;
import com.hypretail.vcart.service.dto.PurchaserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Purchaser} and its DTO {@link PurchaserDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface PurchaserMapper extends EntityMapper<PurchaserDTO, Purchaser> {



    default Purchaser fromId(Long id) {
        if (id == null) {
            return null;
        }
        Purchaser purchaser = new Purchaser();
        purchaser.setId(id);
        return purchaser;
    }
}
