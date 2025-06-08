package com.honda.galc.service.tracking;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TrackException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.rest.ProductTrackDTO;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.WeldTrackResultCode;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Wade Pei <br>
 * @date Oct 22, 2013
 */
public abstract class MbpnProductProcessor<T> {

	private static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE";

	private BuildAttributeDao _buildAttributeDao;
	private MbpnProductDao _mbpnProductDao;
	private ProcessPointDao _processPointDao;
	private PreProductionLotDao preProductionLotDao;
	private ProductStampingSequenceDao productStampingSequenceDao;

	protected T item;

	private String _lineId = "";
	private Logger _logger;
	
	protected String getProductId(){
		if(item instanceof String){
			return (String)item;
		}else if(item instanceof ProductTrackDTO){
			return ((ProductTrackDTO)item).getProductId();
		}
		return null;
	}
	
	protected String getProcessPointId(){
		if(item instanceof ProductTrackDTO){
			return ((ProductTrackDTO)item).getProcessPointId();
		}
		return null;
	}
	
	protected String getProductionLot(){
		if(item instanceof ProductTrackDTO){
			return ((ProductTrackDTO)item).getOrderNo();
		}
		return null;
	}
	protected String getTerminalId(){
		return "";
	}
	
	protected String validateProduct(String productId, PreProductionLot productionLot, ProductStampingSequence pss){
		String errMsg = null;
		if (StringUtils.isBlank(productId)) {
			errMsg = "The product Id should not be blank";
		} else if (null == productionLot) {
			errMsg = "The current order has been corrupted, please check it";
		} else if (null == pss) {
			errMsg = "There's no corresponding product stamping sequence for it, please check it";
		}
		if (null!=errMsg) {
			getLogger().warn(errMsg);
		}
		return errMsg;
	}
	
	protected void actIfProductAlreadyAssigned(String productId) {
		getLogger().warn("The Product " + productId + " is already assigned.");
	}
	
	protected PreProductionLot getNextOrder(String planCode) {
		return getPreProductionLotDao().getNextPreProductionLot(planCode, PreProductionLotSendStatus.WAITING.getId());
	}
	
	protected void actIfProductIsInvalid(String productId) {
		getLogger().warn("The Product " + productId + " is invalid.");
	}
	
	protected void actWhenProcessItemFinished() {
		getLogger().warn("Finished processing Product " + getProductId());
	}
	
	protected void handleException(Exception e) {
		e.printStackTrace();
		if(e instanceof TrackException){
			throw (TrackException)e;
		}	
	}

	public boolean processItem(T item) {
		setItem(item);
		String productId = getProductId();
		getLogger().info("Processing scanned product " + productId);
		PreProductionLot curProductionLot = getCurrentProductionLot();
		if(curProductionLot == null ){
			setInfoCode(WeldTrackResultCode.ERROR);
			return false;
		}
		try {
			ProductStampingSequence pss = getNextScheduledProduct(curProductionLot);
			
			if (null!=validateProduct(productId, curProductionLot, pss)){
				actIfProductIsInvalid(productId);
				setInfoCode(WeldTrackResultCode.ERROR);
				return false;
			}
			if (isProductAlreadyAssigned(productId, curProductionLot)) {
				actIfProductAlreadyAssigned(productId);
				setInfoCode(WeldTrackResultCode.DUPLICATE_REQUEST);
				return false;
			} else {
				createMbpnProduct(productId, pss, StringUtils.trimToEmpty(curProductionLot.getProductSpecCode()));
			}
			// track product
			trackProduct(productId);
			// update ProductStampingSequence status
//			pss.setProductId(productId);
			updateProductPlanStatus(pss);
			//replace order with PreProductionLot
			performOrderFilledCheck(curProductionLot, MbpnProductHelper.getPlanCode(getProcessPointId()));
			actWhenProcessItemFinished();
			setInfoCode(WeldTrackResultCode.REQUEST_PROCESSED_OK);
		} catch (Exception e) {
			getLogger().error("Unable to process Item: " + productId);
			handleException(e);
			setInfoCode(WeldTrackResultCode.ERROR);
			return false;
		}
		return true;
	}

	private void performOrderFilledCheck(PreProductionLot curProductionLot, String planCode) {
		int productLotFilledQTY = getProductStampingSequenceDao().getFilledStampCount(curProductionLot.getProductionLot());
		if(productLotFilledQTY >= curProductionLot.getLotSize()){
			updatePreProductionLotStatus(curProductionLot, PreProductionLotSendStatus.DONE);
			//get next productionLot by current productionLot and planCode
			PreProductionLot nextProductionLot = getPreProductionLotDao().getNextByLastPreProductionLot(curProductionLot.getNextProductionLot(), planCode);
			//update next productionLot status to current
			this.updatePreProductionLotStatus(nextProductionLot, PreProductionLotSendStatus.INPROGRESS);
			getLogger().info("PreProductionLot " + curProductionLot.getProductionLot() + " completed and status updated to " + PreProductionLotSendStatus.DONE);
			getLogger().info("PreProductionLot " + nextProductionLot.getProductionLot() + " started and status updated to " + PreProductionLotSendStatus.INPROGRESS);
		}else{
			updatePreProductionLotStatus(curProductionLot, PreProductionLotSendStatus.INPROGRESS);
		}
	}

