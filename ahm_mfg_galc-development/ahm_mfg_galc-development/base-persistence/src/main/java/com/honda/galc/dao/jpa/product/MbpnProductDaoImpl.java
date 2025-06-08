package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.Parameters;

/**
 * @author Gangadhararao Gadde 
 * @date Nov 1, 2012
 */
public class MbpnProductDaoImpl extends ProductDaoImpl<MbpnProduct> implements MbpnProductDao {
	
	private static final String SQL_findSubAssemblyInventoryItemByDivisonId =
		"select t235.zone_name as cellZone, tmbpn.name as product, tmp.current_product_spec_code as currentProductSpecCode, 0 as actual, tmp.current_order_no as currentOrderNo " + 
		"FROM gal214tbx t214 " +  
		"join gal235tbx t235 on (t214.line_id=t235.zone_id) " +  
		"join mbpn_product_tbx tmp on (tmp.last_passing_process_point=t214.process_point_id) " +  
		"join mbpn_tbx tmbpn on (tmp.current_product_spec_code=tmbpn.product_spec_code) " +  
		"WHERE 1=1 " + 
		"AND t235.division_id=?1 ";
	
	private static final String SQL_findSubAssemblyInventoryDetailItemByZoneNameAndProductSpecCode =
		"select t235.zone_name as cellZone, tmbpn.name as product, tmp.current_product_spec_code as currentProductSpecCode, tmp.product_id as sn " + 
		"FROM gal214tbx t214 " +  
		"join gal235tbx t235 on (t214.line_id=t235.zone_id) " +  
		"join mbpn_product_tbx tmp on (tmp.last_passing_process_point=t214.process_point_id) " +  
		"join mbpn_tbx tmbpn on (tmp.current_product_spec_code=tmbpn.product_spec_code) " +
		"WHERE 1=1 " + 
		"AND t235.zone_name=?1 " +
		"AND tmp.current_product_spec_code=?2 " +
		"AND tmp.current_order_no=?3 ";
	
	private static final String SQL_FIND_PRODUCT_PROGRESS =
		"select distinct t235.zone_name as cellZone, tmbpn.name as product, tmp.current_product_spec_code as currentProductSpecCode, torder.prod_order_qty as plan, torder.prod_order_qty as actual, tmp.current_order_no as currentOrderNo " + 
		"FROM gal214tbx t214 " +  
		"join gal235tbx t235 on (t214.line_id=t235.zone_id) " +  
		"join mbpn_product_tbx tmp on (tmp.last_passing_process_point=t214.process_point_id) " +  
		"join mbpn_tbx tmbpn on (tmp.current_product_spec_code=tmbpn.product_spec_code) " +   
		"join order_tbx torder on (tmp.current_order_no=torder.order_no) " +  
		"WHERE 1=1 " + 
		"AND t235.division_id=?1 ";
	
	private static final String FIND_ALL_LAST_PROCESSED = "SELECT P.*" + 
			"  FROM GALADM.MBPN_PRODUCT_TBX P" + 
			"       JOIN" + 
			"          (SELECT T.PRODUCT_ID," + 
			"                  T.PRODUCT_SPEC_CODE," + 
			"                  T.ACTUAL_TIMESTAMP," + 
			"                  ROW_NUMBER ()" + 
			"                  OVER (PARTITION BY T.PRODUCT_ID, T.PRODUCT_SPEC_CODE" + 
			"                        ORDER BY T.ACTUAL_TIMESTAMP DESC) AS RN" + 
			"             FROM GALADM.GAL215TBX AS T" + 
			"            WHERE T.PROCESS_POINT_ID = ?1) R" + 
			"       ON     R.PRODUCT_ID = P.PRODUCT_ID" + 
			"          AND R.PRODUCT_SPEC_CODE = P.CURRENT_PRODUCT_SPEC_CODE" + 
			"          AND R.RN = 1" + 
			" ORDER BY R.ACTUAL_TIMESTAMP DESC";

