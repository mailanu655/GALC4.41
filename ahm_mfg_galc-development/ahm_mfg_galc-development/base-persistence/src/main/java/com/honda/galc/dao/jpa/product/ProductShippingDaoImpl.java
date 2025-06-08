package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.ProductShippingDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.entity.product.ProductShippingId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.TrackingService;
/**
 * 
 * 
 * <h3>ProductShippingDaoImpl Class description</h3>
 * <p> ProductShippingDaoImpl description </p>
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
 * Sep 11, 2014
 *
 *
 */
public class ProductShippingDaoImpl extends BaseDaoImpl<ProductShipping, String> implements ProductShippingDao{
	private static final long serialVersionUID = 1L;
	
	private static final String FIND_ALL_ACTIVE_DUNNAGES = 
		"SELECT TRAILER_NUMBER,PRODUCT_TYPE,DUNNAGE,SHIP_DATE,COUNT(*) AS TRAILER_STATUS, TRACKING_STATUS FROM GALADM.MC_SHIPPING_TBX " + 
		"WHERE TRAILER_STATUS = 0 GROUP BY TRAILER_NUMBER,PRODUCT_TYPE,DUNNAGE,SHIP_DATE,TRACKING_STATUS";

	private static final String FIND_ALL_SHIPPING_DESTINATIOINS = 
			"SELECT DISTINCT(TRACKING_STATUS) FROM GALADM.PRODUCT_SHIPPING_TBX";
	private static final String FIND_ALL_CONSUMED_DUNNAGES = 
		"SELECT DISTINCT A.DUNNAGE FROM GALADM.MC_SHIPPING_TBX A, GALADM.HEAD_TBX B " +
		" WHERE A.TRAILER_NUMBER = ?1 AND A.TRAILER_STATUS = 0 AND A.PRODUCT_TYPE ='HEAD' AND A.PRODUCT_ID = B.HEAD_ID AND B.DUNNAGE IS NULL " + 
		"UNION ALL " + 
		"SELECT DISTINCT A.DUNNAGE FROM GALADM.MC_SHIPPING_TBX A, GALADM.BLOCK_TBX B WHERE A.TRAILER_NUMBER = ?1 AND A.TRAILER_STATUS = 0 AND A.PRODUCT_TYPE ='BLOCK' AND A.PRODUCT_ID = B.BLOCK_ID AND B.DUNNAGE IS NULL";
	
	private static final String SHIP_TRAILER = 
		"UPDATE GALADM.MC_SHIPPING_TBX SET TRAILER_STATUS = 1,TRACKING_STATUS = ?2, PROCESS_POINT_ID = ?3 WHERE TRAILER_NUMBER = ?1 AND TRAILER_STATUS = 0";
	private static final String FIND_ALL_TRAILERS = "SELECT DISTINCT(TRAILER_NUMBER) FROM GALADM.PRODUCT_SHIPPING_TBX WHERE TRACKING_STATUS=?1 AND SHIP_DATE=?2";
	
	private static final String FIND_ALL_DUNNAGES = "select dunnage, PRODUCT_TYPE, SHIP_DATE, count(dunnage) from galadm.product_shipping_tbx where TRAILER_NUMBER= ?1 and SHIP_DATE = ?2 group by dunnage, PRODUCT_TYPE,SHIP_DATE";
	
	@Autowired
	BlockDao blockDao;
	
	@Autowired
	HeadDao headDao;
	
	@Autowired
	TrackingService trackingService;
	
	@SuppressWarnings("unchecked")
	public List<ProductShipping> findAllActiveDunnages() {
		List<Object[]> results =  findResultListByNativeQuery(FIND_ALL_ACTIVE_DUNNAGES, null);
		List<ProductShipping> productShippingList = new ArrayList<ProductShipping>();
		for(Object[] result :results) {
			ProductShipping productShipping = new ProductShipping();
			ProductShippingId id = new ProductShippingId();
			id.setTrailerNumber((String)result[0]);
			productShipping.setId(id);
			productShipping.setProductTypeString((String)result[1]);
			productShipping.setDunnage((String)result[2]);
			productShipping.setShipDate((Date)result[3]);
			productShipping.setCount((Integer)result[4]);
			productShipping.setTrackingStatus((String)result[5]);
			productShippingList.add(productShipping);
		}
		return productShippingList;
	}
	