	protected boolean isProductAlreadyAssigned(String productId, PreProductionLot productionLot) {
		try {
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			if (mbpnProduct != null) {
				return true;
			}
		} catch (Exception e) {
			getLogger().info("Could not check if MbpnProduct " + productId + " was already created");
			handleException(e);
		}
		return false;
	}

	private void createMbpnProduct(String productId, ProductStampingSequence pss, String productSpecCode) {
		try {
			MbpnProduct mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			getLogger().info("New MbpnProduct " + productId + " created");

			String currOrderNumber = StringUtils.trimToEmpty(pss.getId().getProductionLot());
			mbpnProduct.setCurrentOrderNo(currOrderNumber);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProduct.setTrackingStatus(StringUtils.trimToEmpty(getLineId()));
			mbpnProduct.setLastPassingProcessPointId(getProcessPointId());
			mbpnProduct.setTrackingSeq(Double.valueOf(pss.getStampingSequenceNumber().toString()));
			mbpnProduct.setContainerId(null);
			mbpnProduct.setContainerPos(null);
			getMbpnProductDao().save(mbpnProduct);
		} catch (Exception e) {
			getLogger().error("Unable to create MbpnProduct " + productId);
			handleException(e);
		}
	}

	private void updatePreProductionLotStatus(PreProductionLot preProductionLot, PreProductionLotSendStatus status){
		preProductionLot.setSendStatus(status);
		getPreProductionLotDao().save(preProductionLot);
		getLogger().info("Updated PreProductionLot status for PreProductionLot " + preProductionLot.getProductionLot() + " to " + status);
	}

	public PreProductionLot getCurrentProductionLot(){
		PreProductionLot curProductionLot = null;
		String productionLot = getProductionLot();
		String planCode = MbpnProductHelper.getPlanCode(getProcessPointId());
		if(StringUtils.isEmpty(productionLot) || StringUtils.isEmpty(planCode)){
			return null;
		}
		if(StringUtils.isBlank(productionLot)){
			curProductionLot = getPreProductionLotDao().findCurrentPreProductionLotByPlanCode(planCode);
		}else{
			curProductionLot = getPreProductionLotDao().findByProductionLotAndPlanCode(productionLot, planCode);
		}
		return curProductionLot;
	}

	public ProductStampingSequence getNextScheduledProduct(PreProductionLot preProductionLot){
		return getProductStampingSequenceDao().findNextProduct(getProductionLot(preProductionLot), PlanStatus.SCHEDULED);
	}

	private String getProductionLot(PreProductionLot preProductionLot){
		return StringUtils.trimToEmpty(preProductionLot.getProductionLot());
	}

	public MbpnProductDao getMbpnProductDao() {
		if (_mbpnProductDao == null){
			_mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		}
		return _mbpnProductDao;
	}

	public BuildAttributeDao getBuildAttributeDao() {
		if (_buildAttributeDao == null){
			_buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return _buildAttributeDao;
	}

	private void updateProductPlanStatus(ProductStampingSequence pss) {
		pss.setSendStatus(PlanStatus.ASSIGNED.getId());
		getProductStampingSequenceDao().save(pss);
		getLogger().info("Updated ProductStampingSequence status for product " + pss.getId().getProductID() + " to " + PlanStatus.ASSIGNED);
	}

	protected boolean trackProduct(String productId) {
		try {
			String productType = PropertyService.getProperty(getProcessPointId(), PRODUCT_TYPE_KEY);
			if (productType != null) {
				ProductResult productResult = new ProductResult(productId, getProcessPointId(), getProcessPointDao().getDatabaseTimeStamp());
				getTrackingService().track(ProductType.getType(productType), productResult);
				getLogger().info("Tracking Successfully completed for product " + productId);
			} else {
				getLogger().error("Product Type is unknown for product " + productId);
			}
		} catch (Exception e) {
			getLogger().info("Tracking was not Successful for product " + productId);
			handleException(e);
			return false;
		}
		return true;
	}

	public String getLineId() {
		if (_lineId.equals("")) {
			try {
				ProcessPoint pp = getProcessPointDao().findByKey(getProcessPointId());
				_lineId = pp.getLineId();
			} catch (Exception e) {
				handleException(e);
			}
		}
		return _lineId;
	}

	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}

	public ProcessPointDao getProcessPointDao() {
		if (_processPointDao == null){
			_processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return _processPointDao;
	}

	protected Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(StringUtils.isBlank(getTerminalId())?getProcessPointId():getTerminalId() + "_" + getProcessPointId());
			_logger.getLogContext().setApplicationInfoNeeded(true);
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		_logger.getLogContext().setThreadName(getProcessPointId() + "-" + Thread.currentThread().getName());
		return _logger;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}
	
	protected void setInfoCode(WeldTrackResultCode weldTrackResultCode){
		if(item instanceof ProductTrackDTO){
			((ProductTrackDTO)getItem()).setINFO_CODE(weldTrackResultCode.getInfoCode());
			((ProductTrackDTO)getItem()).setINFO_MSG(weldTrackResultCode.getInfoMsg());
		}
	}

	public PreProductionLotDao getPreProductionLotDao() {
		if(preProductionLotDao == null){
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		}
		return preProductionLotDao;
	}

	public ProductStampingSequenceDao getProductStampingSequenceDao() {
		if(productStampingSequenceDao == null){
			productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		}
		return productStampingSequenceDao;
	}

}