	private static final String FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT =
			"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" +
					"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " +
					"WHERE PRODUCT_ID = ?1 " +
					"UNION ALL " +
					"SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
					"WHERE ll.NEXT_PRODUCT_ID = s.PRODUCT_ID and level <= ?2) " +
					"SELECT b.* FROM SortedList a, GALADM.MBPN_PRODUCT_TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID ";

	private static final String FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT =
			"WITH SortedList (PRODUCT_ID, NEXT_PRODUCT_ID, Level) AS(" +
					"SELECT PRODUCT_ID, NEXT_PRODUCT_ID, 0 as Level FROM GALADM.GAL176tbx " +
					"WHERE PRODUCT_ID = ?1 " +
					"UNION ALL " +
					"SELECT ll.PRODUCT_ID, ll.NEXT_PRODUCT_ID, Level+1 as Level FROM GALADM.GAL176tbx ll, SortedList as s " +
					"WHERE ll.PRODUCT_ID = s.NEXT_PRODUCT_ID and level <= ?2) " +
					"SELECT b.* FROM SortedList a, GALADM.MBPN_PRODUCT_TBX b WHERE a.PRODUCT_ID = b.PRODUCT_ID " ;
	
	private static final String FIND_PRODUCTS_BY_PRODUCT_SEQUENCE =
			"SELECT A.* FROM GALADM.MBPN_PRODUCT_TBX A,GALADM.PRODUCT_SEQUENCE_TBX B " +
					"WHERE A.PRODUCT_ID = B.PRODUCT_ID AND B.PROCESS_POINT_ID =?1  " +
					"ORDER BY B.REFERENCE_TIMESTAMP ASC";

	private static final String FIND_VALID_PRODUCT_FOR_PROCESS_POINT = "SELECT A.* FROM MBPN_PRODUCT_TBX A WHERE A.PRODUCT_ID = ?1 AND A.TRACKING_STATUS IN (" +
			"SELECT B.PREVIOUS_LINE_ID FROM GAL236TBX B WHERE B.LINE_ID = (" +
			"SELECT C.LINE_ID FROM GAL195TBX C WHERE EXISTS (" +
			"SELECT 1 FROM GAL214TBX D WHERE C.LINE_ID = D.LINE_ID AND D.PROCESS_POINT_ID = ?2 " +
			"FETCH FIRST ROW ONLY) FETCH FIRST ROW ONLY)) FETCH FIRST ROW ONLY";
	
	private static final String ORDER_BY = " ORDER BY Level asc fetch first ";
	
	private static final String ROWS_ONLY = " rows only with ur";
	
	private final String SQL_FIND_MBPN_SEQ =	"select PRODUCT_ID from mbpn_product_tbx where PRODUCT_ID like ?1 order by PRODUCT_ID DESC fetch first 1 rows only ";

	private static final String RELEASE_PRODUCT_HOLD_WITH_CHECK = "update MbpnProduct e set e.holdStatusId = 0 where e.productId = :productId and (select count(hr) from HoldResult hr where hr.id.productId = :productId and (hr.releaseFlag <> 1 or hr.releaseFlag is null)) = 0";

	private static final String FIND_ALL_BY_ORDER_NO_NATIVE = "SELECT * FROM (SELECT E.*, ROW_NUMBER() OVER(ORDER BY PRODUCT_ID) AS rn FROM MBPN_PRODUCT_TBX E WHERE CURRENT_ORDER_NO = ?1) AS temp WHERE rn BETWEEN %d AND %d";

	//generated UPC format: MainNo(5) + SupplierNo(2) + Year(2) + Month(1) + Date(2) + Sequence(5) = 17 chars
	//SUBSTR(PRODUCT_ID, 8, 5) should match Year(2) + Month(1) + Date(2) for generated UPC
	private static final String FIND_ALL_MBPN_PRODUCT = "select * from GALADM.MBPN_PRODUCT_TBX where CREATE_TIMESTAMP LIKE ?1 and SUBSTR(PRODUCT_ID, 8, 5) = ?2 ORDER BY CREATE_TIMESTAMP desc";

