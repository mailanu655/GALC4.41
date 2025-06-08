package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>SubProductShippingDaoImpl Class description</h3>
 * <p> SubProductShippingDaoImpl description </p>
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
 * Jan 16, 2013
 *
 *
 */
public class SubProductShippingDaoImpl extends BaseDaoImpl<SubProductShipping,SubProductShippingId> implements SubProductShippingDao {

    private static final long serialVersionUID = 1L;
    @Autowired
    private GenericDaoService genericDaoService;
    
    private static final String FIND_ALL_LOTS = 
    	"SELECT A.* FROM GALADM.SUB_PRODUCT_SHIPPING_TBX A,GALADM.GAL212TBX B " +
    	"WHERE A.PRODUCT_TYPE = 'KNUCKLE' AND A.PRODUCTION_LOT = B.PRODUCTION_LOT AND B.PROCESS_LOCATION =?1 " +
    	"ORDER BY PRODUCTION_DATE ASC,SEQ_NO ASC WITH UR FOR READ ONLY";
   
    private static final String FIND_ALL_SHIPPING_LOTS =
    	"SELECT * " +
    	"FROM GALADM.SUB_PRODUCT_SHIPPING_TBX " +
    	"WHERE PRODUCT_TYPE =?1 and LINE_NUMBER =?2 AND  SUBSTR(KD_LOT_NUMBER,1,17) IN(" +
    	"		SELECT DISTINCT SUBSTR(a.KD_LOT_NUMBER,1,17) " +
    	"       FROM GALADM.SUB_PRODUCT_SHIPPING_DETAIL_TBX a,GALADM.SUB_PRODUCT_SHIPPING_TBX b " + 
    	"  		WHERE a.PRODUCT_ID IS NULL and a.SUB_ID IN($SUB_IDS) and b.KD_LOT_NUMBER = a.KD_LOT_NUMBER and " +
    	"            b.PRODUCTION_LOT = a.PRODUCTION_LOT and b.PRODUCT_TYPE =?1 and b.LINE_NUMBER =?2 and b.STATUS < 2) " +
    	"ORDER BY PRODUCTION_DATE ASC,SEQ_NO ASC WITH UR FOR READ ONLY";
    		    	
   private static final String FIND_LAST_LOT_LINE = 
    	"SELECT A.* FROM GALADM.SUB_PRODUCT_SHIPPING_TBX A,GALADM.GAL212TBX B " +
    	"WHERE A.PRODUCT_TYPE = 'KNUCKLE' AND A.PRODUCTION_LOT = B.PRODUCTION_LOT AND A.LINE_NUMBER = ?1 AND B.PROCESS_LOCATION =?2 " +
    	"ORDER BY PRODUCTION_DATE DESC,SEQ_NO DESC WITH UR FOR READ ONLY";
    private static final String FIND_LAST_LOT = 
    	"SELECT A.* FROM GALADM.SUB_PRODUCT_SHIPPING_TBX A " +
    	"WHERE A.PRODUCT_TYPE = 'KNUCKLE' " +
    	"ORDER BY PRODUCTION_DATE DESC,SEQ_NO DESC";
    
    private static final String FIND_SAME_KDLOT = 
    	"SELECT * FROM GALADM.SUB_PRODUCT_SHIPPING_TBX WHERE SUBSTR(KD_LOT_NUMBER,1,17) = SUBSTR(?1,1,17) " +
    	"AND PRODUCT_TYPE =(SELECT PRODUCT_TYPE FROM GALADM.SUB_PRODUCT_SHIPPING_TBX WHERE KD_LOT_NUMBER =?1 AND PRODUCTION_LOT =?2)" + 
    	" ORDER BY PRODUCTION_DATE ASC,SEQ_NO ASC";
	
    private final String FIND_MIN_PRUNING_DATE_1 =
        "SELECT MIN(PRODUCTION_DATE) " +
        "FROM " +
        "(SELECT DISTINCT CHAR(PRODUCTION_DATE) AS PRODUCTION_DATE" +
        " FROM GALADM.SUB_PRODUCT_SHIPPING_TBX " +
        " ORDER BY 1 DESC" +
        " FETCH FIRST ";
    private final String FIND_MIN_PRUNING_DATE_2 =
        " ROWS ONLY) as SHIPPED_LOTS";
    
    private static final String FIND_LAST_SHIPPING_LOT = "SELECT * FROM GALADM.SUB_PRODUCT_SHIPPING_TBX WHERE PRODUCTION_DATE = ?1 AND PRODUCT_TYPE = ?2 ORDER BY SEQ_NO DESC FETCH FIRST 1 ROW ONLY";
    
