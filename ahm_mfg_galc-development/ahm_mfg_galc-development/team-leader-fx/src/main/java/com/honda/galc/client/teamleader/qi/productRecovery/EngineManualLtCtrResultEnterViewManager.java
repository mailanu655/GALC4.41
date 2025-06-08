package com.honda.galc.client.teamleader.qi.productRecovery;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.service.ServiceFactory;

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
 * @author L&T infotech
 * Aug 18, 2017
 *
 */

public class EngineManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{

	public EngineManualLtCtrResultEnterViewManager(ApplicationContext appContext,Application application, ProductType currentProductType) {
		super(appContext, application, currentProductType);
	}

	@Override
	protected void validatePartSn() throws TaskException, Exception {
		
		UpperCaseFieldBean partSnField = dialog.getPartSnField();
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
			dialog.displayErrorMessage("DCH is empty for head, inputNumber:" + partSnField, "DCH is empty for head, inputNumber:" + partSnField);
		
		if(StringUtils.isEmpty(head.getMcSerialNumber()))
			dialog.displayErrorMessage("MCH is empty for head, inputNumber:" + partSnField,"MCH is empty for head, inputNumber:" + partSnField);

		partSnField.setText(head.getDcSerialNumber());
	}


	private void validateEngineBlock(UpperCaseFieldBean partSnField) {
		Block block = findBlock(partSnField.getText());
		NumberType numberType = ManualLotCtrRepairUtil.getBlockPartNameSnType(property);
		if (numberType == null) {
			String msg = String.format("Block can be associated to engine by IN, DC, or MC number only. Please review property BLOCK_LOAD_PROPERTY_MAP{BlockPartNameSnType}");
			dialog.displayErrorMessage(msg,msg);
		}
		String sn = block.getSerialNumber(numberType); 
		if (StringUtils.isBlank(sn)) {
			String msg = String.format("%s number is empty for Block(ID:%s) - can not be used to associate Block to Engine.", numberType, block.getProductId());
			dialog.displayErrorMessage(msg,msg);
		}
		sn = StringUtils.trim(sn);
		partSnField.setText(sn);
	}
	
	@Override
	public void doSaveUpdate() {
		//save missionType or missionSn to Engine table
		InstalledPart part = (InstalledPart)installedPartList.get(0);
		EngineDao dao = ServiceFactory.getDao(EngineDao.class);
		Engine engine = dao.findByKey(part.getId().getProductId());
		if(ManualLotCtrRepairUtil.isMission(property, partResult)){
			engine.setMissionSerialNo(part.getPartSerialNumber());
			if(isInstallledPartStatus(part.getId().getProductId(),ManualLotCtrRepairUtil.getMissionTypePartName(property)))
				engine.setMissionStatus(InstalledPartStatus.OK.getId());
			dao.update(engine);
			
		} else if(ManualLotCtrRepairUtil.isMissionType(property, partResult)){
			engine.setActualMissionType(part.getPartSerialNumber());
			if(isInstallledPartStatus(part.getId().getProductId(),ManualLotCtrRepairUtil.getMissionPartName(property)))
				engine.setMissionStatus(InstalledPartStatus.OK.getId());
			dao.update(engine);
		} else if(ManualLotCtrRepairUtil.isEngineHead(property, partResult)){
			associateEngineAndHead();
		} else if(ManualLotCtrRepairUtil.isEngineBlock(property, partResult)){
			associateEngineAndBlock();
		}
		
		super.doSaveUpdate();
		
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
			dialog.displayErrorMessage("Can not find head:" + headSn + " in database.","Can not find head:" + headSn + " in database.");
		
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
			dialog.displayErrorMessage("Can not find Block:" + blockSn + " in database.","Can not find Block:" + blockSn + " in database.");
		
		return block;
	}

	private boolean isInstallledPartStatus(String productId, String partName) {
		InstalledPart part = getInstalledPartDao().findByKey(new InstalledPartId(productId, partName));
		
		return (part == null) ? false : (part.getInstalledPartStatus() == InstalledPartStatus.OK);
	}

	@Override
	protected void loadHeadLessResult() {
		super.loadHeadLessResult();
	}

	@Override
	protected void loadHeadedResult() {
		super.loadHeadedResult();
	}
	
	

}
