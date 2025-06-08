package com.honda.galc.dao.jpa.conf;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

public class ProcessPointDaoImpl extends BaseDaoImpl<ProcessPoint,String> implements ProcessPointDao {

	private static final long serialVersionUID = 1L;
    private final String FIND_ALL_LOTCTR_ROCESS_POINT ="select p from ProcessPoint p, LotControlRule r where p.processPointId = r.id.processPointId";
    private final String FIND_ALL_LOTCTR_ROCESS_POINT_BY_DIVISION ="select p from ProcessPoint p " +
    		"where p.siteName = :siteName and p.plantName = :plantName and p.divisionId = :divisionId and p.processPointId in " +
    		"(select distinct r.id.processPointId from LotControlRule r) " +
    		"order by p.processPointId";
    private final String FIND_PREVIOUS_PROCESS_POINT = "select p2 from ProcessPoint p, ProcessPoint p2 " +
    		"where p.processPointId = :processPointId and p.lineId = p2.lineId and p2.sequenceNumber < p.sequenceNumber";

    private final String FIND_DEVICE_DRIVEN_PROCESS_POINT = "select p from ProcessPoint p, LotControlRule r, Terminal t " +
	"where p.processPointId not in (select t.locatedProcessPointId from Terminal t) and p.processPointId not in (select r.id.processPointId from LotControlRule r)";
    private final String SYSTEM_INFO = "System_Info";
    private final String PRODUCT_TYPE = "PRODUCT_TYPE";
    
    private static final String FIND_ALL_BY_APPLICATION_TYPE = "select p from ProcessPoint p, Application a where p.processPointId = a.applicationId and a.applicationTypeId in (:applicationTypeId)";
    private static final String FIND_ALL_BY_APPLICATION_TYPE_AND_DIVISION = FIND_ALL_BY_APPLICATION_TYPE + " and p.divisionId = :divisionId";    
    private static final String FIND_ALL_BY_IDS = "select pp from ProcessPoint pp where pp.processPointId in (:processPointId)";
    
    private final String FIND_ALL_BY_GROUP = "select p from ProcessPoint p, ProcessPointGroup g where p.processPointId = g.id.processPointId " + 
			" and g.id.categoryCode = :categoryCode and g.id.site = :site and g.id.processPointGroupName = :processPointGroupName";
    
    private final String FIND_TRACKING_POINTS_BY_LINE_AND_PRODUCT_TYPE = "select p from ProcessPoint p, ComponentProperty q "
    		+ " where p.processPointId = q.id.componentId and q.id.propertyKey='PRODUCT_TYPE' and q.propertyValue=:product_type " 
			+ " and p.lineId=:lineId and p.trackingPointFlag=1";
    
    private final String FIND_ALL_BY_GROUP_AND_MATCHING_TEXT = "select p from ProcessPoint p, ProcessPointGroup g where p.processPointId = g.id.processPointId " + 
			" and g.id.categoryCode = :categoryCode and g.id.site = :site and g.id.processPointGroupName = :processPointGroupName" +
			" and (upper(p.processPointId) like :text or upper(p.processPointName) like :text or upper(p.processPointDescription) like :text)";
    
    private final String FIND_ALL_BY_MATCHING_TEXT_AND_NOT_IN_GROUP = "select p from ProcessPoint p where (upper(p.processPointId) like :text" + 
			" or upper(p.processPointName) like :text or upper(p.processPointDescription) like :text) and " +
    		"p.processPointId not in (select g.id.processPointId from ProcessPointGroup g where " +
			" g.id.categoryCode = :categoryCode and g.id.site = :site and g.id.processPointGroupName = :processPointGroupName)";
    
