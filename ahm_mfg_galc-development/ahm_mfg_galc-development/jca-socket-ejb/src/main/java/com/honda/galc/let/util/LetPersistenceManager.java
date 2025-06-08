package com.honda.galc.let.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.task.TaskExecutor;

import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.client.enumtype.MessageType;
import com.honda.galc.common.exception.LetPersistenceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.dao.product.LetProgramResultValueDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LetInspectionParamType;
import com.honda.galc.entity.enumtype.LetProgramResultEnum;
import com.honda.galc.entity.enumtype.LetProgramResultValueEnum;
import com.honda.galc.entity.product.LetDiagDetail;
import com.honda.galc.entity.product.LetDiagDetailId;
import com.honda.galc.entity.product.LetDiagFaultDetail;
import com.honda.galc.entity.product.LetDiagFaultDetailId;
import com.honda.galc.entity.product.LetDiagName;
import com.honda.galc.entity.product.LetDiagNameId;
import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.entity.product.LetDiagResultId;
import com.honda.galc.entity.product.LetProgramResult;
import com.honda.galc.entity.product.LetProgramResultId;
import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.entity.product.LetResult;
import com.honda.galc.entity.product.LetResultId;
import com.honda.galc.let.message.LetProcessItem;
import com.honda.galc.letxml.model.FaultCode;
import com.honda.galc.letxml.model.Process;
import com.honda.galc.letxml.model.Test;
import com.honda.galc.letxml.model.TestAttrib;
import com.honda.galc.letxml.model.TestParam;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.property.NGTDAFeedPropertyBean;
import com.honda.galc.service.LetXmlService;
import com.honda.galc.service.NGTMQMessagingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class LetPersistenceManager {
	
	private Timestamp processStartTimestamp;
	private Timestamp processEndTimestamp;
	
	private Class<? extends LetProgramResult> letPgmResultClass;
	private Class<? extends LetProgramResultValue> letPgmResultValueClass;
	private final String RESULT_ENTITIES = "RESULT_ENTITIES";
	private final String PROGRAM_RESULT_ENTITIES = "PROGRAM_RESULT_ENTITIES";
	private UnitInTest unitInTest = null;
	private static final String FAULT_CODE_VALUE = "X";
	private final String DIAG_RESULT_ENTITIES = "DIAG_RESULT_ENTITIES";
	private final String DIAG_DETAIL_ENTITIES = "DIAG_DETAIL_ENTITIES"; 
	private final String DIAG_FAULT_DETAIL_ENTITIES = "DIAG_FAULT_DETAIL_ENTITIES"; 
	private final String DIAG_NAME_ENTITIES = "DIAG_NAME_ENTITIES";  
	private final String PRODUCT_ID = "PRODUCT_ID";

	private static final String LET_APP_KEY = "JcaAdaptor";
	private static TaskExecutor sendTaskExecutor = (TaskExecutor) ApplicationContextProvider.getBean("NGTDAFeedSendTaskExecutor");
	private boolean isDuplicateXml;
	
	public LetPersistenceManager(UnitInTest unitInTest) {
		this.unitInTest = unitInTest;
		identifyPhysicalTables();
	}
	
	private void identifyPhysicalTables(){
    	Calendar cal = GregorianCalendar.getInstance();
    	Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
    	setLetPgmResultClass(ServiceFactory.getService(LetXmlService.class).getPhysicalTableName(timestamp, LetProgramResult.class));
    	setLetPgmResultValueClass(ServiceFactory.getService(LetXmlService.class).getPhysicalTableName(timestamp, LetProgramResultValue.class));
    	prepareProcessStartEndTimeStamp(unitInTest);
	}
	
	public void persist(LetProcessItem item) throws LetPersistenceException {
        if (item == null) {
            return;
        }
		long startTime = System.currentTimeMillis();
		long processEndTime = getProcessEndTimeStamp().getTime();
		long processStartTime = getProcessStartTimestamp().getTime();
		ArrayList<LetResult> letResultList = new ArrayList<LetResult>();
		ArrayList<LetDiagResult> letDiagResultList = new ArrayList<LetDiagResult>();
		ArrayList<LetDiagDetail> letDiagDetailList = new ArrayList<LetDiagDetail>();
		ArrayList<LetDiagFaultDetail> letDiagFaultDetailList = new ArrayList<LetDiagFaultDetail>();
		ArrayList<LetDiagName> letDiagNameList = new ArrayList<LetDiagName>();

		ArrayList<String> productId = new ArrayList<String>();
		HashMap<LetProgramResultId, LetProgramResult> letProgramResultMap = new HashMap<LetProgramResultId, LetProgramResult>();
		HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap = new HashMap<LetProgramResultValueId, LetProgramResultValue>();
		
		try {
		    boolean saveSuccessful = false;
			LetResult letResult = null;
            int testSeq = 0;
            boolean diagMessage = MessageType.DIAG.name().equalsIgnoreCase(item.getMsgType());
			productId.add(unitInTest.getProductId());
			
			ArrayList<Process> processList = unitInTest.getProcesses();
			
            if (diagMessage) {
				LetDiagResult letDiagResult = buildLetDiagResult(unitInTest, item.getTerminalId());
				letDiagResultList.add(letDiagResult);
			} else {
				letResult = buildLetResult(unitInTest);
                LetResultDao letResultDao = ServiceFactory.getDao(LetResultDao.class);
                boolean created = letResultDao.insertIfNotExist(letResult);
                if (created) {
                    isDuplicateXml = false;
                    letResult = letResultDao.findFirstByProductIdTimestamp(unitInTest.getProductId(), getProcessEndTimeStamp());
                } else {
                    isDuplicateXml = true;
                    LetResult existing = letResultDao.findFirstByProductIdTimestamp(unitInTest.getProductId(), getProcessEndTimeStamp());
                    letResult.getId().setTestSeq(existing.getId().getTestSeq());
				    letResultList.add(letResult);
			    }
                testSeq = letResult.getId().getTestSeq();
            }
			
			for(Process process : processList){
				ArrayList<Test> testList = process.getTests();
				
				for(Test test : testList){
					LetProgramResult letProgramResult = buildLetProgramResult(process, test, unitInTest.getProductId(), testSeq);
					// prepare diagnostic detail
                    if (diagMessage) {
						LetDiagDetail letDiagDetail = buildLetDiagDetail(item.getTerminalId(), test);
						if (!letDiagDetailList.contains(letDiagDetail))
							letDiagDetailList.add(letDiagDetail);
                    } else {
                        addLetProgramResult(letProgramResultMap, letProgramResult, item);
					}
					
                    if (!diagMessage && test.getTestAttribs() != null) {
						for(TestAttrib testAttrib : test.getTestAttribs()){
							LetProgramResultValue letProgramResultValue = buildLetPgmRsltValFromTestForAttrib(letProgramResult, testAttrib, process, test, unitInTest.getProductId(), testSeq);
							addLetProgramResultValue(letProgramResultValueMap, letProgramResultValue, item);
						}
					}
					if (test.getTestParams() != null) {
						for(TestParam testParam : test.getTestParams()){
							// prepare diagnostic test parameters
                            if (diagMessage) {
								LetDiagName letDiagName = buildLetDiagName(item.getTerminalId(), test, testParam);
                                if (!letDiagNameList.contains(letDiagName)) {
									letDiagNameList.add(letDiagName);
							    }
                            } else {
                                LetProgramResultValue letProgramResultValue = buildLetPgmRsltValFromTestForParam(letProgramResult, testParam, process, test, unitInTest.getProductId(), testSeq);
                                addLetProgramResultValue(letProgramResultValueMap, letProgramResultValue, item);
                            }
						}	
					}
					if (test.getFaultCodes() != null) {
						for(FaultCode faultCode : test.getFaultCodes()){
							// prepare diagnostic fault details
                            if (diagMessage) {
								LetDiagFaultDetail letDiagFaultDetail = buildLetDiagFaultDetail(item.getTerminalId(), test, faultCode);
                                if (!letDiagFaultDetailList.contains(letDiagFaultDetail)) {
									letDiagFaultDetailList.add(letDiagFaultDetail);
							    }
                            } else {
                                LetProgramResultValue letProgramResultValue = buildLetPgmRsltValFromTestForFaultCode(letProgramResult, faultCode, process, test, unitInTest.getProductId(), testSeq);
                                addLetProgramResultValue(letProgramResultValueMap, letProgramResultValue, item);
                            }
						}	
					}
				}
			}

			HashMap<String, ArrayList<?>> letEntityLstMap = new HashMap<String, ArrayList<?>>();
			letEntityLstMap.put(RESULT_ENTITIES, letResultList);
			letEntityLstMap.put(DIAG_RESULT_ENTITIES, letDiagResultList);
			letEntityLstMap.put(DIAG_DETAIL_ENTITIES, letDiagDetailList);
			letEntityLstMap.put(DIAG_FAULT_DETAIL_ENTITIES, letDiagFaultDetailList);
			letEntityLstMap.put(DIAG_NAME_ENTITIES, letDiagNameList);
			letEntityLstMap.put(PRODUCT_ID, productId);
			letEntityLstMap.put(PROGRAM_RESULT_ENTITIES, new ArrayList<LetProgramResult>(letProgramResultMap.values()));
			
			saveSuccessful = ServiceFactory.getService(LetXmlService.class).saveLetXmlData(letEntityLstMap);

            if (saveSuccessful) {
                if (!diagMessage) {
                    List<LetProgramResultValue> letProgramResultValues = new ArrayList<LetProgramResultValue>(letProgramResultValueMap.values());
                    saveSuccessful = saveLetProgramResultValues(letProgramResultValues);     
                } 
            }
			
			if (saveSuccessful) {
				getLogger().check("Saved Let Result for product: " + unitInTest.getProductId());
				getLogger().check("Persisting LET data for request " + item.getMsgKey() + " took " +  (System.currentTimeMillis() - startTime) + " milliseconds");
				
				updateProcessStatus(item, processStartTime, processEndTime);
			} else {
				updateProcessStatus(item, LetTotalStatus.FAIL_PROC, processStartTime, processEndTime);
			}
		} catch (Exception ex){
			ex.printStackTrace();
			updateProcessStatus(item, LetTotalStatus.FAIL_PROC, processStartTime, processEndTime);
			String msg = "Unable to save parsed Let XML data to db for request " + item.getMsgKey() + ": " + ex.getLocalizedMessage();
			getLogger().error(ex, msg);
			throw new LetPersistenceException(msg, ex);
		}
		
		//Send Display Audio Data Feed
		sendNGTDAFeed(letProgramResultValueMap, item.getMsgKey());
	}

	private void updateProcessStatus(LetProcessItem item, long processStartTime, long processEndTime) {
		LetTotalStatus status = isDuplicateXml ? LetTotalStatus.DUP : LetTotalStatus.OK;
		updateProcessStatus(item, status, processStartTime, processEndTime);
	}

	private void updateProcessStatus(LetProcessItem item, LetTotalStatus status, long processStartTime, long processEndTime) {
		double timeDiffInMin = LetUtil.calculateTimeDiffInMins(processStartTime, processEndTime);
		int updateCount = getDao(LetMessageDao.class).updateStatusAndDurationByMessageId(item.getMsgId(), status.name(), timeDiffInMin );
		if(updateCount > 0){
			getLogger().info("Record for Terminal " + item.getTerminalId() + " sent at " + item.getActualTimeStamp() + " was updated successfully"); 
		}
	}
	
	
    protected boolean saveLetProgramResultValues(List<LetProgramResultValue> values) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        int ctr = 0;
        int batchSize = 100;
        int batchCtr = 0;
        int dataSize = values.size();
        
        List<LetProgramResultValue> batch = new ArrayList<LetProgramResultValue>();
        LetXmlService service = ServiceFactory.getService(LetXmlService.class);
        
        LetProgramResultValueDao letProgramResultValueDao = ServiceFactory.getDao(LetProgramResultValueDao.class);
        long existingDataCount = letProgramResultValueDao.count(unitInTest.getProductId(), getProcessEndTimeStamp());
        boolean insert = existingDataCount > 0 ? false : true;

        for (int i = 0; i < dataSize; i++) {
            batch.add(values.get(i));
            ctr++;
            if (ctr >= batchSize || i >= (dataSize -1)) {
                batchCtr++;
                String msg = "LetProgramResultValue batch:" + batchCtr + ", size:" + batch.size() + ", of :" + dataSize; 
                if (insert) {
                    service.insertLetProgramResultValues(batch);
                    msg = "Inserted " + msg;
                } else {
                    service.saveLetProgramResultValues(batch);
                    msg = "Saved " + msg;
                }
                ctr = 0;
                batch.clear();
                getLogger().info(msg);
            }
        }        
        return true;
    }
	
	private void addLetProgramResult(HashMap<LetProgramResultId, LetProgramResult> letProgramResultMap, 
			LetProgramResult letProgramResult, LetProcessItem item) {
		
		// Persisting only the latest LetProgramResult
		if (letProgramResultMap.containsKey(letProgramResult.getId())) {
			getLogger().check("Duplicate LetProgramResult identified in request " + item.getMsgKey() 
					+ ": " + letProgramResult.getId().toString());
		}
		letProgramResultMap.put(letProgramResult.getId(), letProgramResult);
	}

	private void addLetProgramResultValue(HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap, 
			LetProgramResultValue letProgramResultValue, LetProcessItem item) {
		
		// Persisting only the latest LetProgramResultValue
		if (letProgramResultValueMap.containsKey(letProgramResultValue.getId())) {
			getLogger().check("Duplicate LetProgramResultValue identified in request " + item.getMsgKey() 
					+ ": " + letProgramResultValue.getId().toString());
		}
		letProgramResultValueMap.put(letProgramResultValue.getId(), letProgramResultValue);
	}

	private LetProgramResultValue buildLetPgmRsltValFromTestForParam(
			LetProgramResult letProgramResult, TestParam testParam,
			Process process, Test test, String productId, int testSeq) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		
		LetProgramResultValue letProgramResultValue = getLetPgmResultValueClass().newInstance();
		LetProgramResultValueId letProgramResultValueId = new LetProgramResultValueId();
		letProgramResultValue.setId(letProgramResultValueId);
		letProgramResultValue.getId().setEndTimestamp(getProcessEndTimeStamp());
		letProgramResultValue.getId().setProductId(productId);
		letProgramResultValue.getId().setTestSeq(testSeq);
		letProgramResultValue.getId().setInspectionPgmId(letProgramResult.getId().getInspectionPgmId());
		letProgramResultValue.getId().setInspectionParamId(getLetInspectionParamId(testParam.getParam().trim()));
		letProgramResultValue.getId().setInspectionParamType(LetInspectionParamType.TEST_PARAM.getType());

		letProgramResultValue.setInspectionParamValue(testParam.getVal());
		letProgramResultValue.setInspectionParamUnit(testParam.getUnit());
		letProgramResultValue.setLowLimit(testParam.getLoLimit());
		letProgramResultValue.setHighLimit(testParam.getHiLimit());
		return letProgramResultValue;
	}

	private LetProgramResultValue buildLetPgmRsltValFromTestForAttrib(LetProgramResult letProgramResult, TestAttrib testAttrib, Process process,
			Test test, String productId, int testSeq) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		
		LetProgramResultValue letProgramResultValue = getLetPgmResultValueClass().newInstance();
		LetProgramResultValueId letProgramResultValueId = new LetProgramResultValueId();
		letProgramResultValue.setId(letProgramResultValueId);
		letProgramResultValue.getId().setEndTimestamp(getProcessEndTimeStamp());
		letProgramResultValue.getId().setProductId(productId);
		letProgramResultValue.getId().setTestSeq(testSeq);
		letProgramResultValue.getId().setInspectionPgmId(letProgramResult.getId().getInspectionPgmId());
		letProgramResultValue.getId().setInspectionParamId(getLetInspectionParamId(testAttrib.getAtt().trim()));
		letProgramResultValue.getId().setInspectionParamType(LetInspectionParamType.TEST_ATTRIB.getType());
		
		letProgramResultValue.setInspectionParamValue(testAttrib.getVal());
		return letProgramResultValue;
	}
	
	private LetProgramResultValue buildLetPgmRsltValFromTestForFaultCode(
			LetProgramResult letProgramResult, FaultCode faultCode,
			Process process, Test test, String productId, int testSeq) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		
		LetProgramResultValue letProgramResultValue = getLetPgmResultValueClass().newInstance();
		LetProgramResultValueId letProgramResultValueId = new LetProgramResultValueId();
		letProgramResultValue.setId(letProgramResultValueId);
		letProgramResultValue.getId().setEndTimestamp(getProcessEndTimeStamp());
		letProgramResultValue.getId().setProductId(productId);
		letProgramResultValue.getId().setTestSeq(testSeq);
		letProgramResultValue.getId().setInspectionPgmId(letProgramResult.getId().getInspectionPgmId());
		letProgramResultValue.getId().setInspectionParamId(getLetInspectionParamId(faultCode.getShortDesc().trim()));
		letProgramResultValue.getId().setInspectionParamType(LetInspectionParamType.TEST_FAULT_CODE.getType());
		//Per Brian D Ayers, hard code value for Fault Code
		letProgramResultValue.setInspectionParamValue(FAULT_CODE_VALUE);
		return letProgramResultValue;
	}

	private LetProgramResult buildLetProgramResult(Process process, Test test,
			String productId, int testSeqNo) throws IllegalAccessException,
			InstantiationException, ClassNotFoundException {
		
		LetProgramResult letProgramResult = getLetPgmResultClass().newInstance();
		LetProgramResultId letProgramResultId = new LetProgramResultId();
		letProgramResult.setId(letProgramResultId);
		letProgramResult.getId().setEndTimestamp(getProcessEndTimeStamp());
		letProgramResult.getId().setProductId(productId);
		letProgramResult.getId().setTestSeq(testSeqNo);
		letProgramResult.getId().setInspectionPgmId(getLetInspectionProgramId(test.getId()));
		
		letProgramResult.setInspectionPgmStatus(Integer.toString(test.getStatus()));
		letProgramResult.setProcessStep(process.getId());
		letProgramResult.setProcessStatus(Integer.toString(process.getStatus()));
		letProgramResult.setProcessStartTimestamp(prepareTimeStamp(test.getTestTime()));
		letProgramResult.setProcessEndTimestamp(prepareTimeStamp(test.getTestEndTime()));
		letProgramResult.setLetTerminalId(process.getCell());
		letProgramResult.setLetResultCal(process.getCal());
		letProgramResult.setLetResultDcrev(process.getDcRev());
		letProgramResult.setSoftwareVersion(process.getSoftwareVersion());
		letProgramResult.setLetOperatorId(process.getOperatorId());
		letProgramResult.setInCycleRetestNum(Integer.parseInt(test.getInCycleRetestNum()));

		return letProgramResult;
	}

	private LetResult buildLetResult(UnitInTest parsedXml) {
		LetResult letResult = new LetResult(new LetResultId());
		letResult.getId().setProductId(parsedXml.getProductId());
		letResult.setBuildCode(parsedXml.getBuildCode());
		letResult.setTestId(parsedXml.getTestId());
		letResult.setSeqStepFile(parsedXml.getSeqStepFile());
		letResult.setStartTimestamp(getProcessStartTimestamp());
		letResult.setEndTimestamp(getProcessEndTimeStamp());
		letResult.setContStepFile(parsedXml.getContStepFile());
		letResult.setSeqRange(parsedXml.getSeqData());
		letResult.setLetMfgAreaCode( parsedXml.getMfgId().substring(0,1));
		letResult.setLetMfgNo(parsedXml.getMfgId().substring(1,2));
		letResult.setLetLineNo(parsedXml.getMfgId().substring(2,3));
		letResult.setProduction(parsedXml.getProduction());
		letResult.setTotalResultStatus(Integer.toString(parsedXml.getTotalStatus().getStatus()));
		if(parsedXml.getProcesses() != null && parsedXml.getProcesses().size() > 0) {
			letResult.setBaseRelease(parsedXml.getProcesses().get(0).getCal());
		}
		if(parsedXml.getProcesses() != null && parsedXml.getProcesses().size() > 0) {
			letResult.setBaseRelease(parsedXml.getProcesses().get(0).getCal());
		}
		return letResult;
	}

	private LetDiagResult buildLetDiagResult(UnitInTest parsedXml, String terminalId) {
		LetDiagResult letResult = new LetDiagResult(new LetDiagResultId());
		letResult.getId().setEndTimestamp(getProcessEndTimeStamp());
		letResult.getId().setLetTerminalId(terminalId);
		letResult.setTotalResultStatus(Integer.toString(parsedXml.getTotalStatus().getStatus()));
		if(parsedXml.getProcesses() != null && parsedXml.getProcesses().size() > 0) {
			letResult.setBaseRelease(parsedXml.getProcesses().get(0).getCal());
			letResult.setSoftwareVersion(parsedXml.getProcesses().get(0).getSoftwareVersion());
		}
		return letResult;
	}
	
	private LetDiagDetail buildLetDiagDetail(String terminalId, Test test) {
		LetDiagDetail letDiagDetail = new LetDiagDetail(new LetDiagDetailId());
		letDiagDetail.getId().setEndTimestamp(prepareTimeStamp(test.getTestEndTime()));
		letDiagDetail.getId().setLetTerminalId(terminalId);
		letDiagDetail.getId().setDiagTestId(getLetInspectionProgramId(test.getId()));
		
		letDiagDetail.setDiagTestStatus(Integer.toString(test.getStatus()));
		
		return letDiagDetail;
	}

	private LetDiagFaultDetail buildLetDiagFaultDetail(String terminalId, Test test, FaultCode faultCode) {
		LetDiagFaultDetail letDiagFaultDetail = new LetDiagFaultDetail(new LetDiagFaultDetailId());
		letDiagFaultDetail.getId().setEndTimestamp(prepareTimeStamp(test.getTestEndTime()));
		letDiagFaultDetail.getId().setLetTerminalId(terminalId);
		letDiagFaultDetail.getId().setDiagTestId(getLetInspectionProgramId(test.getId()));
		letDiagFaultDetail.getId().setFaultCodeId(getLetInspectionParamId(faultCode.getFaultCode().trim()));
		
		letDiagFaultDetail.setShortDescId(getLetInspectionParamId(faultCode.getShortDesc().trim()));
		
		return letDiagFaultDetail;
	}

	private LetDiagName buildLetDiagName(String terminalId, Test test, TestParam testParam) {
		LetDiagName letDiagName = new LetDiagName(new LetDiagNameId());
		letDiagName.getId().setEndTimestamp(prepareTimeStamp(test.getTestEndTime()));
		letDiagName.getId().setLetTerminalId(terminalId);
		letDiagName.getId().setDiagTestId(getLetInspectionProgramId(test.getId()));
		letDiagName.getId().setDiagParamId(getLetInspectionParamId(testParam.getParam()));
		
		letDiagName.setDiagParamType(LetInspectionParamType.TEST_PARAM.getType());
		letDiagName.setDiagParamValue(testParam.getVal());
		
		return letDiagName;
	}
	
	private void prepareProcessStartEndTimeStamp(UnitInTest unitInTest) {
		
		if (unitInTest.getProcesses().isEmpty()) {
			throw new LetPersistenceException ("No Process tags in the UnitInTest xml");
		} else {
			setProcessStartTimestamp(prepareTimeStamp(unitInTest.getProcesses().get(0).getStartTime()));
			setProcessEndTimestamp(prepareTimeStamp(unitInTest.getProcesses().get(0).getFinishTime()));
		}

		for (Process process: unitInTest.getProcesses()) {
			Timestamp tmpStartTime = prepareTimeStamp(process.getStartTime());
			Timestamp tmpEndTime = prepareTimeStamp(process.getFinishTime());
			if (processStartTimestamp.after(tmpStartTime)) {
				setProcessStartTimestamp(tmpStartTime);
			}
			if (processEndTimestamp.before(tmpEndTime)) {
				setProcessEndTimestamp(tmpEndTime);
			}
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	private Timestamp prepareTimeStamp(String timeStamp){
		String receiveTimestamp = timeStamp;
		receiveTimestamp = receiveTimestamp.replace('T', ' ');
		receiveTimestamp = receiveTimestamp.replace('-', '/');
		return new Timestamp(new Date(receiveTimestamp).getTime());
	}

	private void setProcessStartTimestamp(Timestamp processStartTimestamp) {
		this.processStartTimestamp = processStartTimestamp;
	}

	private Timestamp getProcessStartTimestamp() {
		return processStartTimestamp;
	}
	
	private void setProcessEndTimestamp(Timestamp letEndTimestamp) {
		this.processEndTimestamp = letEndTimestamp;
	}

	private Timestamp getProcessEndTimeStamp() {
		return processEndTimestamp;
	}
	
	public Class<? extends LetProgramResult> getLetPgmResultClass() {
		return letPgmResultClass;
	}

	public void setLetPgmResultClass(String letPgmResultTableName) {
		this.letPgmResultClass = Enum.valueOf(LetProgramResultEnum.class, letPgmResultTableName).getLetPgmResultClass();
	}

	public Class<? extends LetProgramResultValue> getLetPgmResultValueClass() {
		return letPgmResultValueClass;
	}

	public void setLetPgmResultValueClass(String letPgmResultValueTableName) {
		this.letPgmResultValueClass = Enum.valueOf(LetProgramResultValueEnum.class, letPgmResultValueTableName).getLetPgmResultValueClass();
	}
	
	private int getLetInspectionParamId(String letInspectionParamStr) {
		return ServiceFactory.getService(LetXmlService.class).getLetInspectionParamId(letInspectionParamStr);
	}

	private int getLetInspectionProgramId(String letInspectionProgramStr) {
		return ServiceFactory.getService(LetXmlService.class).getLetInspectionPgmId(letInspectionProgramStr);
	}

    public static Logger getLogger(){
		return Logger.getLogger(LET_APP_KEY);
	}
    
    /**
	 * RGALCDEV-869 Send Display Audio data feed.
	 */
	private void sendNGTDAFeed(HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap, String msgKey) {
	    
	    NGTDAFeedPropertyBean property = PropertyService.getPropertyBean(NGTDAFeedPropertyBean.class);
        if (!property.isNGTDAFeedEnabled()) {
            getLogger().debug("NGT DA Feed is disabled. ");
            return;
        }
	    
		long startTime = System.currentTimeMillis();
		try {
			sendTaskExecutor.execute(new SendTask(letProgramResultValueMap));
		}catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, "Unable to send Display Audio Data Feed." + e.getLocalizedMessage());
		}
		getLogger().check("Sending NGT DA feed for request " + msgKey + " took " +  (System.currentTimeMillis() - startTime) + " milliseconds");
	}

	/**
	 * RGALCDEV-869 Read Display Audio data from test result into a data container. return null if no display audio data is found.
	 */
	private DataContainer createNGTDAFeed(
			HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap,
			NGTDAFeedPropertyBean property) {
		int serialId=getLetInspectionParamId(property.getAudioSerialParamName());
		int dwgId=getLetInspectionParamId(property.getAudioDwgParamName());
		int versionId=getLetInspectionParamId(property.getAudioVersionParamName());
		
		String clientId=property.getNGTDAFeedClientID();
		String productId = null;
		String audioSerial = null;
		String audioDwg = "";
		String audioVersion = "";
		
		for(LetProgramResultValueId id: letProgramResultValueMap.keySet()){
			if(id.getInspectionParamId() == serialId){
				audioSerial = letProgramResultValueMap.get(id).getInspectionParamValue();
				productId = id.getProductId();
			}
			else if(id.getInspectionParamId() == dwgId){
				audioDwg = letProgramResultValueMap.get(id).getInspectionParamValue();
			}
			else if(id.getInspectionParamId() == versionId){
				audioVersion = letProgramResultValueMap.get(id).getInspectionParamValue();
			}
		}
		if (productId == null || audioSerial == null) {
			// No DA audio serial found
			getLogger().debug("No Display Audio found in the test");
			return null;
		}

		DataContainer dc=new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		dc.put(DataContainerTag.CLIENT_ID, clientId);
		dc.put(property.getAudioSerialTagName(), audioSerial);
		dc.put(property.getAudioDwgTagName(), audioDwg);
		dc.put(property.getAudioVersionTagName(), audioVersion);
		return dc;
	}
	
	private class SendTask implements Runnable {
		HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap;
		
		SendTask(HashMap<LetProgramResultValueId, LetProgramResultValue> letProgramResultValueMap){
			this.letProgramResultValueMap = letProgramResultValueMap;
		}
		
		public void run() {
			NGTDAFeedPropertyBean property = PropertyService.getPropertyBean(NGTDAFeedPropertyBean.class);
			if (!property.isNGTDAFeedEnabled()) {
				getLogger().debug("NGT DA Feed is disabled. ");
				return;
			}
			DataContainer dc = createNGTDAFeed(letProgramResultValueMap, property);
			if (dc != null) {
				NGTMQMessagingService service = ServiceFactory.getService(NGTMQMessagingService.class);
				service.send(dc);
				getLogger().info(
				"NGT DA Feed has been sent for the product: "
						+ dc.get(DataContainerTag.PRODUCT_ID));
			}
		}
	}
}
