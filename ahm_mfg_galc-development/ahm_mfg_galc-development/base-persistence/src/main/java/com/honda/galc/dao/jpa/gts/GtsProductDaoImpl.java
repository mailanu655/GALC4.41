package com.honda.galc.dao.jpa.gts;


import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>GtsProductDaoImpl Class description</h3>
 * <p> GtsProductDaoImpl description </p>
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
 * Jun 1, 2012
 *
 *
 */
public class GtsProductDaoImpl extends BaseDaoImpl<GtsProduct,GtsProductId> implements GtsProductDao {

	private static String FIND_ALL_BY_LANE = "select c.* FROM GTS_CARRIER_TBX a " + 
			"JOIN GTS_LANE_CARRIER_TBX b ON b.LANE_CARRIER = a.CARRIER_NUMBER " + 
			"JOIN GTS_PRODUCT_TBX c on c.PRODUCT_ID = a.PRODUCT_ID AND c.TRACKING_AREA = a.TRACKING_AREA " + 
			"WHERE  b.TRACKING_AREA = ?1 AND b.LANE_ID = ?2 and a.TRACKING_AREA = ?3";
	
	private static String FIND_ALL_BY_AREA = "select c.* FROM GTS_CARRIER_TBX a " + 
			"JOIN GTS_LANE_CARRIER_TBX b ON b.LANE_CARRIER = a.CARRIER_NUMBER " + 
			"JOIN GTS_PRODUCT_TBX c on c.PRODUCT_ID = a.PRODUCT_ID AND c.TRACKING_AREA = a.TRACKING_AREA " + 
			"where  b.TRACKING_AREA = ?1 and a.TRACKING_AREA = ?2";
	
	private static String FIND_ALL_WITH_DEFECTS = "SELECT * FROM GTS_PRODUCT_TBX WHERE TRACKING_AREA = ?1 AND DEFECT_STATUS IN (0,3)";
	
	
	@Override
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<GtsProduct> findAllByLane(String trackingArea, String laneId, String carrierArea) {
		return findAllByNativeQuery(FIND_ALL_BY_LANE,
				Parameters.with("1", trackingArea).put("2", laneId).put("3", carrierArea));
	}

	@Override
	public List<GtsProduct> findAll(String trackingArea,String carrierArea) {
		return findAllByNativeQuery(FIND_ALL_BY_AREA,Parameters.with("1", trackingArea).put("2", carrierArea));
	}

	@Override
	public List<GtsProduct> findAllWithDefects(String trackingArea) {
		return findAllByNativeQuery(FIND_ALL_WITH_DEFECTS,Parameters.with("1", trackingArea));
	}


}
