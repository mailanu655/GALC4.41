package com.honda.galc.client.datacollection.observer.engine;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IExpectedProductManager;
import com.honda.galc.client.datacollection.observer.LotControlPersistenceManagerExt;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.strategy.MissionInstall;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;

public class LotControlMissionPersistenceManager extends LotControlPersistenceManagerExt{

	public LotControlMissionPersistenceManager(ClientContext context) {
		super(context);
	}

	
	
	@Override
	public IExpectedProductManager getExpectedProductManger() {
		
		return super.getExpectedProductManger();
		
	}

	@Override
	public void initPart(ProcessPart state) {
		if(getProperty().getMissionTypePartName().equals(state.getCurrentPartName())){
			((MissionInstall)DataCollectionController.getInstance().getCurrentProcessor()).setMissionRecconect(false);
		}
			
	}

	@Override
	public void completeTorque(ProcessTorque state) {
		
		if(!getProperty().getMissionPartName().equals(DataCollectionController.getInstance().getCurrentLotControlRule().getPartNameString())) return;
		
		MissionInstall missionInstall = (MissionInstall)DataCollectionController.getInstance().getCurrentProcessor();
		if(missionInstall.isMissionRecconect()){
			removeMissionTorques(state);
		}
		
		missionInstall.setMissionRecconect(false);
		
	}
	
	@Override
	protected void saveCollectedData(ProcessProduct state) {
		
		//skip product will not save build results
		if(isSkippedProduct(state) && !context.getProperty().isSaveBuildResultsForSkippedProduct()) return;
		
		super.saveCollectedData(state);
		
		saveEngineMissionData(state);
	}

	private void saveEngineMissionData(ProcessProduct state) {
		try {
			//only save mission data into engine table if both mission and mission type ok
			InstalledPart missionType = findInstallledPart(state, getProperty().getMissionTypePartName());
			InstalledPart mission = findInstallledPart(state, getProperty().getMissionPartName());
			boolean missionTypeFlag = false;
			if(getProperty().isMissionTypeRequired()) {
				if(missionType != null && missionType.getInstalledPartStatus() == InstalledPartStatus.OK)
					missionTypeFlag = true;
			}else {
				if(missionType == null) missionTypeFlag = true;
				else if(missionType.getInstalledPartStatus() == InstalledPartStatus.OK) missionTypeFlag = true;
			}
			
			if (mission != null	&& mission.getInstalledPartStatus() == InstalledPartStatus.OK && missionTypeFlag) {
				
				EngineDao dao = ServiceFactory.getDao(EngineDao.class);
				Engine engine = dao.findByKey(state.getProductId());
				engine.setActualMissionType((missionType!=null)?missionType.getPartSerialNumber():mission.getPartSerialNumber());
				engine.setMissionSerialNo(mission.getPartSerialNumber());
				engine.setMissionStatus(InstalledPartStatus.OK.getId());
				engine.setLastPassingProcessPointId(context.getProcessPointId());
				dao.update(engine);
				
				Logger.getLogger().info("updated engne:", engine.getId()," missionType:", engine.getActualMissionType(),
					" mission:", engine.getMissionSerialNo(), " missionStatus:" + engine.getMissionStatus());
			}
			
		} catch(BaseException be){
			Logger.getLogger().error(be, "Failed to update Engine table for " + state.getProductId());
			state.exception(new LotControlTaskException("Failed to update Engine table.", this.getClass().getSimpleName()),true);
		}catch (Exception e) {
			Logger.getLogger().error(e, "Failed to update Engine table for " + state.getProductId());
			if(!(e instanceof ServiceTimeoutException || e instanceof ConnectException || e instanceof UndeclaredThrowableException)){
				throw new TaskException("Error: Failed to connect to database.","LotControlPersistenceManager",e);
			}
		}
	}

	private void removeMissionTorques(DataCollectionState state) {
		InstalledPart mission = findInstallledPart(state, getProperty().getMissionPartName());
		mission.getMeasurements().clear();
		
		MeasurementDao dao = ServiceFactory.getDao(MeasurementDao.class);
		dao.deleteAll(state.getProductId(), getProperty().getMissionPartName());
		Logger.getLogger().info("Remove measurement for Mission Reconnect. ProductId:", state.getProductId());
	}

	private InstalledPart findInstallledPart(DataCollectionState state, String partName) {
		for(InstalledPart part : state.getProduct().getPartList()){
			if(partName.equals(part.getId().getPartName()))
				return part;
		}
		
		return null;
	}
}