	public static final String DUNNAGE_FIELD_NAME = "containerId";
	
	private static final String FIND_CURRENT_PRODUCT_SPEC_CODE = "Select CURRENT_PRODUCT_SPEC_CODE from MBPN_PRODUCT_TBX where PRODUCT_ID = ?1";
	
	public List<MbpnProduct> findAllByProductionLot(String productionLot) {
		return findAllByOrderNo(productionLot);
	}
	
	@Override
	public List<MbpnProduct> findPageByProductionLot(String productionLot, int pageNumber, int pageSize) {
		return findPageByOrderNo(productionLot, pageNumber, pageSize);
	}
	
	public List<InventoryCount> findAllInventoryCounts() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MbpnProduct> findSubAssemblyInventoryByDivisonId(String divisionId) {
		Parameters params = new Parameters();
		params.put("1", divisionId);
		return findAllByNativeQuery(SQL_findSubAssemblyInventoryItemByDivisonId, params);
	}

	public List<MbpnProduct> findSubAssemblyInventoryDetailByZoneNameAndProductSpecCode(String zone,
			String productSpecCode, String orderNo) {
		Parameters params = new Parameters();
		params.put("1", zone);
		params.put("2", productSpecCode);
		params.put("3", orderNo);
		return findAllByNativeQuery(SQL_findSubAssemblyInventoryDetailItemByZoneNameAndProductSpecCode, params);
	}

	public List<MbpnProduct> findProductProgressByDivisonId(String divisionId) {
		Parameters params = new Parameters();
		params.put("1", divisionId);
		return findAllByNativeQuery(SQL_FIND_PRODUCT_PROGRESS, params);
	}
	
	public String findLastProductId(String productIdPrefix) {
		Parameters params = new Parameters();
		params.put("1", productIdPrefix);
		return findFirstByNativeQuery(SQL_FIND_MBPN_SEQ, params,String.class);
	}

	public List<MbpnProduct> findAllByDunnage(String dunnage) {
		return findAllByContainer(dunnage);
	}
	
