package com.honda.galc.client.teamlead.ltCtrRepair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;


import javafx.collections.FXCollections;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.checkers.PartMaskChecker;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.checkers.TorqueMeasurementChecker;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.fx.ManualLotControlRepairActions;
import com.honda.galc.client.teamleader.fx.ManualLotControlResultsDialog;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.InstalledPartHistoryDao;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.device.dataformat.MeasurementInputData;
import com.honda.galc.device.dataformat.MeasurementValue;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.device.dataformat.Torque;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartHistory;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

public class ManualLotControlResultsController {

	BaseProduct baseProduct;
	PartResult partResult;
	PartSpec partSpec;
	MCOperationRevision operation;
	ManualLotControlResultsDialog view;

	public ManualLotControlResultsController(BaseProduct baseProduct, PartResult partResult,
			MCOperationRevision operation, ManualLotControlResultsDialog dialog) {
		this.baseProduct = baseProduct;
		this.partResult = partResult;
		this.operation = operation;
		this.view = dialog;
	}

	public void populateComponents(ManualLotControlRepairActions action) {
		if (baseProduct != null) {
			this.view.getProductIdTextField().setText(baseProduct.getProductId());
			this.view.getProductTypeTextField().setText(baseProduct.getProductSpecCode());
			if(partResult != null) {
				this.view.getOperationNameTextField().setText(partResult.getPartName());
				if(operation != null) {
					this.view.getPartDesc().setText(operation.getDescription());
				}
			}

			if (action == ManualLotControlRepairActions.EDIT_RESULTS) {
				if (operation != null) {
					int i = 0;
					int j = 0;
					for (MCOperationPartRevision part : operation.getParts()) {
						if (partResult.getInstalledPart() != null && (partResult.getInstalledPart().getPartId()
								.equalsIgnoreCase(part.getId().getPartId())
								&& partResult.getInstalledPart().getPartRevision() == part.getId().getPartRevision())) {
							if (ManualLotCtrRepairUtil.hasScanPart(operation)) {
								this.view.getInputFields().get(i)
									.setText(partResult.getBuildResult().getPartSerialNumber());
							}
						}

						if (partResult.getBuildResult() != null && partResult.getInstalledPart().getPartId().equalsIgnoreCase(part.getId().getPartId())) {
							List<Measurement> measurements = partResult.getBuildResult().getMeasurements();
							for (MCOperationMeasurement opMeas : part.getMeasurements()) {
								for (Measurement measurement : measurements) {
									if (opMeas.getId().getMeasurementSeqNum() == measurement.getId()
											.getMeasurementSequenceNumber()) {

										this.view.getMeasRejectFields().get(j)
												.setText(String.valueOf(measurement.getMeasurementValue()));
										j++;
									}

								}

							}
						}

						i++;
					}
				} else {
					LotControlRule rule = partResult.getLotControlRule();
					if (rule != null) {
						int i = 0;
						int j = 0;
						for (PartSpec part : rule.getParts()) {
							if(rule.getSerialNumberScanType() != PartSerialNumberScanType.NONE){
								if (partResult.getInstalledPart() != null && partResult.getInstalledPart().getPartId()
										.equalsIgnoreCase(part.getId().getPartId())) {
									this.view.getInputFields().get(i)
											.setText(partResult.getBuildResult().getPartSerialNumber());
								}
							}

							if (partResult.getBuildResult() != null && partResult.getInstalledPart().getPartId().equalsIgnoreCase(part.getId().getPartId())) {
								List<Measurement> measurements = partResult.getBuildResult().getMeasurements();
								for (MeasurementSpec measSpec : part.getMeasurementSpecs()) {
									for (Measurement measurement : measurements) {
										if (measSpec.getId().getMeasurementSeqNum() == measurement.getId()
												.getMeasurementSequenceNumber()) {
											this.view.getMeasRejectFields().get(j)
													.setText(String.valueOf(measurement.getMeasurementValue()));
											j++;
										}
									}
									
								}
							}

							i++;
						}
					} else {
						if (partResult.getInstalledPart() != null) {
							if(!this.view.getInputFields().isEmpty()) {
								this.view.getInputFields().get(0)
										.setText(partResult.getBuildResult().getPartSerialNumber());
							}
						}
					}
				}

			} else if(action == ManualLotControlRepairActions.SHOW_HISTORY) {

				List<InstalledPartHistory> partHistory = new ArrayList<InstalledPartHistory>();
				partHistory = ServiceFactory.getDao(InstalledPartHistoryDao.class)
						.findAllByProductIdAndOperationName(baseProduct.getProductId(), partResult.getPartName());

				List<MeasurementAttempt> measurementHistory = new ArrayList<MeasurementAttempt>();
				measurementHistory = ServiceFactory.getDao(MeasurementAttemptDao.class)
						.findAllByProductIdAndOperationName(baseProduct.getProductId(), partResult.getPartName());

				this.view.getPartInformationTableView().setItems(FXCollections.observableArrayList(partHistory));
				this.view.getMeasurementInformationTableView()
						.setItems(FXCollections.observableArrayList(measurementHistory));
			}
		}
	}

