package com.honda.galc.client.teamleader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.model.PartResultTableModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.BearingSelectPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.BearingSelectHelper;

/**
 * 
 * <h3>ManualLotControlRepairEngineController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairEngineController description </p>
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
 * @author Paul Chou
 * Mar 11, 2011
 *
 */

public class ManualLotControlRepairEngineController extends	ManualLotControlRepairController <Engine, InstalledPart> {

	protected FrameDao frameDao;
	protected Engine engine;
	protected Frame frame;
	protected List<InstalledPart> installedParts;
	protected List<PartResult> partResultList = new ArrayList<PartResult>();
	BearingSelectPropertyBean bearingSelectPropertyBean;
	BearingSelectHelper bearingSelectHelper;
	
	public ManualLotControlRepairEngineController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}

	@Override
	protected boolean validateSubproduct(LotControlRule rule, PartResult parentPartResult) {
		try {
			if(!StringUtils.isEmpty(rule.getPartName().getSubProductType()) &&  rule.getPartName().getSubProductType().equals(ProductType.ENGINE.name())){
				EngineLoadUtility engineLoadUtil = new EngineLoadUtility();

				if(engineLoadUtil.isEngineOnDepartmentHold(product.getProductId())){
					subProductErrorMessage = "Selected engine " + product.getProductId()
					+ " is on hold: " + engineLoadUtil.getHoldResult().getHoldReason() + " and will be marked NG.";
					return false;
				}	
				if(engineLoadUtil.isEngineAssignedToFrame((Engine) product) && (!((Engine) product).getVin().equals(parentPartResult.getBuildResult().getProductId()))) {
					subProductErrorMessage = "Engine it is already assigned to frame " + ((Engine) product).getVin() + " and will be marked NG.";
					return false;
				}
				//check Engine type mismatch	
				Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(parentPartResult.getBuildResult().getProductId());
				ProductCheckUtil checkUtil = new ProductCheckUtil(frame, parentPartResult.getProcessPoint());	
				boolean useAltEngineMto=PropertyService.getPropertyBean(ProductCheckPropertyBean.class,
						parentPartResult.getProcessPoint().getProcessPointId()).isUseAltEngineMto();
				boolean engineTypeCheck = checkUtil.checkEngineTypeForEngineAssignment(frame, (Engine) product, useAltEngineMto);
				if (!engineTypeCheck) {
					subProductErrorMessage = "Engine Type is incorrect and will be marked NG.";
					return false;
				}
				//check if engine came from a valid previous line only if configured to do so.
				if(!engineLoadUtil.checkValidPreviousEngineLine((Engine) product, parentPartResult.getProcessPointId())){
					subProductErrorMessage = "Engine " + product.getProductId() + " is from an invalid Line " + product.getTrackingStatus()
					+ " and will be marked NG.";
					return false;
				}
			}
		} catch (Exception e) {
			subProductErrorMessage = "Error occured while validating subproduct " + product.getProductId();
			return false;
		}
		return super.validateSubproduct(rule, parentPartResult);
	}

	@Override
	protected void doParentUpdate() {
		engine = getEngine(product.getProductId());
		frame = getFrame(parentInstalledPartList.get(0).getProductId());
		if(ManualLotCtrRepairUtil.isEngine(property, getView().parentView.getController().partResult)) {
			associateEngineAndFrame();
		} else if (ManualLotCtrRepairUtil.isMission(property, partResult)) {
			associateMission();
		} else if(ManualLotCtrRepairUtil.isMissionType(property, partResult)) {
			associateMissionType();
		} else super.doParentUpdate();
	}

	private void associateMissionType() {
		try {
			getEngineMarriageService().assignMissionType(
					Arrays.asList((InstalledPart)partResult.getBuildResult()),
					appContext.getProcessPointId().toString(), appContext.getApplicationId().toString());
		} catch (Exception e) {
			MessageDialog.showError(super.getView(), "Failed to save data:" + e);
			Logger.getLogger().error(e, "Failed to save data.");
			return;
		}		
	}

	private void associateMission() {
		try {
			setProcessPointId();
			
			getEngineMarriageService().assignEngineAndFrame(installedParts);
		} catch (Exception e) {
			MessageDialog.showError(super.getView(), "Failed to save data:" + e);
			Logger.getLogger().error(e, "Failed to save data.");
			return;
		}		
	}

	protected void removeInstalledPartEngine(PartResult result) {
		partResultList.add(result);
		result.getInstalledPart().setPartSerialNumber(null);
		result.getInstalledPart().setInstalledPartStatus(InstalledPartStatus.REMOVED);
		result.getInstalledPart().setActualTimestamp(null);

		List<Measurement> measurementList = new ArrayList<Measurement>();
		for(Measurement measurement : result.getInstalledPart().getMeasurements()){
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementValue(0.0);
			measurement.setPartSerialNumber(null);
			measurement.setMeasurementName(null);
			measurement.setMeasurementStringValue(null);
			measurement.setActualTimestamp(null);
			measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
			measurementList.add(measurement);
		}
		result.getInstalledPart().setMeasurements(measurementList);

		installedParts.add(result.getInstalledPart());
	}

	private void associateEngineAndFrame() {
		try {
			
			getEngineMarriageService().assignEngineAndFrame((List<InstalledPart>) parentInstalledPartList);

			Logger.getLogger().info("Engine and frame successfully assigned by " + appContext.getUserId());
		} catch (Exception e) {
			MessageDialog.showError(super.getView(), "Failed to save data:" + e);
			Logger.getLogger().error(e, "Failed to save data.");
			return;
		}		
	}

	@Override
	protected void removeInstalledPart() {
		installedParts = new ArrayList<InstalledPart>();

		//--------- remove mission and mission Type --------------
		//If remove mission and mission_type, then remove both of them 
		if(ManualLotCtrRepairUtil.isMission(property, partResult)) {
			disassociateMission((InstalledPart) partResult.getBuildResult());

		} else if(ManualLotCtrRepairUtil.isMissionType(property, partResult)) {
			disassociateMissionType((InstalledPart) partResult.getBuildResult());
		}
		//--------- remove head marriage --------------
		else if(ManualLotCtrRepairUtil.isEngineHead(property, partResult)) {
			disassociateEngineAndHead();
		//--------- remove block load  --------------

		} else if(ManualLotCtrRepairUtil.isEngineBlock(property, partResult)){
			disassociateEngineAndBlock();
		//--------- remove Bearing -------------------
		}
		if(property.isManualRepairUpdateBearingSelectResult()) {
			removeBearingResults();
		}
		super.removeInstalledPart();
	}
	
	private void removeBearingResults(){
		if(getBearingSelectHelper().getProperty().isBlockMeasurementsCollected() && 
				partResult.getInstalledPart().getPartName().equals(getBearingSelectHelper().getProperty().getInstalledBlockPartName())){
			getBearingSelectHelper().removeBockMeasurements(partResult.getInstalledPart().getId().getProductId());
			Logger.getLogger().info("Bearings were removed by ", getAppContext().getUserId(), " by removing Block.");
		} else if(getBearingSelectHelper().getProperty().isConrodMeasurementsCollected() && 
				partResult.getInstalledPart().getPartName().equals(LineSideContainerTag.CON_ROD_CAPS)){
			getBearingSelectHelper().removeConrodMeasurements(partResult.getInstalledPart().getId().getProductId());
			Logger.getLogger().info("Bearings were removed by ", getAppContext().getUserId(), " by removing Conrod.");
		} else if(getBearingSelectHelper().getProperty().isCrankSnCollected() && 
				partResult.getInstalledPart().getPartName().equals(LineSideContainerTag.CRANK_SERIAL_NUMBER)){
			getBearingSelectHelper().removeCrankMainMeasurements(partResult.getInstalledPart().getId().getProductId());
			Logger.getLogger().info("Bearings were removed by ", getAppContext().getUserId(), " by removing Crankshaft.");
		}
	}

	private void disassociateMission(InstalledPart installedPart) {
		try {
			removeInstalledPartEngine(ManualLotCtrRepairUtil.getMissionPartName(property));
			removeInstalledPartEngine(ManualLotCtrRepairUtil.getMissionTypePartName(property));
		
			getEngineMarriageService().deassignMission(
					installedParts, appContext.getApplicationId().toString(), appContext.getProcessPointId().toString());
			
			updateRemovedPartRows(partResultList);
			Logger.getLogger().info("Mission and mission type successfully deassociated by " + appContext.getUserId());
		} catch (Exception e) {
			MessageDialog.showError(super.getView(), "Failed to delete data:" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void disassociateMissionType(InstalledPart installedPart) {
		try {
			removeInstalledPartEngine(ManualLotCtrRepairUtil.getMissionPartName(property));
			removeInstalledPartEngine(ManualLotCtrRepairUtil.getMissionTypePartName(property));
		
			getEngineMarriageService().deassignMissionType(
					installedParts, appContext.getApplicationId().toString(), appContext.getProcessPointId().toString());
			
			updateRemovedPartRows(partResultList);
			Logger.getLogger().info("Mission and mission type successfully deassociated by " + appContext.getUserId());
		} catch (Exception e) {
			MessageDialog.showError(super.getView(), "Failed to delete data:" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void disassociateEngineAndHead() {
		String ein = partResult.getInstalledPart().getId().getProductId();

		Head head = ManualLotCtrRepairUtil.findHead(partResult.getPartSerialNumber());

		if(head == null){
			//allow to delete invalid head which does not exist in database
			Logger.getLogger().warn("Can not find head:" + partResult.getPartSerialNumber() + " in database." );
		} else {
			ProcessPoint lastPassingPpoint = ManualLotCtrRepairUtil.getHeadLastPassingPoint(property);
			if (lastPassingPpoint != null) {
				ProcessPoint mcOffProcessPoint = ManualLotCtrRepairUtil.getHeadMcOffProcessPoint(property);
				head.setLastPassingProcessPointId(mcOffProcessPoint.getProcessPointId());
				head.setTrackingStatus(mcOffProcessPoint.getLineId());
			} else {
				Logger.getLogger().info("Last Passing Process Point Id is invalid:",
						ManualLotCtrRepairUtil.getHeadLastPassingPointId(property), " Head:",
						head.getId().toString(), " Engine:", ein);
			}
			
			// reset product attributes
			head.setEngineSerialNumber(null);
			ManualLotCtrRepairUtil.updateHead(head);
		}
		Logger.getLogger().info("Head:", partResult.getPartSerialNumber(), " was disassociated with Engine:",ein);
	}

	private void disassociateEngineAndBlock() {
		Block block = ManualLotCtrRepairUtil.findBlock(partResult.getPartSerialNumber());
		if(block == null)
			throw new TaskException("Can not find block:" + partResult.getPartSerialNumber() + " in database.");

		ProcessPoint lastPassingPpid = ManualLotCtrRepairUtil.getBlockLastPassingPoint(property);
		if(lastPassingPpid != null){
			ProcessPoint mcOffPpid = ManualLotCtrRepairUtil.getBlockMcOffProcessPoint(property);
			block.setLastPassingProcessPointId(mcOffPpid.getProcessPointId());
			block.setTrackingStatus(mcOffPpid.getLineId());
		} else {
			Logger.getLogger().info("Last Passing Proces Point Id is invalid:", 
					ManualLotCtrRepairUtil.getBlockLastPassingPointId(property),
					" Block:", block.getMcSerialNumber(), " Engine:", 
					partResult.getInstalledPart().getId().getProductId(), " status was not updated.");
		}

		block.setEngineSerialNumber(null);
		ManualLotCtrRepairUtil.updateBlock(block);

		Logger.getLogger().info("Block:", block.getMcSerialNumber(), " was disassociated with Engine:",
				partResult.getInstalledPart().getId().getProductId());
	}

	private void removeInstalledPartEngine(String partName) {
		for(PartResult result : lotControlPartResultData){
			if(result.getPartName().equals(partName)){
				if(result.getBuildResult() != null) {
					removeInstalledPartEngine(result);
					
				}
			}
		}
	}

	private void updateRemovedPartRows(List<PartResult> results) {
		PartResultTableModel model = (PartResultTableModel)getView().getPartStatusTable().getModel();
		for(PartResult result : results) {
		int index = model.getIndex(result);
		model.fireTableRowsUpdated(index , index);
		}
	}



	@Override
	protected void addInstalledPartResult(List<InstalledPart> installedParts) {
		super.addInstalledPartResult(installedParts);
		
		//Engine specific check for Head Marriage and Block Load
		for(PartResult result : lotControlPartResultData){
			if(ManualLotCtrRepairUtil.isEngineHead(property, result)){
				if(result.getInstalledPart() != null && 
						result.getInstalledPart().getInstalledPartStatus() == InstalledPartStatus.OK &&
						!isAssociatedHeadValid(result.getInstalledPart())){
					Logger.getLogger().info("Invalid Engine/Head mirrage detected head:", 
							result.getInstalledPart().getPartSerialNumber(),
							" Engine:", result.getInstalledPart().getProductId());
					result.getInstalledPart().setInstalledPartStatus(InstalledPartStatus.NM);
				}
			} else if(ManualLotCtrRepairUtil.isEngineBlock(property, result)){
				if(result.getInstalledPart() != null && 
						result.getInstalledPart().getInstalledPartStatus() == InstalledPartStatus.OK &&
						!isAssociatedBlockValid(result.getInstalledPart())){
					Logger.getLogger().info("Invalid Engine/Block mirrage detected block:", 
							result.getInstalledPart().getPartSerialNumber(),
							" Engine:", result.getInstalledPart().getProductId());
					result.getInstalledPart().setInstalledPartStatus(InstalledPartStatus.NM);
				}

			}
		}
	}

	private boolean isAssociatedBlockValid(InstalledPart installedPart) {
		Block block = ManualLotCtrRepairUtil.findBlock(installedPart.getPartSerialNumber());
		
		return block != null && !StringUtils.isEmpty(block.getEngineSerialNumber()) &&
				block.getEngineSerialNumber().trim().equals(installedPart.getProductId());
	}

	private boolean isAssociatedHeadValid(InstalledPart installedPart) {
		Head head = ManualLotCtrRepairUtil.findHead(installedPart.getPartSerialNumber());

		return head != null && !StringUtils.isEmpty(head.getEngineSerialNumber()) &&
				head.getEngineSerialNumber().trim().equals(installedPart.getProductId());
	}

	@Override
	protected boolean isMeasurementStatusOk() {
		
		if(partResult.isHeadLess()) return true;
		
		return super.isMeasurementStatusOk();
	}

	@Override
	protected void loadProductBuildResults() {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());

		loadInstalledParts(productBuildResulits);
	}

	protected void setProcessPointId() {
		if(partResult.getBuildResult().getProductId() == null)
			partResult.getBuildResult().setProcessPointId(partResult.getProcessPointId());
	}

	public String getMissionTypePartName() {
		return ManualLotCtrRepairUtil.getMissionTypePartName(property);
	}

	public String getMissionPartName() {
		return ManualLotCtrRepairUtil.getMissionPartName(property);
	}

	public Frame getFrame(String productId) {
		return getFrameDao().findByKey(productId);
	}
	
	public BearingSelectHelper getBearingSelectHelper() {
		if(bearingSelectHelper == null)
			bearingSelectHelper = new BearingSelectHelper(appContext.getApplicationId(), Logger.getLogger()); 
		return bearingSelectHelper;
	}

	public Frame getFrame(Engine engine) {
		Frame frame = null;
		if(engine.getVin() != null) {
			frame = getFrame(engine.getVin());
		}
		return frame;
	}

	public Engine getEngine(String productId) {
		return getEngineDao().findByKey(productId);
	}

	public FrameDao getFrameDao() {
		return (frameDao == null) ? frameDao = ServiceFactory.getDao(FrameDao.class) : frameDao;
	}
}