    @Autowired
	public PreProductionLotDao preProductionLotDao;
    
    @Autowired
	public SubProductShippingDetailDao subProductShippingDetailDao;
	
    public SubProductShipping findLastKnuckle(String processLocation,String lineNumber) {
    	return findFirstByNativeQuery(FIND_LAST_LOT_LINE, Parameters.with("1",lineNumber).put("2",processLocation));
	}
	
	/**
	 * find all items order by production date and seq no
	 */
	public List<SubProductShipping> findAllKnuckleShippingLots(String processLocation) {
		return findAllByNativeQuery(FIND_ALL_LOTS, Parameters.with("1",processLocation));
	}

	public List<SubProductShipping> findAllKnuckleShipping(String processLocation,String lineNumber) {
		
		List<SubProductShipping> items = findAllKnuckleShippingLots(processLocation);
		
		List<SubProductShipping> shippingLots = new ArrayList<SubProductShipping>();
		SubProductShipping currentShippingLot = null;
		
		// get non_started and in-progress shipping lots (including same kd-lot lots which has be shipped( short shipped and shipped)
		for(int i = items.size() -1 ;i>=0;i--) {
			
			SubProductShipping item = items.get(i);	
			if(lineNumber.equals("00")){
				if ((item.getStatus() == 0 || item.getStatus() == 1) ||	currentShippingLot != null 
						&& currentShippingLot.isSameKdLot(item)){
					shippingLots.add(0,item);
					currentShippingLot = item;
				}
			}else{
				if(lineNumber.equals(item.getLineNumber())){
					if ((item.getStatus() == 0 || item.getStatus() == 1) ||	currentShippingLot != null 
							&& currentShippingLot.isSameKdLot(item)){
						shippingLots.add(0,item);
						currentShippingLot = item;
					}
				}
			}
		}
		
		return shippingLots;
	}

	public List<SubProductShipping> findAllKuckleShippingAndShipped() {
		return findAll(Parameters.with("productType","KNUCKLE"), new String[]{"productionDate","seqNo"},true);
	}   
	
	public List<SubProductShipping> findAllKuckleShippingAndShipped(String processLocation) {
		
		List<SubProductShipping> items = findAllKnuckleShippingLots(processLocation);
		
		List<SubProductShipping> shippingLots = new ArrayList<SubProductShipping>();
		
		for(SubProductShipping item : items) {
			if(item.getStatus() == SubProductShipping.IN_PROGRESS ||
					item.getStatus() == SubProductShipping.SHORT_SHIPPED || 
					item.getStatus() == SubProductShipping.SHIPPED)
				shippingLots.add(item);
		}
		return shippingLots;
		
	}
	
	

    public String findMinPruningDate(int days)
    {
        return findFirstByNativeQuery(FIND_MIN_PRUNING_DATE_1 + 
                                      Integer.toString(days)  + 
                                      FIND_MIN_PRUNING_DATE_2, null,String.class);
    }

    @Transactional 
    public int deleteKdLots(String productType,List <String> kdLots)  
    {
        int count = 0;
        for( String kdLot : kdLots )
        {
            count = count + delete(Parameters.with("id.kdLotNumber", kdLot).put("productType", productType));
        }
        return count;
    }

	public SubProductShipping findLastShippingLot() {
    	return findFirstByNativeQuery(FIND_LAST_LOT, null);

	}

	public List<SubProductShipping> findAllWithSameKdLot(String kdLotNumber,String productionLot) {
		return findAllByNativeQuery(FIND_SAME_KDLOT, Parameters.with("1",kdLotNumber).put("2", productionLot));
	}

	@Transactional
	public List<SubProductShipping> createKnuckleShippingLots(String productionLot) {
		List<PreProductionLot> orderedLots = preProductionLotDao.findAllWithSameKdLotFromProductionLot(productionLot);
		SubProductShipping lastShippingLot = findLastShippingLot();
		
		long currentTime = System.currentTimeMillis();
		int seqNo =lastShippingLot != null && DateUtils.isSameDay(lastShippingLot.getProductionDate(), new Date(currentTime)) 
			? lastShippingLot.getSeqNo() + 1000 : 1000;
		List<SubProductShipping> shippingLots = new ArrayList<SubProductShipping>();
		for(PreProductionLot preProductionLot : orderedLots) {
			SubProductShipping shippingLot = SubProductShipping.createKnuckleShipping(preProductionLot, seqNo, currentTime);
			shippingLots.add(shippingLot);
			seqNo += 1000;
		}
		
		List<SubProductShippingDetail> details = createShippingDetails(shippingLots);
		saveAll(shippingLots);
		subProductShippingDetailDao.saveAll(details);
		
		return shippingLots;
	}
	
