package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneCarrierId;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsLaneCarrierDaoImpl Class description</h3>
 * <p> GtsLaneCarrierDaoImpl description </p>
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
public class GtsLaneCarrierDaoImpl extends BaseDaoImpl<GtsLaneCarrier,GtsLaneCarrierId> implements GtsLaneCarrierDao{

	public static final String APPEND_CARRIER ="update GtsLaneCarrier e set e.id.laneId = :laneId, e.id.lanePosition = CURRENT_TIMESTAMP " +
		"where e.id.trackingArea =:trackingArea and e.id.laneId = :oldLaneId and e.id.lanePosition = :oldLanePosition";
	
	public static final String FIND_ALL_BY_PRODUCT_ID =
		"SELECT a.* from GALADM.GTS_LANE_CARRIER_TBX a, GALADM.GTS_CARRIER_TBX b, GALADM.GTS_PRODUCT_TBX c " + 
		"WHERE c.PRODUCT_ID = ?1 AND b.TRACKING_AREA = ?2 and c.TRACKING_AREA = ?3 " +
	    "AND b.PRODUCT_ID = c.PRODUCT_ID AND a.TRACKING_AREA = c.TRACKING_AREA AND a.LANE_CARRIER = b.CARRIER_NUMBER " ;
	
	public static final String UPDATE_STATUS ="update GtsLaneCarrier e set e.discrepancyStatus = :discrepancyStatus " +
	"where e.id.trackingArea =:trackingArea and e.id.laneId = :laneId and e.id.lanePosition = :lanePosition";

	public static final String REMOVE_ALL_BY_LAND_ID ="delete from GtsLaneCarrier e " +
			"where e.id.trackingArea =:trackingArea and e.id.laneId = :laneId"; 
	
	public static final String LANE_LOGICALLY_FULL = 
		"SELECT " + 
		"	CASE WHEN COUNT(*) < (SELECT LANE_CAPACITY FROM GALADM.GTS_LANE_TBX WHERE TRACKING_AREA = ?1 AND LANE_ID = ?2)  " + 
	    "	THEN 0 ELSE 1 END " + 
	    "FROM GALADM.GTS_LANE_CARRIER_TBX " + 
	    "WHERE TRACKING_AREA = ?1 AND LANE_ID = ?2";
	
	@Autowired
	GtsCarrierDao carrierDao;
	
	@Autowired
	GtsProductDao productDao;
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<GtsLaneCarrier> findAll(String trackingArea, String laneId,String carrierArea) {
		Parameters params = Parameters.with("id.trackingArea", trackingArea).put("id.laneId", laneId);
		List<GtsLaneCarrier> laneCarriers = findAll(params,new String[]{"id.lanePosition"},true);
		return fetchProducts(laneId,laneCarriers,carrierArea);
	}

	@Transactional
	public void replaceCarrier(GtsLaneCarrier target, GtsLaneCarrier source) {
		target.setLaneCarrier(source.getLaneCarrier());
		remove(source);
		update(target);
	}

