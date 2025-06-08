package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.msip.dto.inbound.EngDataDto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.msip.property.inbound.EngDataPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class EngDataHandler extends BaseMsipInboundHandler<EngDataPropertyBean, EngDataDto> {
	
	public EngDataHandler() {}

	public boolean execute(List<EngDataDto> dtoList) {
		try {
			processEngineManifest(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	public void processEngineManifest(List<EngDataDto> dtoList) {
//		The list of file names that are received from AEP(MQ).
		String recievingPpid = getPropertyBean().getShippingRecvPpid();
		
		for(EngDataDto engineManifestDTO : dtoList) {
			String engSN = engineManifestDTO.getEngineSerialNumber();
			if(engSN == null || StringUtils.isEmpty(engSN.trim()))  {
				continue;
			}
			
			EngineDao engineDao = getDao(EngineDao.class);
			Engine engine = engineDao.findByKey(engSN);
			if(!saveEngine(engineDao, engine, engineManifestDTO)){
				continue;
			}
			//update KD lot number in Frame table if AUTO_ASSIGN = true in Gal489TBX
			if(!saveFrame(engineDao, engine)){
				continue;
			}
			
			TrackingService ts = ServiceFactory.getService(TrackingService.class);
//			track method should create ProductStampingSequence, InProcessProduct entities and update tracking status on Engine  
			ts.track(ProductType.ENGINE, engineManifestDTO.getEngineSerialNumber(), recievingPpid);
			getLogger().info("Tracking of Engine; " + engine.toString() + " completed.");
		}
	}
	
	private boolean saveEngine(EngineDao engineDao, Engine engine, EngDataDto engineManifestDTO){
		String aepFiredFlag = getPropertyBean().getAepFiredFlag();
		short galcFiredFlag = getPropertyBean().getGalcFiredFlag();
		short galcNotFiredFlag = getPropertyBean().getGalcNotFiredFlag();
		if(engine == null)  {
			engine = new Engine();
			engine.setProductId(engineManifestDTO.getEngineSerialNumber());
			engine.setProductionLot("");
			engine.setProductSpecCode(generateProductSpecCode(engineManifestDTO));
			engine.setPlantCode(getPropertyBean().getPlantCode());
			engine.setKdLotNumber(engineManifestDTO.getEngineShipKD());
			engine.setActualMissionType(engineManifestDTO.getMissionModelType());
			engine.setMissionSerialNo(engineManifestDTO.getMissionSerialNumber());
			short eff = aepFiredFlag.equals(engineManifestDTO.getEngineFiredFlag()) ? galcFiredFlag : galcNotFiredFlag;   
			engine.setEngineFiringFlag(eff);
			engineDao.save(engine);
			getLogger().info("Engine created; " + engine.toString());
			return true;
		}
		else  {
			StringBuilder sb = new StringBuilder();
			sb.append("Engine ").append(engine.getProductId()).append(" already exists:");
			String error = sb.toString();
			getLogger().error(error);
			return false;
		}
	}
	
	private boolean saveFrame(EngineDao engineDao, Engine engine){
		if(getPropertyBean().isAutoAssign()){
			FrameDao frameDao = getDao(FrameDao.class);
			Frame frame = frameDao.findByKDLotNumber(engine.getKdLotNumber());
			
			if(frame == null){
				String error = "No existing Frame for the given KD Lot number :"+engine.getKdLotNumber();
				getLogger().error(error);
				return false;
			}
			else {  // there is a frame matching kd-lot and this engine has not been assigned to any frame
				if(StringUtils.isEmpty(frame.getEngineSerialNo())){
					frame.setEngineSerialNo(engine.getProductId());
					frameDao.update(frame);
					getLogger().info("Updated Frame with engine serial number :"+engine.getProductId());
					//update vin number in the engine table
					engine.setVin(frame.getProductId());
					engineDao.update(engine);
					getLogger().info("Updated Engine with Frame VIN number :"+frame.getProductId());
					return true;
				}else if(!frame.getEngineSerialNo().equalsIgnoreCase(engine.getProductId())){
					StringBuilder sb = new StringBuilder();
					sb.append("The received Engine serial number ").append(engine.getProductId())
							.append(" doesn't match with assigned engine serial number ")
							.append(frame.getEngineSerialNo());
					String error = sb.toString();
					getLogger().error(error);
					return false;
				}
			}
		}
		return true;
	}

//	productSpecCode = engineModel + engineType + engineOption
	public String generateProductSpecCode(EngDataDto emDTO) {
		StringBuffer productSpecCode = new StringBuffer(); 
		boolean isValid = true;
		String engineModel = emDTO.getEngineModel();
		if(engineModel == null || engineModel.trim().length() == 0) {
			isValid = false;
			getLogger().error("Missing engineCode: " + engineModel);
		}
		String engineType = emDTO.getEngineType();
		if(engineType == null || engineType.trim().length() == 0) {
			isValid = false;
			getLogger().error("Missing engineTypeCode: " + engineType);
		}
		String engineOption = emDTO.getEngineOption();
		if(engineOption == null) {
			isValid = false;
			getLogger().error("Missing engineOption: " + engineOption);
		}
		if(isValid) {
			productSpecCode
				.append(engineModel)
				.append(engineType)
				.append(engineOption);
		}
		return productSpecCode.toString();
	}
	
	//	Add validation to check if needed AEP engines are in DB for the Lots that need AEP engines in next 2 days
	// The following are the models of engines that AEP will send to HMA per Corporate Planning. 
    //Pilot - G5MH R05 
   //**MDX - H5WS - A00, C00, H00, H01, R00 
	
	public boolean checkIfMissingEngines() {
		
		boolean missingEng = false;
		
		String[] aepEngineModels = getPropertyBean().getAepModels(); 
		int daysToCheck = getPropertyBean().getDaysToCheck(); 
		
		String plantCode = getPropertyBean().getPlantCode(); 
		String[] notSellableTrackingStatus = getPropertyBean().getNotSellableTrackingStatus();
		
		getLogger().info("Days to check :  " + daysToCheck + " Plant code : "+plantCode );
		
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		
		for(String engineModel : aepEngineModels){			
			// get count of required engines by MTOC
			List<String> framesToBuild = frameDao.getProductsByEngMTOC(engineModel, daysToCheck,notSellableTrackingStatus);
			int requiredEngCount = framesToBuild.size();
			getLogger().info("Count of frames to build  for engine model "+ engineModel + "is :"+requiredEngCount);
			
			//get count of available engines by MTOC
			List<String> availableEng = engineDao.findAvailEnginesByMTOC(plantCode,engineModel);
			int availableEngineCount = availableEng.size();
			getLogger().info("Count of engines available for model "+ engineModel + "is :"+availableEngineCount);
			
			// calculate the missing count
			int missingEngCount = requiredEngCount - availableEngineCount;
			
			// If missing Engines add to notification
			if (missingEngCount > 0){
				// message - n engines of model ENGMODEL are missing
				String error = Integer.toString(missingEngCount) +" engines of model "+engineModel+" are missing";
				getLogger().error(error);
				missingEng = true;
			}
			getLogger().info("Engine model " + engineModel + " has " + availableEngineCount + "Engines and need " + requiredEngCount + "engines");
		}
		return missingEng ;
	}
}
