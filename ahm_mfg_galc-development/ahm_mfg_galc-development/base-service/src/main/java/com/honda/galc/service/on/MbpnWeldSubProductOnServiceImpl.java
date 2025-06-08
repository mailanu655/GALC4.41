package com.honda.galc.service.on;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCProductPddaPlatformDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.MbpnData;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCProductPddaPlatform;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * 
 * <h3>MbpnWeldSubProductOnServiceImpl</h3> <h3>Class description</h3> <h4>
 * Description</h4>
 * <p>
 * ProductDataCollectorBase description
 * </p>
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
 * <TD>Alok Ghode</TD>
 * <TD>Apr 14, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @see
 * @version 0.1
 * @author Alok Ghode
 * @since Apr 14, 2015
 */
public class MbpnWeldSubProductOnServiceImpl extends ProductOnServiceImpl
		implements MbpnSubProductOnService {
	PreProductionLot currentPreProductionLot;
	private String planCode;
	public ProductStampingSequenceDao productStampingSequenceDao;
	public ProductStructureService productStructureService;
	public MbpnProductDao mbpnProductDao;
	private String _lineId;
	private ProcessPointDao processPointDao;
	private String orderNo;
	private String productSpecCode;
	private String divisionId;
	private PddaPlatformDto pddaPlatform;
	
	@Override
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
		currentPreProductionLot = null;
	}

	protected void processSingleProduct() {
		try {
			if(getCurrentPreProductionLot()!=null) {
				productId = (String) context.get(getFullTag(TagNames.PRODUCT_ID
						.name()));
				//Get PDDA Platform
				String pddaPlatformJson = (String) context.get(getFullTag(TagNames.PDDA_PLATFORM
						.name()));
				pddaPlatform = new Gson().fromJson(pddaPlatformJson, PddaPlatformDto.class);
				
				//Current Order Number
				orderNo = getCurrentPreProductionLot().getProductionLot();
				//Current product spec code
				productSpecCode = getCurrentPreProductionLot().getProductSpecCode();
				
				validateProductId();

				if (executeCheckers(prepareInputData())) {

					saveMbpnProduct(productId, productSpecCode, orderNo);
					// GALC validate Product ID exits
					retrieveProduct(getFullTag(TagNames.PRODUCT_ID.name()));

					// GALC update Order (GAL216TBX, GAL212TBX, GAL217TBX)
					createProductStampingSequence(productId, orderNo);
					updatePreproductionLot();

					// This is for tracking
					if (getPropertyBean().isAutoTracking())
						track();
					contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(),
							LineSideContainerValue.COMPLETE);
					logAndNotify(MessageType.INFO,"");
				}
			} else {
				logAndNotify(MessageType.WARN, "No Current Pre-Production Lot. Showing VIOS instructions.");
			}
		} catch (TaskException te) {
			logAndNotify(MessageType.ERROR, te.getMessage());
		} catch (Exception e) {
			getLogger().error(e, " Exception to process Product On.");
			logAndNotify(MessageType.ERROR, "An exception occurred: ",
					productId, ": ", e.getMessage());

		}

	}

	/**
	 * Get current pre-production lot for plan code set in properties
	 * 
	 * @return pre-production lot
	 */
	private PreProductionLot getCurrentPreProductionLot() {
		if (currentPreProductionLot == null)
			currentPreProductionLot = getPreProductionLotDao()
					.findFirstUpcomingLotByPlanCode(getPlanCode());
		return currentPreProductionLot;

	}

	private void createProductStampingSequence(String productId, String orderNo) {
		try {
			int stampingSequenceNumber = getProductStampingSequenceDao()
					.findStampCount(orderNo);
			ProductStampingSequence productStampSequence = new ProductStampingSequence();
			productStampSequence.setProductId(productId);
			productStampSequence.setProductionLot(orderNo);
			productStampSequence
					.setStampingSequenceNumber(stampingSequenceNumber);
			productStampSequence.setSendStatus(2);
			getProductStampingSequenceDao().save(productStampSequence);
			getLogger()
					.info("ProductStampingSequence record saved" + productId);
		} catch (Exception e) {
			getLogger().error(e,
					"Unable to create ProductStampingSequence " + productId);
			throw new TaskException(
					"Exception to create Product Stamping Sequence: "
							+ productId);

		}
	}

	private String getPlanCode() {
		if (planCode == null)
			planCode = getPropertyBean().getPlanCode();

		return planCode;
	}

	private ProductStampingSequenceDao getProductStampingSequenceDao() {
		if (productStampingSequenceDao == null) {
			productStampingSequenceDao = ServiceFactory
					.getDao(ProductStampingSequenceDao.class);
		}
		return productStampingSequenceDao;
	}
	
	private ProductStructureService getProductStructureService() {
		if (productStructureService == null) {
			productStructureService = ServiceFactory
					.getService(ProductStructureService.class);
		}
		return productStructureService;
	}

	private void saveMbpnProduct(String productId, String productSpecCode,
			String orderNo) {
		try {
			MbpnProduct mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProduct.setCurrentOrderNo(orderNo);
			mbpnProduct.setTrackingStatus(StringUtils.trimToEmpty(getLineId()));
			mbpnProduct.setLastPassingProcessPointId(getProcessPointId());
			mbpnProduct
					.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			getMbpnProductDao().save(mbpnProduct);
			getLogger().info("New MbpnProduct " + productId + " created");
		} catch (Exception e) {
			getLogger().error(e, "Unable to create MbpnProduct " + productId);
			throw new TaskException("Unable to create MbpnProduct: "
					+ productId);
		}
	}

	public MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}

	public String getLineId() {
		if (_lineId == null) {
			try {
				ProcessPoint pp = getProcesPointDao().findByKey(
						getProcessPointId());
				_lineId = pp.getLineId();
			} catch (Exception e) {
				getLogger().warn(
						e,
						"WARN: Unable to find line id for process point "
								+ getProcessPointId());
			}
		}
		return _lineId;
	}

	public String getDivisionId() {
		if (divisionId == null) {
			try {
				ProcessPoint pp = getProcesPointDao().findByKey(
						getProcessPointId());
				divisionId = pp.getDivisionId();
			} catch (Exception e) {
				getLogger().warn(
						e,
						"WARN: Unable to find line id for process point "
								+ getProcessPointId());
			}
		}
		return divisionId;
	}

	public ProcessPointDao getProcesPointDao() {
		if (processPointDao == null) {
			processPointDao = getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}

	private MbpnData prepareInputData() {
		MbpnData inputData = new MbpnData();
		inputData.setProductId(productId);
		inputData.setProductSpecCode(productSpecCode);
		inputData.setProcessPointId(getProcessPointId());
		inputData.setOrderNo(orderNo);
		inputData.setPddaPlatform(pddaPlatform);
		return inputData;
	}

	public boolean executeCheckers(MbpnData mbpnData) throws Exception {
		List<String> warningMessage = new ArrayList<String>();
		//Executing checkers before creating the structure
		SortedArrayList<MCAppChecker> checkers = CheckerUtil.getAppCheckers(
				getProcessPointId(), CheckPoints.BEFORE_STRUCTURE_CREATE.toString());
		warningMessage.addAll(executeCheckers(checkers, mbpnData));
		if(warningMessage.isEmpty()) {
			//No warnings occur before creating structure
			//Insert PDDA Platform details in MC_PRODUCT_PDDA_PLATFORM_TBX table
			if(pddaPlatform != null && productId != null) {
				MCProductPddaPlatform platform = new MCProductPddaPlatform(productId, pddaPlatform);
				ServiceFactory.getDao(MCProductPddaPlatformDao.class).save(platform);
			}
			//Getting checkers (if any) configured to execute after structure create
			checkers = CheckerUtil.getAppCheckers(
					getProcessPointId(), CheckPoints.AFTER_STRUCTURE_CREATE.toString());
			if(checkers!=null && !checkers.isEmpty()) {
				MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
				if(mbpnProduct!=null) {
					//Creating product structure for process point if it does not exist
					try {
						ProcessPoint pp = getProcesPointDao().findByKey(
								getProcessPointId());
						getProductStructureService().findOrCreateProductStructure(mbpnProduct, pp, PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()));
					} catch(Exception e) {
						getLogger().error(e, "Ignoring Exception occurred while creating structure before executing checkers at check point "
								+ CheckPoints.AFTER_STRUCTURE_CREATE.toString());
					}
					
					//Executing checkers after structure create
					warningMessage.addAll(executeCheckers(checkers, mbpnData));
				}
			}
		}
		if (!warningMessage.isEmpty()) {
			String wMessage = StringUtils.join(warningMessage, Delimiter.NEW_LINE);
			logAndNotify(MessageType.WARN, wMessage);
			return false;
		}
		return true;
	}
	
	private List<String> executeCheckers(SortedArrayList<MCAppChecker> checkers, MbpnData mbpnData) {
		List<String> warningMessage = new ArrayList<String>();
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		for (MCAppChecker appChecker : checkers) {
			getLogger().info("Executing checker: " + appChecker.getCheckName());
			AbstractBaseChecker<MbpnData> checker = CheckerUtil.createChecker(appChecker.getChecker(), MbpnData.class);
			checker.setReactionType(appChecker.getReactionType());
			List<CheckResult> ckResults = checker.executeCheck(mbpnData);
			checkResults.addAll(ckResults);
			for (CheckResult checkResult : ckResults) {
				if (checkResult.getReactionType().ordinal() > 0) {
					throw new TaskException(checkResult.getCheckMessage());
				} else {
					warningMessage.add(checkResult.getCheckMessage());
				}
			}
		}
		return warningMessage;
	}

	/**
	 * Validate Product ID matches ProductNumberDef by using PRODUCT_TYPE
	 * property configured on the process point
	 * 
	 * @return validation status
	 */
	private void validateProductId() {
		if (!ServiceUtil.validateProductIdNumber(getPropertyBean()
				.getProductType(), productId, context, productName)) {
			getLogger().warn("Invalid product id format:", productId);
			throw new TaskException("Invalid product id format: " + productId);
		}

	}

	protected void updatePreproductionLot() {

		preProductionLot = getPreProductionLot();

		if (preProductionLot == null) {
			getLogger()
					.warn("Preproduction Lot is empty, so not update pre-production lot.");
			return;
		}

		if (preProductionLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS) {
			updateStampCount(preProductionLot);
			if (isLotCompleted(preProductionLot))
				completeLot(preProductionLot);
		} else if (preProductionLot.getSendStatus() == PreProductionLotSendStatus.WAITING) {
			updateStampCount(preProductionLot);
			if (isLotCompleted(preProductionLot)) { // For lot size == 1
				completeLot(preProductionLot);
			} else {
				startLot(preProductionLot);
			}

		} else {
			// still update stamp count
			updateStampCount(preProductionLot);
		}

		invokeNotification(preProductionLot);

	}

	@Override
	protected void invokeNotification(PreProductionLot preProdLot) {
		ServiceFactory.getNotificationService(IProductOnNotification.class, processPointId)
				.execute(product.getProductId(),
						preProdLot.getProductionLot(),
						preProdLot.getPlanCode(), preProdLot.getStampedCount());

	}
	
	@Override
	protected void invokeNotification(String productId, String msg, MessageType mtype) {
		ServiceFactory.getNotificationService(IProductOnNotification.class, processPointId).execute(productId, msg, getPlanCode(), mtype);
		
	}

}
