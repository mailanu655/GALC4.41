package com.honda.galc.oif.task;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.lcvinbom.ModelLotDao;
import com.honda.galc.dao.lcvinbom.ModelPartApprovalDao;
import com.honda.galc.dao.lcvinbom.ModelPartDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dto.lcvinbom.VinBomDesignChangeDto;
import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>VinBomMissingProductSpecCheckTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> VinBomLoadBeamDataTask.java description </p>
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
 * <TR>
 * <TD>Ambica Gawarla</TD>
 * <TD>Mar 25, 2021</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
public class VinBomMissingProductSpecCheckTask extends OifTask<Object> implements IEventTaskExecutable {

	private static final String PLANT_CODE = "PLANT_CODE";
	private static final String NO_OF_DAYS_AHEAD = "NO_OF_DAYS_AHEAD";
	private static final String DISTRIBUTION_LIST = "DISTRIBUTION_EMAIL_LIST";
	private static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	private static final String SYSNAME_EXCLUDE = "SYSNAME_EXCLUDE";
	
	// File names received from GPCS(MQ).
	protected String[] aReceivedFileList;

	public VinBomMissingProductSpecCheckTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {
			processDesignChange();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	/**
	 * This method is used for receive files/files from MQ and process it/them for current line.
	 * @throws ParseException
	 */
	private void processDesignChange() throws Exception{
		
		long startTime = System.currentTimeMillis();
		logger.info("start to process VinBomMissingProductSpecCheckTask ");

		// Refresh properties

		refreshProperties();
		
		String distributionList = getProperty(DISTRIBUTION_LIST);
		String emailSubject = getProperty(EMAIL_SUBJECT);
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT);
		String outPutFileName = "MissingProductSpecReport";
			
		// Get configured parsing data
		String daysAhead = getProperty(NO_OF_DAYS_AHEAD);
		String plantCode = getProperty(PLANT_CODE);
		String sysNameToExclude = getProperty(SYSNAME_EXCLUDE);
		
		int totalCount = 0;
		try {
				
				List<String> missingProductSpeccodes =  new ArrayList<String>();
				VinBomDesignChangeDto[] designChangeRecords = getVinBomService().retrieveDesignChangeByEffectiveDate(plantCode,getEffectiveDate(daysAhead));
				totalCount = designChangeRecords != null?designChangeRecords.length:0;
				if(totalCount == 0) {
					logger.info(" No Design Change records exist  for Plant - "+ plantCode +" and EffectiveDate - "+getEffectiveDate(daysAhead));
					return;
				}
				
				for(VinBomDesignChangeDto designChangeRecord:designChangeRecords) {
					String dcNumber = designChangeRecord.getDesignChangeNumber();
					String partNumber = designChangeRecord.getDesignChangePartNumber();
					String productSpec = designChangeRecord.getYmtoCode()+designChangeRecord.getModelType();
					List<String> sysNameToExcludeList = Arrays.asList(StringUtils.split(StringUtils.defaultIfBlank(sysNameToExclude, ""), ","));
					List<String> systemNames = getVinBomPartDao().getSystemNamesByProductSpecPartNumber(productSpec,partNumber,sysNameToExcludeList);//look at vinbom part by spec and part number
					for(String systemName:systemNames) {
						
							if(isModelInSchedule(productSpec)) {//check spec in 212 table
								List<ModelPart> modelParts = getModelPartDao().findAllBy(productSpec, systemName, partNumber);//check in model part
								if(modelParts != null && !modelParts.isEmpty()) {
									logger.info(" ModelPart exists: "+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
									for(ModelPart modelPart: modelParts) {
										List<ModelLot> modelLots = getModelLotDao().getAssignedLots(modelPart.getModelPartId());//is part assigned
										if( modelLots != null && ! modelLots.isEmpty()) {
											logger.info(" ModelLot exists: "+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
											if(getModelPartApprovalDao().getApprovalStatusByModelPartId(modelPart.getModelPartId())) {//is part approved
												logger.info(" ModelPart approved: "+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
											}else {
												logger.info("ModelPart not approved:"+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
												logger.info(productSpec+" - adding productSpec to missing ProductSpec List");
												missingProductSpeccodes.add(productSpec+", "+systemName+", "+partNumber +", "+dcNumber +"  -  "+ "ModelPart not approved");
											}
											
										}else {
											logger.info("ModelLot does not exist for : "+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
											logger.info(productSpec+" - adding productSpec to missing ProductSpec List");
											missingProductSpeccodes.add(productSpec+", "+systemName+", "+partNumber +", "+dcNumber +"  -  "+"ModelLot does not exist");
										}
									}
								}else {
									logger.info("ModelPart does not exist for :"+productSpec+", "+systemName+", "+partNumber+", "+dcNumber );
									logger.info(productSpec+" - adding productSpec to missing ProductSpec List");
									missingProductSpeccodes.add(productSpec+", "+systemName+", "+partNumber +", "+dcNumber +"  -  "+"ModelPart does not exist");
								}
							}else {
								logger.info(" Model Not In Schedule : "+productSpec);
							}
					}
				}
	
				if(missingProductSpeccodes.isEmpty()) {
					logger.info(" No missing productSpecs found ");
				}else {
					List<String> outputMessages = new ArrayList<String>();
					
					//Create and form the report
					outputMessages.add("Missing Product Spec Report");
					outputMessages.add("==========================================================================================");
					outputMessages.add("Product Spec, System Name, Part Number, DC Number   -      Reason               ");
					outputMessages.add("------------------------------------------------------------------------------------------");
					//get all information about vins that not exist into database
					for (String message : missingProductSpeccodes) {
						outputMessages.add( message);//include system name, part number, design change number and reason
					}
					
					//Create a report file
					OIFFileUtility.writeToFile(outputMessages, resultPath + outPutFileName +".txt");
					
					//Send the report file in an email
					sendEmail(resultPath + outPutFileName +".txt", distributionList, emailSubject);
				}
			
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;

			
			logger.info("VinBomMissingProductSpecCheckTask  complete.  Received "+totalCount+" records total; processed records in "+totalTime+" milliseconds.");
			
			
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
		
			setIncomingJobCount(0,totalCount , aReceivedFileList);
			logger.error("VinBomMissingProductSpecCheckTask  failed.  Processed "+totalCount+" records before failure; inserted and updated records in "+totalTime+" milliseconds.");
			throw e;
		}finally{
			errorsCollector.sendEmail();
		}
		
	}

	
	private boolean isModelInSchedule(String modelPrefix) {
		return getPreProductionLotDao().isModelInSchedule(modelPrefix);
	}

	/*private VinBomDesignChangeDto[] getDesignChangeRecords(String plantCode, Date effectiveDate) {
		DataContainer data = new DefaultDataContainer();
		data.put("plantName", plantCode);
		data.put("effectiveBeginDate", effectiveDate);
		String url = getProperty(DCMS_URL);
		String json = DataContainerJSONUtil.convertToRawJSON(data);
		String getUrl = url.concat(json);
		String jsonResponse = HttpClient.get(getUrl, HttpURLConnection.HTTP_OK);
		if (StringUtils.isBlank(jsonResponse)) return null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		VinBomDesignChangeDto[] response=gson.fromJson(jsonResponse,VinBomDesignChangeDto[].class);
		
		return response;
	}*/
	
	private ModelPartApprovalDao getModelPartApprovalDao() {
		
		return ServiceFactory.getDao(ModelPartApprovalDao.class);
	}
	
	private ModelLotDao getModelLotDao() {
		return ServiceFactory.getDao(ModelLotDao.class);
	}
	
	private ModelPartDao getModelPartDao() {
		return ServiceFactory.getDao(ModelPartDao.class);
	}
	
	private VinBomPartDao getVinBomPartDao() {
		return ServiceFactory.getDao(VinBomPartDao.class);
	}
	private PreProductionLotDao getPreProductionLotDao() {
		return ServiceFactory.getDao(PreProductionLotDao.class);
	}
	
	private VinBomService getVinBomService() {
		return ServiceFactory.getService(VinBomService.class);
	}

	private Date getEffectiveDate(String daysAhead) {
		LocalDateTime currentDate =new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime newDate = currentDate.plusDays(Long.parseLong(daysAhead));
		return Date.from(newDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * 	email report file
	 * @param reportFile	-	this is the path of the report
	 * @param emailList		-	this is the email distribution list
	 * @param emailSubject	-	this is the subject for the email
	 */
	private void sendEmail(String reportFile, String emailList, String emailSubject) {
		OifServiceEMailHandler eMailHandler = new OifServiceEMailHandler(OIFConstants.OIF_NOTIFICATION_PROPERTIES);		
		eMailHandler.setEmailAddressList(emailList);
		StringBuffer msg = new StringBuffer();			
		msg.append("This e-mail has an attached with information about current inventory. \n");
		msg.append("Please review the attached inventory comparison report for details. \n");		
		eMailHandler.delivery(emailSubject, msg.toString(), reportFile);
	}
		
	
}