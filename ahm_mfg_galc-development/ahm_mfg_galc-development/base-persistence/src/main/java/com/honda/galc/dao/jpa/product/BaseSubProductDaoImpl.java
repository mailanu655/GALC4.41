package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.BaseSubProductDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.dto.InventoryCount;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductLot;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.KeyValue;

//REMARK : this class is needed as parameterized super class for SubProduct types Daos.
//Currently we still need to have concrete SubProductDaoImpl as it is used by SubProductTracker. 
// When Trackers in general are updated to to do dynamic lookups for ProductDaos by ProductType(currenlty daos are injected or looked up by Class) then we can make SubProductDaoImpl parameterized and code can be moved back from BaseSubProductDaoImpl to SubProductDaoImpl,
//and BaseSubProductDaoImpl could  be deleted. 

public abstract class BaseSubProductDaoImpl<E extends SubProduct> extends ProductDaoImpl<E> implements  BaseSubProductDao<E>{

	private final String FIND_MAX_PRODUCT_ID ="select max(PRODUCT_ID) from galadm.SUB_PRODUCT_TBX where SUBSTR(PRODUCT_ID,1,?1) = ?2 AND SUBSTR(PRODUCT_ID,?3) < \'" + MIN_REMAKE_SN_LIMIT + "\'";
	private final String FIND_MIN_PRODUCT_ID ="select min(PRODUCT_ID) from galadm.SUB_PRODUCT_TBX where SUBSTR(PRODUCT_ID,1,?1) = ?2 and PRODUCTION_LOT = ?3 and SUBSTR(PRODUCT_ID,?4) < \'" + MIN_REMAKE_SN_LIMIT + "\'";
	
	private final static String FIND_PRODUCTION_LOT_INFO = "SELECT Min(s.PRODUCT_ID) AS startProductId, Max(s.PRODUCT_ID) AS endProductId, s.SUB_ID as subId, COUNT(1) AS SIZE, 0 as STATUS, s.PRODUCTION_LOT as productionLot, " +
			"s.PRODUCT_SPEC_CODE as productSpecCode, s.KD_LOT_NUMBER as kdLotNumber, b.ATTRIBUTE, b.ATTRIBUTE_VALUE as attributeValue FROM GALADM.SUB_PRODUCT_TBX s, GALADM.GAL259TBX b WHERE s.STATUS = 0 " +
			"and SUBSTR(s.PRODUCT_SPEC_CODE,1,9) = b.PRODUCT_SPEC_CODE AND SUBSTR(s.PRODUCT_ID, 1, 10) = b.ATTRIBUTE_VALUE GROUP BY s.PRODUCTION_LOT, s.PRODUCT_SPEC_CODE, s.KD_LOT_NUMBER, s.SUB_ID, b.ATTRIBUTE, b.ATTRIBUTE_VALUE";
	
	private final static String FIND_REMAKED_SUB_PRODUCTS = "SELECT s.PRODUCT_ID AS startProductId, s.PRODUCT_ID AS endProductId, s.SUB_ID as subId, 1 AS SIZE, 1 as STATUS, s.PRODUCTION_LOT as productionLot, " +
			"s.PRODUCT_SPEC_CODE as productSpecCode, s.KD_LOT_NUMBER as kdLotNumber, b.ATTRIBUTE, b.ATTRIBUTE_VALUE as attributeValue FROM GALADM.SUB_PRODUCT_TBX s, GALADM.GAL259TBX b WHERE s.STATUS = 1 AND " +
			"SUBSTR(s.PRODUCT_SPEC_CODE,1,9) = b.PRODUCT_SPEC_CODE AND SUBSTR(s.PRODUCT_ID, 1, 10) = b.ATTRIBUTE_VALUE";
	
