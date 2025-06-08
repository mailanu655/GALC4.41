package com.honda.galc.dao.jpa.product;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartShipmentDao;
import com.honda.galc.entity.product.PartShipment;
import com.honda.galc.service.Parameters;

public class PartShipmentDaoImpl extends BaseDaoImpl<PartShipment,Integer> implements PartShipmentDao {

	
	private static final String FIND_UNSENT_SHIPMENT_BY_SITE = "SELECT * FROM galadm.PART_SHIPMENT_TBX p where p.SEND_STATUS <> 2 AND RECEIVING_SITE = ?1 ";
	private static final String FIND_BY_SITE_AND_SHIP_DATE = "SELECT * FROM galadm.PART_SHIPMENT_TBX where DATE(SENT_TIMESTAMP) = ?1";
	
	public List<PartShipment> findAllUnSentShipmentsForReceivingSite(String site) {
		String sql = FIND_UNSENT_SHIPMENT_BY_SITE ;
		Parameters params = Parameters.with("1", site);
		return findAllByNativeQuery(sql, params, PartShipment.class);
	}

	public List<PartShipment> findByReceivingSiteAndShipmentDate(String site, Date shipmentDate) {
		String sql = FIND_BY_SITE_AND_SHIP_DATE ;
		Parameters params = Parameters.with("1", shipmentDate);
		
		if(StringUtils.isNotEmpty(site)){
			sql = sql + " and RECEIVING_SITE = ?2";
			params.put("2", site);
		}
		return findAllByNativeQuery(sql, params, PartShipment.class);
	}


}
