package com.honda.galc.oif.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCMdrsTrainingDao;
import com.honda.galc.dao.conf.MCMdrsTrainingStatusDao;
import com.honda.galc.dao.conf.MCTrainingDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.conf.MCMdrsTraining;
import com.honda.galc.entity.conf.MCMdrsTrainingId;
import com.honda.galc.entity.conf.MCMdrsTrainingStatus;
import com.honda.galc.entity.conf.MCMdrsTrainingStatusId;
import com.honda.galc.entity.conf.MCTraining;
import com.honda.galc.entity.conf.MCTrainingId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.LDAPService;

public class MfgCtrlValidateTrainingOIFJob extends OifAbstractTask implements
IEventTaskExecutable {

	Map<String, String> processPtVsProdTypeMap = null;
	Map<String, String> modelCodeYearEngineMap = null;
	Map<String, String> modelCodeYearFrameMap = null;
	private final String FRAME_PRODUCT = "FRAME";
	private final String ENGINE = "ENGINE";
	private final String ENGINE_ASSEMBLY = "Engine Assembly";
	private final String SKIP = "SKIP";
	private final String NOT_FOUND = "NOT_FOUND";
	Map<String, String> userIdNetworkIdMap = new HashMap<String, String>();
	HashSet<String> userIdNotInLdap = new HashSet<String>(); 
	private final String TRAINED = "TRAINED";
	private final String EXPIRED = "EXPIRED";
	private final String NEW_MDRS_DESIGN_FLG = "NEW_MDRS_DESIGN_FLG";

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSSSSS a");
	SimpleDateFormat executionTsf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	SimpleDateFormat pgmExeTsf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	SimpleDateFormat processingDayTsf = new SimpleDateFormat("MM/dd/yyyy");
	private final String PROD_TYPE_NOT_FOUND = "PROD_TYPE_NOT_FOUND";
	private final String USER_NOT_FOUND = "USER_NOT_FOUND";
	private final String MODEL_YEAR_NOT_FOUND = "MODEL_YEAR_NOT_FOUND";
	private final String EXCEPTION_USER = "EXCEPTION_USER";
	
	private final String EMAIL_SUBJECT = "Manufacturing Control Validate Training OIF Job : ";
	private static final String THANKS = "\n\nMessage From";
	private static final String THANKS_MESSAGE = "\nManufacturing Control Validate Training OIF Job";
	private static final String INIT_MESSAGE = "Manufacturing Control Validate Training OIF Job executed for the day :";
	
	
	public MfgCtrlValidateTrainingOIFJob(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		
		String prodNotFound = null;
		String prodNotFoundMessage = null;
		String userNotFound = null;
		String userNotFoundMessage = null;
		String modelYrNotFound = null;
		String modelYrNotFoundMessage = null;
		String exceptionUsr = null;
		String exceptionUsrMessage = null;
		String finalMessage = null;
		
		Timestamp startTs = new Timestamp(System.currentTimeMillis());
		
		OifServiceEMailHandler emailHandler = new OifServiceEMailHandler(getName());
		
		Map<String, Set<String>> responseSet = new HashMap<String, Set<String>>();
		if(getPropertyBoolean(NEW_MDRS_DESIGN_FLG, false)) {
			//New MDRS Design implementation
			Logger.getLogger().info("MfgCtrlValidateTrainingOIFJob :: New MDRS Design implementation");
			responseSet = populateTrainingDetailsFromNewMdrs();
		}
		else {
			//Old MDRS Design implementation
			Logger.getLogger().info("MfgCtrlValidateTrainingOIFJob :: Old MDRS Design implementation");
			responseSet = populateTrainingDetailsFromMdrs();
		}
		
		prodNotFound = constructMsgStr(responseSet.get(PROD_TYPE_NOT_FOUND));
		if(StringUtils.isNotBlank(prodNotFound))
			prodNotFoundMessage = "Following Process point(s) has not been configured with Product type, Please check with support team : " + prodNotFound;
		
		userNotFound = constructMsgStr(responseSet.get(USER_NOT_FOUND));
		if(StringUtils.isNotBlank(userNotFound))
			userNotFoundMessage = "Following user(s) not found in the LDAP system, We cannot process related record(s) till their entry found in LDAP : " + userNotFound;
		
		modelYrNotFound = constructMsgStr(responseSet.get(MODEL_YEAR_NOT_FOUND));
		if(StringUtils.isNotBlank(modelYrNotFound))
			modelYrNotFoundMessage = "Following Model code & Year combination not found in the system. Related records cannot be processed till we have proper record. Pleaes check with support team : " + modelYrNotFound;
		
		exceptionUsr = constructMsgStr(responseSet.get(EXCEPTION_USER));
		if(StringUtils.isNotBlank(exceptionUsr))
			exceptionUsrMessage = "Exception caught while processing following record(s), Please contact support team : " + exceptionUsr;
		
		finalMessage = INIT_MESSAGE + processingDayTsf.format(startTs);
		if(StringUtils.isNotBlank(prodNotFound) || StringUtils.isNotBlank(userNotFound) || StringUtils.isNotBlank(modelYrNotFound) || StringUtils.isNotBlank(exceptionUsr)){
			
			if(StringUtils.isNotBlank(prodNotFound))
				finalMessage = finalMessage + "\n\n" + prodNotFoundMessage;
			
			if(StringUtils.isNotBlank(userNotFound))
				finalMessage = finalMessage + "\n\n" + userNotFoundMessage;
			
			if(StringUtils.isNotBlank(modelYrNotFound))
				finalMessage = finalMessage + "\n\n" + modelYrNotFoundMessage;
			
			if(StringUtils.isNotBlank(exceptionUsr))
				finalMessage = finalMessage + "\n\n" + exceptionUsrMessage;
		}else{
			finalMessage =finalMessage + "\n\n" + "Job executed successfully";
		}
		

		emailHandler.delivery(EMAIL_SUBJECT + processingDayTsf.format(startTs),finalMessage+THANKS+THANKS_MESSAGE);
	}
	
	private String constructMsgStr(Set<String> msgLst){
		String message = "";
		for(String msgStr : msgLst){
			message += (message.length() == 0) ? msgStr :" , " + msgStr;
		}
		return message;
	}
	
	/*
	 * This population is as per the old design and may get discarded 
	 *  once new design gets rolled out in all plants
	 */
	public Map<String, Set<String>> populateTrainingDetailsFromMdrs(){
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Logger.getLogger().info("MfgCtrlValidateTrainingOIFJob :: populateTrainingDetailsFromMdrs() triggered @ " + pgmExeTsf.format(ts));
		Set<String> userNotFound = new HashSet<String>();
		Set<String> modelYearNotFound = new HashSet<String>();
		Set<String> prodTypeNotFound = new HashSet<String>();
		Set<String> exceptionUsr = new HashSet<String>();
		
		MCMdrsTrainingDao mcMdrsTrainingDao = ServiceFactory.getDao(MCMdrsTrainingDao.class);
		MCTrainingDao mcTraingingDao = ServiceFactory.getDao(MCTrainingDao.class);
		
		Map<String, Set<String>> responseMap = new HashMap<String, Set<String>>();
		
		List<Object[]> mappedUserLst = mcMdrsTrainingDao.getUnmappedUsersFromMDRS();
		Logger.getLogger().info("There are " + mappedUserLst.size() + " record(s) to process for the day : " + processingDayTsf.format(ts));
		
		String prodType = null;
		String modelYearCode = null;
		String userNetworkId = null;
		
		for(Object[] unmappedUsr : mappedUserLst){
			
			prodType = getProductTypeForProcessPoint(unmappedUsr[0].toString().trim());

			modelYearCode = getModelYearCodeForProdType(prodType, unmappedUsr[5].toString().trim(), unmappedUsr[6].toString().trim());
			
			userNetworkId = getMdrsUserNetworkId(unmappedUsr[1].toString().trim());
			
			if(prodType != null){
				
				if(modelYearCode != null && !SKIP.equals(modelYearCode) && !NOT_FOUND.equals(modelYearCode)){
					
					if(userNetworkId != null){
						
						try {
							boolean doInsertMCMdrsTraining = false;
							MCTraining mcTraining = getMCTraining(
									mcTraingingDao, unmappedUsr, userNetworkId,
									prodType, modelYearCode);
							int knowledgeSts = Integer.parseInt(unmappedUsr[10]
									.toString().trim());
							
							if (knowledgeSts == 0) {
								// case 1 : Knowledge sts = 0 & has reference in MC_TRAINING_TBX
								// get the existing record from MC_TRAINING_TBX and update EXPIRED with TSTP
								if(mcTraining != null && !mcTraining.isExpired()) {
									// just update the entered TSTP
									mcTraining.setExpired((Timestamp) unmappedUsr[7]);
									mcTraining.setComments(EXPIRED);
									mcTraingingDao.save(mcTraining);
								}
								// case 2 : Knowledge sts = 0 & no reference in MC_TRAINING_TBX
								// insert in MC_MDRS_TRAINING_TBX
								doInsertMCMdrsTraining = true;

							}  else if (knowledgeSts == 1) {
								// case 3 : Knowledge sts = 1 & no reference in MC_TRAINING_TBX
								// insert new record in MC_TRAINING_TBX and MC_MDRS_TRAINING_TBX
								// Or
								// case 4 : Knowledge sts = 1 & has reference in MC_TRAINING_TBX
								// get the record from MC_TRAINING_TBX with latest TSTP, check whether it is a expired or not
								// if expired insert new record in MC_TRAINING_TBX and MC_MDRS_TRAINING.
								// if not expired, just insert into MC_MDRS_TRAINING_TBX 
								if(mcTraining == null || mcTraining.isExpired()) {
									insertMCTraining(mcTraingingDao, unmappedUsr,
											userNetworkId, prodType, modelYearCode);
								}
								// insert in MC_MDRS_TRAINING_TBX
								doInsertMCMdrsTraining = true;

							}
							if(doInsertMCMdrsTraining) {
								insertMCMdrsTraining(mcMdrsTrainingDao, unmappedUsr,
										userNetworkId, prodType, modelYearCode);
							}
							mcTraining = null;
							
						} catch (Exception e) {
							System.out.println("Exception caught");
							Logger.getLogger().error("Exception caught :: while processing  [" + unmappedUsr[1].toString().trim()+ "," + Integer.parseInt(unmappedUsr[3].toString().trim())+ "," + (Timestamp) unmappedUsr[11]+"] : "   + e.getMessage());
							exceptionUsr.add("[" + unmappedUsr[1].toString().trim()+ "," + Integer.parseInt(unmappedUsr[3].toString().trim())+ "," + (Timestamp) unmappedUsr[11]+"]");
							e.printStackTrace();
						}
						

					}else{
						/* Not able to pull the LDAP details for the MDRS User Logon Id*/

							userNotFound.add(unmappedUsr[1].toString().trim());
					}

				}else{
					/* Model Year Code has not been found in GALC */
					if(!SKIP.equals(modelYearCode))
						modelYearNotFound.add(new String("["+unmappedUsr[4].toString().trim()+"-"+unmappedUsr[5].toString().trim()+"]"));
				}
			}else{
				// Very very rare case, but need to capture
				prodTypeNotFound.add(new String("["+unmappedUsr[0].toString().trim()+"]"));
			}
			
			userNetworkId = null;
			modelYearCode = null;
			prodType = null;
		}
		
		responseMap.put(USER_NOT_FOUND, userNotFound);
		responseMap.put(MODEL_YEAR_NOT_FOUND, modelYearNotFound);
		responseMap.put(PROD_TYPE_NOT_FOUND, prodTypeNotFound);
		responseMap.put(EXCEPTION_USER, exceptionUsr);
		
		return responseMap;
	} 
	
	/*
	 * This population is as per new MDRS design. Its implementation
	 * is almost similar to old design, but maintained in different method
	 * so as to keep it separate from old one for further maintenance.
	 */
	public Map<String, Set<String>> populateTrainingDetailsFromNewMdrs(){
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Logger.getLogger().info("MfgCtrlValidateTrainingOIFJob :: newPopulateTrainingDetailsFromMdrs() triggered @ " + pgmExeTsf.format(ts));
		Set<String> userNotFound = new HashSet<String>();
		Set<String> modelYearNotFound = new HashSet<String>();
		Set<String> prodTypeNotFound = new HashSet<String>();
		Set<String> exceptionUsr = new HashSet<String>();
		
		MCMdrsTrainingStatusDao mcMdrsTrainingStatusDao = ServiceFactory.getDao(MCMdrsTrainingStatusDao.class);
		MCTrainingDao mcTraingingDao = ServiceFactory.getDao(MCTrainingDao.class);
		
		Map<String, Set<String>> responseMap = new HashMap<String, Set<String>>();
		
		List<Object[]> mappedUserLst = mcMdrsTrainingStatusDao.getUnmappedUsersFromMDRS();
		Logger.getLogger().info("There are " + mappedUserLst.size() + " record(s) to process for the day : " + processingDayTsf.format(ts));
		
		String prodType = null;
		String modelYearCode = null;
		String userNetworkId = null;
		
		for(Object[] unmappedUsr : mappedUserLst){
			
			prodType = getProductTypeForProcessPoint(unmappedUsr[0].toString().trim());

			modelYearCode = getModelYearCodeForProdType(prodType, unmappedUsr[5].toString().trim(), unmappedUsr[6].toString().trim());
			
			userNetworkId = getMdrsUserNetworkId(unmappedUsr[1].toString().trim());
			
			if(prodType != null){
				
				if(modelYearCode != null && !SKIP.equals(modelYearCode) && !NOT_FOUND.equals(modelYearCode)){
					
					if(userNetworkId != null){
						
						try {
							boolean doInsertMCMdrsTrainingStatus = false;
							MCTraining mcTraining = getMCTraining(
									mcTraingingDao, unmappedUsr, userNetworkId,
									prodType, modelYearCode);
							int statusId = Integer.parseInt(unmappedUsr[10]
									.toString().trim());
							
							if (statusId == 0) {
								// case 1 : sts = 0 & has reference in MC_TRAINING_TBX
								// get the existing record from MC_TRAINING_TBX and update EXPIRED with TSTP
								if(mcTraining != null && !mcTraining.isExpired()) {
									// just update the entered TSTP
									mcTraining.setExpired((Timestamp) unmappedUsr[7]);
									mcTraining.setComments(EXPIRED);
									mcTraingingDao.save(mcTraining);
								}
								
								// case 2 : sts = 0 & no reference in MC_TRAINING_TBX
								// insert new row in MC_MDRS_TRAINING_STATUS_TBX
								doInsertMCMdrsTrainingStatus = true;

							} else if (statusId == 1) {
								if(mcTraining == null || mcTraining.isExpired()) {
									// case 3 : sts = 1 & no reference in MC_TRAINING_TBX
									// insert new record in MC_TRAINING_TBX and MC_MDRS_TRAINING_STATUS_TBX
									// Or
									// case 4 : sts = 1 & has reference in MC_TRAINING_TBX
									// get the record from MC_TRAINING_TBX with latest TSTP, check whether it is a expired or not
									// if expired insert new record in MC_TRAINING_TBX and MC_MDRS_TRAINING
									insertMCTraining(mcTraingingDao, unmappedUsr,
											userNetworkId, prodType, modelYearCode);
								}
								// insert new record in MC_MDRS_TRAINING_STATUS_TBX 	
								doInsertMCMdrsTrainingStatus = true;
							} 
							
							//Insert record in MC_MDRS_TRAINING_STATUS_TBX if the flag is true
							if(doInsertMCMdrsTrainingStatus) {
								insertMCMdrsTrainingStatus(mcMdrsTrainingStatusDao, unmappedUsr,
										userNetworkId, prodType, modelYearCode);
							}
							
							mcTraining = null;
							
						} catch (Exception e) {
							System.out.println("Exception caught");
							Logger.getLogger().error("Exception caught :: while processing  [" + unmappedUsr[1].toString().trim()+ "," + Integer.parseInt(unmappedUsr[3].toString().trim())+ "," + (Timestamp) unmappedUsr[11]+"] : "   + e.getMessage());
							exceptionUsr.add("[" + unmappedUsr[1].toString().trim()+ "," + Integer.parseInt(unmappedUsr[3].toString().trim())+ "," + (Timestamp) unmappedUsr[11]+"]");
							e.printStackTrace();
						}
						

					}else{
						/* Not able to pull the LDAP details for the MDRS User Logon Id*/

							userNotFound.add(unmappedUsr[1].toString().trim());
					}

				}else{
					/* Model Year Code has not been found in GALC */
					if(!SKIP.equals(modelYearCode))
						modelYearNotFound.add(new String("["+unmappedUsr[4].toString().trim()+"-"+unmappedUsr[5].toString().trim()+"]"));
				}
			}else{
				// Very very rare case, but need to capture
				prodTypeNotFound.add(new String("["+unmappedUsr[0].toString().trim()+"]"));
			}
			
			userNetworkId = null;
			modelYearCode = null;
			prodType = null;
		}
		
		responseMap.put(USER_NOT_FOUND, userNotFound);
		responseMap.put(MODEL_YEAR_NOT_FOUND, modelYearNotFound);
		responseMap.put(PROD_TYPE_NOT_FOUND, prodTypeNotFound);
		responseMap.put(EXCEPTION_USER, exceptionUsr);
		
		return responseMap;
	}
	
	private void insertMCMdrsTraining(MCMdrsTrainingDao mcMdrsTrainingDao, Object[] unmappedUsr, String userNetworkId, String prodType, String modelYearCode) throws Exception{
		
		
			MCMdrsTraining mcMdrsTraining = new MCMdrsTraining();
			MCMdrsTrainingId mcMdrsTrainingId = new MCMdrsTrainingId();
			mcMdrsTrainingId.setUserLogonIdNo(unmappedUsr[1].toString().trim());
			mcMdrsTrainingId.setProcessId(Integer.parseInt(unmappedUsr[3]
					.toString().trim()));
			mcMdrsTrainingId.setModelYearDate(new BigDecimal(unmappedUsr[4]
					.toString().trim()));
			mcMdrsTrainingId.setMtcModel(unmappedUsr[5].toString());
			mcMdrsTrainingId.setExtractDate((Timestamp) unmappedUsr[11]);
			mcMdrsTrainingId.setEnteredTstp((Timestamp) unmappedUsr[7]);
			mcMdrsTrainingId.setPlantLocCode(unmappedUsr[12].toString().trim());
			mcMdrsTrainingId.setDeptCode(unmappedUsr[13].toString().trim());
			mcMdrsTraining.setKnowledgestatusNo(Integer
					.parseInt(unmappedUsr[10].toString().trim()));
			mcMdrsTraining.setProcessPointId(unmappedUsr[0].toString().trim());
			mcMdrsTraining.setAssociateNo(userNetworkId.trim());
			mcMdrsTraining.setPddaPlatformId(Integer.parseInt(unmappedUsr[2]
					.toString().trim()));
			mcMdrsTraining.setSpecCodeType(prodType.trim());
			mcMdrsTraining.setSpecCodeMask(modelYearCode.trim()
					+ unmappedUsr[5].toString().trim() + "*");
			mcMdrsTraining.setId(mcMdrsTrainingId);
			mcMdrsTrainingDao.save(mcMdrsTraining);
		
		
	}
	
	private void insertMCMdrsTrainingStatus(MCMdrsTrainingStatusDao mcMdrsTrainingStatusDao, Object[] unmappedUsr, String userNetworkId, String prodType, String modelYearCode) throws Exception{
		
		
		MCMdrsTrainingStatus mcMdrsTrainingStatus = new MCMdrsTrainingStatus();
		
		MCMdrsTrainingStatusId mcMdrsTrainingStatusId = new MCMdrsTrainingStatusId();
		
		mcMdrsTrainingStatusId.setTrainingStatusId(new BigDecimal(unmappedUsr[9]
				.toString().trim()).longValue());
		mcMdrsTrainingStatusId.setPddaPlatformId(Integer.parseInt(unmappedUsr[2]
				.toString().trim()));
		mcMdrsTrainingStatus.setId(mcMdrsTrainingStatusId);
		
		mcMdrsTrainingStatus.setProcessPointId(unmappedUsr[0].toString().trim());
		mcMdrsTrainingStatus.setAssociateNo(unmappedUsr[1].toString().trim());
		mcMdrsTrainingStatus.setSpecCodeType(prodType.trim());
		mcMdrsTrainingStatus.setSpecCodeMask(modelYearCode.trim()
				+ unmappedUsr[5].toString().trim() + "*");
		mcMdrsTrainingStatus.setTrained((Timestamp)unmappedUsr[7]);
		mcMdrsTrainingStatus.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		
		mcMdrsTrainingStatusDao.save(mcMdrsTrainingStatus);
	
	
	}
	
	private void insertMCTraining(MCTrainingDao mcTraingingDao, Object[] unmappedUsr, String userNetworkId, String prodType, String modelYearCode) throws Exception{
		
		MCTraining mcTraining = new MCTraining();
		MCTrainingId mcTrainingId = new MCTrainingId();
		
		mcTrainingId.setProcessPointId(unmappedUsr[0].toString().trim());
		mcTrainingId.setAssociateNo(userNetworkId.trim());
		mcTrainingId.setPddaPlatformId(Integer.parseInt(unmappedUsr[2].toString().trim()));
		mcTrainingId.setSpecCodeType(prodType.trim());
		mcTrainingId.setSpecCodeMask(modelYearCode.trim()+unmappedUsr[5].toString().trim()+"*");
		mcTrainingId.setTrained((Timestamp)unmappedUsr[7]);
		
		mcTraining.setTrainingMethod(unmappedUsr[8].toString().trim());
		mcTraining.setComments(TRAINED);
		mcTraining.setId(mcTrainingId);
		mcTraingingDao.save(mcTraining);
	}
	
	private MCTraining getMCTraining(MCTrainingDao mcTraingingDao, Object[] unmappedUsr, String networkId, String prodType, String modelYearCode){
		
		return mcTraingingDao.findMostRecentEntry(unmappedUsr[0].toString().trim(), networkId.trim(), Integer.parseInt(unmappedUsr[2].toString().trim()), prodType.trim(), modelYearCode.trim()+unmappedUsr[5].toString().trim()+"*");

	}
	
	private String getProductTypeForProcessPoint(String processPoint){
		
		if(processPtVsProdTypeMap == null){
			processPtVsProdTypeMap = ServiceFactory.getDao(ProcessPointDao.class).findAllProcessPtProductTypeMapping();
		}
		
		return processPtVsProdTypeMap.get(processPoint);
		
	}
	
	private String getModelYearCodeForProdType(String productType, String modelCode, String modelYearDescription){
		
		String modelYearCode = null;
		
		if(ENGINE_ASSEMBLY.equalsIgnoreCase(productType) || ENGINE.equalsIgnoreCase(productType)){
			
			if(modelCodeYearEngineMap == null)
				modelCodeYearEngineMap = (HashMap<String, String>) ServiceFactory.getDao(EngineSpecDao.class).findAllModelCodeYear();
			
			modelYearCode = modelCodeYearEngineMap.get(modelCode.trim()+"-"+modelYearDescription.trim());
			modelYearCode = ((modelYearCode != null)? modelYearCode : NOT_FOUND);
			
		}else if(FRAME_PRODUCT.equals(productType)){
			
			if(modelCodeYearFrameMap == null)
				modelCodeYearFrameMap = (HashMap<String, String>) ServiceFactory.getDao(FrameSpecDao.class).findAllModelCodeYear();
			
			modelYearCode = modelCodeYearFrameMap.get(modelCode.trim()+"-"+modelYearDescription.trim());
			modelYearCode = ((modelYearCode != null)? modelYearCode : NOT_FOUND);
		}else{
			/* other than ENGINE OR FRAME Skip for now*/
			modelYearCode = SKIP;
		}
		
		return modelYearCode;
	}

	private String getMdrsUserNetworkId(String mdrsUserLogonIdNo){
		String networkId = null;

		networkId = userIdNetworkIdMap.get(mdrsUserLogonIdNo);
		
		if(userIdNotInLdap.contains(mdrsUserLogonIdNo)) return null;
		
		if(networkId == null){
			networkId = LDAPService.getInstance().getUseridWithPrefix(mdrsUserLogonIdNo);
			if(networkId != null)
				userIdNetworkIdMap.put(mdrsUserLogonIdNo, networkId);
			else{
				userIdNotInLdap.add(mdrsUserLogonIdNo);
				Logger.getLogger().warn("LDAP doesn't have details for the mdrs user :: " + mdrsUserLogonIdNo);
			}
		}
		
		return networkId;
	}
	

}
