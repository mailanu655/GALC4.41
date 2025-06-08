package com.honda.galc.let.message;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.LetChecker;
import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.common.exception.LetPersistenceException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.dao.product.LetPassResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LetPassResult;
import com.honda.galc.entity.product.LetPassResultId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.let.util.LetPersistenceManager;
import com.honda.galc.let.util.LetUtil;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.QueueProcessor;

/**
 * @author Subu Kathiresan
 * @date Apr 9, 2015
 */
public class LetDataQueueProcessor extends QueueProcessor<LetProcessItem> {

	private static LetDataQueueProcessor instance = null;
	
	private static int STATUS_FAIL = 0;
	private static int STATUS_PASS = 1;
    
	private Logger logger;
	
	private LetDataQueueProcessor() {
		super();
		logger = Logger.getLogger("JcaAdaptor");
	}
	
	public static LetDataQueueProcessor getInstance() {
		if(instance == null){ 
			instance = new LetDataQueueProcessor();
		    instance.start();
		}
		return instance;
	}
	
	@Override
	public void processItem(LetProcessItem processItem) {
		
		long startTime = System.currentTimeMillis();
		getLogger().info("Started processing LET request " + processItem.getMsgKey());
		try {
            UnitInTest unitInTest = processItem.getUnitInTest();            
			if (unitInTest != null) {
				new LetPersistenceManager(unitInTest).persist(processItem);						// save LET data to db
				if (getPropertyBean().isPerformLetChecks()) {
					performLetCheck(processItem, unitInTest);									// perform LET checks
				}
				if (LetUtil.getLetPropertyBean().getInstalledPartName() != null) {
					saveInstalledPart(processItem, unitInTest);									// save Installed part, if configured
				}
			} else {
                LetUtil.sendAlertEmail("Invalid xml received in request " + processItem.getFileLocation());
				updateProcesStatus( processItem, LetTotalStatus.NG_XML, startTime, System.currentTimeMillis());
			}
		} catch (LetPersistenceException ex) {
			handleLetPersistenceException(processItem, ex);
		} catch (Exception ex) {
			handleException(processItem, ex);
		}
		getLogger().info("Processing Let request " + processItem.getMsgKey() + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	protected void updateProcesStatus(LetProcessItem processItem, LetTotalStatus status, long startTime, long endTime) {
		try {
			double timeDiffInMin = LetUtil.calculateTimeDiffInMins(startTime, endTime);
			ServiceFactory.getDao(LetMessageDao.class).updateStatusAndDurationByMessageId(processItem.getMsgId(), status.name(), timeDiffInMin );
		} catch(Exception e) {
			getLogger().error(e, "Failed to update status to " + status + " for " + processItem.getFileLocation());
		}
	}

	private void saveInstalledPart(LetProcessItem processItem, UnitInTest unitInTest) {
		try {			
			String propVal = LetUtil.getLetPropertyBean().getInstalledPartName().get(processItem.getTerminalId().trim());
			if (StringUtils.trimToNull(propVal) != null) {
				String[] propValSplit = propVal.split(",");
				String partName = StringUtils.trimToNull(propValSplit[0]);
				String partId = StringUtils.trimToEmpty(propValSplit[1]);
			
				if (partName != null) {
					getInstalledPartDao().save(createInstalledPart(processItem, unitInTest, partName, partId));
				}
			}
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to save installed part for product " + StringUtils.trimToEmpty(unitInTest.getProductId()) + ": " + StringUtils.trimToEmpty(ex.getMessage()));
		}
	}

	private InstalledPart createInstalledPart(LetProcessItem processItem, UnitInTest unitInTest, String partName, String partId) {		
		
		InstalledPart installedPart = new InstalledPart(unitInTest.getProductId(), partName);	
		PartName pName = getPartNameDao().findByKey(partName);
		BaseProduct product = ProductTypeUtil.getProductDao(pName.getProductType()).findByKey(unitInTest.getProductId());
		String processPointId = LetUtil.getLetPropertyBean().getLetTerminalProcessPoint().get(processItem.getTerminalId().trim());
		
		// set installed part values		
		if (unitInTest.isTotalStatusPass()) {
			installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		} else {
			installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
		}
		installedPart.setPartId(partId);
		installedPart.setPassTime(0);
		installedPart.setAssociateNo(processItem.getTerminalId());
		installedPart.setPartSerialNumber(product.getProductId() + ',' + product.getProductSpecCode());
		installedPart.setProcessPointId(processPointId);
		installedPart.setProductType(pName.getProductTypeName());
		
		return installedPart;
	}
	
	private void performLetCheck(LetProcessItem processItem, UnitInTest unitInTest) {
		try {
			BaseProductCheckerData checkerData = getCheckerData(unitInTest, getProcessPointId(processItem));
			List<CheckResult> checkResults = new LetChecker().executeCheck(checkerData);
		
			// remove existing entries for this product id
			List<LetPassResult> deleteList = getDao().findAllByProductId(unitInTest.getProductId());
			
			LetPassResultId id = new LetPassResultId();
			id.setProductId(unitInTest.getProductId());
			id.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			LetPassResult letPassResult = new LetPassResult(id);
				
			if (checkResults.size() > 0) {
				letPassResult.setPassStatus(STATUS_FAIL);
			} else {
				letPassResult.setPassStatus(STATUS_PASS);
			}
			getDao().save(letPassResult);
			getDao().removeAll(deleteList);
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to update LET check pass status: "  + StringUtils.trimToEmpty(ex.getMessage()));
		}
	}

	private String getProcessPointId(LetProcessItem processItem) {
		String processPointId = LetUtil.getLetPropertyBean().getLetTerminalProcessPoint().get(processItem.getTerminalId().trim());
		if (StringUtils.trimToNull(processPointId) == null) {
			processPointId = LetUtil.getLetPropertyBean().getLetTerminalProcessPoint().get(LetUtil.getLetPropertyBean().getDefaultUploadTerminal());
		}
		return processPointId;
	}

	private BaseProductCheckerData getCheckerData(UnitInTest unitInTest, String processPointId) {
		BaseProductCheckerData checkerData = new BaseProductCheckerData(ProductType.FRAME.toString(), unitInTest.getProductId(), processPointId);
		checkerData.setProductId(unitInTest.getProductId());
		return checkerData;
	}

	public void handleIncompleteMsgRecdException(LetProcessItem processItem, Exception ex) {
		ex.printStackTrace();
		String msg = "Did not receive complete LET message for request "
				+ processItem.getFileLocation()
				+ ": " 
				+ ex.getMessage();
		getLogger().error(ex, msg);
		LetUtil.sendAlertEmail(msg, ex);
	}

	public void handleException(LetProcessItem processItem, Exception ex) {
		ex.printStackTrace();
		String msg = "Exception encountered when processing request "
				+ processItem.getFileLocation()
				+ ": " 
				+ ex.getMessage();
		getLogger().error(ex, msg);
		LetUtil.sendAlertEmail(msg, ex);
	}

	public void handleLetPersistenceException(LetProcessItem processItem, LetPersistenceException ex) {
		ex.printStackTrace();
		getLogger().error(ex, ex.getMessage());
		LetUtil.sendAlertEmail(ex.getMessage(), ex);
	}

	public int getCurrentSize() {
		return instance.queue.size();
	}
	
	public LetPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(LetPropertyBean.class);  
	}

	public LetPassResultDao getDao() {
		return ServiceFactory.getDao(LetPassResultDao.class);	
	}
	
	public InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);	
	}
	
	public PartNameDao getPartNameDao() {
		return ServiceFactory.getDao(PartNameDao.class);	
	}
	
	public Logger getLogger(){
		return logger;
	}
}
