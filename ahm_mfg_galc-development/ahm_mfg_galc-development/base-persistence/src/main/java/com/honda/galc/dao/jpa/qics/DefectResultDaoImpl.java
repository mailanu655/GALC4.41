package com.honda.galc.dao.jpa.qics;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.qics.DefectRepairResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.dao.qics.vo.AddNewDefectRepairResultRequest;
import com.honda.galc.dao.qics.vo.AddNewDefectResultRequest;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.DeptDefectResult;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * 
 * <h3>DefectResultDaoImpl Class description</h3>
 * <p> DefectResultDaoImpl description </p>
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
 * Feb 28, 2011
 *
 *
 */
public class DefectResultDaoImpl extends BaseDaoImpl<DefectResult,DefectResultId> implements DefectResultDao{
	
	private static final String FIND_ALL_BY_PRODUCT_ID = "select * from galadm.gal125tbx where product_id =?1 and defect_status in(0,1,3)";
	private static final String FIND_LEGACY_COUNT_BY_PRODUCT_ID = "select count(*) from galadm.gal125tbx where product_id =?1 and ( NAQ_DEFECTRESULTID = 0 or NAQ_DEFECTRESULTID is null )";
	private static final String FIND_OUTSTANDING_BY_PRODUCT_ID = "select * from galadm.gal125tbx where product_id =?1 and defect_status = 0 with cs for read only";
	private static final String FIND_ALL_DEFECT_BY_PRODUCT_ID =  "SELECT e FROM DefectResult e WHERE e.id.productId = :productId and e.defectStatus = 0";	
	
    private static final String DELETE_BY_PRODUCTION_LOT_ENGINE = "delete from DefectResult m where m.id.productId in(" + 
	"select e.productId from Engine e where e.productionLot = :productionLot)";

    private static final String DELETE_BY_PRODUCTION_LOT_FRAME = "delete from DefectResult m where m.id.productId in(" + 
	"select f.productId from frame f where f.productionLot = :productionLot)";

    private static final String UPDATE_GDP_DEFECT_VQ_WRITE_UP_DEPT = " UPDATE GALADM.GAL125TBX DEFECT SET DEFECT.GDP_DEFECT = 1 " +
	" WHERE (INSPECTION_PART_NAME, INSPECTION_PART_LOCATION_NAME," +
	" DEFECT_TYPE_NAME, SECONDARY_PART_NAME,DEFECTRESULTID,APPLICATION_ID, " +
	" PRODUCT_ID, TWO_PART_PAIR_PART, TWO_PART_PAIR_LOCATION) IN " +
	" (SELECT INSPECTION_PART_NAME, INSPECTION_PART_LOCATION_NAME, " +
	" DEFECT_TYPE_NAME, SECONDARY_PART_NAME,DEFECTRESULTID,APPLICATION_ID, " +
	" DEFECT.PRODUCT_ID, TWO_PART_PAIR_PART, TWO_PART_PAIR_LOCATION  " +
	" FROM GALADM.GAL215TBX RESULT INNER JOIN GALADM.GAL125TBX DEFECT " +
	" ON RESULT.PRODUCT_ID = DEFECT.PRODUCT_ID WHERE RESULT.PROCESS_POINT_ID = ?1 " +
	" AND RESULT.PRODUCT_ID=?2 AND DEFECT.DEFECT_STATUS <> 2 " +
	" AND (RESULT.ACTUAL_TIMESTAMP <= DEFECT.REPAIR_TIMESTAMP " +
	" OR DEFECT.REPAIR_TIMESTAMP IS NULL) AND DEFECT.ACTUAL_TIMESTAMP < RESULT.ACTUAL_TIMESTAMP " +
	" AND DEFECT.WRITE_UP_DEPARTMENT <> 'VQ' )";
    
    private static final String UPDATE_GDP_DEFECT = "UPDATE GALADM.GAL125TBX DEFECT SET DEFECT.GDP_DEFECT = 1 " +
	" WHERE (INSPECTION_PART_NAME, INSPECTION_PART_LOCATION_NAME, " +
	" DEFECT_TYPE_NAME, SECONDARY_PART_NAME,DEFECTRESULTID,APPLICATION_ID, " +
	" PRODUCT_ID, TWO_PART_PAIR_PART, TWO_PART_PAIR_LOCATION) " +
	" IN (SELECT INSPECTION_PART_NAME, INSPECTION_PART_LOCATION_NAME, DEFECT_TYPE_NAME, " +
	" SECONDARY_PART_NAME,DEFECTRESULTID,APPLICATION_ID, DEFECT.PRODUCT_ID, " +
	" TWO_PART_PAIR_PART, TWO_PART_PAIR_LOCATION  " +
	" FROM GALADM.GAL215TBX RESULT INNER JOIN GALADM.GAL125TBX DEFECT ON " +
	" RESULT.PRODUCT_ID = DEFECT.PRODUCT_ID WHERE RESULT.PROCESS_POINT_ID = ?1 " +
	" AND RESULT.PRODUCT_ID=?2 AND DEFECT.DEFECT_STATUS <> 2 " +
	" AND (RESULT.ACTUAL_TIMESTAMP <= DEFECT.REPAIR_TIMESTAMP " +
	" OR DEFECT.REPAIR_TIMESTAMP IS NULL) AND DEFECT.ACTUAL_TIMESTAMP < RESULT.ACTUAL_TIMESTAMP)";
    
