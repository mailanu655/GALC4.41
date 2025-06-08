package com.honda.galc.client.teamleader;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.LineSideContainerTag;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.engine.EngineMarriageService;
import com.honda.galc.util.BearingSelectHelper;

/**
 * 
 * <h3>EngineManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManualLtCtrResultEnterViewManager description </p>
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
 * Aug 23, 2010
 *
 */

public class EngineManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{
	
	private FrameDao frameDao;
	private BearingSelectHelper bearingSelectHelper;

	public EngineManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}
	
	public EngineManualLtCtrResultEnterViewManager(MainWindow mainWin,
			ManualLotControlRepairPropertyBean property, ProductType currentProductType) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication(), currentProductType);
	}

	@Override
	protected void validatePartSn() throws TaskException, Exception {
		
		UpperCaseFieldBean partSnField = view.getDataPanel().getPartSnField();
		if(ManualLotCtrRepairUtil.isEngineHead(property, partResult)){
			validateEngineHead(partSnField);
		} else if(ManualLotCtrRepairUtil.isEngineBlock(property, partResult)){
			validateEngineBlock(partSnField);
		}
		
		super.validatePartSn();
		
	}

	private void validateEngineHead(UpperCaseFieldBean partSnField) {
		Head head = findHead(partSnField.getText());
		
		if(StringUtils.isEmpty(head.getDcSerialNumber()))
			throw new TaskException("DCH is empty for head, inputNumber:" + partSnField);
		
		if(StringUtils.isEmpty(head.getMcSerialNumber()))
			throw new TaskException("MCH is empty for head, inputNumber:" + partSnField);

		partSnField.setText(head.getDcSerialNumber());
	}


	private void validateEngineBlock(UpperCaseFieldBean partSnField) {
		Block block = findBlock(partSnField.getText());
		NumberType numberType = ManualLotCtrRepairUtil.getBlockPartNameSnType(property);
		if (numberType == null) {
			String msg = String.format("Block can be associated to engine by IN, DC, or MC number only. Please review property BLOCK_LOAD_PROPERTY_MAP{BlockPartNameSnType}");
			throw new TaskException(msg);
		}
		String sn = block.getSerialNumber(numberType); 
		if (StringUtils.isBlank(sn)) {
			String msg = String.format("%s number is empty for Block(ID:%s) - can not be used to associate Block to Engine.", numberType, block.getProductId());
			throw new TaskException(msg);
		}
		sn = StringUtils.trim(sn);
		partSnField.setText(sn);
	}
	
	@Override
	public void doSaveUpdate() {
		//save missionType or missionSn to Engine table
		if(ManualLotCtrRepairUtil.isMission(property, partResult)){
			associateEngineAndMission();
		} else if(ManualLotCtrRepairUtil.isMissionType(property, partResult)){
			associateEngineAndMissionType();
		} else if(ManualLotCtrRepairUtil.isEngineHead(property, partResult)){
			associateEngineAndHead();
		} else if(ManualLotCtrRepairUtil.isEngineBlock(property, partResult)){
			associateEngineAndBlock();
		} 
		if(property.isManualRepairUpdateBearingSelectResult()) {
			updateBearingSelectResults();
		}
		super.doSaveUpdate();
	}

	private void associateEngineAndMissionType() {
		setProcessPointId();

		getEngineMarriageService().assignMissionType(
				(List<InstalledPart>) Arrays.asList((InstalledPart)partResult.getBuildResult()),
				partResult.getProcessPointId().toString(),
				appContext.getApplicationId().toString());	

		getLogger().info(getEngineProductMarriageLogMessage());
	}

	private void associateEngineAndMission() {
		setProcessPointId();

		getEngineMarriageService().assignMission(
				(List<InstalledPart>) Arrays.asList((InstalledPart)partResult.getBuildResult()),
				partResult.getProcessPointId().toString(),
				appContext.getApplicationId().toString());

		getLogger().info(getEngineProductMarriageLogMessage());
	}

	protected void setProcessPointId() {
		if(partResult.getBuildResult().getProcessPointId() == null)
			partResult.getBuildResult().setProcessPointId(partResult.getProcessPointId());
	}
	
	private void updateBearingSelectResults() {
        Engine product = ServiceFactory.getDao(EngineDao.class).findByKey(partResult.getInstalledPart().getProductId());
		if(getBearingSelectHelper().getProperty().isBlockMeasurementsCollected() && 
				partResult.getInstalledPart().getPartName().equals(getBearingSelectHelper().getProperty().getInstalledBlockPartName())){
			
			Block block = ServiceFactory.getDao(BlockDao.class).findBySn(partResult.getPartSerialNumber(), getBearingSelectHelper().getProperty().getInstalledBlockPartNameSnType());
			if(block == null) {
				getLogger().warn("Error to update Bearing Select Result. Block:", partResult.getPartSerialNumber(), "does not exist in database.");
			} else {
				getBearingSelectHelper().updateBockMeasurements(product, block);
				Logger.getLogger().info(getBearingSelectLogMessage(product, "Block"));
			}
			
		} else if(getBearingSelectHelper().getProperty().isConrodMeasurementsCollected() && 
				partResult.getInstalledPart().getPartName().equals(LineSideContainerTag.CON_ROD_CAPS)){
			
			getBearingSelectHelper().updateConrodMeasurements(product);
			Logger.getLogger().info(getBearingSelectLogMessage(product, "Conrod"));
			
		} else if(getBearingSelectHelper().getProperty().isCrankSnCollected() && 
				partResult.getInstalledPart().getPartName().equals(LineSideContainerTag.CRANK_SERIAL_NUMBER)){
			
			getBearingSelectHelper().updateCrankMainMeasurements(product, partResult.getPartSerialNumber());
			Logger.getLogger().info(getBearingSelectLogMessage(product, "Crankshaft"));
		}	
	}

	private String getBearingSelectLogMessage(Engine product, String partName) {
		StringBuilder bs = new StringBuilder();
		bs.append("Bearing Select Result for product:").append(product.getProductId()).append(" was updated by ");
		bs.append( appContext.getUserId()).append(" by installing ").append(partName).append(".");
		return bs.toString();
	}
	
	private String getEngineProductMarriageLogMessage() {
		StringBuilder bs = new StringBuilder();
		bs.append("Product:").append(partResult.getInstalledPart().getProductId()).append(" was updated by ");
		bs.append( appContext.getUserId()).append(" by marrying ").append(partResult.getPartSerialNumber()).append(" to Engine");
		return bs.toString();
		
	}

	private void associateEngineAndHead() {
		Head head = findHead(partResult.getPartSerialNumber());
		
		head.setEngineSerialNumber(partResult.getInstalledPart().getProductId());
		ProcessPoint lastPassingPpoint = ManualLotCtrRepairUtil.getHeadLastPassingPoint(property);
		
		if(lastPassingPpoint != null){
			head.setLastPassingProcessPointId(lastPassingPpoint.getProcessPointId());
			head.setTrackingStatus(lastPassingPpoint.getLineId());
		} else{
			Logger.getLogger().info("Last Passing Process Point Id is invalid:", 
					ManualLotCtrRepairUtil.getHeadLastPassingPointId(property),
					" Head:" + partResult.getPartSerialNumber(), " Engine:",
					partResult.getInstalledPart().getProductId(), " status was not updated.");
		}
		
		ManualLotCtrRepairUtil.updateHead(head);
		
		//save head engine firing flag to engine table
		setEngineFiringFlag(head);
		
		Logger.getLogger().info("Head:", partResult.getPartSerialNumber(), " was associated with Engine:",
				head.getEngineSerialNumber());
		
	}

	private Head findHead(String headSn) {
		Head head = ManualLotCtrRepairUtil.findHead(headSn);
		
		if(head == null)
			throw new TaskException("Can not find head:" + headSn + " in database.");
		
		return head;
	}

	private void setEngineFiringFlag(Head head) {
		if(!head.getEngineFiringFlag()){ 
			Logger.getLogger().info("Head:", head.getId().toString(), 
					" engine firing flag is false so no update on engine.");
			return;
		}
		
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		engineDao.updateFiringFlag(head.getEngineSerialNumber(), head.getEngineFiringFlag());
		
		Logger.getLogger().info("Engine: engine firing flag updated(transfered from head), engineId:",
				head.getEngineSerialNumber(), " firing flag:" + head.getEngineFiringFlag(), " headId:",
				head.getId().toString());
		
	}

	private void associateEngineAndBlock() {
		Block block = findBlock(partResult.getPartSerialNumber());
	
		block.setEngineSerialNumber(partResult.getInstalledPart().getProductId());
		ProcessPoint lastPassingPpoint = ManualLotCtrRepairUtil.getBlockLastPassingPoint(property);
		
		if(lastPassingPpoint != null)
		{
			block.setLastPassingProcessPointId(lastPassingPpoint.getId());
			block.setTrackingStatus(lastPassingPpoint.getLineId());
		} else {
			Logger.getLogger().info("Last Passing Process Point Id is invalid:", 
					ManualLotCtrRepairUtil.getBlockLastPassingPointId(property),
					" Block:", partResult.getPartSerialNumber(), " Engine:",
					partResult.getInstalledPart().getProductId(), " status was not updated.");
		}
		
		ManualLotCtrRepairUtil.updateBlock(block);
		ManualLotCtrRepairUtil.updateBlockLoad(block);
		
		Logger.getLogger().info("Block:", partResult.getPartSerialNumber(), " was associated with Engine:",
				block.getEngineSerialNumber());
		
	}

	private Block findBlock(String blockSn) {
		Block block = ManualLotCtrRepairUtil.findBlock(blockSn);
		
		if(block == null)
			throw new TaskException("Can not find Block:" + blockSn + " in database.");
		
		return block;
	}

	@Override
	protected void loadHeadLessResult() {
		super.loadHeadLessResult();
		
		if(hasComment() && !resetScreen)
			showPartComment();
	}

	@Override
	protected void loadHeadedResult() {
		super.loadHeadedResult();
		
		if(hasComment() && !resetScreen)
			showPartComment();
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

	private FrameDao getFrameDao(){ 
		return (frameDao == null) ? frameDao = ServiceFactory.getDao(FrameDao.class) : frameDao;
	}

	public Frame getFrame(String productId) {
		return getFrameDao().findByKey(productId);
	}

	public String getMissionTypePartName() {
		return ManualLotCtrRepairUtil.getMissionTypePartName(property);
	}

	public String getMissionPartName() {
		return ManualLotCtrRepairUtil.getMissionPartName(property);
	}
	
	public EngineMarriageService getEngineMarriageService() {
		return ServiceFactory.getService(EngineMarriageService.class);
	}
}