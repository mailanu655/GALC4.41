package com.honda.galc.service.letservice;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.LetDiagDetailDao;
import com.honda.galc.dao.product.LetDiagFaultDetailDao;
import com.honda.galc.dao.product.LetDiagNameDao;
import com.honda.galc.dao.product.LetDiagResultDao;
import com.honda.galc.dao.product.LetInspectionParamDao;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.dao.product.LetProgramResultValueDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.dao.product.PhysicalTableMapDao;
import com.honda.galc.entity.enumtype.LetProgramResultEnum;
import com.honda.galc.entity.enumtype.LetProgramResultValueEnum;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.LetDiagDetail;
import com.honda.galc.entity.product.LetDiagFaultDetail;
import com.honda.galc.entity.product.LetDiagName;
import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.entity.product.LetInspectionParam;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.entity.product.PhysicalTableMap;
import com.honda.galc.entity.product.PhysicalTableMapId;
import com.honda.galc.let.message.RestLetMessageHandler;
import com.honda.galc.service.LetXmlService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

public class LetXmlServiceImpl implements LetXmlService {

	private static Map<String, Integer> letInspectionProgramMap = null;
	private static Map<String, Integer> letInspectionParamMap = null;
	private final String INSPECTION_PARAM_ID = "INSPECTION_PARAM_ID";
	private final String INSPECTION_PGM_ID = "INSPECTION_PGM_ID";
	private final String RESULT_ENTITIES = "RESULT_ENTITIES";
	private final String DIAG_RESULT_ENTITIES = "DIAG_RESULT_ENTITIES";
	private final String DIAG_DETAIL_ENTITIES = "DIAG_DETAIL_ENTITIES"; 
	private final String DIAG_FAULT_DETAIL_ENTITIES = "DIAG_FAULT_DETAIL_ENTITIES"; 
	private final String DIAG_NAME_ENTITIES = "DIAG_NAME_ENTITIES"; 
	private final String PRODUCT_ID = "PRODUCT_ID";
	private final String PROGRAM_RESULT_ENTITIES = "PROGRAM_RESULT_ENTITIES";
	private final String PROGRAM_RESULT_VALUE_ENTITIES = "PROGRAM_RESULT_VALUE_ENTITIES";
	private static final String LOGGER_ID = "LetXmlService";
	
	private LetXmlServiceImpl() {}
	
	public void loadLetInspectionParam() {
		if(letInspectionParamMap == null) {
			letInspectionParamMap = ServiceFactory.getDao(LetInspectionParamDao.class).loadAllLetInspectionParam();
		}
	}

	public void loadLetInspectionProgram() {
		if(letInspectionProgramMap == null) {
			letInspectionProgramMap = ServiceFactory.getDao(LetInspectionProgramDao.class).loadLetInspectionProgram();
		}
	}

	public void reloadLetInspectionParam() {
		letInspectionParamMap = null;
		loadLetInspectionParam();
	}

	public void reloadLetInspectionProgram() {
		letInspectionProgramMap = null;
		loadLetInspectionProgram();
	}

	public Map<String, Integer> getLetInspectionParamMap() {
		if(letInspectionParamMap == null)
			loadLetInspectionParam();
		
		return letInspectionParamMap;
	}

	public Map<String, Integer> getLetInspectionProgramMap() {
		if(letInspectionProgramMap == null)
			loadLetInspectionProgram();
		
		return letInspectionProgramMap;
	}

	public int getLetResultCountForProduct(String productId) {
		return ServiceFactory.getDao(LetResultDao.class).getProductCount(productId);
	}

	public int getLetInspectionParamId(String letInspectionParam) {

		if(letInspectionParamMap == null) loadLetInspectionParam();

		Integer inspectionParam = letInspectionParamMap.get(letInspectionParam);

		if(inspectionParam == null){
			LetInspectionParam param = getLetParamFromDb(letInspectionParam.trim());
			if(param == null){
				insertNewParamStr(letInspectionParam.trim());
			}else{
				reloadLetInspectionParam();
			}
		}
		return letInspectionParamMap.get(letInspectionParam).intValue();
	}
	