    private static final String UPDATE_GDP_DEFECT_BY_CURRENT_PROCESS = "update gal125tbx d set d.gdp_defect = 1 where d.product_id = ?1 and (d.defect_status is null or d.defect_status = 0) and (d.gdp_defect is null or d.gdp_defect != 1)";

    private static final String SELECT_CORE_MQ =
    "SELECT "+
    "product_id, "+
    "defectresultid, "+
	"actual_timestamp, "+
	"inspection_part_name, "+
	"inspection_part_location_name, "+
	"defect_type_name, "+
	"two_part_pair_part, "+
	"two_part_pair_location, "+
	"responsible_dept "+
	"FROM galadm.gal125tbx "+
	"WHERE defect_status != 2 "+
	"AND actual_timestamp >= ?1 "+
	"AND actual_timestamp <= ?2";
	
    private final static String FIND_ALL_REJECTION_COUNTS =
    	"SELECT DIVISION_ID, SHIFT, INSPECTION_PART_NAME, DEFECT_TYPE_NAME, COUNT(APPLICATION_ID) as REJECTION_COUNT " + 
    	"FROM GALADM.GAL214TBX " + 
    	"INNER JOIN GALADM.GAL125TBX ON PROCESS_POINT_ID = APPLICATION_ID " +
    	"WHERE DATE = ?1 AND DIVISION_ID = ?2 " +
    	"GROUP BY DIVISION_ID, INSPECTION_PART_NAME, DEFECT_TYPE_NAME, SHIFT " +
    	"ORDER BY DIVISION_ID,SHIFT,REJECTION_COUNT DESC";
    
    private static final String SELECT_QI_CORE_MQ_DATA =  "select "+
			"product_id, "+
			"defectresultid, "+
			"actual_timestamp, "+
			"inspection_part_name, "+
			"concat(inspection_part_location_name,inspection_part_location2_name) as inspection_part_location_name, "+
			"defect_type_name, "+
			"defect_type_name2, "+
			"concat(inspection_part2_name,inspection_part3_name) as two_part_pair_part, "+
			"concat(inspection_part2_location_name,inspection_part2_location2_name) as two_part_pair_location, "+
			"responsible_plant, "+
			"responsible_dept , "+
			"defect_category_name "+
			"from galadm.qi_defect_result_tbx where   "+
			" actual_timestamp >= ?1 "+
			"and actual_timestamp <= ?2 and UPPER(defect_category_name) != UPPER('Informational') ";
 
    private static final String FIND_ALL_BY_PDC_AND_PRODUCT_ID =
    	    "select * from galadm.GAl125TBX DEF "+
    	    "join galadm.QI_MAPPING_COMBINATION_TBX MCT on MCT.OLD_INSPECTION_PART_NAME = DEF.INSPECTION_PART_NAME "+
    	    "where MCT.OLD_INSPECTION_PART_NAME =DEF.INSPECTION_PART_NAME  and MCT.OLD_INSPECTION_PART_LOCATION_NAME = DEF.INSPECTION_PART_LOCATION_NAME "+
    	    "and MCT.OLD_INSPECTION_PART2_NAME =DEF.TWO_PART_PAIR_PART and MCT.OLD_INSPECTION_PART2_LOCATION_NAME = DEF.TWO_PART_PAIR_LOCATION and  "+
    	    "MCT.OLD_DEFECT_TYPE_NAME =DEF.DEFECT_TYPE_NAME and "+
    	    "MCT.INSPECTION_PART_NAME = ?1 and  MCT.INSPECTION_PART_LOCATION_NAME= ?2 and MCT.INSPECTION_PART3_NAME =?3 "+
    	    "and MCT.INSPECTION_PART2_NAME =?4 and  MCT.INSPECTION_PART2_LOCATION_NAME =?5 and MCT.DEFECT_TYPE_NAME =?6  "+
    	    "and DEF.PRODUCT_ID	=?7 and MCT.DEFECT_TYPE_NAME2 =?8";

