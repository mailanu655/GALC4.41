package com.honda.galc.dao.jpa.lcvinbom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.SystemRelationshipDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.lcvinbom.VinPartFilterDto;
import com.honda.galc.entity.lcvinbom.VinPart;
import com.honda.galc.entity.lcvinbom.VinPartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class VinPartDaoImpl extends BaseDaoImpl<VinPart, VinPartId> 
	implements VinPartDao {
	
	private final Logger logger = Logger.getLogger("VinPartDaoImpl");

	private static final String FIND_PART_NUMBERS_BY_PRODUCTID_AND_SYSTEM = "SELECT DISTINCT DC_PART_NUMBER FROM LCVINBOM.VIN_PART WHERE PRODUCT_ID = ?1 AND LET_SYSTEM_NAME = ?2";
	private static final String FIND_BY_LET_DETAILS = "select p from VinPart p where "
			+ "p.id.productId = :productId "
			+ "and p.id.letSystemName = :letSysName ";
	private static final String FIND_BY_PRODUCTION_LOT = "select p from VinPart p where p.id.productId in (select f.productId from Frame f where f.productionLot = :productionLot )";
	private static final String FIND_BY_CRITERIA = "select p from VinPart p ";
	private static final String FIND_DISTINCT_VIN_PART_IDS = "select distinct p.let_system_name, p.dc_part_number, f.production_lot from lcvinbom.vin_part p join gal143tbx f on p.product_id = f.product_id ";
	
	private static final String DELETE_VIN_PARTS = "delete from lcvinbom.vin_part "	
			+ "where product_id in (select product_id from GALADM.gal143tbx where substr(product_spec_code,1,7) = ?2 and tracking_status not in (@TRACKING_STATUS@)) and let_system_name = ?3";
	
	private static final String INSERT_VIN_PART = "insert into lcvinbom.vin_part select distinct product_id, ?3, ?1, 0, CURRENT TIMESTAMP, null from lcvinbom.vin_part "	
			+ "where product_id in (select product_id from GALADM.gal143tbx where substr(product_spec_code,1,7) = ?2 and tracking_status not in (@TRACKING_STATUS@))";
	
	@Override
	public List<String> getPartNumbersByProductIdAndSystemName(String productId, String systemName) {
		Parameters parameters = Parameters.with("1", productId);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_PART_NUMBERS_BY_PRODUCTID_AND_SYSTEM, parameters, String.class);
	}
	
	private static final String FIND_BY_PART_NUMBERS_BY_YMT_CODE_BY_SYSTEM = "WITH CTE AS (" + 
			"SELECT vinpart.*, ROW_NUMBER() OVER (PARTITION BY vinpart.PRODUCT_ID) AS rn " + 
			"FROM lcvinbom.vin_part as vinpart " + 
			"WHERE vinpart.PRODUCT_ID IN (" + 
			"SELECT frame.PRODUCT_ID " + 
			"FROM GALADM.gal143tbx as frame " + 
			"WHERE SUBSTR(frame.PRODUCT_SPEC_CODE, 1, 7) = @prodSpecCode@" + 
			")" + 
			")" + 
			"SELECT * " + 
			"FROM CTE " + 
			"WHERE rn = 1";
	
	private static final String FIND_DISTINCT_VINPART_LET_SYSTEM_NAMES = "select distinct LET_SYSTEM_NAME from lcvinbom.VIN_PART where PRODUCT_ID = ?1";

	@Override
	public List<String> getDistinctVinPartSystemNames(String productId) {
		Parameters parameters = Parameters.with("1", productId);
		return findAllByNativeQuery(FIND_DISTINCT_VINPART_LET_SYSTEM_NAMES, parameters, String.class);
	}
	
	@Autowired
	protected SystemRelationshipDao systemRelationshipDao;
	
	@Autowired
	protected VinBomPartDao vinBomPartDao;
	
	/**
	 * The LET device will send the VIN and optionally a system name.
	 * Example: /getPartNumber?productId=5J8TC2H68ML022679&systemName=ADS_PROD
	 */
	@Override
	public List<VinPartId> getPartNumber(String productId) {
		return getPartNumber(productId, null);
	}
	
	@Override
	public List<VinPartId> getPartNumber(String productId, String systemName) {
		Parameters parameters = Parameters.with("id.productId", productId);
		if(StringUtils.isNotBlank(systemName)) {
			parameters.put("id.letSystemName",systemName);
		}
		List<VinPartId> VinPartIdList = new ArrayList<VinPartId>();
		List<VinPart> vinPartList = findAll(parameters);
		if(vinPartList!=null && !vinPartList.isEmpty()) {
			for(VinPart vinPart: vinPartList) {
				VinPartIdList.add(vinPart.getId());
			}
		}
		
		return VinPartIdList;
	}
	
	/**
	 * LET JSON Input example:
	 * {
     * 	"productId": "5J8TC2H68ML022679",
     * 	"EPS_SOFT": "39110TX5 A1**",
     *	"ABSVSA_PROD": "57110TX5 K010"
	 * }
	 */
	@Override
	@Transactional
	public void putFlashResults(DataContainer letVinPartDataContainer) {
		HashMap<VinPart, Boolean> vinPartsMap = new HashMap<VinPart, Boolean>();
		HashMap<VinPart, Boolean> vinPartsNotInDBMap = new HashMap<VinPart, Boolean>();
		
		if(letVinPartDataContainer!=null) {
			String productId = (String)letVinPartDataContainer.get("PRODUCTID");//upper case
			if(StringUtils.isNotBlank(productId)) {
				letVinPartDataContainer.remove("PRODUCTID");
				
	            // Get the unique system names from the VIN_PART table
	            List<String> uniqueSystemNames = getDistinctVinPartSystemNames(productId);
	            
				//looping through all system names
				for(Object systemName:  letVinPartDataContainer.keySet()) {
					String systemNameKey = (String) systemName;
					String partNumber = (String)letVinPartDataContainer.get(systemName);
					
	                if (uniqueSystemNames.contains(systemNameKey)) {
	                    // If the system name is already, proceed as usual
						Parameters parameters = Parameters.with("productId", productId).put("letSysName",systemNameKey);
						List<VinPart> vinPartList = findAllByQuery(FIND_BY_LET_DETAILS, parameters);
						processVinPartList(vinPartList, partNumber, systemNameKey, productId, vinPartsNotInDBMap, vinPartsMap);
	                } else {
						// If the system name is not unique, use the Relation table to find the VINBOM
						// system name
						List<String> vinbomSystemName = getVinbomSystemName(systemNameKey);
						if (vinbomSystemName != null && !vinbomSystemName.isEmpty()) {
							Parameters parameters = Parameters.with("productId", productId).put("letSysName",
									vinbomSystemName.get(0));
							List<VinPart> vinPartList = findAllByQuery(FIND_BY_LET_DETAILS, parameters);
							processVinPartList(vinPartList, partNumber,vinbomSystemName.get(0), productId, vinPartsNotInDBMap, vinPartsMap);
						}
	                }
				}
			}
		}
		
	//Save vinpart records
		saveVinParts(vinPartsMap);
		saveVinParts(vinPartsNotInDBMap);
	}
	
	private void processVinPartList(List<VinPart> vinPartList, String partNumber,String baseSystemName, String productId, HashMap<VinPart, Boolean> dbVinpartMap, HashMap<VinPart, Boolean> vinpartMap) {
		int count = 0;
		HashMap<VinPart, Boolean> tempVinpartMap = new HashMap<>();
		
		if (vinPartList != null && !vinPartList.isEmpty()) {
			boolean matchFound = false;
			for (VinPart vinPart : vinPartList) {
				// Match found! Mark as shippable.
				String partNo = vinPart.getId().getDcPartNumber();

				logger.info("calculating shipStatus for letSystemName - " + vinPart.getId().getLetSystemName() + ", letPartNumber - " + partNumber + ", vinPartNo - " + partNo);

				boolean shippingStatus = getShipStatus(partNumber, partNo);
				
				if (vinpartMap.containsKey(vinPart)) {
					boolean existingStatus = vinpartMap.get(vinPart);
					if (!existingStatus && shippingStatus) {
						vinpartMap.put(vinPart, true);
					}
				} else {
					vinpartMap.put(vinPart, shippingStatus);
				}
				
				if(shippingStatus) {
					matchFound = true;
					break;
				}
				tempVinpartMap.put(vinPart, false);
				count++;
			}
			
			//Check the iteration count
			if(!matchFound && vinPartList.size()==count) {
				//update Status
				for(Map.Entry<VinPart,Boolean> vinPartEntry: tempVinpartMap.entrySet()) {
					VinPart vinPart = vinPartEntry.getKey();
					if(vinPart.getId().getLetSystemName().equals(baseSystemName) && vinPart.getId().getProductId().equals(productId)) {					
						dbVinpartMap.put(vinPart, false);
					}
				}
			}
		}
	}
	
	private void saveVinParts(HashMap<VinPart, Boolean> vinPartsMap) {
	    for (Map.Entry<VinPart, Boolean> vinPartEntry : vinPartsMap.entrySet()) {
	        VinPart vinPart = vinPartEntry.getKey();
	        boolean shippingStatus = vinPartEntry.getValue();
	        vinPart.setShipStatus(shippingStatus);
	        save(vinPart);
	    }
	}
	
	private List<String> getVinbomSystemName(String splitSystemName) {
	    // Use the Relation table to find the VINBOM system name for the split system name
	    List<String> vinbomSystemNames = systemRelationshipDao.findVinbomSystemNames(splitSystemName);
	    return vinbomSystemNames;
	}
	
	private boolean getShipStatus(String letPartNumber, String vinPartNo) {
		// TODO Auto-generated method stub
		boolean shipStatus= false;
		int letCharIndex=0;
		if(!StringUtils.isEmpty(letPartNumber) && letPartNumber.length() >= 12) {
			//<12 characters fail
			//default to false
			
			if(letPartNumber.length() > vinPartNo.length()) {
				if(!letPartNumber.endsWith("*")) {
					logger.info(" letPartNumber - "+letPartNumber+", vinPartNo - "+vinPartNo+", Ship Status - "+shipStatus);
					return shipStatus;
				}
			}
			
			for(char c:vinPartNo.toCharArray()) {
				
				if(letCharIndex < letPartNumber.length()) {
					if(letPartNumber.charAt(letCharIndex) == ' ' ) { //if a space is detected in let part shipstatus=N
						shipStatus=false;
						logger.info(" letPartNumber - "+letPartNumber+"has space, Ship Status - "+shipStatus);
						break;
					}else if(letPartNumber.charAt(letCharIndex) != c) { 
						//if letpart and vin part do not match and vin part char is not space and let part char is not '*' then ship status = N
						
						logger.info(" letPartNumber - "+letPartNumber+"char at index - "+letCharIndex+" does not match vinpartNo char - "+c);
						if(c == ' ') {
							
							continue;
						}
						if(letPartNumber.charAt(letCharIndex) == '*') {
							letCharIndex++;
							continue;
						}
						shipStatus=false;
						break;
							
					}else {
						
						shipStatus=true;
					}
					
					letCharIndex++;
					
				}else {
					shipStatus=false;
				}
				
			}
			
			
		}
		logger.info(" letPartNumber - "+letPartNumber+", vinPartNo - "+vinPartNo+", Ship Status - "+shipStatus);
		return shipStatus;
	}

	@Override
	public List<VinPart> filterByCriteria(String productId, String productionLot, String partNumber,
			String systemName) {
	Parameters params = new Parameters();
	
	
	if(StringUtils.isNotBlank(productionLot)) {
		String sql = FIND_BY_PRODUCTION_LOT;
		params.put("productionLot", productionLot);
		if(StringUtils.isNotBlank(productId)) {
			sql= sql +" and p.id.productId like '%"+ productId+"%' ";
			
		}
		if(StringUtils.isNotBlank(systemName)) {
			sql= sql +" and p.id.letSystemName = :letSystemName  ";
			params.put("letSystemName",systemName);
		}
		if(StringUtils.isNotBlank(partNumber)) {
			sql= sql +" and p.id.dcPartNumber = :dcPartNumber ";
			params.put("dcPartNumber",partNumber);
		}
		return findAllByQuery(sql, params);
		
	}else {
		String sql= FIND_BY_CRITERIA;
		if(StringUtils.isNotBlank(productId)||StringUtils.isNotBlank(systemName)||StringUtils.isNotBlank(partNumber)) {
			sql=sql+" where ";
		}
		if(StringUtils.isNotBlank(productId)) {
			sql= sql +" p.id.productId like '%"+productId+"%' ";
		}
		if(StringUtils.isNotBlank(systemName)) {
			if(StringUtils.isNotBlank(productId)) {sql= sql +" and ";}
			sql= sql +" p.id.letSystemName = :letSystemName  ";
			params.put("id.letSystemName",systemName);
		}
		if(StringUtils.isNotBlank(partNumber)) {
			if(StringUtils.isNotBlank(productId)||StringUtils.isNotBlank(systemName)) {sql= sql +" and ";}
			sql= sql +" p.id.dcPartNumber = :dcPartNumber ";
			params.put("id.dcPartNumber",partNumber);
		}
	
		return findAllByQuery(sql,params);
	}
	
	
	}

	@Override
	public List<VinPartFilterDto> findAllDistinctVinParts() {
		
		return findAllByNativeQuery(FIND_DISTINCT_VIN_PART_IDS, null, VinPartFilterDto.class);
		
	}

	@Transactional
	@Override
	public void deleteVinParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(DELETE_VIN_PARTS.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}
	
	@Transactional
	@Override
	public void insertVinParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(INSERT_VIN_PART.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}

	@Override
	public List<VinPart> filterVinPartsBySysNamePartNumAndProdSpecCode(String partNumber, String productSpecCode, String systemName) {

		logger.info("STARTED preparing the SQL query");
		String sql = FIND_BY_PART_NUMBERS_BY_YMT_CODE_BY_SYSTEM.replace("@prodSpecCode@", "'"+productSpecCode+"'");
		
		if (partNumber != null && !partNumber.isEmpty()) {
			sql = sql + "" + "and dc_part_number = '"+ partNumber + "'";
		}

		if (systemName != null && !systemName.isEmpty()) {
			sql = sql + "" + "and let_system_name = '"+systemName+"'";
		}

		logger.info("END of preparing the SQL query: " + sql);
		return findAllByNativeQuery(sql, null, VinPart.class);
	}
}