	public void rejectMeasurement(Measurement meas) {
		List<Measurement> measurements = partResult.getBuildResult().getMeasurements();
		List<Measurement> tempMeasurements = new ArrayList<Measurement>();
		tempMeasurements.addAll(measurements);
		int i = 0;
		for (Measurement measurement : tempMeasurements) {
			if (measurement.getId().getMeasurementSequenceNumber() == meas.getId().getMeasurementSequenceNumber()) {
				measurements.remove(i);
				i--;
			}
			i++;
		}

		deleteMeasurement(meas);
	}

	private void deleteMeasurement(Measurement meas) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		measurementDao.remove(meas);
	}

	public void addMeasurement(Measurement meas) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		measurementDao.saveAll(Arrays.asList(meas));

	}

	public void rejectPart(String partName) {
		InstalledPart installedPart = partResult.getInstalledPart();
		if (installedPart != null) {
			for (Measurement meas : installedPart.getMeasurements()) {
				deleteMeasurement(meas);
			}
			InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
			installedPartDao.remove(installedPart);
		}

	}

	protected void validatePartSnLotControl(String partSn ,PartSpec partSpecVal) {
		List<PartSpec>  partspecList = new ArrayList<PartSpec>();
		partspecList.add(partSpecVal);
		if (partResult.getLotControlRule().isVerify()) {
			partSpec = checkPartSnMask(partSn ,partspecList);
			if (partSpec == null) {
				String msg = "failed part serial number verification for :" + partSn + " part Mask:"
						+ partSpecVal.getPartSerialNumberMask();
				this.view.setErrorMessage(msg);
				Logger.getLogger().warn(msg);
				throw new TaskException("Verification error " + partSpecVal.getPartSerialNumberMask());
			}
		} else {
				partSpec = partSpecVal;
		}

		if (partResult.getLotControlRule().isUnique())
			checkDuplicatePart(partSn);


	}

	private PartSpec checkPartSnMask(String partSn , List<PartSpec> partSpecs) {
		return CommonPartUtility.verify(partSn, partSpecs,
				PropertyService.getPartMaskWildcardFormat());
	}

	protected void checkDuplicatePart(String partSn) {
		List<InstalledPart> installedParts = ServiceFactory.getDao(InstalledPartDao.class)
				.findAllByPartNameAndSerialNumber(partResult.getLotControlRule().getPartName().getPartName(), partSn);

		for (InstalledPart part : installedParts) {

			if (!part.getId().getProductId().equals(baseProduct.getProductId())) {
				String msg = "Duplicate part # with product:" + part.getId().getProductId();
				this.view.setErrorMessage(msg);
				throw new TaskException(msg);
			}
		}

	}
	

	public boolean confirmMeasurement(String measValue, MeasurementSpec measSpec) {

		try {
			this.view.setErrorMessage(null);
			double doubleToqueVlue = new Double(measValue).doubleValue();
			checkTorqueValue(doubleToqueVlue, measSpec);
			return true;
		} catch (BaseException te) {
			this.view.setErrorMessage(te.getMessage());
		} catch (Exception e) {
			Logger.getLogger().error(e, "Error to confirm torque value.");
			this.view.setErrorMessage(e.toString());
		}

		return false;
	}

	private void checkTorqueValue(double doubleToqueVlue, MeasurementSpec measurementSpec) {

		if (measurementSpec == null)
			return;
		if (measurementSpec.getMaximumLimit() == 0 && measurementSpec.getMinimumLimit() == 0)
			return;
		if (doubleToqueVlue > measurementSpec.getMaximumLimit() || doubleToqueVlue < measurementSpec.getMinimumLimit())
			throw new TaskException("Invalid torque value " + doubleToqueVlue + " Max:"
					+ measurementSpec.getMaximumLimit() + " Min:" + measurementSpec.getMinimumLimit());

	}

	private void createAndSaveInstalledPart(String partSerialNumber, List<Measurement> measurements) {
		InstalledPart installedPart = null;
		String productId = baseProduct.getProductId();
		String partName = ManualLotControlRepairActions.isInsertResultsAction(view.getAction()) ? view.getPartNameTextField().getText() : partResult.getPartName();
		if (installedPart == null) {
			installedPart = new InstalledPart(productId, partName);

		}

		installedPart.setAssociateNo(ClientMainFx.getInstance().getApplicationContext().getUserId());
		installedPart.setProcessPointId(null);
		installedPart.setPartSerialNumber(partSerialNumber);

		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);

		if (operation != null) {
			installedPart.setOperationRevision(operation.getId().getOperationRevision());
			if (operation.getSelectedPart() == null) {
				installedPart.setPartId("");
			} else {
				installedPart.setPartId(operation.getSelectedPart().getId().getPartId());
				installedPart.setPartRevision(operation.getSelectedPart().getId().getPartRevision());
			}
		} else {
			if (partResult != null && partResult.getLotControlRule() != null && partSpec != null) {
				installedPart.setPartId(partSpec.getId().getPartId());
			}
		}
		if(view.getAction() == ManualLotControlRepairActions.INSERT_RESULTS ||
				(partResult != null && partResult.getInstalledPart() != null && 
				StringUtils.trimToEmpty(partResult.getInstalledPart().getInstalledPartReason()).equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT))) {
			installedPart.setInstalledPartReason(ApplicationConstants.MANUAL_INSERT);
		} else {
			installedPart.setInstalledPartReason(DefectStatus.REPAIRED.name());
		}
		installedPart.setValidPartSerialNumber(true);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));

		if (measurements != null) {
			installedPart.setMeasurements(measurements);
		}
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		installedPartDao.saveAll(Arrays.asList(installedPart)) ;

	}
	/**
	 * This method is used to check if the Installed Part already exists or not
	 */
	public boolean isPartNameExist(String partName) {
		String productId = baseProduct.getProductId();
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		return installedPartDao.findByKey(new InstalledPartId(productId, partName)) != null;
	}

	public boolean validatePart(String part) {
		if(ManualLotCtrRepairUtil.hasScanPart(operation))
		{
			List<String> warningMessage = new ArrayList<String>();
			MCOperationPartRevision opPart = operation.getSelectedPart();
			PartSerialScanData partSerialScanData = new PartSerialScanData(opPart.getPartMask(), part,
					baseProduct.getProductType().getProductName(), baseProduct.getProductId());
	
			List<MCPartChecker> partCheckers = getPartCheckers();
			partCheckers.add(getDefaultPartChecker());
	
			for (MCPartChecker partChecker : partCheckers) {
				Logger.getLogger().info("Executing checker: " + partChecker.getCheckName());
				AbstractBaseChecker<PartSerialScanData> checker = CheckerUtil.createChecker(partChecker.getChecker(),
						PartSerialScanData.class);
				if (checker != null) {
					checker.setOperation(operation);
					checker.setReactionType(partChecker.getReactionType());
	
					List<CheckResult> ckResults = checker.executeCheck(partSerialScanData);
	
					for (CheckResult checkResult : ckResults) {
						if (checkResult.getReactionType().equals(ReactionType.DISPLAY_ERR_MSG)) {
							warningMessage.add(checkResult.getCheckMessage());
						}
					}
				}
			}
	
			if (warningMessage.size() > 0) {
				String errorMessage = "";
				for (String msg : warningMessage) {
					errorMessage = errorMessage + msg + " \n";
				}
				this.view.setErrorMessage(errorMessage);
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	public List<MCPartChecker> getPartCheckers() {
		List<MCPartChecker> partCheckers = ServiceFactory.getDao(MCPartCheckerDao.class)
				.findAllBy(operation.getId().getOperationName(), operation.getId().getOperationRevision());

		return partCheckers;
	}

	private MCPartChecker getDefaultPartChecker() {

		MCPartCheckerId id = new MCPartCheckerId();
		id.setCheckSeq(0);
		MCPartChecker partMaskChecker = new MCPartChecker();
		partMaskChecker.setId(id);
		partMaskChecker.setCheckName("PART_MASK_CHECK");
		partMaskChecker.setChecker(PartMaskChecker.class.getCanonicalName());

		return partMaskChecker;
	}

	protected MeasurementId createMeasurementId(int measSeqNum) {
		return new MeasurementId(baseProduct.getProductId(), partResult.getPartName(), measSeqNum);
	}

	public void savePart(String part, List<Measurement> measurements) {
		if (operation != null || 
				ManualLotControlRepairActions.isInsertResultsAction(view.getAction()) ||
				partResult.getInstalledPart().getInstalledPartReason().equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT)) {
			if (validatePart(part)) {
				createAndSaveInstalledPart(part, measurements);
			}else{
				return;
			}
		}

		this.view.setMessage("Data Saved");
	}

	public void saveMeasurement(String measValue, MCOperationMeasurement m) {
		if (validateMeasurement(measValue, m.getId().getMeasurementSeqNum(), m)) {
			String partId = operation.getSelectedPart().getId().getPartId();
			int partRevision = operation.getSelectedPart().getId().getPartRevision();
			Measurement measurement = createMeasurement(measValue, m.getId().getMeasurementSeqNum(), partId,
					partRevision);
			savePartAndMeasurement(measurement);
		}

	}

	public void saveLotControlMeasurement(String measValue, MeasurementSpec mSpec ,PartSpec partSpecValue) {
		partSpec = partSpecValue ;
		if (confirmMeasurement(measValue, mSpec)) {
			Measurement measurement = createMeasurement(measValue, mSpec.getId().getMeasurementSeqNum(), partSpecValue.getId().getPartId(), 0);
			savePartAndMeasurement(measurement);
		}

	}

	public void savePartAndMeasurement(Measurement measurement) {
		try {
			InstalledPart installedPart = partResult.getInstalledPart();
			if(installedPart == null ){
				installedPart = new InstalledPart(measurement.getId().getProductId(), measurement.getId().getPartName());
				installedPart.setMeasurements(Arrays.asList(measurement));
				if(operation != null)
					savePart(StringUtils.isBlank(installedPart.getPartSerialNumber()) ? null : installedPart.getPartSerialNumber(), Arrays.asList(measurement));
				else
					saveLotControlPart(StringUtils.isBlank(installedPart.getPartSerialNumber()) ? null : installedPart.getPartSerialNumber(), Arrays.asList(measurement) ,partSpec);
			}else{
				addMeasurement(measurement); 
				LotControlRule rule = partResult.getLotControlRule();
				//part Mask is not associated
				if(rule!= null && rule.getSerialNumberScanType().equals(PartSerialNumberScanType.NONE)){
					installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
					installedPart.setAssociateNo(ClientMainFx.getInstance().getApplicationContext().getUserId());
					installedPart.setInstalledPartReason(DefectStatus.REPAIRED.name());
					installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
					InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
					installedPartDao.saveAll(Arrays.asList(installedPart));
				}
			}
			this.view.setMessage("Data Saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to delete all measurements by Product ID and Installed Part Name
	 */
	public void deleteMeasurements() {
		ServiceFactory.getDao(MeasurementDao.class).deleteAll(baseProduct.getProductId(), 
				partResult.getInstalledPart().getPartName());
	}

	private boolean validateMeasurement(String measVal, int meaSeq, MCOperationMeasurement m) {
		MeasurementValue measurementVal = new MeasurementValue();
		try{
			measurementVal.setMeasurementValue(Double.parseDouble(measVal));
			measurementVal.setMeasurementIndex(meaSeq);
	
			List<String> warningMessage = new ArrayList<String>();
			List<MCMeasurementChecker> measCheckers = ServiceFactory.getDao(MCMeasurementCheckerDao.class)
					.findAllBy(operation.getId().getOperationName(), operation.getId().getOperationRevision());
	
			measCheckers.add(addDefaultMeasurementCheckers(measCheckers.size()));
	
			for (MCMeasurementChecker measChecker : measCheckers) {
				Logger.getLogger().info("Executing checker: " + measChecker.getCheckName());
				AbstractBaseChecker<MeasurementInputData> checker = CheckerUtil.createChecker(measChecker.getChecker(),
						MeasurementInputData.class);
				checker.setReactionType(measChecker.getReactionType());
				checker.setOperation(operation);
				checker.setMeasurement(m);
				Torque torque = new Torque();
				torque.setMeasurementValue(measurementVal.getMeasurementValue());
				torque.setMeasurementIndex(measurementVal.getMeasurementIndex());
				torque.setAngleStatus(1);
				torque.setTighteningStatus(1);
				torque.setTorqueStatus(1);
	
				List<CheckResult> ckResults = checker.executeCheck(torque);
				for (CheckResult checkResult : ckResults) {
					if (checkResult.getReactionType().equals(ReactionType.DISPLAY_ERR_MSG)) {
						warningMessage.add(checkResult.getCheckMessage());
					}
				}
			}
	
			if (warningMessage.size() > 0) {
				String errorMessage = "";
				for (String msg : warningMessage) {
					errorMessage = errorMessage + msg + " \n";
				}
				this.view.setErrorMessage(errorMessage);
				return false;
			} else {
				return true;
			}
		}catch (NumberFormatException exception){
			this.view.setErrorMessage("Please enter valid measurement value");
			return false;
		}

	}

	public Measurement createMeasurement(String value, int measurementSequenceNumber, String partId,
			int partRevision) {
		String productId = baseProduct.getProductId();
		String partName = ManualLotControlRepairActions.isInsertResultsAction(view.getAction()) ? view.getPartNameTextField().getText() : partResult.getPartName();

		Measurement measurement = new Measurement(productId, partName, measurementSequenceNumber);
		measurement.setPartId(partId);
		measurement.setPartRevision(partRevision);
		measurement.setMeasurementStatus(MeasurementStatus.OK);

		measurement.setMeasurementValue(Double.parseDouble(value));

		return measurement;
	}

	MCMeasurementChecker addDefaultMeasurementCheckers(int size) {

		MCMeasurementCheckerId id = new MCMeasurementCheckerId();
		id.setCheckSeq(size);
		id.setCheckName("MEASUREMENT_CHECK_LIMIT");
		MCMeasurementChecker torqueLimitChecker = new MCMeasurementChecker();
		torqueLimitChecker.setId(id);
		torqueLimitChecker.setChecker(TorqueMeasurementChecker.class.getCanonicalName());
		return torqueLimitChecker;
	}

	public PartResult reloadPartResult() {

		ProductBuildResult buildResult = (ProductBuildResult) ProductTypeUtil.getTypeUtil(baseProduct.getProductType())
				.getProductBuildResultDao().findById(baseProduct.getProductId(), partResult.getPartName());
		if (buildResult != null) {
			List<Measurement> measurements = ServiceFactory.getDao(MeasurementDao.class)
					.findAll(baseProduct.getProductId(), buildResult.getPartName());
			buildResult.getMeasurements().addAll(measurements);
		}

		partResult.setBuildResult(buildResult);

		return partResult;
	}
	
	/**
	 * This method save lot control part 
	 * @param part
	 * @param measurements
	 * @param partSpec
	 */
	public void saveLotControlPart(String part, List<Measurement> measurements, PartSpec partSpec) {
		if (partResult.getLotControlRule() != null) {
			validatePartSnLotControl(part , partSpec);
		}
		createAndSaveInstalledPart(part, measurements);
		this.view.setMessage("Data Saved");
	}
	
	
	/**
	 * This method returns measurement data for  lot control  
	 * @param measurement
	 */
	public Measurement getMeasurement(MeasurementSpec measurementSpec) {
		Measurement meas  = new Measurement();
		for (Measurement measurement : partResult.getBuildResult().getMeasurements()) {
			if (measurement.getId().getMeasurementSequenceNumber() == measurementSpec.getId().getMeasurementSeqNum()) {
				meas = measurement;
				break;
			}
		}
		return meas;
	}
	
}