    private static final String FIND_ALL_BY_APPLICATION_COMPONENT_DIVISION_ID = "select distinct a.* from galadm.gal214tbx a, galadm.gal241tbx b where a.process_point_id = b.application_id and b.application_id in (select component_id from galadm.gal489tbx where property_key='IS_NAQ_STATION' and property_value='TRUE') and DIVISION_ID =?1";
	private static final String FIND_ALL_BY_DB_PROPERTY = "select * from galadm.gal214tbx p where p.process_point_id IN (select component_id from galadm.gal489tbx where property_key =?1 and property_value = ?2) order by p.process_point_id asc";
    private static final String FINDFIRSTKICKOUTFORPRODUCT = "SELECT " +
			"p.* " +
			"FROM galadm.GAL147TBX h " +
			"inner join gal214tbx p on p.PROCESS_POINT_ID = h.HOLD_PROCESS_POINT " +
			"inner join GAL195TBX l on l.LINE_ID=p.LINE_ID " +
			"inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"WHERE h.PRODUCT_ID = ?1 " +
			"and h.HOLD_PROCESS_POINT IS NOT NULL " +
			"AND h.HOLD_PROCESS_POINT !='' " +
			"AND h.RELEASE_FLAG = 0 " +
			"and d.SEQUENCE_NUMBER <= " +
			"( " +
			"   SELECT " +
			"   d.SEQUENCE_NUMBER " +
			"   FROM gal214tbx p1 " +
			"   inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			"   inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"   WHERE p1.PROCESS_POINT_ID = ?2 " +
			") " +
			"and " +
			"( " +
			"   l.LINE_SEQUENCE_NUMBER <= " +
			"   ( " +
			"      SELECT " +
			"      l.LINE_SEQUENCE_NUMBER " +
			"      FROM gal214tbx p2 " +
			"      inner join GAL195TBX l on l.LINE_ID=p2.LINE_ID " +
			"      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"      WHERE p2.PROCESS_POINT_ID = ?2 " +
			"   ) " +
			"   or d.SEQUENCE_NUMBER < " +
			"   ( " +
			"      SELECT " +
			"      d.SEQUENCE_NUMBER " +
			"      FROM gal214tbx p1 " +
			"      inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			"      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"      WHERE p1.PROCESS_POINT_ID = ?2 " +
			"   ) " +
			") " +
			"and " +
			"( " +
			"   p.SEQUENCE_NUMBER <= CAST(?3 AS INTEGER) or l.LINE_SEQUENCE_NUMBER < " +
			"   ( " +
			"      SELECT " +
			"      l.LINE_SEQUENCE_NUMBER " +
			"      FROM gal214tbx p2 " +
			"      inner join GAL195TBX l on l.LINE_ID=p2.LINE_ID " +
			"      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"      WHERE p2.PROCESS_POINT_ID = ?2 " +
			"   ) " +
			"   or d.SEQUENCE_NUMBER < " +
			"   ( " +
			"      SELECT " +
			"      d.SEQUENCE_NUMBER " +
			"      FROM gal214tbx p1 " +
			"      inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			"      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			"      WHERE p1.PROCESS_POINT_ID = ?2 " +
			"   ) " +
			") " +
			"order by d.SEQUENCE_NUMBER, l.LINE_SEQUENCE_NUMBER, p.SEQUENCE_NUMBER ";
    
	private static final String FIND_ALL_DISTINCT_NAQ_PROCESS_POINT = "SELECT DISTINCT a.PROCESS_POINT_ID FROM GALADM.GAL214TBX a, GALADM.GAL489TBX b WHERE a.PROCESS_POINT_ID=b.COMPONENT_ID AND b.PROPERTY_KEY='IS_NAQ_STATION' AND UPPER(b.PROPERTY_VALUE)='TRUE' ORDER BY a.PROCESS_POINT_ID";    
	
	private static final String FIND_ALL_PROCESS_POINT = "SELECT PROCESS_POINT_ID FROM galadm.GAL214TBX ORDER BY PROCESS_POINT_ID";
	
	public static final String SELECT_LAST_TRACKING_STATUS = "SELECT p.LINE_ID FROM gal214tbx p inner join @PRODUCT_HISTORY_TBX@ h "
			+ "on p.PROCESS_POINT_ID = h.PROCESS_POINT_ID where h.@PRODUCT_ID@ = ?1 and  p.TRACKING_POINT_FLAG = 1 and p.Line_id not in (@EXCEPTIONAL_lINES@) "
			+ "order by h.CREATE_TIMESTAMP desc";
	
	private static final String FIND_ALL_KICKOUT_PROCESS_BY_LINE = "SELECT p.* FROM galadm.GAL214TBX p inner join galadm.gal489tbx a on a.COMPONENT_ID = p.PROCESS_POINT_ID"
			+ " where p.LINE_ID = ?1 and a.PROPERTY_KEY = 'KICKOUT_PROCESS' and upper(a.PROPERTY_VALUE) = 'TRUE'";
	
	private static final String FIND_ALL_KICKOUT_PROCESS = "SELECT p.* FROM galadm.GAL214TBX p inner join galadm.gal489tbx a on a.COMPONENT_ID = p.PROCESS_POINT_ID"
			+ " where a.PROPERTY_KEY = 'KICKOUT_PROCESS' and upper(a.PROPERTY_VALUE) = 'TRUE'";
	
	private static final String FIND_TRACKING_POINTS_BY_LINE = "select p from ProcessPoint p where p.lineId=:lineId and p.trackingPointFlag=1";

	public List<ProcessPoint> findAllOrderByProcessPointId() {
		return findAll(null, new String [] {"processPointId"});
	}

    public List<ProcessPoint> findAllByDivision(Division division) {
        return findAll(Parameters.with("siteName", division.getSiteName())
                        .put("plantName", division.getPlantName())
                        .put("divisionId", division.getDivisionId()),
                        new String [] {"processPointId"},true);
    }