	@Transactional
	public int appendCarrier(String targetLane, GtsLaneCarrier source) {
		Parameters params = Parameters.with("laneId", targetLane).put("trackingArea", source.getId().getTrackingArea())
								.put("oldLaneId", source.getId().getLaneId()).put("oldLanePosition", source.getId().getLanePosition());
		return executeUpdate(APPEND_CARRIER, params);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<GtsLaneCarrier> findAll(String trackingArea,String carrierArea) {
		Parameters params = Parameters.with("id.trackingArea", trackingArea);
		List<GtsLaneCarrier> laneCarriers = findAll(params,new String[]{"id.laneId","id.lanePosition"},true);
		return fetchProducts(laneCarriers,carrierArea);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<GtsLaneCarrier> findAllByCarrierId(String trackingArea,
			String carrierId) {
		Parameters params = Parameters.with("id.trackingArea", trackingArea).put("laneCarrier",carrierId);
		return findAll(params);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<GtsLaneCarrier> findAllByProductId(String trackingArea,	String productId, String carrierArea) {
		Parameters params = Parameters.with("1", productId).put("2",trackingArea).put("3", carrierArea);
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCT_ID, params);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public GtsLaneCarrier update(GtsLaneCarrier entity) {
		return super.save(entity);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void remove(GtsLaneCarrier entity) {
		super.remove(entity);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int removeAll(String trackingArea, String laneId) {
		Parameters params = Parameters.with("trackingArea", trackingArea).put("laneId",laneId);
		return executeUpdate(REMOVE_ALL_BY_LAND_ID,params);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int updateStatus(GtsLaneCarrier laneCarrier) {
		Parameters params = Parameters.with("laneId", laneCarrier.getId().getLaneId()).put("trackingArea", laneCarrier.getId().getTrackingArea())
		.put("lanePosition", laneCarrier.getId().getLanePosition()).put("discrepancyStatus",laneCarrier.getDiscrepancyStatus());
		return executeUpdate(UPDATE_STATUS, params);
	}
	
	private List<GtsLaneCarrier> fetchProducts(String laneId,List<GtsLaneCarrier> laneCarriers,String carrierArea) {
		if(laneCarriers.isEmpty()) return laneCarriers;
		
		List<GtsCarrier> carriers = carrierDao.findAllByLane(laneCarriers.get(0).getId().getTrackingArea(), laneId, carrierArea);
		
		List<GtsProduct> products = productDao.findAllByLane(
				laneCarriers.get(0).getId().getTrackingArea(), 
				laneCarriers.get(0).getId().getLaneId(),carrierArea);
		
		fillLaneCarriers(laneCarriers,carriers, products);
		
		return laneCarriers;
	}
	
	private List<GtsLaneCarrier> fetchProducts(List<GtsLaneCarrier> laneCarriers,String carrierArea) {
		if(laneCarriers.isEmpty()) return laneCarriers;
		
		List<GtsCarrier> carriers = carrierDao.findAll(laneCarriers.get(0).getId().getTrackingArea(), carrierArea);
		
		List<GtsProduct> products = productDao.findAll(laneCarriers.get(0).getId().getTrackingArea(),carrierArea);
		
		fillLaneCarriers(laneCarriers,carriers, products);

		return laneCarriers;
	}
	
	private void fillLaneCarriers(List<GtsLaneCarrier> laneCarriers, List<GtsCarrier> carriers, List<GtsProduct> products) {
		for(GtsLaneCarrier laneCarrier : laneCarriers) {
			GtsCarrier carrier = findCarrier(carriers,laneCarrier.getLaneCarrier());
			laneCarrier.setCarrier(carrier);
			if(carrier != null && !StringUtils.isEmpty(carrier.getProductId())) {
				GtsProduct product = getProduct(products,carrier.getProductId());
				if(product != null) {
					carrier.setProduct(product);
					laneCarrier.setProduct(product);
				}
			}
		}
	}
	
	private GtsCarrier findCarrier(List<GtsCarrier> carriers, String carrierId) {
		for(GtsCarrier carrier : carriers) {
			if(carrier.getCarrierNumber().equalsIgnoreCase(carrierId)) return carrier;
		}
		
		return null;
	}
	
	private GtsProduct getProduct(List<GtsProduct> products, String productId) {
		if(StringUtils.isEmpty(productId)) return null;
		for(GtsProduct product:products) {
			if(productId.equals(product.getId().getProductId())) return product;
		}
		return null;
	}

	@Override
	public int isLaneLogicallyFull(String trackingArea, String laneId) {
		Parameters params = Parameters.with("1", trackingArea).put("2", laneId);
		List<Integer> items = findAllByNativeQuery(LANE_LOGICALLY_FULL, params,Integer.class);
		return !items.isEmpty() ? items.get(0) : 0;  
	
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<GtsLaneCarrier> saveAll(List<GtsLaneCarrier> laneCarriers) {
		return super.saveAll(laneCarriers);
	}



}
