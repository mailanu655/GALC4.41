package com.honda.galc.client.teamleader.qi.productRecovery;


import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;

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
 * @author L&T infotech
 * Aug 18, 2017
 *
 */

public class ManualLotControlRepairEngineController extends	ManualLotControlRepairController <Engine, InstalledPart> {
	
	public ManualLotControlRepairEngineController(MainWindow mainWin,
			ManualLotControlRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
	}


	@Override
	protected void removeInstalledPart() {
		String ein = partResult.getInstalledPart().getId().getProductId();
		String mPartName = null; 
		//--------- remove mission install -------------- 
		//If remove mission or mission_type, then remove both of them  
		if(ManualLotCtrRepairUtil.isMission(property, partResult)){ 
			mPartName = ManualLotCtrRepairUtil.getMissionTypePartName(property); 
		} else if(ManualLotCtrRepairUtil.isMissionType(property, partResult)){ 
			mPartName = ManualLotCtrRepairUtil.getMissionPartName(property); 
		} 
		if(null != mPartName){ 
			removeInstalledPart(mPartName); 
			removeEngineMissionPart(ein); 
		} 
		
		//--------- remove head marriage --------------
		if(ManualLotCtrRepairUtil.isEngineHead(property, partResult))
			disassociateEngineAndHead();
		
		
		//--------- remove block load  --------------
		if(ManualLotCtrRepairUtil.isEngineBlock(property, partResult))
			disassociateEngineAndBlock();
		
		super.removeInstalledPart();
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

	
	private void removeEngineMissionPart(String productId) {
		EngineDao dao = ServiceFactory.getDao(EngineDao.class);
		Engine engine = dao.findByKey(productId);
		engine.setMissionSerialNo(null);
		engine.setActualMissionType(null);
		engine.setMissionStatus(InstalledPartStatus.NG.getId());
		
		dao.update(engine);
		
	}

	
	private void removeInstalledPart(String partName) {
		for(PartResult result : lotControlPartResultData){
			if(result.getPartName().equals(partName)){
				removeInstalledPart(result);
				getView().reload(getView().getProductId());
			}
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
	protected void removeInstalledPart(PartResult result) {
		if(result.getInstalledPart() == null || result.getInstalledPart().getMeasurements() == null)
			return;
		
		//make it exactly the same as current HCM production for Engine Plant, e.g. when repair 
		//head less measurement data is keep untouched. 
		if(!result.isHeadLess())
			super.removeMeasurementData(result);

		removeInstalledPartData(result);

		Logger.getLogger().info("Installed Part Result was removed by user:" + appContext.getUserId() +
				System.getProperty("line.separator") + partResult.getInstalledPart());
	}


	@Override
	protected void loadProductBuildResults() {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		productBuildResulits = installedPartDao.findAllByProductId(product.getProductId());
		
		loadInstalledParts(productBuildResulits);
		
	}
	

}