	private final static String FIND_SCRAPPED_SUB_PRODUCTS = "SELECT s.PRODUCT_ID AS startProductId, s.PRODUCT_ID AS endProductId, s.SUB_ID as subId, 1 AS SIZE, 2 as STATUS, s.PRODUCTION_LOT as productionLot, " +	   
	        "s.PRODUCT_SPEC_CODE as productSpecCode, s.KD_LOT_NUMBER as kdLotNumber, b.ATTRIBUTE, b.ATTRIBUTE_VALUE as attributeValue FROM GALADM.SUB_PRODUCT_TBX s, GALADM.GAL259TBX b WHERE s.STATUS = 2 AND " +
	        "SUBSTR(s.PRODUCT_SPEC_CODE,1,9) = b.PRODUCT_SPEC_CODE AND SUBSTR(s.PRODUCT_ID, 1, 10) = b.ATTRIBUTE_VALUE";

	
	private final static String FIND_SUB_PRODUCT = "SELECT s.PRODUCT_ID AS startProductId, s.PRODUCT_ID AS endProductId, s.SUB_ID as subId, 1 AS SIZE, s.STATUS, s.PRODUCTION_LOT as productionLot, s.PRODUCT_SPEC_CODE as productSpecCode, " +
			"s.KD_LOT_NUMBER as kdLotNumber, b.ATTRIBUTE, b.ATTRIBUTE_VALUE as attributeValue FROM GALADM.SUB_PRODUCT_TBX s, GALADM.GAL259TBX b WHERE s.PRODUCT_ID = ?1 and " +
			"SUBSTR(s.PRODUCT_SPEC_CODE,1,9) = b.PRODUCT_SPEC_CODE AND SUBSTR(s.PRODUCT_ID, 1, 10) = b.ATTRIBUTE_VALUE";
	
	private final static String FIND_MIN_REMAKE = "SELECT MIN(PRODUCT_ID) FROM GALADM.SUB_PRODUCT_TBX WHERE PRODUCT_ID LIKE ?1 AND SUBSTR(PRODUCT_ID,12,6) > ?2";
	
	private final static String FIND_ALL_MATCH_NUMBER = "SELECT * FROM GALADM.SUB_PRODUCT_TBX WHERE PRODUCT_ID LIKE ?1 WITH UR";

	private final static String FIND_PASSING_SUB_PRODUCT = "select sub_id as key, count(product_id) as value from galadm.sub_product_tbx where production_lot = ?1 and LAST_PASSING_PROCESS_POINT_ID != ?2 group by sub_id";
	
	private final static String FIND_ALL_NOT_MATCH = 
		    "SELECT b.PRODUCT_ID,b.PRODUCTION_LOT,b.KD_LOT_NUMBER,b.PRODUCT_SPEC_CODE, c.ATTRIBUTE_VALUE AS TRACKING_STATUS " +
			"FROM GALADM.GAL212TBX a, GALADM.SUB_PRODUCT_TBX b, GALADM.GAL259TBX c " + 
				"WHERE  a.SEND_STATUS = 0 AND b.PRODUCTION_LOT = a.PRODUCTION_LOT AND  b.SUB_ID =?1  AND c.ATTRIBUTE =?2 AND " +  
                "(c.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,9) or c.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,4))" + 
                " AND SUBSTR(b.PRODUCT_ID,1,10)  NOT IN(" + 
                " SELECT d.ATTRIBUTE_VALUE FROM GALADM.GAL259TBX d WHERE " +   
                " d.ATTRIBUTE = ?3 AND (d.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,9) OR d.PRODUCT_SPEC_CODE = SUBSTR(b.PRODUCT_SPEC_CODE,1,4))) WITH UR";
	
    private final static String FIND_ALL_SHIPPED =
        "SELECT A.* " + 
        "FROM   GALADM.SUB_PRODUCT_TBX A " +
        "JOIN   GALADM.SUB_PRODUCT_SHIPPING_TBX B " +
        "ON     A.KD_LOT_NUMBER  = B.KD_LOT_NUMBER " +
        "AND    B.STATUS         = 3 " + 
        "JOIN   GALADM.GAL212TBX C " +
        "ON     A.PRODUCTION_LOT = C.PRODUCTION_LOT " +
        "AND    C.LOT_SIZE * 2   = C.STAMPED_COUNT " +
        "WHERE  B.PRODUCTION_DATE < ?1 " +
        "ORDER BY " +
        "A.PRODUCTION_LOT,A.KD_LOT_NUMBER,A.PRODUCT_ID WITH UR";       
    
    private static final String FIND_INVENTORY_COUNTS =
		"SELECT COUNT(*) as COUNT,LAST_PASSING_PROCESS_POINT_ID as PROCESS,  SUBSTR(KD_LOT_NUMBER,1,6) as PLANT " + 
		"FROM GALADM.SUB_PRODUCT_TBX " +  
		"WHERE AUTO_HOLD_STATUS <>1 " + 
		"GROUP BY LAST_PASSING_PROCESS_POINT_ID,SUBSTR(KD_LOT_NUMBER,1,6) " + 
		"ORDER BY PROCESS,PLANT";

    private final String SELECT_PRODUCTS_BY_LAST_DIGITS = "select e from %s e where trim(e.productId) like :snMask order by e.productId";