	public List<MbpnProduct> findAllByContainer(String containerId) {
		return findAll(Parameters.with("containerId", containerId));
	}
	
	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("containerId", null), Parameters.with("productId", productId));
	}
	
	@Override
	@Transactional
	public int releaseHoldWithCheck(String productId) {
		return executeUpdate(RELEASE_PRODUCT_HOLD_WITH_CHECK, Parameters.with("productId", productId));
	}
	
	@Override
	@Transactional
	public void updateHoldStatus(String productId, int status) {
		update(Parameters.with("holdStatusId", status), Parameters.with("productId", productId));
	}
	
	@Transactional
    public int createProducts(ProductionLot prodLot,String productType,String lineId,String ppId) {
		return saveProductionAndPreProductionLot(prodLot);
	}

	public Double findMaxSequence(String trackingStatus) {
		try {
			return max("trackingSeq", Double.class, Parameters.with("trackingStatus",trackingStatus));
		} catch (Exception e) {
			return 0.0;
		}
	}
	
    public List<MbpnProduct> findAllByOrderNo(String orderNo) {
    	String[] orderBy = {"productId"};
		return findAll(Parameters.with("currentOrderNo", orderNo), orderBy);
	}
    
	@Override
    public List<MbpnProduct> findPageByOrderNo(String orderNo, int pageNumber, int pageSize) {
		Parameters params = Parameters.with("1", orderNo);
		int offset = Math.max(pageNumber, 0) * pageSize;
		String query = String.format(FIND_ALL_BY_ORDER_NO_NATIVE, (pageNumber > 0) ? offset + 1 : offset, offset+pageSize);
		return findAllByNativeQuery(query, params);

	}
	
	@Override
	public long countByProductionLot(String prodLot) {
		return countByOrderNo(prodLot);
	}
	
	public long countByOrderNo(String orderNo) {
		return count(Parameters.with("currentOrderNo", orderNo));
	}
	
    @Override
	public List<MbpnProduct> findAllLastProcessed(String processPointId, int rowCount) {
		Parameters params = Parameters.with("1", processPointId);
		String query = FIND_ALL_LAST_PROCESSED + " FETCH FIRST " + rowCount + " ROWS ONLY WITH UR FOR READ ONLY";
		return findAllByNativeQuery(query, params);
	}

	public List<MbpnProduct> findAllByCreateTimeStamp(String yearMonthDay, String createDate) {
		Parameters params = Parameters.with("1", createDate+"%");
		params.put("2", yearMonthDay);
		return findAllByNativeQuery(FIND_ALL_MBPN_PRODUCT, params);
	}
	
	@Transactional
	public int deleteProductionLot(String orderNo) {
		return delete(Parameters.with("currentOrderNo", orderNo));
	}
	
	@Override
	protected String getDunnageFieldName() {
		return DUNNAGE_FIELD_NAME;
	}

	@Override
	public List<MbpnProduct> findAllByInProcessProduct(String currentProductId,
			int processedSize, int upcomingSize) {
		List<MbpnProduct> products = new ArrayList<MbpnProduct>();
		List<MbpnProduct> processedProducts  = findProcessedProducts(currentProductId, processedSize);
		// reverse the list and removed the current product id since it is in the upcoming list
		for(int i = processedProducts.size() - 1; i >= 1; i--)
			products.add(processedProducts.get(i));
		List<MbpnProduct> upcomingProducts  = findUpcomingProducts(currentProductId, upcomingSize);
		for(MbpnProduct product : upcomingProducts) products.add(product);
		return products;
	}

	private List<MbpnProduct> findProcessedProducts(String currentProductId, int processedSize) {
		Parameters params = Parameters.with("1", currentProductId).put("2", processedSize + 1);
		return findAllByNativeQuery(
				FIND_PROCESSED_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(processedSize + 1), params);
	}

	private List<MbpnProduct> findUpcomingProducts(String currentProductId, int upcomingSize) {
		Parameters params = Parameters.with("1", currentProductId).put("2", upcomingSize + 1);;
		return findAllByNativeQuery(
				FIND_UPCOMING_PRODUCTS_BY_IN_PROCESS_PRODUCT + getOrderBy(upcomingSize + 1), params);
	}

	private String getOrderBy(int size) {
		return ORDER_BY + size + ROWS_ONLY;
	}

	@Override
	public List<MbpnProduct> findAllByProductSequence(String processPointId, String currentProductId,
			int processedSize, int upcomingSize) {

		Parameters params = Parameters.with("1", processPointId);

		List<MbpnProduct> mbpnProducts = new ArrayList<MbpnProduct>();
		List<MbpnProduct> allMbpnProducts  = findAllByNativeQuery(FIND_PRODUCTS_BY_PRODUCT_SEQUENCE, params);
		int index = -1;
		for(int i = 0;i< allMbpnProducts.size();i++) {
			if(allMbpnProducts.get(i).getProductId().equals(currentProductId)) {
				index = i;
				break;
			}
		}
		if (index == -1) return mbpnProducts;
		int start = (index - processedSize < 0) ? 0 :index - processedSize;
		int end = (index + upcomingSize >=allMbpnProducts.size()) ? allMbpnProducts.size() -1 : index + upcomingSize;
		for(int i=start;i<=end;i++) {
			mbpnProducts.add(allMbpnProducts.get(i));
		}
		return mbpnProducts;

	}

	@Override
	public boolean isProductTrackingStatusValidForProcessPoint(String productId, String processPointId) {
		Parameters params = Parameters.with("1", productId).put("2", processPointId);
		MbpnProduct result = findFirstByNativeQuery(FIND_VALID_PRODUCT_FOR_PROCESS_POINT, params);
		return result != null;
	}
	
	public String findCurrentProductSpecCode(String productId) {
		return findFirstByNativeQuery(FIND_CURRENT_PRODUCT_SPEC_CODE, Parameters.with("1", productId), String.class);
	}
}