package com.honda.galc.dao.product;

import java.util.Date;
import java.util.List;


import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.service.IDaoService;

public interface PartShipmentDao extends IDaoService<PartShipment, Integer>{

	public List<PartShipment> findAllUnSentShipmentsForReceivingSite(String site);
	
	public List<PartShipment> findByReceivingSiteAndShipmentDate(String site, Date shipmentDate);
}
