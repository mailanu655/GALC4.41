package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */
public class ProductStampingSequenceDaoImpl extends BaseDaoImpl<ProductStampingSequence, ProductStampingSequenceId> implements ProductStampingSequenceDao {
	
	private static final String GET_FILLED_STAMP_COUNT_NATIVE_SQL = "select count(*) from gal216tbx where production_lot = ?1 and send_status >= "
				+ PlanStatus.ASSIGNED.getId();
	
	private static final String GET_PRODUCT_ID_BY_PRODUCTION_LOT_LIST= "SELECT PRODUCTION_LOT, LISTAGG(PRODUCT_ID, ',') AS PRODUCT_ID FROM GAL216TBX WHERE PRODUCTION_LOT IN (@productionLotList@) group by PRODUCTION_LOT";
	
	private static final String GET_BY_PRODUCTION_LOT_AND_SEQ= "SELECT * FROM GALADM.GAL216TBX WHERE production_lot = ?1 AND STAMPING_SEQUENCE_NO > ?2 order by STAMPING_SEQUENCE_NO asc";

	private static final String GET_PRODUCTS_IN_STAMPING_SEQ_BY_LOC = "" + 
			" WITH LINKED_LOTS (PRODUCTION_LOT, LEVEL, SEND_STATUS)" + 
			"     AS (SELECT PRODUCTION_LOT, 0 AS LEVEL, SEND_STATUS" + 
			"           FROM GALADM.GAL212TBX" + 
			"          WHERE HOLD_STATUS = 1" + 
			"            AND (SEND_STATUS = 0 OR SEND_STATUS = 1)" + 
			"            AND PROCESS_LOCATION = ?1" + 
			"            AND NEXT_PRODUCTION_LOT IS NULL" + 
			"         UNION ALL" + 
			"         SELECT LOTS.PRODUCTION_LOT, LEVEL + 1 AS LEVEL, LOTS.SEND_STATUS" + 
			"           FROM GALADM.GAL212TBX LOTS, LINKED_LOTS" + 
		    //create linked list up to last lot in WAITING/INPROGRESS status
			"          WHERE LOTS.NEXT_PRODUCTION_LOT = LINKED_LOTS.PRODUCTION_LOT" + 
			"            AND (LOTS.SEND_STATUS = 0 OR LOTS.SEND_STATUS = 1))" + 
			" SELECT B.*" + 
			"   FROM LINKED_LOTS A, GALADM.GAL216TBX B" + 
			"  WHERE A.PRODUCTION_LOT = B.PRODUCTION_LOT" + 
			//select all in progress lots and top n waiting lots
			"   AND (A.SEND_STATUS = 1" + 
			"     OR A.LEVEL > (SELECT (MAX (LINKED_LOTS.LEVEL) - ?2) FROM LINKED_LOTS))" + 
			" ORDER BY A.LEVEL DESC, B.STAMPING_SEQUENCE_NO ASC" + 
			"  WITH CS FOR READ ONLY";
	
	public List<ProductStampingSequence> findAllByProductId(String productId) {
		return findAll( Parameters.with("id.productID",productId));
	}

	public List<ProductStampingSequence> findAllByProductionLot(String productionLot) {
		return findAll( Parameters.with("id.productionLot",productionLot));
	}

	public ProductStampingSequence findById(String productionLot,
			String productId) {
		return findByKey(new ProductStampingSequenceId(productionLot,productId));
	}

	public List<ProductStampingSequence> findAllNext(String productionLot,
			String productId) {
		List<ProductStampingSequence> result = new ArrayList<ProductStampingSequence>(
				0);
		ProductStampingSequence currentSeq = findById(productionLot, productId);
		if (currentSeq == null
				|| currentSeq.getStampingSequenceNumber() == null) {
			return result;
		}
		return findAllNext(productionLot,currentSeq.getStampingSequenceNumber());
	}

	public List<ProductStampingSequence> findAllNext(String productionLot, int seq) {
		Parameters params = new Parameters();
		params.put("productionLot", productionLot);
		params.put("stampingSequenceNumber", seq);
		return findAllByQuery(
				"SELECT o FROM ProductStampingSequence o " +
				"WHERE o.id.productionLot = :productionLot AND o.stampingSequenceNumber > :stampingSequenceNumber " +
				"ORDER BY o.stampingSequenceNumber ASC",
				params);
	}
	
	public int findStampCount(String productionLot){
		Parameters params = Parameters.with("1", productionLot);
		return findFirstByNativeQuery("SELECT COALESCE(MAX(STAMPING_SEQUENCE_NO) + 1,1) FROM GAL216TBX WHERE PRODUCTION_LOT =?1 ", params, Integer.class);
	}
	
	public ProductStampingSequence findNextProduct(String productionLot, PlanStatus planStatus){
		Parameters params = Parameters.with("id.productionLot", productionLot);
		params.put("sendStatus", planStatus.getId());
		String[] orderBy = {"stampingSequenceNumber"};
		return findFirst(params, orderBy, true);
	}
	
	public ProductStampingSequence findNextProduct(String productionLot, int seq){
		Parameters params = Parameters.with("1", productionLot);
		params.put("2", seq);
		return findFirstByNativeQuery(GET_BY_PRODUCTION_LOT_AND_SEQ,params);
	}
	
	public int getFilledStampCount(String productionLot){
		Parameters params = Parameters.with("1", productionLot);
		Object[] objects = findFirstByNativeQuery(GET_FILLED_STAMP_COUNT_NATIVE_SQL, params, Object[].class);
		return objects[0] == null ? null : (Integer) objects[0];
	}
	
	public List<Object[]> findAllProductIdByProductionLotList(List<String> productionLotList) {	
		return executeNative(GET_PRODUCT_ID_BY_PRODUCTION_LOT_LIST.replace("@productionLotList@", StringUtil.toSqlInString(productionLotList)));
	}
	
	@Transactional
	public int deleteProductionLot(String productionLot) {
		return delete(Parameters.with("id.productionLot", productionLot));
	}

	@Override
	public List<ProductStampingSequence> getLinkedProducts(String processLocation, int lotCount) {
		Parameters params = Parameters.with("1", processLocation).put("2", lotCount);
		return findAllByNativeQuery(GET_PRODUCTS_IN_STAMPING_SEQ_BY_LOC, params);
	}
}

