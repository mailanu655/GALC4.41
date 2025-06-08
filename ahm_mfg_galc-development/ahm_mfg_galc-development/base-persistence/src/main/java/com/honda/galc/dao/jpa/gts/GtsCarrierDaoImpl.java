package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsCarrierDaoImpl Class description</h3>
 * <p> GtsCarrierDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 21, 2015
 *
 *
 */
public class GtsCarrierDaoImpl extends BaseDaoImpl<GtsCarrier,GtsCarrierId> implements GtsCarrierDao{

	private final String FIND_CARRIER= "SELECT * FROM GALADM.GTS_CARRIER_TBX WHERE TRACKING_AREA = ?1 AND CARRIER_NUMBER LIKE ?2"; 
			
	private final String FIND_CARRIER_BY_LANE = "SELECT * FROM GALADM.GTS_CARRIER_TBX a " + 
	         "JOIN  GTS_LANE_CARRIER_TBX b on b.LANE_CARRIER = a.CARRIER_NUMBER "+ 
			 "where b.TRACKING_AREA = ?1 and b.LANE_ID = ?2 and a.TRACKING_AREA = ?3"; 
	
	private final String FIND_ALL = "SELECT * FROM GALADM.GTS_CARRIER_TBX a " + 
	         "JOIN  GTS_LANE_CARRIER_TBX b on b.LANE_CARRIER = a.CARRIER_NUMBER "+ 
			 "where b.TRACKING_AREA = ?1 and a.TRACKING_AREA = ?2";
	
	private final String FIND_ALL_NO_GTS_PRODUCT = "SELECT * FROM GALADM.GTS_CARRIER_TBX a " + 
	         "JOIN  GTS_LANE_CARRIER_TBX b on b.LANE_CARRIER = a.CARRIER_NUMBER "+ 
			 "WHERE b.TRACKING_AREA = ?1 and a.TRACKING_AREA = ?2 AND a.PRODUCT_ID IS NOT NULL " +
	         "AND NOT EXISTS (SELECT PRODUCT_ID FROM GTS_PRODUCT_TBX WHERE TRACKING_AREA = ?2 and PRODUCT_ID = a.PRODUCT_ID)";
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsCarrier> findAllByProductId(String trackingArea, String productId) {
		return findAll(Parameters.with("id.trackingArea", trackingArea).put("productId", productId));
	}

	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public GtsCarrier findByCarrierId(String trackingArea, int carrierId) {
		
		Parameters params = Parameters.with("1", trackingArea).put("2", "%"+String.valueOf(carrierId));
		List<GtsCarrier> carriers = findAllByNativeQuery(FIND_CARRIER, params);
		
		for(GtsCarrier carrier : carriers) {
			if(StringUtils.isNumeric(carrier.getCarrierNumber()) &&
					Integer.parseInt(carrier.getCarrierNumber())==carrierId) return carrier;
		}
		return null;
	}

	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsCarrier> findAllByCarrierStatus(String trackingArea, int carrierStatus) {
		return findAll(Parameters.with("id.trackingArea", trackingArea).put("status", carrierStatus));
	}

	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsCarrier> findAllByLane(String trackingArea, String laneId, String carrierArea) {
		
		Parameters params = Parameters.with("1", trackingArea).put("2", laneId).put("3", carrierArea);
		return findAllByNativeQuery(FIND_CARRIER_BY_LANE, params);

	}

	
	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsCarrier> findAll(String trackingArea, String carrierArea) {
		Parameters params = Parameters.with("1", trackingArea).put("2", carrierArea);
		return findAllByNativeQuery(FIND_ALL, params);
	}
	
	
	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsCarrier> findAllNoProduct(String trackingArea, String carrierArea) {
		Parameters params = Parameters.with("1", trackingArea).put("2", carrierArea);
		return findAllByNativeQuery(FIND_ALL_NO_GTS_PRODUCT, params);
	}
}