	private List<SubProductShippingDetail> createShippingDetails(List<SubProductShipping> shippingLots) {
		Map<String,Integer> lotSequenceMap = new HashMap<String,Integer>();
		List<SubProductShippingDetail> details = new ArrayList<SubProductShippingDetail>();
		for(SubProductShipping item : shippingLots) {
			
			int startIndex = lotSequenceMap.containsKey(item.getKdLotNumber())? 
								lotSequenceMap.get(item.getKdLotNumber()) : 0;
			lotSequenceMap.put(item.getKdLotNumber(), startIndex + (item.getSchQuantity() / 2));

			for(int i=startIndex;i<startIndex + item.getSchQuantity() / 2;i++){
				SubProductShippingDetail detailLeft = 
					new SubProductShippingDetail(item.getKdLotNumber(),item.getProductionLot(),SubProduct.SUB_ID_LEFT,i+1);
				SubProductShippingDetail detailRight = 
					new SubProductShippingDetail(item.getKdLotNumber(),item.getProductionLot(),SubProduct.SUB_ID_RIGHT,i+1);
				details.add(detailLeft);
				details.add(detailRight);
			}
		}
		return details;
	}

	public List<SubProductShipping> findAllShipping(String productType,String lineNo,List<String> subIds) {
		return findAllByNativeQuery(FIND_ALL_SHIPPING_LOTS.replace("$SUB_IDS", StringUtil.toSqlInString(subIds)), 
				Parameters.with("1",productType).put("2", lineNo));
	}

	@Transactional
	public SubProductShipping incrementActualQuantity(String kdLotNumber, String productionLot) {
		SubProductShipping subProductShipping = findByKey(new SubProductShippingId(kdLotNumber,productionLot));
		if(subProductShipping != null) {
			subProductShipping.incrementActQuantity();
			if(update(subProductShipping) != null) return subProductShipping;
		}
		return null;
	}
	public SubProductShipping findLastShippingLot(ProductType productType, java.sql.Date date) {
		Parameters params = Parameters.with("1", date.toString()).put("2",productType.toString());
		return findFirstByNativeQuery(FIND_LAST_SHIPPING_LOT, params);
	}

	@Transactional
	public SubProductShipping decrementActualQuantity(String kdLotNumber, String productionLot) {
		SubProductShipping subProductShipping = findByKey(new SubProductShippingId(kdLotNumber,productionLot));
		if(subProductShipping != null) {
			subProductShipping.decrementActQuantity();
			if(update(subProductShipping) != null) return subProductShipping;
		}
		return null;
	}

	@Transactional
	public SubProductShipping createSubProductShipping(PreProductionLot preproductionLot, ProductType productType, int seqIntval, String[] subIds) {

		long currentDate = genericDaoService.getCurrentTime();
		
		SubProductShipping exist = findByKey(new SubProductShippingId(preproductionLot.getKdLotNumber(), preproductionLot.getProductionLot()));
		
		if(exist != null) return exist; //shipping info already generated, so return
		
		SubProductShipping lastShipping = findLastShippingLot(productType, new java.sql.Date(currentDate));
		
		int seqNo = lastShipping == null ?  seqIntval : lastShipping.getSeqNo() +  seqIntval; 
		
		SubProductShipping subProdShipping = SubProductShipping.createShipping(preproductionLot, seqNo, currentDate,productType);

		save(subProdShipping);
		
		//create sub product shipping details
		List<SubProductShippingDetail> currentLotDetails = new ArrayList<SubProductShippingDetail>();
		for(int i = 0 ; i < preproductionLot.getLotSize(); i++){
			for(String subId : subIds){
				SubProductShippingDetail shippingDetail = new SubProductShippingDetail(preproductionLot.getKdLotNumber(),preproductionLot.getProductionLot(),subId, i+1);
				currentLotDetails.add(shippingDetail);
			}
		}
		subProductShippingDetailDao.saveAll(currentLotDetails);
		
		return subProdShipping;
	}

	public Double findMaxSequence(String type) {
		SubProductShipping lastShippingLot = findLastShippingLot(ProductType.valueOf(type), new java.sql.Date(genericDaoService.getCurrentTime()));
		return lastShippingLot == null ? 0.0 : lastShippingLot.getSeqNo();
	}
 
}