	private final static String FIND_OLD_DEFECT_RESULT = "SELECT * FROM GALADM.GAL125TBX WHERE NAQ_DEFECTRESULTID IN (SELECT A.DEFECTRESULTID FROM GALADM.QI_REPAIR_RESULT_TBX A, GALADM.QI_DEFECT_RESULT_TBX B WHERE A.REPAIR_ID = ?1 AND A.DEFECTRESULTID = B.DEFECTRESULTID)";
	
	private final static String FIND_OLD_DEFECT_RESULT_BY_QIDEFECTID = "SELECT e FROM DefectResult e WHERE e.naqDefectResultId = :qiDefectId ";
	
	private final static String UPDATE_OLD_DEFECT_RESULT_REPAIRED = "UPDATE GALADM.GAL125TBX SET DEFECT_STATUS = ?2, REPAIR_TIMESTAMP = ?3, REPAIR_ASSOCIATE_NO = ?4 WHERE NAQ_DEFECTRESULTID in (SELECT DEFECTRESULTID from QI_DEFECT_RESULT_TBX where DEFECTRESULTID = ?1 and CURRENT_DEFECT_STATUS = 7)";
	
	private final static String UPDATE_OLD_DEFECT_RESULT_OUTSTANDING = "UPDATE GALADM.GAL125TBX SET DEFECT_STATUS = ?2, REPAIR_TIMESTAMP = ?3, REPAIR_ASSOCIATE_NO = ?4 WHERE NAQ_DEFECTRESULTID in (SELECT DEFECTRESULTID from QI_DEFECT_RESULT_TBX where DEFECTRESULTID = ?1 and CURRENT_DEFECT_STATUS != 7)";
    
	@Autowired
	private StationResultDao stationResultDao;
	
	@Autowired
	private DefectRepairResultDao defectRepairResultDao;
	
	@Autowired
	private ExceptionalOutDao exceptionalOutDao;
	
	@Autowired
	private DailyDepartmentScheduleDao dailyDepartmentScheduleDao;
	