     public List<ProcessPoint> findAllByLine(Line line) {
        return findAll(Parameters.with("siteName", line.getSiteName())
                        .put("plantName", line.getPlantName())
                        .put("divisionId", line.getDivisionId())
                        .put("lineId",line.getLineId()));
    } 	

	public List<ProcessPoint> findAllKickoutPocessByLine(Line line) {
		if(line != null) {
			return findAllByNativeQuery(FIND_ALL_KICKOUT_PROCESS_BY_LINE,Parameters.with("1", line.getLineId()));
		} else {
			return findAllByNativeQuery(FIND_ALL_KICKOUT_PROCESS, new Parameters());
		}
	}
     
     public List<ProcessPoint> findAllByProcessPointType(ProcessPointType type) {
         return findAll(Parameters.with("processPointTypeId", type.getId()));
     }

 	public List<ProcessPoint> findAllByApplicationType(List<ApplicationType> types) {
 		if (types == null || types.isEmpty()) {
 			return new ArrayList<ProcessPoint>();
 		}
 		List<Integer> ids =  ApplicationType.getApplicationTypeIds(types); 
		Parameters params = Parameters.with("applicationTypeId", ids);
		return findAllByQuery(FIND_ALL_BY_APPLICATION_TYPE, params);
	}	
	
	public List<ProcessPoint> findAllByApplicationType(List<ApplicationType> types, String divisionId) {
 		if (StringUtils.isBlank(divisionId) || types == null || types.isEmpty()) {
 			return new ArrayList<ProcessPoint>();
 		}
		List<Integer> ids =  ApplicationType.getApplicationTypeIds(types); 		
		Parameters params = Parameters.with("applicationTypeId", ids);
		params.put("divisionId", divisionId);
		return findAllByQuery(FIND_ALL_BY_APPLICATION_TYPE_AND_DIVISION, params);
	}
     
     public int countByLine(Line line) {
		return (int)count(Parameters.with("siteName", line.getSiteName())
                        .put("plantName", line.getPlantName())
                        .put("divisionId", line.getDivisionId())
                        .put("lineId",line.getLineId()));
	}

	public List<ProcessPoint> findAllLotControlProcessPoint() {
		
		return findAllByQuery(FIND_ALL_LOTCTR_ROCESS_POINT);
	}

	public List<ProcessPoint> findAllLotControlProcessPointByDivision(Division division) {

		return findAllByQuery(FIND_ALL_LOTCTR_ROCESS_POINT_BY_DIVISION, 
				Parameters.with("siteName", division.getSiteName())
				.put("plantName", division.getPlantName())
				.put("divisionId", division.getDivisionId()));
	}
	
	public List<ProcessPoint> findAllByIds(List<String> processPointIds) {
		Parameters params = new Parameters();
		params.put("processPointId", processPointIds);
		return findAllByQuery(FIND_ALL_BY_IDS, params);
	}
	
	public ProcessPoint findPreviousProcessPointByProcessPointId(String processPointId){
		return findFirstByQuery(FIND_PREVIOUS_PROCESS_POINT, Parameters.with("processPointId", processPointId));
	}
	
	public ProcessPoint findById( String processPointId ) {
		return findByKey( processPointId );
	}
	
	@Override
	public List<ProcessPoint> findAllTrackingPointsByLineAndProductType(String productType, String lineId) {
		if(StringUtils.isBlank(productType) || StringUtils.isBlank(lineId))  return null;
       	Parameters params = Parameters.with("product_type", productType).put("lineId", lineId);
		return findAllByQuery(FIND_TRACKING_POINTS_BY_LINE_AND_PRODUCT_TYPE, params);
	}
	
	public Map<String, String> findAllProcessPtProductTypeMapping(){
		String ProdType = null;
		String defaultProductType = null;
		Map<String, String> processPtToProdType = new HashMap<String, String>();
		ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		
		defaultProductType = componentPropertyDao.findValueForCompIdAndKey(SYSTEM_INFO, PRODUCT_TYPE);
		
		List<ProcessPoint> processPointLst = findAll();
		
		for(ProcessPoint processPt : processPointLst){
			ProdType =  componentPropertyDao.findValueForCompIdAndKey(processPt.getProcessPointId(), PRODUCT_TYPE);
			if(ProdType != null)
				processPtToProdType.put(processPt.getProcessPointId(), ProdType);
			else
				processPtToProdType.put(processPt.getProcessPointId(), defaultProductType);
			
			ProdType = null;
		}
		
		return processPtToProdType;
	}
	
	/*
	 * For example Input string as :: YAF4FR1P0110,YAF4FR1P0100,YAF4FR1P0090
	 * */
	public List<ProcessPoint> getProcessPointLst(List<String> processPointList) {
		List<ProcessPoint> processPointLst = new ArrayList<ProcessPoint>();
		if(processPointList == null) return processPointLst;
		for(String processPointStr : processPointList){
			processPointLst.add(findById(processPointStr));
		}
		return processPointLst;
	}