    private final String FIND_ELIGIBLE_PRODUCTS = "WITH ID_ORDERED (PRODUCT_ID, NEXT_PRODUCT_ID, ID_SEQ) "
			+ "AS (SELECT ID_START.PRODUCT_ID, ID_START.NEXT_PRODUCT_ID, 1 AS ID_SEQ "
			+ " FROM GAL176TBX ID_START "
			+ "WHERE     ID_START.NEXT_PRODUCT_ID IS NULL "
			+ "   AND ID_START.LINE_ID = ?1 "
			+ " UNION ALL "
			+ " SELECT ID_NEXT.PRODUCT_ID, "
			+ " ID_NEXT.NEXT_PRODUCT_ID, "
			+ " s.ID_SEQ + 1 AS ID_SEQ "
			+ "   FROM GAL176TBX ID_NEXT, ID_ORDERED AS s "
			+ " WHERE ID_NEXT.NEXT_PRODUCT_ID = s.PRODUCT_ID) "
			+ "SELECT NON_INSTALLED.PRODUCT_ID "
			+

			" FROM (SELECT ROW_NUMBER () OVER (ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ "     AS LINE_SEQ_NUM, "
			+ "  ROW_NUMBER () "
			+ "  OVER (PARTITION BY INSTALLED_PART.INSTALLED_PART_STATUS "
			+ " ORDER BY ID_ORDERED.ID_SEQ DESC) "
			+ " AS PRINT_ELIGIBLE_SEQ_NUM, "
			+ "  PRODUCT.PRODUCT_ID, "
			+ " ID_ORDERED.NEXT_PRODUCT_ID, "
			+ " PRODUCT.PRODUCT_SPEC_CODE, "
			+ "  RULES.PART_NAME, "
			+ " INSTALLED_PART.PART_NAME AS INSTALLED_PART_NAME, "
			+ " INSTALLED_PART.INSTALLED_PART_STATUS "
			+ "  FROM ID_ORDERED "
			+ " JOIN SUB_PRODUCT_TBX PRODUCT "
			+ "   ON ID_ORDERED.PRODUCT_ID = PRODUCT.PRODUCT_ID "
			+ "  JOIN GAL246TBX RULES "
			+ "  ON (   RULES.MODEL_YEAR_CODE = '*' "
			+ "     OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,1,1) = RULES.MODEL_YEAR_CODE) "
			+ "  AND (   RULES.MODEL_CODE = '*' "
			+ "     OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,2,3) = RULES.MODEL_CODE) "
			+ "  AND (   RULES.MODEL_TYPE_CODE = '*' "
			+ "     OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,5,3) = RULES.MODEL_TYPE_CODE) "
			+ " AND (   RULES.MODEL_OPTION_CODE = '*' "
			+ "     OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,8,3) = RULES.MODEL_OPTION_CODE) "
			+ " AND (   RULES.EXT_COLOR_CODE = '*' "
			+ "    OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,11,10) = RULES.EXT_COLOR_CODE) "
			+ "  AND (   RULES.INT_COLOR_CODE = '*' "
			+ "  OR SUBSTR(PRODUCT.PRODUCT_SPEC_CODE,21,2) = RULES.INT_COLOR_CODE) "
			+ " AND RULES.PROCESS_POINT_ID = ?4 "
			+ " AND RULES.PART_NAME = ?5 "
			+ " LEFT  JOIN GAL185TBX INSTALLED_PART "
			+ "     ON     ID_ORDERED.PRODUCT_ID = INSTALLED_PART.PRODUCT_ID "
			+ "  AND INSTALLED_PART.PART_NAME = RULES.PART_NAME "
			+ "WHERE    INSTALLED_PART.INSTALLED_PART_STATUS IS NULL "
			+ "    OR INSTALLED_PART.INSTALLED_PART_STATUS < 0) AS NON_INSTALLED "
			+ "WHERE     NON_INSTALLED.LINE_SEQ_NUM <= ?2 "
			+ "   AND NON_INSTALLED.PRINT_ELIGIBLE_SEQ_NUM <= ?3 "
			+ "   AND NON_INSTALLED.INSTALLED_PART_STATUS IS NULL ";

    private final String FIND_MIN_PRODUCT_ID_IN_LOT =
    	"select sp from SubProduct sp " +
    	"where sp.productId = " +
    	"(select min(ss.productId) from SubProduct ss " +
    	"where ss.productionLot = :productionLot and ss.subId = :subId)";
    