	private boolean insertNewParamStr(String newLetInspectionParamStr){
		
		try {
			int currentMaxId = updateIdCreate(CommonUtil.getTableName(LetInspectionParam.class), INSPECTION_PARAM_ID);
			
			LetInspectionParamDao letInspectionParamDao = ServiceFactory.getDao(LetInspectionParamDao.class);
			LetInspectionParam letInspectionParam = new LetInspectionParam();
			letInspectionParam.setInspectionParamId(currentMaxId);
			letInspectionParam.setInspectionParamName(newLetInspectionParamStr);
			LetInspectionParam newLetInspectionParam = letInspectionParamDao.insert(letInspectionParam);
			getLogger().info("New Inspection Parameter Name :" +newLetInspectionParamStr+ " Id : "+ currentMaxId+ " created");
			reloadLetInspectionParam();
			return newLetInspectionParam != null;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Exception while inserting New Param String : " + newLetInspectionParamStr);
			throw new TaskException("Exception while inserting New Param String : " + newLetInspectionParamStr);
		}
	}

	private int updateIdCreate(String tableName, String columnName){
		try {
			IdCreate idCreate = ServiceFactory.getDao(IdCreateDao.class).getIdCreate(tableName, columnName);
			int currentMaxId = idCreate.getCurrentId();
			
			IdCreateDao idCreateDao = ServiceFactory.getDao(IdCreateDao.class);
			idCreate.setCurrentId(currentMaxId + 1);
			idCreateDao.update(idCreate);
			return currentMaxId;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex,"Exception while updating IdCreate for  : " + tableName + "  " + columnName);
			throw new TaskException("Exception while updating IdCreate for  : " + tableName + "  " + columnName);
		}
	}

	public int getLetInspectionPgmId(String letInspectionPgmName) {

		if(letInspectionProgramMap == null) loadLetInspectionProgram();

		Integer letInspectionProgram = null;
		letInspectionProgram = letInspectionProgramMap.get(letInspectionPgmName);
		if(letInspectionProgram == null){
			LetInspectionProgram program = getLetProgramFromDb(letInspectionPgmName.trim());
			if(program == null){			
				insertNewLetInpectionPgm(letInspectionPgmName.trim());
			}else{
				reloadLetInspectionProgram();
			}
		}
		return letInspectionProgramMap.get(letInspectionPgmName).intValue();
	}

	private boolean insertNewLetInpectionPgm(String letInspectionPgmName) {
		try {
			int currentMaxId = updateIdCreate(CommonUtil.getTableName(LetInspectionProgram.class), INSPECTION_PGM_ID);
			LetInspectionProgramDao letInspectionProgramDao = ServiceFactory.getDao(LetInspectionProgramDao.class);
			int count = letInspectionProgramDao.insertIfNotExists(currentMaxId, letInspectionPgmName);
			if (count > 0) {
			    getLogger().info("New Inspection PGM Name : " +letInspectionPgmName+ " Id : " + currentMaxId + " created.");
			} else {
			    getLogger().info("Inspection PGM Name " + letInspectionPgmName + " created by another process.");
			}
			reloadLetInspectionProgram();
			return count > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Exception while inserting New Let Inspection Program : " + letInspectionPgmName);
			throw new TaskException("Exception while inserting New Let Inspection Program : " + letInspectionPgmName);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean saveLetXmlData(HashMap<String, ArrayList<?>> letEntitiesMap) {
		try {
			ArrayList<LetResult> letResultList = (ArrayList<LetResult>) letEntitiesMap.get(RESULT_ENTITIES);
			ArrayList<LetDiagResult> letDiagResultList = (ArrayList<LetDiagResult>) letEntitiesMap.get(DIAG_RESULT_ENTITIES);
			String productId = (String) letEntitiesMap.get(PRODUCT_ID).get(0);
			
			saveLETResults(letResultList);
			saveLETDiagResults(letDiagResultList);
			saveLetDiagDetails((ArrayList<LetDiagDetail>) letEntitiesMap.get(DIAG_DETAIL_ENTITIES)); 
            saveLetDiagFaultDetails((ArrayList<LetDiagFaultDetail>) letEntitiesMap.get(DIAG_FAULT_DETAIL_ENTITIES)); 
			saveLetDiagName((ArrayList<LetDiagName>) letEntitiesMap.get(DIAG_NAME_ENTITIES));
			saveLETProgramResults((ArrayList<LetProgramResult>) letEntitiesMap.get(PROGRAM_RESULT_ENTITIES));
			saveLetProgramResultValues((ArrayList<LetProgramResultValue>) letEntitiesMap.get(PROGRAM_RESULT_VALUE_ENTITIES));
			
			Timestamp timestamp = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
			saveToPhysicalTableMap(productId, getPhysicalTableName(timestamp, LetProgramResult.class));
			saveToPhysicalTableMap(productId, getPhysicalTableName(timestamp, LetProgramResultValue.class));
		
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Exception while inserting Let Data in DB");
			throw new TaskException("Exception while inserting Let Data in DB");
		}
	}

	@Transactional
	private void saveLETResults(List<LetResult> letResults) {
	    if (letResults == null || letResults.isEmpty()) {
	        return;
	    }
		long startTime = System.currentTimeMillis();
		
		LetResultDao letResultDao = (LetResultDao) ServiceFactory.getDao(LetResultDao.class);
		letResultDao.saveAll(letResults);
		
		getLogger().info("Saving LETResults took " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	@Transactional
	private void saveLetDiagDetails(List<LetDiagDetail> letDiagDetail) {

		if (letDiagDetail != null && !letDiagDetail.isEmpty()) {
			long startTime = System.currentTimeMillis();
			LetDiagDetailDao letDiagDetailDao = ServiceFactory.getDao(LetDiagDetailDao.class);
			letDiagDetailDao.saveAll(letDiagDetail);
			getLogger().info("Saving LETDiagDetail took " + (System.currentTimeMillis() - startTime) + " milliseconds");
		}
	}

	@Transactional
	private void saveLetDiagFaultDetails(List<LetDiagFaultDetail> letDiagFaultDetail) {

		if (letDiagFaultDetail != null && !letDiagFaultDetail.isEmpty()) {
			long startTime = System.currentTimeMillis();
			LetDiagFaultDetailDao letDiagFaultDetailDao = ServiceFactory.getDao(LetDiagFaultDetailDao.class);
			letDiagFaultDetailDao.saveAll(letDiagFaultDetail);
			getLogger().info("Saving LETDiagFaultDetail took " + (System.currentTimeMillis() - startTime) + " milliseconds");
		}
	}

	@Transactional
	private void saveLetDiagName(List<LetDiagName> letDiagName) {

		if (letDiagName != null && !letDiagName.isEmpty()) {
			long startTime = System.currentTimeMillis();
			LetDiagNameDao letDiagNameDao = ServiceFactory.getDao(LetDiagNameDao.class);
			letDiagNameDao.saveAll(letDiagName);
			getLogger().info("Saving LETDiagName took " + (System.currentTimeMillis() - startTime) + " milliseconds");
		}
	}
	
	@Transactional
	private void saveLETProgramResults(List<LetProgramResult> letProgramResults) {
	    if (letProgramResults == null || letProgramResults.isEmpty()) {
	        return;
	    }
		long startTime = System.currentTimeMillis();
		
		LetProgramResultDao letProgramResultDao = ServiceFactory.getDao(LetProgramResultDao.class);
		letProgramResultDao.saveAll(letProgramResults);
		
		getLogger().info("Saving LETProgramResults took " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	@Transactional
	private void saveLETDiagResults(List<LetDiagResult> letDiagResults) {
	    if (letDiagResults == null || letDiagResults.isEmpty()) {
	        return;
	    }
		long startTime = System.currentTimeMillis();
		LetDiagResultDao letDiagResultDao = ServiceFactory.getDao(LetDiagResultDao.class);
		letDiagResultDao.saveAll(letDiagResults);
		
		getLogger().info("Saving LETDiagResults took " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	@Transactional
    public void saveLetProgramResultValues(List<LetProgramResultValue> letProgramResultValues) {
        if (letProgramResultValues == null || letProgramResultValues.isEmpty()) {
            return;
        }

		long startTime = System.currentTimeMillis();
		
		LetProgramResultValueDao letProgramResultValueDao = ServiceFactory.getDao(LetProgramResultValueDao.class);
		letProgramResultValueDao.saveAll(letProgramResultValues);
		
        getLogger().info("saveLetProgramResultValues() took " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
	
    @Transactional
    public void insertLetProgramResultValues(List<LetProgramResultValue> letProgramResultValues) {
        if (letProgramResultValues == null || letProgramResultValues.isEmpty()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        LetProgramResultValueDao letProgramResultValueDao = ServiceFactory.getDao(LetProgramResultValueDao.class);
        letProgramResultValueDao.insertAll(letProgramResultValues);
        getLogger().info("insertLetrogramResultValues() took " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	@Transactional
	private void saveToPhysicalTableMap(String productId, String physicalTableName) {
		PhysicalTableMapId id = new PhysicalTableMapId(productId, physicalTableName.split("_")[0]);
		PhysicalTableMap physicalTableMap = new PhysicalTableMap(id);
		physicalTableMap.setPhysicalTableName(physicalTableName);
		ServiceFactory.getDao(PhysicalTableMapDao.class).save(physicalTableMap);
	}
	
	public String getPhysicalTableName(Timestamp timestamp, Class<?> clazz) {
		if (clazz.equals(LetProgramResult.class))
			return getPhysicalTableName(timestamp, LetProgramResultEnum.class.getEnumConstants());
		else if (clazz.equals(LetProgramResultValue.class))
			return getPhysicalTableName(timestamp, LetProgramResultValueEnum.class.getEnumConstants());
		else
			return "";
	}
	
	public String getPhysicalTableName(Timestamp timestamp, Enum<?>[] physicalTblNames) {
		int resultPresDays = 45;

		Calendar epochDate = Calendar.getInstance();
		epochDate.clear();
		epochDate.set(1970, 0, 1);
		long epoch = epochDate.getTime().getTime();

		long diff = timestamp.getTime() - epoch;
		long julian = diff / (24L * 60L * 60L * 1000L);

		int daysPerTable = resultPresDays / (physicalTblNames.length - 1);
		int daysPerCycle = daysPerTable * physicalTblNames.length;

		int offset = (int) (julian % daysPerCycle) / daysPerTable;
		return physicalTblNames[offset].toString();
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
	
	
	private LetInspectionProgram getLetProgramFromDb(String programName) {
		LetInspectionProgram program = null;
		try{
			program = ServiceFactory.getDao(LetInspectionProgramDao.class).findPgmIdByName(programName);
		}catch(Exception e) {
			getLogger().error(e);
			e.printStackTrace();
		}
		return program;
	}

	private LetInspectionParam getLetParamFromDb(String paramName) {
		LetInspectionParam param = null;
		try{
			param = ServiceFactory.getDao(LetInspectionParamDao.class).findParamIdByName(paramName);
		}catch(Exception e) {
			getLogger().error(e);
			e.printStackTrace();
		} 
		return param;
	}
	
    public void processLetMessage(LetSpool letSpool, String letDeviceIpAddress, String message) {  
        RestLetMessageHandler handler = new RestLetMessageHandler(letSpool, letDeviceIpAddress, message);
        handler.handleLetMessage();
    }
}