	public List<ProcessPoint> findDeviceDrivenHeadlessProcessPoints() {
		return findAllByQuery(FIND_DEVICE_DRIVEN_PROCESS_POINT);
	}

	/**
	 * This method is used to find all Qics Station(Process Point) based on ApplicationId,ComponentId and DivisionId.
	 * @param divisionId.
	 * @return List of Qics station(Process Point).
	 * **/
	public List<ProcessPoint> findAllByApplicationComponentDivision(String divisionId) {
		Parameters params = Parameters.with("1", divisionId);
		return findAllByNativeQuery(FIND_ALL_BY_APPLICATION_COMPONENT_DIVISION_ID, params);
	}
	
	public ProcessPoint findFirstKickoutProcessPointForProduct(
			String productId, String processPointid, int currentProcessSeq) {

		Parameters params = new Parameters();
		params.put("1", productId);
		params.put("2", processPointid);
		params.put("3", currentProcessSeq);

		return findFirstByNativeQuery(FINDFIRSTKICKOUTFORPRODUCT, params);
	}
	
	public List<ProcessPoint> findAllByDivisionId(String divisionId){
		return findAll(Parameters.with("divisionId", divisionId));
	}
	
	public List<ProcessPoint> findAllByDbProperty(String propertyKey, String propertyValue) {
		Parameters params = new Parameters();
		params.put("1", propertyKey);
		params.put("2", propertyValue);
		return findAllByNativeQuery(FIND_ALL_BY_DB_PROPERTY, params);
	}
		
	public List<String> findAllNAQProcessPointId()
	{
		return findAllByNativeQuery(FIND_ALL_DISTINCT_NAQ_PROCESS_POINT,null,String.class);
	}
	
	public List<ProcessPoint> findAllByGroup(RegionalProcessPointGroup group) {
       	Parameters params = Parameters.with("categoryCode", group.getId().getCategoryCode())
				.put("site", group.getId().getSite())
				.put("processPointGroupName", group.getId().getProcessPointGroupName());
        return findAllByQuery(FIND_ALL_BY_GROUP, params);
    }

    public List<ProcessPoint> findAllByGroupAndMatchingText(RegionalProcessPointGroup group, String searchText) {
       	Parameters params = Parameters.with("categoryCode", group.getId().getCategoryCode())
				.put("site", group.getId().getSite())
				.put("processPointGroupName", group.getId().getProcessPointGroupName())
       			.put("text", "%" + searchText.toUpperCase() + "%");
        return findAllByQuery(FIND_ALL_BY_GROUP_AND_MATCHING_TEXT, params);
    }

    public List<ProcessPoint> findAllByMatchingTextAndNotInGroup(String searchText, RegionalProcessPointGroup group) {
       	Parameters params = Parameters.with("text", "%" + searchText.toUpperCase() + "%")
       			.put("categoryCode", group.getId().getCategoryCode())
				.put("site", group.getId().getSite())
				.put("processPointGroupName", group.getId().getProcessPointGroupName());
        return findAllByQuery(FIND_ALL_BY_MATCHING_TEXT_AND_NOT_IN_GROUP, params);
    }

	@Override
	public List<String> findAllProcessPoint() {
		return findAllByNativeQuery(FIND_ALL_PROCESS_POINT, null, String.class);
	}
	
	public String findLastTrackingStatus(String productId, String exceptionLineIds, String productType) {
		Parameters params = Parameters.with("1",productId);
		
		ProductTypeUtil productTypeUtil = ProductTypeUtil.getTypeUtil(productType);
		String productIdColumn = CommonUtil.getIdColumnName(productTypeUtil.getProductClass());
		String productHistoryTable = CommonUtil.getTableName(productTypeUtil.getProductHistoryClass());
		
		String sql = null;
		sql=SELECT_LAST_TRACKING_STATUS.replace("@PRODUCT_HISTORY_TBX@", productHistoryTable).replace("@PRODUCT_ID@", productIdColumn).replace("@EXCEPTIONAL_lINES@", exceptionLineIds);
		
		return findFirstByNativeQuery(sql, params, String.class);
	}
  
  	@Override
	public List<ProcessPoint> findAllByDivisionIdandByType(String divisionId, ProcessPointType type) {
		  return findAll(Parameters.with("processPointTypeId", type.getId()).put("divisionId", divisionId));
	}
  	
	@Override
	public List<ProcessPoint> findTrackingPointsByLine(String lineId) {
       	Parameters params = Parameters.with("lineId", lineId);
		return findAllByQuery(FIND_TRACKING_POINTS_BY_LINE, params);
	}
}