	public List<DefectResult> findAllByProductId(String productId) {
		
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCT_ID, Parameters.with("1", productId));
		
	}
	
	public int getLegacyRowCountByProductId(String productId) {
		Integer count = findFirstByNativeQuery(FIND_LEGACY_COUNT_BY_PRODUCT_ID, Parameters.with("1", productId), Integer.class);
		return (count == null)? 0 : count.intValue();
	}

	@Transactional
	public List<DefectResult> saveAll(List<DefectResult> defectResults) {
		
		return super.saveAll(defectResults);
		
	}
	
	private StationResult saveStationResult(BaseProduct product, DefectStatus defectStatus, String processPointId, List<DefectResult> defectResults, DailyDepartmentSchedule schedule) {
		if (schedule == null) {
			return null;
		}
		StationResult stationResult = getStationResult(product, processPointId, schedule);
		DailyDepartmentSchedule shiftFirstPeriod = getDailyDepartmentScheduleDao().findShiftFirstPeriod(schedule);
		if (shiftFirstPeriod == null || shiftFirstPeriod.getStartTimestamp() == null) {
			return stationResult;
		}
		ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(product.getProductType());
		boolean productProcessed = historyDao.isProductProcessedOnOrAfter(product.getProductId(), processPointId, shiftFirstPeriod.getStartTimestamp());
		if (productProcessed) {
			return stationResult;
		}
		// only update station result when the product has not been processed at this station after the beginning of this shift
		stationResult.updateResult(deriveDefectStatus(defectStatus, defectResults));
		stationResult.setLastProductId(product.getProductId());
		Logger.getLogger().info("product:", product.getProductId(), " defect status:" + product.getDefectStatus());
		stationResult = stationResultDao.save(stationResult);
		return stationResult;
	}
	
	private DefectStatus deriveDefectStatus(DefectStatus defectStatus, List<DefectResult> defectResults) {
		
		if(defectStatus != null &&(defectStatus.isScrap() || defectStatus.isPreheatScrap() || defectStatus.isDirectPass())) return defectStatus;
		if(defectResults == null || defectResults.isEmpty()) 
			defectStatus = DefectStatus.DIRECT_PASS;
		else {
			defectStatus = hasOutstandingDefect(defectResults) ? 
				DefectStatus.OUTSTANDING:DefectStatus.REPAIRED;
		}
		return defectStatus;
	}
	
	
	private boolean hasOutstandingDefect(List<DefectResult> defectResults) {
		
		for(DefectResult defectResult : defectResults) {
			if(defectResult.isOutstandingStatus()) return true;
		}
		return false;
	}
 
    private StationResult getStationResult(BaseProduct product,String processPointId,
			DailyDepartmentSchedule schedule) {
    	
    	StationResultId stationResultId = new StationResultId();
		
    	stationResultId.setApplicationId(processPointId);
    	stationResultId.setProductionDate(schedule.getId().getProductionDate());
    	stationResultId.setShift(schedule.getId().getShift());

		StationResult stationResult = stationResultDao.findByKey(stationResultId);
		
		if(stationResult != null) return stationResult;
		
		stationResult = new StationResult();
		stationResult.setId(stationResultId);
		stationResult.setFirstProductId(product.getProductId());
		
		return stationResult;
		
    }
    
    private void updateProduct(BaseProduct product, List<DefectResult> defectResults) {
    	
    	ProductDao<? extends BaseProduct> productDao = 
    		ProductTypeUtil.getProductDao(product.getProductType());
    	
    	productDao.updateDefectStatus(product.getProductId(),product.getDefectStatus());
    	
    	if(product.getProductType().equals(ProductType.ENGINE)){
    		if(isEngineFiring(defectResults))
    			((EngineDao)productDao).updateEngineFiringFlag(product.getProductId(), (short)1);
    	} else if(product.getProductType().equals(ProductType.HEAD)){
    		if(isEngineFiring(defectResults))
    			((HeadDao)productDao).updateEngineFiringFlag(product.getProductId(), (short)1);
    	}	
    }
    
    protected boolean isEngineFiring(List<DefectResult> defectResults) {
		if(defectResults == null || defectResults.size() == 0) return false;
		
		for(DefectResult result: defectResults)
			if(result != null && result.isEngineFiring()) return true;
		
		return false;
	}
    
    
    /**
     * Adds a new defect result to the specified unit and defect.
     */
    @Transactional
	public void addNewDefectResult( AddNewDefectResultRequest request ) {
    	
    	ExceptionalOut exceptionalOut= null;
    	DailyDepartmentSchedule schedule = null;
    	
    	// get all the existing defects
    	List<DefectResult> defectResultList = this.findAllByProductId( request.getProductId());
    	
    	// Returned jpa result lists are unmodifiable, so make a new list
    	List<DefectResult> newList = new ArrayList<DefectResult>();
    	for ( DefectResult dr : defectResultList) {
    		newList.add( dr );
    	}
     	// add the new one to the existing set
    	newList.add( request.getNewDefectResult() );
   	
    	Product product = getProduct( request.getProductId() );
     	
    	// Since this is a new Defect, the status must be outstanding 
    	saveAllDefectResults( product,  DefectStatus.OUTSTANDING, request.getProductId(),  newList,
    			 schedule,  exceptionalOut, false, true);	
    }
 
    /**
     * Returns the product give the productId. It only looks for frames and engines.
     * @param productid
     * @return Product or null if not found 
     */
    private Product getProduct( String productid ) {
     	Product product = null;
    	
    	FrameDao frameDao = (FrameDao) ProductTypeUtil.getProductDao(ProductType.FRAME);
    	Frame frame = frameDao.findBySn(productid);
    	if ( frame != null ) {
    		product = frame;
    	} else {
        	EngineDao engineDao = (EngineDao) ProductTypeUtil.getProductDao(ProductType.ENGINE);
        	Engine engine = engineDao.findBySn(productid);
        	if ( engine != null ) {
        		product = engine;
        	} 
    	}
    	return product;
    }
    
    @Transactional
	public void addNewDefectRepairResult( AddNewDefectRepairResultRequest request ) {
		Timestamp currentTimestamp = getDatabaseTimeStamp();
    	// save the repair result
    	DefectRepairResult defectRepairResult = request.getNewDefectRepairResult();
    	defectRepairResult.setActualTimestamp(currentTimestamp);
    	defectRepairResultDao.save( defectRepairResult );

    	ExceptionalOut exceptionalOut= null;
    	DailyDepartmentSchedule schedule = null;
    	
    	// get all the existing defects, which will include repair we just added
    	List<DefectResult> defectResultList = this.findAllByProductId( request.getProductId());
    	
    	DefectResult defectResult = null;
    	// find the defect we are adding the repair result
    	for ( DefectResult dr: defectResultList ) {
    		if ( dr.getId().getDefectResultId()== request.getDefectResultId()) {
    			defectResult = dr;
    			break;
    		}
    	}
    	if ( defectResult == null ) {
    		throw new IllegalArgumentException("The specified product id is not associated with a frame or engine.");
    	}
    	
     	
    	defectResult.setDefectStatus(DefectStatus.getType(request.getDefectStatusId()));
    	defectResult.setRepairTimePlan(request.getRepairTimePlan());

    	// get the product 
    	Product product = getProduct(request.getProductId() );
    	
     	DefectStatus overallDefectStatus = selectOverallProductDefectStatus(defectResultList);
     	saveAllDefectResults( product,  overallDefectStatus, request.getProductId(),  defectResultList,
    			 schedule,  exceptionalOut, false, true);	
    }

    /**
     * This method computes the overall defect status by looking at the individual defect results. 
     * 
     * @param defectResults
     * @return DefectStatus that best represents the unit
     */
    private DefectStatus selectOverallProductDefectStatus( List<DefectResult> defectResults ) {
    	DefectStatus defectStatus = null;
    	
    	for( DefectResult defectResult : defectResults) {
    		if ( defectStatus == null || 
    				( defectResult.getDefectStatus() != null 
    						&& defectStatus.getId() > defectResult.getDefectStatus().getId())) {
    			defectStatus = defectResult.getDefectStatus();
    		}    		
    	}    	
    	return defectStatus;  	
    }
    
    @Transactional
	public StationResult saveAllDefectResults(BaseProduct product,DefectStatus defectStatus,
			String processPointId, List<DefectResult> defectResults,
			DailyDepartmentSchedule schedule, ExceptionalOut exceptionalOut, 
			boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222) {
    	
    	if(defectResults != null && !defectResults.isEmpty()){
    		
    		if(schedule != null){
    			for(DefectResult result : defectResults){
    				if(result.isNewDefect()){
    					result.setShift(schedule.getId().getShift());
    					result.setDate(schedule.getId().getProductionDate());
    				}
    			}
    		}
    		
    		saveDefectResults(defectResults, isReplicateDefectRepairResult, isCreateRepairedDefectTo222);
    	}
    	
    	// update product defect status
    	if(defectStatus != null) {
    		product.setDefectStatus( defectStatus);
    		updateProduct(product, defectResults);
    	}
    	
    	StationResult stationResult = null;
    	if (!StringUtils.isBlank(processPointId)) {
    		stationResult = saveStationResult(product,defectStatus,processPointId,defectResults,schedule);
    	}
    	
		// scrap a product
		if(DefectStatus.SCRAP.equals(defectStatus) || product.isPreheatScrapStatus()) { 
			
			if(exceptionalOut == null && !StringUtils.isBlank(processPointId)) {
				exceptionalOut = ExceptionalOut.create(product, processPointId, schedule.getId().getProductionDate());
			}
    	
			exceptionalOutDao.save(exceptionalOut);
		}
		
		return stationResult;
	}
    
    
    @Override
	@Transactional
	public StationResult saveLotControlResults(BaseProduct product,DefectStatus defectStatus,
			String processPointId, List<DefectResult> defectResults,
			DailyDepartmentSchedule schedule, ExceptionalOut exceptionalOut, 
			boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222) {
    	
    	// update product defect status
    	if(defectStatus != null) {
    		product.setDefectStatus( defectStatus);
    		updateProduct(product, defectResults);
    	}
    	
    	StationResult stationResult = null;
    	if (!StringUtils.isBlank(processPointId)) {
    		stationResult = saveStationResult(product,defectStatus,processPointId,defectResults,schedule);
    	}
    	
		// scrap a product
		if(DefectStatus.SCRAP.equals(defectStatus) || product.isPreheatScrapStatus()) { 
			
			if(exceptionalOut == null && !StringUtils.isBlank(processPointId)) {
				exceptionalOut = ExceptionalOut.create(product, processPointId, schedule.getId().getProductionDate());
			}
    	
			exceptionalOutDao.save(exceptionalOut);
		}
		
		return stationResult;
	}
    
    
    public void saveDefectResults(List<DefectResult> defectResults, boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222) {
    	
    	if(defectResults == null || defectResults.isEmpty()) return;
    	
    	Timestamp currentTimestamp = getDatabaseTimeStamp();
		
		for(DefectResult defectResult : defectResults) {
			if(defectResult.isNewDefect()) {
				if (!isReplicateDefectRepairResult){
					defectResult.setActualTimestamp(currentTimestamp);
				}
			} else if(defectResult.isRepairedStatus())
				defectResult.setRepairTimestamp(currentTimestamp);
		}
		
		if(!defectResults.isEmpty()) {
			Integer maxResultId = max("id.defectResultId", Integer.class);
			if (maxResultId == null) {
				maxResultId = 0;
			}
			Integer maxRepairId = defectRepairResultDao.findMaxRepairId();
			if (maxRepairId == null) {
				maxRepairId = 0;
			}
			
			if (isReplicateDefectRepairResult) {
				int initReplicateDefectResultId = PropertyService.getPropertyBean(QiPropertyBean.class).getInitReplicateDefectResultId();
				if (maxResultId < initReplicateDefectResultId - 1) {
					maxResultId = initReplicateDefectResultId - 1;
				}
				int initReplicateRepairId = PropertyService.getPropertyBean(QiPropertyBean.class).getInitReplicateRepairId();
				if (maxRepairId < initReplicateRepairId - 1) {
					maxRepairId = initReplicateRepairId - 1;
				}
			}
			for(DefectResult defectResult : defectResults) {
				if (defectResult.isNewDefect())
				{
					defectResult.getId().setDefectResultId(++maxResultId);
				}
				
				if(defectResult.isRepairedStatus() && isCreateRepairedDefectTo222) {
					if (defectResult.isNewDefect())
					{
						defectResult.getDefectRepairResult().getId().setDefectResultId(maxResultId);
					}else
					{
						defectResult.getDefectRepairResult().getId().setDefectResultId(defectResult.getDefectResultId());
					}
					defectResult.getDefectRepairResult().getId().setRepairId(++maxRepairId);
				}

			}
		}
		
		saveAll(defectResults);
	}
    
    /*
	 * Remark: currenlty this method is used to create one defect per each
	 * product, if there is need to submit batch of different numbers of defects
	 * per each product, then batch of defects should be split by product and
	 * then each batch of defects processed per product.
	 */
	@Transactional
	public void saveInlineDefects(ProductType productType, List<DefectResult> defectResults) {
		if (defectResults == null || defectResults.isEmpty()) {
			return;
		}

		for (DefectResult defectResult : defectResults) {
			if (defectResult.isNewDefect()) {
				Integer nextResultId = max("id.defectResultId", Integer.class);
				nextResultId = nextResultId == null ? 1 : nextResultId + 1;
				defectResult.getId().setDefectResultId(nextResultId);
				if (defectResult.isRepairedStatus()) {
					defectResult.getDefectRepairResult().getId().setDefectResultId(nextResultId);
				}
			}
			save(defectResult);
			ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
			BaseProduct product = productDao.findByKey(defectResult.getId().getProductId());
			if (defectResult.isEngineFiring()) {
				if (productDao instanceof HeadDao) {
					((HeadDao) productDao).updateEngineFiringFlag(defectResult.getId().getProductId(), (short) 1);
				} else if (productDao instanceof EngineDao) {
					((EngineDao) productDao).updateEngineFiringFlag(defectResult.getId().getProductId(), (short) 1);
				}
			}
			if (product.getDefectStatusValue() != null && product.getDefectStatusValue().shortValue() == defectResult.getDefectStatusValue()) {
				continue;
			}
			DefectStatus ds = product.getDefectStatus();
			if (ds == null || (!DefectStatus.SCRAP.equals(ds) && !DefectStatus.PREHEAT_SCRAP.equals(ds) && !DefectStatus.OUTSTANDING.equals(ds))) {
				productDao.updateDefectStatus(defectResult.getId().getProductId(), DefectStatus.OUTSTANDING);
			} 			
		}
	}
    
    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<DefectResult> findAllDefectsByProductId(String productId) {
		return findAllByQuery(FIND_ALL_DEFECT_BY_PRODUCT_ID, Parameters.with("productId", productId));
	}
    
    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<DefectResult> findAllOutstandingByProductId(String productId) {
		return findAllByNativeQuery(FIND_OUTSTANDING_BY_PRODUCT_ID, Parameters.with("1", productId));
	}
    
    @Transactional
    public List<DefectResult> findDepartmentDefects(String department, String processPointId, int vinTotal, int dayTotal)
    {
		Parameters parameters = Parameters.with("1", department.trim());
		parameters.put("2", processPointId.trim());
		String FIND_DEPARTMENT_DEFECTS = "with dates as " +
		"(select production_date, rownumber() over (order by production_date desc) as rownum from gal226tbx schedule where PRODUCTION_DATE between current_date - 1 month and current_date and iswork = 'Y' group by production_date order by production_date desc) " +
		"select * from gal125tbx where product_id in (select distinct history.product_id from dates " +
		"left join gal215tbx history on history.ACTUAL_TIMESTAMP >= dates.PRODUCTION_DATE " +
		"where dates.ROWNUM = 3 and history.PROCESS_POINT_ID = ?2 " +
		"fetch first 1000 rows only) and responsible_dept = ?1";
		
		//setTopDefectsByDepartment(department, processPointId, vinTotal, dayTotal, 10);
		return findAllByNativeQuery(FIND_DEPARTMENT_DEFECTS, parameters);
    }

	@Transactional 
	public int deleteAllByProductionLot(ProductType productType,
			String productionLot) {
		if(ProductType.ENGINE == productType) 
			return executeUpdate(DELETE_BY_PRODUCTION_LOT_ENGINE, Parameters.with("productionLot", productionLot));
		else if(ProductType.FRAME == productType)
			return executeUpdate(DELETE_BY_PRODUCTION_LOT_FRAME, Parameters.with("productionLot", productionLot));
		else return 0;
	}
	
    
   
    
    @Transactional
    public  int updateGdpDefects(Parameters params){
    	
    	return executeNativeUpdate(UPDATE_GDP_DEFECT, params);
    	
    }
	
    @Transactional
	public int updateGdpDefectsVQWriteUpDept(Parameters params)
    {
    	return executeNativeUpdate(UPDATE_GDP_DEFECT_VQ_WRITE_UP_DEPT, params);
    }
    
    @Transactional
    public  int updateGdpDefectsByCurrentProcess(String productId) {
    	return executeNativeUpdate(UPDATE_GDP_DEFECT_BY_CURRENT_PROCESS, Parameters.with("1", productId));
    }    
    
    @Transactional
    private void updateDefectLabels(List<Object[]> defects, String department)
    {
    	Parameters parameters = Parameters.with("1", department.trim() + "_DEFECT_LABEL_%");
		String GET_DEFECT_LABELS = "SELECT FEATURE_ID FROM FEATURE_TBX WHERE FEATURE_ID LIKE ?1";
		List<String> labels = findAllByNativeQuery(GET_DEFECT_LABELS, parameters, String.class);
		String SET_REFERENCE_ID = "UPDATE FEATURE_TBX SET REFERENCE_ID = ?2 WHERE FEATURE_ID = ?1";
		for(int i = 0; i < labels.size(); i++)
		{
			Parameters updateParameters = Parameters.with("1", department.trim() + "_DEFECT_LABEL_" + Integer.toString(i + 1));
			updateParameters.put("2", getString(defects.get(i)[0]));
			executeNativeUpdate(SET_REFERENCE_ID, updateParameters);
			updateParameters = Parameters.with("1", department.trim() + "_DEFECT_COUNT_" + Integer.toString(i + 1));
			updateParameters.put("2", "(" + defects.get(i)[1] + ")");
			executeNativeUpdate(SET_REFERENCE_ID, updateParameters);
		}
				
    }

	@Transactional
	public void setTopDefectsByDepartment(String department,
		String processPointId, int vinTotal, int dayTotal, int howMany) {
		Parameters parameters = Parameters.with("1", department.trim());
		parameters.put("2", processPointId.trim());
		String FIND_TOP_DEFECTS = "with dates as " +
		"(select production_date, rownumber() over (order by production_date desc) as rownum from gal226tbx schedule where PRODUCTION_DATE between current_date - 1 year and current_date and iswork = 'Y' group by production_date order by production_date desc) " +
		"select distinct DEFECT_TYPE_NAME, COUNT(*) as defectcount from gal125tbx where product_id in (select distinct history.product_id from dates " +
		"left join gal215tbx history on history.ACTUAL_TIMESTAMP >= dates.PRODUCTION_DATE " +
		"where dates.ROWNUM = 7 and history.PROCESS_POINT_ID = ?2 " +
		"fetch first 1000 rows only) and responsible_dept = ?1 " +
		"group by DEFECT_TYPE_NAME " +
		"order by defectcount desc ";
		
		
		List<Object[]> defects = findAllByNativeQuery(FIND_TOP_DEFECTS, parameters, Object[].class, howMany);
		updateDefectLabels(defects, department);
	}

	public List<DefectResult> findAllDefectDetails(List<DefectResult> defectResults){
		if(defectResults == null || defectResults.isEmpty()) return null;
		List<DefectResult> newDefectResults = new ArrayList<DefectResult>();
		String FIND_DEFECT_DETAIL="SELECT e FROM DefectResult e WHERE e.id.productId = :productId and " +
		"e.id.defectTypeName= :defectTypeName and e.id.inspectionPartName= :inspectionPartName and " +
		"e.id.inspectionPartLocationName= :inspectionPartLocationName";
		for(DefectResult defectResult : defectResults) {
			Parameters parameters = Parameters.with("productId", defectResult.getId().getProductId());
			parameters.put("defectTypeName",defectResult.getDefectTypeName());
			parameters.put("inspectionPartName",defectResult.getInspectionPartName());
			parameters.put("inspectionPartLocationName",defectResult.getInspectionPartLocationName());
			
			newDefectResults.add(findFirstByQuery(FIND_DEFECT_DETAIL, parameters));
		}
		return newDefectResults;
	}

	public List<Object[]> getAllCoreMQ(Timestamp startTs, Timestamp endTs){
		//query since lastUpdate
		List<Object[]> coreMQList = null;
		String sql = SELECT_CORE_MQ;
		Parameters params = new Parameters();
		params.put("1", startTs);
		params.put("2", endTs);
		coreMQList = findAllByNativeQuery(sql, params, Object[].class);
		return coreMQList;
	}
	
	public List<DeptDefectResult> findAllRejectionCounts(java.sql.Date productionDate, String department) {
		Parameters params = Parameters.with("1", productionDate);
		params.put("2", department);
		return findAllByNativeQuery(FIND_ALL_REJECTION_COUNTS, params,DeptDefectResult.class);
	}

	public DailyDepartmentScheduleDao getDailyDepartmentScheduleDao() {
		return dailyDepartmentScheduleDao;
	}

	public void setDailyDepartmentScheduleDao(DailyDepartmentScheduleDao dailyDepartmentScheduleDao) {
		this.dailyDepartmentScheduleDao = dailyDepartmentScheduleDao;
	}
	
	public List<Object[]> findAllCoreMQDefectDataByTimestamp(Timestamp startTs, Timestamp endTs) {
		Parameters params = new Parameters();
		params.put("1", startTs);
		params.put("2", endTs);
		List<Object[]> qiCoreMQList = findAllByNativeQuery(SELECT_QI_CORE_MQ_DATA, params, Object[].class);
		return qiCoreMQList;
	}
	
	@Transactional
	public DefectResult saveDefectResultForHeadlessService(DefectResult defectResult){
			Integer maxResultId = max("id.defectResultId", Integer.class);
			if (maxResultId == null) {
				maxResultId = 0;
			}
			defectResult.getId().setDefectResultId(++maxResultId);
			return  save(defectResult);
	}
	
	  public List<DefectResult>	findAllByPartDefectCombAndProductId(QiDefectResult qiDefectResult ,String productId){
			 Parameters params = Parameters.with("1",qiDefectResult.getInspectionPartName() )
						.put("2" , qiDefectResult.getInspectionPartLocationName())
						.put("3" , qiDefectResult.getInspectionPart3Name())
						.put("4" , qiDefectResult.getInspectionPart2Name())
						.put("5" , qiDefectResult.getInspectionPart2LocationName())
						.put("6" , qiDefectResult.getDefectTypeName())
						.put("7" ,productId)
						.put("8", qiDefectResult.getDefectTypeName2());
			return findAllByNativeQuery(FIND_ALL_BY_PDC_AND_PRODUCT_ID, params);
		  }
	  
	  @Transactional 
	  public void deleteByQiDefectResultId(long qiDefectResultId) {
	        delete(Parameters.with("naqDefectResultId", qiDefectResultId));
	  }
	  
	public DefectResult findByQiRepairId(long qiRepairId) {
		Parameters params = Parameters.with("1", qiRepairId);
		return findFirstByNativeQuery(FIND_OLD_DEFECT_RESULT, params, DefectResult.class);
	}
	
	public DefectResult findByQiDefectId(long qiDefectId) {
		Parameters params = Parameters.with("qiDefectId", qiDefectId);
		return findFirstByQuery(FIND_OLD_DEFECT_RESULT_BY_QIDEFECTID, params);
	}
	
	@Transactional
	public void updateByQiDefectResultId(long qiDefectResultId, int defectStatus, java.util.Date repairTimestamp, String repairAssociateNo) {
		Parameters params = Parameters.with("1", qiDefectResultId).put("2", defectStatus).put("3", repairTimestamp).put("4", repairAssociateNo);
		if (defectStatus == DefectStatus.REPAIRED.getId()) {
			executeNativeUpdate(UPDATE_OLD_DEFECT_RESULT_REPAIRED, params);
		} else {
			executeNativeUpdate(UPDATE_OLD_DEFECT_RESULT_OUTSTANDING, params);
		}
	}
	
	@Transactional
	public void updateResponsibilityByQiDefectResultId(long qiDefectResultId, String respDept, String respLine, String respZone) {
		update(Parameters.with("responsibleDept", respDept).put("responsibleLine", respLine).put("responsibleZone", respZone), 
				Parameters.with("naqDefectResultId", qiDefectResultId));
	}
}