    private final String FIND_MAX_PRODUCT_ID_IN_LOT =
    	"select sp from SubProduct sp " +
    	"where sp.productId = " +
    	"(select max(ss.productId) from SubProduct ss " +
    	"where ss.productionLot = :productionLot and ss.subId = :subId)";

	private static final String SELECT_DUNNAGE_INFO = "select e.dunnage, count(e) from SubProduct e where e.dunnage like :param  group by e.dunnage order by e.dunnage desc";
    
    @Autowired
    BuildAttributeDao buildAttributeDao;
    
    protected String getJpqlFindAllBySN() {
	    return String.format(SELECT_PRODUCTS_BY_LAST_DIGITS, getEntityClass().getSimpleName());
	}
    
	@Override
	public E findByKey(String id) {
		E entity = super.findByKey(id);
		if (getEntityClass().isInstance(entity)) {
			return entity;
		} else {
			return null;
		}
	}
    
    public List<E> findAllBySN(String SN)
    {
    	Parameters params = Parameters.with("snMask", "%"+SN);
    	return findAllByQuery(getJpqlFindAllBySN(), params);
    }
    
    @Transactional
	public String findMaxProductId(String prefix) {
		Parameters params = Parameters.with("1", prefix.length());
		params.put("2", prefix);
		params.put("3", prefix.length() + 1);
		
		return findFirstByNativeQuery(FIND_MAX_PRODUCT_ID, params,String.class);
	}

	public String findMinProductId(String productionLot, String prefix) {

		Parameters params = Parameters.with("1", prefix.length());
		params.put("2", prefix);
		params.put("3", productionLot);
		params.put("4", prefix.length() + 1);
		
		return findFirstByNativeQuery(FIND_MIN_PRODUCT_ID, params,String.class);
	}

	public List<E> findAllByProductionLot(String productionLot) {
		
		return findAll(Parameters.with("productionLot", productionLot));
	}

	public List<E> findAllShipped(String productionDate) 
	{
        Parameters params = Parameters.with("1", productionDate);
		//List<E> allShippedSubProduct = findAllByNativeQuery(FIND_ALL_SHIPPED, params, SubProduct.class);
        List<E> allShippedSubProduct = findAllByNativeQuery(FIND_ALL_SHIPPED, params, getEntityClass());
		return allShippedSubProduct;
	}

	public List<SubProductLot> findProductionLots() {
		List<SubProductLot> allProductionLots = findAllByNativeQuery(FIND_PRODUCTION_LOT_INFO, null, SubProductLot.class);
		List<SubProductLot> allRemakedProducts = findAllByNativeQuery(FIND_REMAKED_SUB_PRODUCTS, null, SubProductLot.class);
		List<SubProductLot> allScrappedProducts = findAllByNativeQuery(FIND_SCRAPPED_SUB_PRODUCTS, null, SubProductLot.class);
		
		
		List<SubProductLot> newList = new ArrayList<SubProductLot>(allProductionLots);
		
		if(allRemakedProducts != null && allRemakedProducts.size() > 0){
			List<SubProductLot> newRemakedProductList = new ArrayList<SubProductLot>(allRemakedProducts);
			newList.addAll(newRemakedProductList);
		}
		
		if(allScrappedProducts != null && allScrappedProducts.size() > 0){
			List<SubProductLot> newScrappedProductList = new ArrayList<SubProductLot>(allScrappedProducts);
			newList.addAll(newScrappedProductList);
		}
		
		return newList;
	}
	
	public SubProductLot findSubProductSummary(String productId){
		Parameters params = Parameters.with("1", productId);
		return findFirstByNativeQuery(FIND_SUB_PRODUCT, params, SubProductLot.class);
	}

	@Transactional
	public void updateDefectStatus(String productId, DefectStatus status) {
		update(Parameters.with("defectStatus", status.getId()), Parameters.with("productId", productId));
		
	}
	
	@Transactional
	public String findMinRemake(String productId) {
		Parameters params = Parameters.with("1", StringUtils.substring(productId,0, 11) + "%").put("2", "" + MIN_REMAKE_SN_LIMIT);
		return findFirstByNativeQuery(FIND_MIN_REMAKE, params, String.class);
	}

	public List<E> findAllMatchSerialNumber(String serialNumber) {
		
		return findAllByNativeQuery(FIND_ALL_MATCH_NUMBER, Parameters.with("1", "%" + serialNumber));
		
	}
	
