package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.property.EngineLoadPropertyBean;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.LotControlRuleFlag;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.engine.EngineMarriageService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SubproductUtil;


/**
 * <h3>PartSerialNumberProcessor</h3>
 * <h4>
 * Part Serial Number processor - used to verify part serial number from both of
 * device and client input
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
/* @ver 0.2
* @author Gangadhararao Gadde
*/
public class PartSerialNumberProcessor extends ProcessorBase 
	implements IPartSerialNumberProcessor {
	
	protected SubproductPropertyBean subProductProperty = null;
	protected InstalledPart installedPart = null;
	protected BuildAttributeDao buildAttributeDao = null;
	protected FrameSpecDao frameSpecDao = null;
	protected ProcessPointDao processPointDao = null;
	protected FrameDao frameDao = null;
	protected ProductCheckPropertyBean productCheckProperty = null; 
	private int partIdx = 0;
	private List<String> duplicateList = new ArrayList<String>();
	protected static final String PART_SN_MESSAGE_ID = "PART_SN"; //Part Serial Number validation failed. 
	PartSpec currentSpec = null;
	private int scanCounter;
	
	public PartSerialNumberProcessor(ClientContext context) {
		super(context);
		init();
		
	}

	public void init() {
		installedPart = new InstalledPart();
		installedPart.setAssociateNo(getAssociateNo());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		installedPart.setProcessPointId(context.getProcessPointId()); //RGALCPROD-1118
		installedPart.setProductType(context.getProperty().getProductType());
		partIdx = 0;
		duplicateList.clear();
	}
	
	private String getAssociateNo() {
		//make both styles work (regional and HMIN) - code merge.
		return context.getProperty().isInstalledPartAssociateUsingUserId() ? context.getUserId() : context.getProcessPointId();
	}

	public void registerDeviceListener(DeviceListener listener) {
		if(context.getProperty().isPartSerialNumberFromPlc())
			registerListener(EiDevice.NAME, listener, getProcessData());
		
	}

	public List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new PartSerialNumber());
		return list;
		
	}

	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug("PartSerialNumberProcessor : Enter confirmPartSerialNumber");		
		try {
			Logger.getLogger().info("Process part:" + partnumber.getPartSn());
			
			confirmPartSerialNumber(partnumber);
			
			if (isMultiScanReceived(installedPart, partnumber)) {
				return true;
			}
			
			peformSubProductInstalledPartChecks(partnumber);
			
			if (context.getProperty().isUseSubAssyProductType()) 
				installedPart.setProductType(context.getProperty().getSubAssyProductType());
			getController().getFsm().partSnOk(installedPart);
			setScanCounter(0);
			
			Logger.getLogger().debug("PartSerialNumberProcessor:: Exit confirmPartSerialNumber ok");
			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		setScanCounter(0);
		Logger.getLogger().debug("PartSerialNumberProcessor:: Exit confirmPartSerialNumber ng");
		return false;
	}

	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		checkPartSerialNumber(partnumber);

		if(isCheckDuplicatePart())
			checkDuplicatePart(installedPart.getPartSerialNumber());

		installedPart.setValidPartSerialNumber(true);

		return true;

	}

	protected boolean isMultiScanReceived(InstalledPart installedPart, PartSerialNumber partnumber) {
		if (getRequiredScanCount() < 2) {
			setScanCounter(0);
			return false;
		}
		InstalledPart collectedPart = getCollectedPart();
		if (collectedPart == null) {
			setScanCounter(0);
		} else if (getScanCounter() > 0 && !StringUtils.equals(collectedPart.getPartSerialNumber(), partnumber.getPartSn())) {
			String msg = "Scanned SN : " + partnumber + " does not match previous scan : " + collectedPart.getPartSerialNumber();
			handleException(msg);
			return true;
		}
		setScanCounter(getScanCounter() + 1);
		if (getScanCounter() < getRequiredScanCount()) {
			getController().getFsm().receivedPartSn(installedPart);
			Logger.getLogger().debug("PartSerialNumberProcessor:: Exit confirmPartSerialNumber received scan : " + getScanCounter());
			return true;
		} 
		return false;
	}
	
	protected boolean isCheckDuplicatePart() {
	    boolean check = LotControlRuleFlag.ON.getId() == getController().getState().getCurrentLotControlRule().getSerialNumberUniqueFlag(); 
        check = check && (getRequiredScanCount() < 2 || getRequiredScanCount() > 1 && getScanCounter() == 0);
		return check;
	}

	/**
	 * Check if the same part has installed on other product.
	 * Check against installed part database table
	 * @param partnumber 
	 * @return
	 */
	protected void checkDuplicatePart(String partnumber) {
		/**
		 * Don't bother to check on server if the part number is null or empty.
		 */
		if(StringUtils.isEmpty(partnumber)) return;
		
		List<String> partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(currentSpec);
		
		//Check duplicate part on Server
		if(context.isOnLine())
			checkDuplicatePartOnServer(partnumber, partNames);
		
		//Still check duplicate part in cache if required
		if(InstalledPartCache.getInstance().getSize() > 0 )
			updateDuplicatePartList(InstalledPartCache.getInstance().getDuplicatedParts(partNames,partnumber));
		 
		if(duplicateList.size() > 0)
			handleDuplicatePart(partnumber, duplicateList);
		
		checkDuplicatePartLocal(partNames,partnumber);
	}
	
	protected void checkDuplicatePartLocal(List<String>checkPartNames, String partnumber) {
		if(checkPartNames.size() == 0) return; //No need to go through the list
		
		List<InstalledPart> duplidatedPartList = new ArrayList<InstalledPart>();
		for(InstalledPart p: getController().getState().getProduct().getPartList())
			if(checkPartNames.contains(p.getPartName()) && 
					StringUtils.equals(partnumber, p.getPartSerialNumber()) &&
					(p.isValidPartSerialNumber() && !p.isSkipped()))
				duplidatedPartList.add(p);
		
		if(duplidatedPartList.size() > 0){
			installedPart.setValidPartSerialNumber(false);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Duplicate Part:").append(partnumber).append(" was already installed on this product."); 
					
			handleException(sb.toString());
		}
	}


	private void checkDuplicatePartOnServer(String partnumber, List<String> partNames) {
		try {
			List<InstalledPart> installedPartList = context.getDbManager().findDuplicatePartsByPartNames(
							partNames,	partnumber);
			duplicateList.clear();
			updateDuplicatePartList(installedPartList);
		} catch (ServiceTimeoutException e) {
			handleServerOfflineException(e);
		} catch (ServiceInvocationException sie){
			handleServerOfflineException(sie);
		}
	}
	
	private void handleServerOfflineException(ServiceException se) {
		Logger.getLogger().info(se, "Server OffLine detected.");
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}
	
	private void updateDuplicatePartList(List<InstalledPart> installedPartList) {
		if(installedPartList == null) return;
		
		String productId = getController().getState().getProductId();
		for(InstalledPart p : installedPartList)
		{
			if(p != null && !(p.getId().getProductId().equals(productId)))
				duplicateList.add(p.getId().getProductId());
			else if (p !=null && !(p.getId().getPartName().equals(getController().getState().getCurrentPartName())))
				duplicateList.add(p.getId().getPartName());
		}
	}

	private void handleDuplicatePart(String partnumber,
			List<String> duplicateList) {
		StringBuilder userMsg = new StringBuilder("Part:" + partnumber + " already installed on ");
		
		for(int i = 0; i < duplicateList.size(); i++)
		{
			userMsg.append(duplicateList.get(i));
			if( i < duplicateList.size() -1 ) userMsg.append(", ");
		}
		userMsg.append(".");
		
		handleException(userMsg.toString());
	}

	/**
	 * Check part serial number against lot control rule part mask
	 * @param partSerialNumber
	 */
	protected void checkPartSerialNumber(PartSerialNumber partSerialNumber) {
		checkPartSerialNumberIsNotNull(partSerialNumber);	
		installedPart.setPartSerialNumber(partSerialNumber.getPartSn());
		
		if (isVerifyPartSerialNumber()) {
			checkPartSerialNumberMask();
		} else { 	
			//BCC 5/3/12: Still need part_ID added even if the part is not verified
			currentSpec = getController().getState().getCurrentLotControlRulePartList().get(0);
			installedPart.setPartId(currentSpec.getId().getPartId());
		}
	}

	private void peformSubProductInstalledPartChecks(PartSerialNumber partnumber) {
		LotControlRule rule = getController().getState().getCurrentLotControlRule();		
		String subProductTypeName = rule.getPartName().getSubProductType();		
		// perform checks only for Installed part of MBPN product type 
		if (!StringUtils.isBlank(subProductTypeName) && subProductTypeName.equalsIgnoreCase(ProductType.MBPN.name())) {
			SubproductUtil subproductUtil = new SubproductUtil(partnumber, rule,currentSpec);
			BaseProduct subProduct = subproductUtil.findSubproduct();
			if(subProduct==null)
			{
				handleException("Sub Product:"+partnumber.getPartSn()+" not found");
			}
			List<String> failedProductCheckList = new ArrayList<String>();
			try {
				//mandatory checks for Installed part of MBPN product type 
				List<String> checkTypesList = new ArrayList<String>();
				checkTypesList.add(ProductCheckType.RECURSIVE_INSTALLED_PART_CHECK.name());
				checkTypesList.add(ProductCheckType.OUTSTANDING_PARTS_CHECK.name());
				checkTypesList.add(ProductCheckType.OUTSTANDING_DEFECTS_CHECK.name());
				checkTypesList.add(ProductCheckType.CHECK_SCRAPPED_EXCEPTIONAL_OUT.name());
				checkTypesList.add(ProductCheckType.PRODUCT_ON_HOLD_CHECK.name());	
				if(subProduct instanceof MbpnProduct){
					MbpnProduct product = ((MbpnProduct) subProduct);
					if(product.getExternalBuild() == 1){
						checkTypesList.remove(ProductCheckType.RECURSIVE_INSTALLED_PART_CHECK.name());
						checkTypesList.remove(ProductCheckType.OUTSTANDING_PARTS_CHECK.name());
					}
				}
				String[] checkTypes = checkTypesList.toArray(new String[0]);	
				failedProductCheckList = subproductUtil.performSubProductChecks(subProductTypeName, subProduct, context.getProcessPointId(),checkTypes);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != (failedProductCheckList.size() - 1)) {
							msg.append(", ");
						}
					}
					Logger.getLogger().info(msg.toString());
					Message message = new Message(LotControlConstants.FAILED_PRODUCT_CHECKS,msg.toString(),MessageType.EMERGENCY);
					message.setInfo(msg.toString());
					JOptionPane.showConfirmDialog(null, msg.toString()+"\n Parts will be saved with NG status","Failed Product Checks", JOptionPane.DEFAULT_OPTION);
					installedPart.setValidPartSerialNumber(false);
					installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
				}
			} catch (Exception e) {
				handleException ("Could not perfrom checks against part.");
			}	

		}else if(!StringUtils.isBlank(subProductTypeName) && subProductTypeName.equalsIgnoreCase(ProductType.ENGINE.name())) {
			subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, context.getProcessPointId());

			SubproductUtil subproductUtil = new SubproductUtil(partnumber, rule,currentSpec);
			BaseProduct subProduct = subproductUtil.findSubproduct();
			String installProcessPointId = null;
			if(subProduct==null)
			{
				handleException("Engine: " + partnumber.getPartSn() + " not found");
			}

			installProcessPointId = subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());

			EngineLoadUtility engineUtil = new EngineLoadUtility();
			Engine engine = engineUtil.getEngineDao().findByKey(partnumber.getPartSn());

			//check if engine is already assigned to a frame
			if(engineUtil.isEngineAssignedToFrame(engine)) {
				handleException("Engine: " + engine.getProductId() + " is already assigned to VIN: " + engine.getVin());
			}

			//check Engine type mismatch	
			Frame frame = getFrameDao().findByKey(getController().getState().getProductId());
			ProductCheckUtil checkUtil = new ProductCheckUtil(getController().getState().getProduct().getBaseProduct(), getProcesPointDao().findByKey(context.getProcessPointId()));	
			boolean useAltEngineMto=PropertyService.getPropertyBean(ProductCheckPropertyBean.class,
					context.getProcessPointId()).isUseAltEngineMto();
			boolean engineTypeCheck = checkUtil.checkEngineTypeForEngineAssignment(frame,
					engine,useAltEngineMto);
			if (!engineTypeCheck) {
				handleException( "Engine type is incorrect. ");
			}

			//check if engine is on hold
			if(engineUtil.isEngineOnDepartmentHold(engine.getProductId())) {
				handleException("Selected engine " + engine.getProductId()
						+ " is on hold: " + engineUtil.getHoldResult().getHoldReason());
			}

			//check if engine came from a valid previous line only if configured to do so.
			if(!engineUtil.checkValidPreviousEngineLine(engine, context.getProcessPointId())){
				handleException("Engine " + engine.getProductId() + " is from an invalid Line " + engine.getTrackingStatus());
			}
			List<String> failedProductCheckList = new ArrayList<String>();

			try {
				String installedProductCheckPoint = getProductCheckPropertyBean().getInstalledProductLastCheckPoint();
				if(StringUtils.isEmpty(installedProductCheckPoint)) {
					installedProductCheckPoint = context.getProcessPointId();
				}
				failedProductCheckList = subproductUtil.performSubProductChecks(ProductType.ENGINE.name(), subProduct, installProcessPointId);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(engine.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != failedProductCheckList.size() - 1) {
							msg.append(", ");
						}
					}
					Logger.getLogger().info(msg.toString());
	
					JOptionPane.showConfirmDialog(null, msg.toString()+"\n Parts will be saved with NG status","Failed Product Checks", JOptionPane.DEFAULT_OPTION);
					installedPart.setValidPartSerialNumber(false);
					installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
				}
			} catch (Exception e) {
				handleException("Could not peform checks against part.");
			}
			
			try {
				installedPart.setId(new InstalledPartId(getController().getState().getProductId(), rule.getPartNameString()));
				ServiceFactory.getService(EngineMarriageService.class).updateEngineAndFrame(engine, frame, (ProductBuildResult)installedPart);
				Logger.getLogger().info("Marriage successful. " + "VIN: " + frame.getProductId() + " ENGINE: " + engine.getProductId());

				subproductUtil.performSubproductTracking(ProductType.ENGINE.name(), subProduct, installProcessPointId, context.getProcessPointId());
				Logger.getLogger().info("Engine tracking status updated. " + "Tracking status set to " + processPointDao.findByKey(installProcessPointId));
			} catch (Exception e) {
				e.printStackTrace();
				handleException(e.getMessage());
			}
		}
	}
	
	protected boolean isVerifyPartSerialNumber() {
		int verificationFlag = getController().getState().getCurrentLotControlRule().getVerificationFlag();
		return verificationFlag == LotControlRuleFlag.ON.getId();
	}

	protected void checkPartSerialNumberMask() {
		List<PartSpec> partSpecs = getController().getState().getCurrentLotControlRulePartList();
		int addedDays = PropertyService.getPropertyBean(SystemPropertyBean.class,context.getProcessPointId()).getExpirationDays();
		Iterator<PartSpec> iter = partSpecs.iterator();
		currentSpec = null;
		List<String> masks = new ArrayList<String>();		

		partIdx  = 0;
		while(iter.hasNext()){
			currentSpec = iter.next();
			String partSn =  parsePartSerialNumber(currentSpec);
			String partSntoCheck = context.getProperty().isUseParsedDataCheckPartMask() ? parsePartSerialNumber(currentSpec) : installedPart.getPartSerialNumber();
			
			if(getController().getState().getCurrentLotControlRule().isDateScan()){
				if(CommonPartUtility.verifyDateMask(partSntoCheck, currentSpec.getPartSerialNumberMask(),addedDays)){
					installedPart.setPartSerialNumber(partSn);
					installedPart.setPartIndex(partIdx);
					installedPart.setValidPartSerialNumber(true);
					installedPart.setPartId(currentSpec.getId().getPartId());
				return;
				}else{
					handleException("Input Date :"+partSntoCheck +" is not valid ");
				}
			}
			else if(CommonPartUtility.verification(partSntoCheck, currentSpec.getPartSerialNumberMask(),
					PropertyService.getPartMaskWildcardFormat(), getController().getState().getProduct().getBaseProduct()))
			{
				//required for fixed length, if the input length is less than minimum length, fail the part check
				if(!StringUtils.isEmpty(currentSpec.getParseStrategy()) && 
						ParseStrategyType.valueOf(currentSpec.getParseStrategy()) == ParseStrategyType.FIXED_LENGTH) {
					int minLength = CommonPartUtility.getMinPartSnLength(currentSpec); 
					if(installedPart.getPartSerialNumber().length() < minLength)
						handleException("Input Date :"+partSntoCheck +" is not valid - Minimum part sn length check failed.");
				}
				
				Logger.getLogger().info("Part SN:", partSntoCheck, "Part Mask:", currentSpec.getPartSerialNumberMask(), " passed verification!");
				
				installedPart.setPartSerialNumber(partSn);
				installedPart.setPartIndex(partIdx);
				installedPart.setValidPartSerialNumber(true);
				installedPart.setPartId(currentSpec.getId().getPartId());
				return;
			}
			partIdx++;
			masks.add(CommonPartUtility.parsePartMask(currentSpec.getPartSerialNumberMask()));
		}
		handleException("Part serial number:" + installedPart.getPartSerialNumber() + 
				" verification failed. Masks:" + masks.toString());
	}

	private String parsePartSerialNumber(PartSpec partSpec) {
		
		String parseStrategy = partSpec.getParseStrategy();
		String parseInfo = partSpec.getParserInformation();
		String psn = CommonPartUtility.parsePartSerialNumber(partSpec,installedPart.getPartSerialNumber());
        if(psn == null) 
            handleException("Part serial number:" + installedPart.getPartSerialNumber() +  
                       " parse failed. parseStrategy:" + parseStrategy + ";parseInfo:"+parseInfo); 
        return psn;
   }

	private void checkPartSerialNumberIsNotNull(PartSerialNumber partnumber) {
		if(partnumber == null || partnumber.getPartSn() == null)
			handleException("Received part serial number is null!");
		
	}
	
	public void updateEngineSpec(Engine engine) {
		EngineLoadPropertyBean engineLoadPropertyBean = PropertyService.getPropertyBean(EngineLoadPropertyBean.class, context.getProcessPointId());
		if(engineLoadPropertyBean.isEngineMtoUpdateEnabled())
		{
			EngineLoadUtility utility = new EngineLoadUtility();
			utility.updateEngineSpec(getController().getState().getProduct().getProductId(),engine);
		}
	}

	protected void handleException(String info) {
		throw new TaskException(info, PART_SN_MESSAGE_ID);
	}
	
	public BuildAttributeDao getBuildAttributeDao() {
		if (buildAttributeDao == null) {
			buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return buildAttributeDao;
	}
	
	public FrameSpecDao getFrameSpecDao() {
		if (frameSpecDao == null) {
			frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		}
		return frameSpecDao;
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null) {
			frameDao = ServiceFactory.getDao(FrameDao.class);
		}
		return frameDao;
	}
	
	public ProcessPointDao getProcesPointDao() {
		if(processPointDao == null) {
			processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	protected int getScanCounter() {
		return scanCounter;
	}

	protected void setScanCounter(int scanCounter) {
		this.scanCounter = scanCounter;
	}

	protected int getRequiredScanCount() {
		int scanCount = getController().getState().getCurrentPartScanCount();
		if (scanCount < 1) {
			scanCount = 1;
		}
		return scanCount;
	}
	
	protected ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class,context.getProcessPointId());
	}

	protected InstalledPart getCollectedPart() {
		InstalledPart collectedPart = null;
		for (InstalledPart p : getController().getState().getProduct().getPartList()) {
			if (p.getPartName().equalsIgnoreCase(getController().getState().getCurrentLotControlRule().getPartNameString())) {
				collectedPart = p;
				break;
			}
		}
		return collectedPart;
	}
	
	protected boolean performSubProductChecks(BaseProduct subProduct, String subProductType) {
		if(subProduct instanceof MbpnProduct){
			
			MbpnProduct product = ServiceFactory.getDao(MbpnProductDao.class).findByKey(subProduct.getProductId());
			if(product.getExternalBuild() != null &&  product.getExternalBuild() == 1) return false;
		}
		return true;
	}
}
