package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CarrierAttributeDao;
import com.honda.galc.entity.product.CarrierAttribute;
import com.honda.galc.entity.product.CarrierAttributeId;
import com.honda.galc.service.Parameters;

public class CarrierAttributeDaoImpl extends BaseDaoImpl<CarrierAttribute,CarrierAttributeId> implements CarrierAttributeDao {

	public List<CarrierAttribute> findByTrackingAreaAndCarrierNumber(String trackingArea, String carrierNumber) {
		return findAll(Parameters.with("id.trackingArea", trackingArea).put("id.carrierNumber", carrierNumber));
	}

	@Transactional
	public int removeByTrackingAreaAndCarrierNumber(String trackingArea, String carrierNumber) {
		return delete(Parameters.with("id.trackingArea", trackingArea).put("id.carrierNumber", carrierNumber));
	}

}