	public List<String> findAllConsumedDunnages(String trailerNumber) {
		return findAllByNativeQuery(FIND_ALL_CONSUMED_DUNNAGES, Parameters.with("1", trailerNumber),String.class);
	}

	public List<ProductShipping> findAllShippments(String trailerNumber,String dunnage) {
		return findAll(Parameters.with("id.trailerNumber", trailerNumber)
						.put("dunnage", dunnage)
						.put("trailerStatus", 0));
	}
	
	public List<ProductShipping> getAllByDunnage(String trailerNumber, String dunnage,Date shipDate){
		return findAll(Parameters.with("id.trailerNumber", trailerNumber).put("dunnage", dunnage).put("shipDate", shipDate));
	}
	public List<ProductShipping> findAllShippments(String trailerNumber) {
		return findAll(Parameters.with("id.trailerNumber", trailerNumber)
						.put("trailerStatus", 0));
	}

	@Transactional
	public int removeDunnage(String trailerNumber, String dunnage) {
		return delete(Parameters.with("id.trailerNumber", trailerNumber)
						.put("dunnage", dunnage)
						.put("trailerStatus", 0));
	}

	@Transactional
	public int shipTrailer(String trailerNumber,String trackingStaus,String processPointId) {
		return executeNativeUpdate(
					SHIP_TRAILER,
					Parameters.with("1", trailerNumber).put("2", trackingStaus).put("3", processPointId)
				);
	}

	@Transactional
	public void completeTrailer(String trailerNumber,String trackingStatus, String processPointId) {
		List<ProductShipping> shipItems = findAllShippments(trailerNumber);
		
		for(ProductShipping shipItem : shipItems) {
			if(shipItem.getProductTypeString().equalsIgnoreCase(ProductType.BLOCK.toString())){
				Block block = blockDao.findBySn(shipItem.getId().getProductId());
				if(block != null){
					trackingService.track(block, processPointId);
					blockDao.removeDunnage(shipItem.getId().getProductId());
				}
			}else if(shipItem.getProductTypeString().equalsIgnoreCase(ProductType.HEAD.toString())){
				Head head = headDao.findBySn(shipItem.getId().getProductId());
				if(head != null){
					trackingService.track(headDao.findBySn(shipItem.getId().getProductId()), processPointId);
					headDao.removeDunnage(shipItem.getId().getProductId());
				}
			}
		}
		shipTrailer(trailerNumber, trackingStatus, processPointId);
	}
	@Override
	public List<String> findAllShippingDestinations() {
		
		return  findAllByNativeQuery(FIND_ALL_SHIPPING_DESTINATIOINS, null,String.class);
	}

	@Override
	public List<String> getTrailers(String destination, String dateStr) {
		
		return findAllByNativeQuery(FIND_ALL_TRAILERS, Parameters.with("1", destination).put("2", dateStr),String.class);
	}

	@Override
	public List<ProductShipping> findAllDunnages(String trailerNumber, String dateStr) {
		
		List<Object[]> results =  findResultListByNativeQuery(FIND_ALL_DUNNAGES, Parameters.with("1", trailerNumber).put("2", dateStr));
		List<ProductShipping> productShippingList = new ArrayList<ProductShipping>();
		for(Object[] result :results) {
			ProductShipping productShipping = new ProductShipping();
			ProductShippingId id = new ProductShippingId();
			id.setTrailerNumber(trailerNumber);
			id.setProductId((String)result[0]);
			productShipping.setId(id);
			productShipping.setProductTypeString((String)result[1]);
			productShipping.setDunnage((String)result[0]);
			productShipping.setShipDate((Date)result[2]);
			productShipping.setCount((Integer)result[3]);
			productShippingList.add(productShipping);
		}
		return productShippingList;
	}
}
