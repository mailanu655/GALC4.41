package com.honda.galc.client.product;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductReceivedAction;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCProcessAssignmentDao;
import com.honda.galc.dao.conf.MCProductPddaPlatformDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.MCTrainingDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCProductPddaPlatform;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.vios.dto.PddaPlatformDto;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * 
 * 
 * <h3>ProductIdProcessor Class description</h3>
 * <p>
 * ProductIdProcessor description
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
 *
 * </TABLE>
 * 
 * @author Jeffray Huang<br>
 *         Mar 11, 2014
 *
 *
 */
public class VIOSProductIdProcessor extends AbstractProductIdProcessor {

	private String mode = null;

	private boolean isTrained = false;

	public VIOSProductIdProcessor(ProductController productIdController) {
		super(productIdController);
	}

	public void processInputNumber(ProductEvent event) {
		processViosProductId();
	}

	protected void processViosProductId() {
		BaseProduct product = validateAndCreateBaseProduct();
		String inputNumber = getProductController().getView().getInputPane().getProductId();
		
		//In case of Invalid product or doesn't exist in one of the product tables
		if(product == null) {
			ProductTypeData productTypeData = getProductController().getView().getMainWindow().getApplicationContext()
						.getProductTypeData();
			String productType = productTypeData == null ? "Product" : productTypeData.getProductTypeLabel();
			String msg = String.format("%s does not exist for: %s", productType, inputNumber);
			getProductController().getView().setErrorMessage(msg,
					getProductController().getView().getInputPane().getProductIdField());
			return;
		}
		String processPointId = getProductController().getProcessPointId();
		BigDecimal modelYear = null;
		String prodType = getProductController().getModel().getProductType();
		BaseProduct baseProduct = ProductTypeUtil.getProductDao(prodType).findByKey(inputNumber);
		String productSpecCode = baseProduct.getProductSpecCode();
		BaseProductSpec baseProdSpec = ProductTypeUtil.getProductSpecDao(prodType)
				.findByProductSpecCode(productSpecCode, prodType);
		if (baseProdSpec instanceof ProductSpec) {
			ProductSpec productSpec = (ProductSpec) baseProdSpec;
			String modelYearDesc = productSpec.getModelYearDescription();
			modelYear = new BigDecimal(modelYearDesc);
		}
		if (product != null) {
			boolean isProdStructExists = false;
			StructureCreateMode mode = StructureCreateMode.get(getMode());
			String modeId = (mode.equals(StructureCreateMode.DIVISION_MODE))
					? getProductController().getModel().getDivisionId()
					: getProductController().getModel().getProcessPointId();
			BaseMCProductStructure productStructure = getDao(
					StructureCreateMode.get(getMode()).getProductStructureDaoClass()).findByKey(inputNumber, modeId,
							product.getProductSpecCode());
			if (productStructure != null) {
				isProdStructExists = true;
			}
			MCProductPddaPlatform platform = ServiceFactory.getDao(MCProductPddaPlatformDao.class)
					.findByKey(inputNumber);
			if (platform != null) {
				if (productStructure == null) {
					BaseMCOrderStructure orderStructure = getDao(
							StructureCreateMode.get(getMode()).getOrderStructureDaoClass())
									.findByKey(product.getProductionLot(), modeId);
					if (orderStructure != null) {
						List<PddaPlatformDto> platformList = getDao(MCStructureDao.class)
								.findAllPlatformDetailsByStructureRev(orderStructure.getStructureRevision());
						boolean isEqual = false;
						for (PddaPlatformDto dto : platformList) {
							if (isPlatformEqual(platform, dto)) {
								isEqual = true;
								break;
							}
						}
						if (!isEqual) {
							// Remove Order Structure
							if (mode.equals(StructureCreateMode.DIVISION_MODE)) {
								getDao(MCOrderStructureDao.class)
										.removeByKey(new MCOrderStructureId(product.getProductionLot(), modeId));
							} else {
								getDao(MCOrderStructureForProcessPointDao.class).removeByKey(
										new MCOrderStructureForProcessPointId(product.getProductionLot(), modeId));
							}
						}
					}
				} else {
					isProdStructExists = true;
				}
			} else {
				// To check if there are more than one active platforms
				if (!isProdStructExists) {
					List<PddaPlatformDto> platformList = ServiceFactory.getDao(MCViosMasterPlatformDao.class)
							.findAllActivePlatformBy(processPointId, modelYear);
					Map<String, List<PddaPlatformDto>> platformMap = new HashMap<String, List<PddaPlatformDto>>();
					for (PddaPlatformDto platformDto : platformList) {
						List<PddaPlatformDto> valueList;
						if (platformMap.containsKey(getPlatformKey(platformDto))) {
							valueList = platformMap.get(getPlatformKey(platformDto));
							valueList.add(platformDto);
						} else {
							valueList = new ArrayList<PddaPlatformDto>();
							valueList.add(platformDto);
							platformMap.put(getPlatformKey(platformDto), valueList);
						}
					}
					for (Entry<String, List<PddaPlatformDto>> entry : platformMap.entrySet()) {
						if (entry.getValue().size() > 1) {
							getProductController().getView().setErrorMessage(
									"There are more than one active Platform for selected Product Id. Please contact IS.");
							return;
						}
					}
				}
			}
			
			if(!isProdStructExists) {
				//Check if property at current division
				boolean structureCreateAllowPP = PropertyService.hasProperty(ApplicationConstants.STRUCTURE_CREATE_ALLOW_PP, getProductController().getModel().getDivisionId());
				if(structureCreateAllowPP) {
					// if property is present then check which of process points mentioned as property value
					List<String> processPoints = PropertyService.getPropertyList(ApplicationConstants.STRUCTURE_CREATE_ALLOW_PP, getProductController().getModel().getDivisionId(), Delimiter.COMMA);
					//if process point is not present in Property value then show error message
					if((processPoints != null && !processPoints.contains(getProductController().getProcessPointId())) && !processPoints.isEmpty()) {
						getProductController().getView().setErrorMessage("Structures can only be created at these stations: "+processPoints);
						return;
					} 
				} 
			}
		}


		String productType = getProductController().getModel().getProductType();
		String processPoint = getProductController().getModel().getProcessPointId();
		String associateNo = getProductController().getModel().getApplicationContext().getUserId();

		ProductReceivedAction action = new ProductReceivedAction(getProductController().getView(),
				getProductController().getModel());
		if (CheckPointsRegistry.getInstance().isCheckPointConfigured(action)) {
			getProductController().getLogger().info("At CheckPoint: " + action.getCheckPointName());

			/*
			 * Based on PROCESS_POINT_LIST for PROCESS_POINT from GAL489TBX
			 * Structure will be created if there are no structures, Only when
			 * the STRUCTURE_CREATE_MODE is PROCESS_POINT_MODE. This is to show
			 * the out standing operations through OutStandingOperationChecker
			 */
			if (StringUtils.equalsIgnoreCase(getMode(), StructureCreateMode.PROCESS_POINT_MODE.toString())) {
				List<String> configuredProcessPointLst = PropertyService.getPropertyList(
						getProductController().getModel().getProcessPoint().getProcessPointId(),
						getProductController().PROCESS_POINT_LIST);
				List<ProcessPoint> processPointLst = ServiceFactory.getDao(ProcessPointDao.class)
						.getProcessPointLst(configuredProcessPointLst);
				for (ProcessPoint configuredProcessPoint : processPointLst) {
					try {
						ServiceFactory.getService(ProductStructureService.class).findOrCreateProductStructure(product,
								configuredProcessPoint, getMode());
					} catch (Exception e) {
						getProductController().getLogger()
								.error("Exception caught while creating structure for checkers : " + e.getMessage());
					}
				}
			} else if (StringUtils.equalsIgnoreCase(getMode(), StructureCreateMode.DIVISION_MODE.toString())) {
				try {
					ServiceFactory.getService(ProductStructureService.class).findOrCreateProductStructure(product,
							getProductController().getModel().getProcessPoint(), getMode());
				} catch (Exception e) {
					getProductController().getLogger()
							.error("Exception caught while creating structure for checkers : " + e.getMessage());
				}
			}

			if (!action.executeCheckers(new BaseProductCheckerData(productType, inputNumber, processPoint))) {
				getProductController().getView().getInputPane().getProductIdField().requestFocus();
				getProductController().getView().getInputPane().getProductIdField().selectAll();
				return;
			}
			CheckPointsRegistry.getInstance().unregister(action);
		}

		if (getProductController().getModel().getApplicationContext().getApplicationPropertyBean()
				.isMdrsAssignmentCheckEnabled()) {

			MCProcessAssignmentDao mcProcessAssignmentDao = ServiceFactory.getDao(MCProcessAssignmentDao.class);
			boolean isAssigned = mcProcessAssignmentDao.validateUserAssignment(associateNo, processPoint);
			String message = "";

			if (!isAssigned) {
				message += "Associate is not assigned to this task";
				getProductController().overRide("", message);
			}
		}

		try {
			BaseMCProductStructure prodStru = ServiceFactory.getService(ProductStructureService.class)
					.findOrCreateProductStructure(product, getProductController().getModel().getProcessPoint(),
							getMode());
			if (prodStru == null) {
				ProductTypeData productTypeData = getProductController().getView().getMainWindow()
						.getApplicationContext().getProductTypeData();
				String type = productTypeData == null ? "Product" : productTypeData.getProductTypeLabel();
				String msg = String.format("No VIOS structure for %s type in use by (%s,%s)", type,
						product.getProductId(), product.getProductSpecCode());
				getProductController().getView().setErrorMessage(msg);
				return;
			}
		} catch (Exception e) {
			Logger.getLogger().error(e,
					"Exception caught while retrieving Product structure @ Product Controller :" + e.getMessage());
			String msg = String.format("Error while retrieving : %s", product);
			getProductController().getView().setErrorMessage(msg);
			return;
		}
		if (getProductController().getModel().getApplicationContext().getApplicationPropertyBean()
				.isTrainingValidityCheckEnabled() && !getProductController().getModel().isTrainingMode()) {

			String plantName = getProductController().getModel().getProcessPoint().getPlantName().trim();
			String divisionId = getProductController().getModel().getProcessPoint().getDivisionId();
			String lineId = getProductController().getModel().getProcessPoint().getLineId();

			boolean trainingValidityCheckForPlant = PropertyService.getPropertyBoolean(
					getProductController().TRAINING_VALIDITY_CHECK_ENABLED, plantName.trim(), false);
			boolean trainingValidityCheckForDivision = PropertyService.getPropertyBoolean(
					getProductController().TRAINING_VALIDITY_CHECK_ENABLED, divisionId.trim(), false);
			boolean trainingValidityCheckForLine = PropertyService
					.getPropertyBoolean(getProductController().TRAINING_VALIDITY_CHECK_ENABLED, lineId.trim(), false);

			if (trainingValidityCheckForPlant && trainingValidityCheckForDivision && trainingValidityCheckForLine) {

				if (getProductController().getModel().isYearModelCodeChanged(product.getProductSpecCode())) {

					String prodSpecCode = product.getProductSpecCode();

					boolean tcOverrideCaptured = false;
					Cache cache = getProductController().getMdrsUserTrainingCache().getCache();
					if (cache != null) {
						Element element = cache
								.get(getProductController().getModel().getApplicationContext().getUserId().trim() + "-"
										+ prodSpecCode);

						if (element != null) {
							tcOverrideCaptured = true;
						}
					}
					if (!tcOverrideCaptured) {
						MCTrainingDao mcTrainingDao = ServiceFactory.getDao(MCTrainingDao.class);

						isTrained = mcTrainingDao.validateUserTraining(StringUtils.upperCase(associateNo), processPoint,
								prodSpecCode);

						String message = "";

						if (!isTrained) {
							message = " Associate does not have the required training ";
							getProductController().overRide(prodSpecCode, message);
						}
					}
				}
			}
		}

		getProductController().startProduct(product);
		
		boolean enableTorqueDevice = PropertyService.getPropertyBoolean(ApplicationConstants.DEFAULT_VIOS,ApplicationConstants.DEACTIVATE_TORQUE_DEVICES, false);
		
		if(enableTorqueDevice) {
			List<MCOperationRevision> opRevlist = getProductController().getModel().getProduct().getOperations();
			Set<String> devices = new HashSet<String>();
			//Get device ids from measurement units
			for (MCOperationRevision mcOperationRevision : opRevlist) {
				if(mcOperationRevision.getType().equals(OperationType.GALC_MEAS_MANUAL) || mcOperationRevision.getType().equals(OperationType.GALC_MEAS)
						|| mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS) || mcOperationRevision.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL))
				{
					List<MCOperationMeasurement> operationMeas = ServiceFactory.getDao(MCOperationMeasurementDao.class).findAllByOperationNamePartIdAndRevision(mcOperationRevision.getId().getOperationName(),
							mcOperationRevision.getSelectedPart().getId().getPartId(), mcOperationRevision.getSelectedPart().getId().getPartRevision());
					if(operationMeas != null && operationMeas.size() > 0 ){
						if(!operationMeas.get(0).getDeviceId().isEmpty())
							devices.add(operationMeas.get(0).getDeviceId());
					}
				}
			}
			getProductController().getLogger().info(devices.toString()+" are mapped devices to ProductId "+inputNumber);
			//Verify which devices needs to enable and disable
			for(IDevice enabledDevices : DeviceManager.getInstance().getDevices().values()){
				if(enabledDevices instanceof TorqueSocketDevice && enabledDevices.isEnabled()){
					String deviceId = enabledDevices.getId();
					IDevice device = DeviceManager.getInstance().getDevice(deviceId);
					TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
					if(!devices.contains(deviceId)){
						getProductController().getLogger().info("Torque Device "+deviceId+" is not required for product : "+inputNumber+ " and device activated flag is "+torqueDevice.isActive());
						if(torqueDevice.isActive())  {
							getProductController().getLogger().info(deviceId+": device deactivation started.");
							torqueDevice.deActive();
							torqueDevice.destroyKeepAliveTimer();
							getProductController().getLogger().info(deviceId+": device deactivated.");
						} 
					} else if(!device.isActive()){
							getProductController().getLogger().info(deviceId+": device activation started.");
							torqueDevice.startDevice();
							getProductController().getLogger().info(deviceId+": device activated");
						}
					}	
			}
		}	
		if (getProductController().getModel().getProperty().isInvokeStragglerService()
				&& getProductController().getModel().getProperty().isStragglerAsModelChange())
			getProductController().getModel().invokeStragglerService(
					getProductController().getModel().getProcessPoint(),
					getProductController().getModel().getProduct());
		EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_INPUT_OK));
		try {
			getProductController().getAudioManager().playOKSound();  //play OK sound
		} catch (Exception ex) {
			getProductController().getLogger().warn(ex, "VIOSProductIdProcessor unable to play OK sound");
		}
	}

	private boolean isPlatformEqual(MCProductPddaPlatform productPlatform, PddaPlatformDto platform) {
		return (StringUtils.isNotEmpty(productPlatform.getPlantLocCode())
				? productPlatform.getPlantLocCode().equals(platform.getPlantLocCode())
				: true && StringUtils.isNotEmpty(productPlatform.getDeptCode())
						? productPlatform.getDeptCode().equals(platform.getDeptCode())
						: true && productPlatform.getModelYearDate().floatValue() > 0
								? productPlatform.getModelYearDate().floatValue() == platform.getModelYearDate()
								: true && productPlatform.getProdSchQty().floatValue() > 0
										? productPlatform.getProdSchQty().floatValue() == platform.getProdSchQty()
										: true && StringUtils.isNotEmpty(productPlatform.getProdAsmLineNo())
												? productPlatform.getProdAsmLineNo().equals(platform.getProdAsmLineNo())
												: true && StringUtils.isNotEmpty(productPlatform.getVehicleModelCode())
														? productPlatform.getVehicleModelCode()
																.equals(platform.getVehicleModelCode())
														: true);
	}

	private String getMode() {
		if (StringUtils.isBlank(mode))
			this.mode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS,
					ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());

		return StringUtils.trim(mode);
	}

	private String getPlatformKey(PddaPlatformDto platformDto) {
		String key = platformDto.getPlantLocCode() + Delimiter.UNDERSCORE + platformDto.getDeptCode()
				+ Delimiter.UNDERSCORE + String.valueOf(platformDto.getModelYearDate()) + Delimiter.UNDERSCORE
				+ platformDto.getProdAsmLineNo() + Delimiter.UNDERSCORE + platformDto.getVehicleModelCode();
		return key;
	}

}