	@SuppressWarnings("unchecked")
	public List<KeyValue> findPassingSubProduct(String productionLot, String processPoint) {
		Parameters params = Parameters.with("1", productionLot).put("2", processPoint);
		return findAllByNativeQuery(FIND_PASSING_SUB_PRODUCT, params, KeyValue.class);
	}

	public List<E> findAllNotMatchingPartNumbers(String productionLot) {

		Parameters paramsLeft = Parameters.with("1",Product.SUB_ID_LEFT)
									  .put("2",BuildAttributeTag.KNUCKLE_LEFT_SIDE)
									  .put("3",BuildAttributeTag.KNUCKLE_LEFT_SIDE);
		
		List<E> subProducts = new ArrayList<E>();
		subProducts.addAll(findAllByNativeQuery(FIND_ALL_NOT_MATCH, paramsLeft));
		
		Parameters paramsRight = Parameters.with("1",Product.SUB_ID_RIGHT)
										.put("2",BuildAttributeTag.KNUCKLE_RIGHT_SIDE)
										.put("3",BuildAttributeTag.KNUCKLE_RIGHT_SIDE);

		subProducts.addAll(findAllByNativeQuery(FIND_ALL_NOT_MATCH, paramsRight));
		return subProducts;
		
	}
	
	@Transactional
	public void deleteAll(String productionLot, String subId) {
		
		delete(Parameters.with("productionLot", productionLot).put("subId", subId));
		
	}

	@Transactional
	public int deleteKdLots(String productType,List <String> kdLots) 
	{
        int count = 0;
		for( String kdLot : kdLots )
		{
			count = count + delete(Parameters.with("kdLotNumber", kdLot).put("productType", productType));
		}
        return count;
	}

	public List<InventoryCount> findAllInventoryCounts() {
		List<?> results =  findResultListByNativeQuery(FIND_INVENTORY_COUNTS, null);
		return toInventoryCounts(results);
	}
	
	public List<E> findByTrackingStatus(String trackingStatus) {
		
		return findAll(Parameters.with("trackingStatus", trackingStatus));
	}

	public List<E> findByPartName(String lineId, int prePrintQty,
			int maxPrintCycle, String ppid, String partName) {
		Parameters parameters = Parameters.with("1", lineId.trim());
		parameters.put("2", prePrintQty);
		parameters.put("3", maxPrintCycle);
		parameters.put("4", ppid.trim());
		parameters.put("5", partName.trim());
		
		return findAllByNativeQuery(FIND_ELIGIBLE_PRODUCTS, parameters );
	}

	@Transactional
	public E findFistProduct(String productionLot, String subId) {
		
		Parameters parm = Parameters.with("productionLot",productionLot);
		parm.put("subId", subId);
		return findFirstByQuery(FIND_MIN_PRODUCT_ID_IN_LOT, parm);
	}

	@Transactional
	public E findLastProduct(String productionLot, String subId) {
		Parameters parm = Parameters.with("productionLot",productionLot);
		parm.put("subId", subId);
		return findFirstByQuery(FIND_MAX_PRODUCT_ID_IN_LOT, parm);
	}
	
	@Transactional
    public int createProducts(ProductionLot prodLot,String productType,String lineId,String ppId) {
		int count = 0;
		count = saveProductionAndPreProductionLot(prodLot);
		return count;
	}
	
	public int countProductionLotByKdLot(String productionLot, String kdLot) {
		return (int)count(Parameters.with("productionLot", productionLot).put("kdLotNumber", kdLot));
	}
	
	public int count(String productionLot, String subId) {
		return (int)count(Parameters.with("productionLot", productionLot).put("subId", subId));
	}
	
	@Override
	public List<E> findAllByDunnage(String dunnage) {
		return findAll(Parameters.with("dunnage", dunnage));
	}
	
	@Override
	@Transactional
	public int removeDunnage(String productId) {
		return update(Parameters.with("dunnage", null), Parameters.with("productId", productId));
	}
	
	@Override
	public List<Map<String, Object>> selectDunnageInfo(String criteria, int resultsetSize) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<?> resultList = findAllByQuery(SELECT_DUNNAGE_INFO, Parameters.with("param", criteria), resultsetSize);
		if (resultList != null) {
			for (Object item : resultList) {
				Object[] row = (Object[]) item;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dunnage", row[0]);
				map.put("count", row[1]);
				list.add(map);
			}
		}
		return list;
	}
	
	@Override	
	public long countByDunnage(String dunnage) {
		return count(Parameters.with("dunnage", dunnage));
	}
}
