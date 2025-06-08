package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.CarrierAttribute;
import com.honda.galc.entity.product.CarrierAttributeId;
import com.honda.galc.service.IDaoService;

public interface CarrierAttributeDao extends IDaoService<CarrierAttribute, CarrierAttributeId> {

	public List<CarrierAttribute> findByTrackingAreaAndCarrierNumber(String trackingArea, String carrierNumber);

	public int removeByTrackingAreaAndCarrierNumber(String trackingArea, String carrierNumber);

}
