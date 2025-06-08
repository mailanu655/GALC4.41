package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.msip.dto.inbound.Gpp305Dto;
import com.honda.galc.service.msip.dto.inbound.PreProductionLotDto;
import com.honda.galc.service.msip.dto.inbound.ProductionLotDto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.property.inbound.Gpp305PropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2017
 */
public class Gpp305Handler extends BaseMsipInboundHandler<Gpp305PropertyBean, Gpp305Dto> {
	
	public Gpp305Handler() {}
	
	public boolean execute(List<Gpp305Dto> receivedRecords) {
		try {
			List<PreProductionLotDto> ppldtoList = new ArrayList<PreProductionLotDto>();
			List<ProductionLotDto> pldtoList = new ArrayList<ProductionLotDto>();
		
			// create and fill tailsBylocation
			Map<String, PreProductionLotDto> tailsByLocation = getTailsByProcessLocation();
			Map<String, Double> sequences = new HashMap<String, Double>();
		
			for(Gpp305Dto plan305 : receivedRecords) {
				processGpp305Record(tailsByLocation, plan305, ppldtoList, pldtoList, sequences, false);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	public void processGpp305Record(Map<String, PreProductionLotDto> tailsByLocation, Gpp305Dto plan305,
			List<PreProductionLotDto> ppldtoList, List<ProductionLotDto> pldtoList, Map<String, Double> sequences,
			boolean isAutoHold) {
		
		PreProductionLotDto previousPplDto = null;
		String strProductionLot = plan305.generateProductionLot();
		PreProductionLot ppl = getDao(PreProductionLotDao.class).findByKey(strProductionLot);
		if(ppl == null) {
			PreProductionLotDto ppldto = new PreProductionLotDto(plan305);
				
			// Check logic about sequence list confirm on condition for Hold Lot
			// Check if lot to be placed on hold based on demand type
			if  (isAutoHold && (!isNotHoldDemandType(plan305.getDemandType()))){
				ppldto.setHoldStatus(0);
				String planCode = ppldto.getPlanCode().trim();
				Double maxSequence = findSequence(planCode, sequences);
				ppldto.setSequence(maxSequence);
				sequences.put(planCode, maxSequence);
				// Save the lot without adding to linked list
				getDao(PreProductionLotDao.class).save(ppldto.derivePreProductionLot());
			} else {
				String currentProcessLocation = ppldto.getProcessLocation();
				previousPplDto = tailsByLocation.get(currentProcessLocation);
				tailsByLocation.remove(currentProcessLocation);
				tailsByLocation.put(currentProcessLocation, ppldto);
				previousPplDto.setNextProductionLot(strProductionLot);
				String planCode = previousPplDto.getPlanCode().trim();
				// Check to see if the sequence logic can be made consistent with 631 Task
				Double maxSequence = findSequence(planCode, sequences);
				previousPplDto.setSequence(maxSequence);
				sequences.put(planCode, maxSequence);
				ppldtoList.add(previousPplDto);
			}

			pldtoList.add(addPLDTO(plan305));
		} else {
			getLogger().info("A pre production lot record already exists. No production lot will be created for this lot "
					+ ppl);
		}
		saveProductionLot(tailsByLocation, ppldtoList, pldtoList, sequences);
	}
	
	public void saveProductionLot(Map<String, PreProductionLotDto> tailsByLocation, List<PreProductionLotDto> ppldtoList, 
			List<ProductionLotDto> pldtoList, Map<String, Double> sequences){
		if(ppldtoList.size() > 0) {
			for(String siteProcessLocation : getPropertyBean().getProcessLocations()) {
				PreProductionLotDto ppldto = tailsByLocation.get(siteProcessLocation);
				String planCode = ppldto.getPlanCode().trim();
				if(sequences.containsKey(planCode)) {
					ppldto.setSequence(sequences.get(planCode) + 1);
				}
				ppldtoList.add(ppldto);
			}
			for(PreProductionLotDto ppldto : ppldtoList) {
				PreProductionLot pplot = ppldto.derivePreProductionLot();
				getDao(PreProductionLotDao.class).save(pplot);
			}
			for(ProductionLotDto pldto : pldtoList) {
				ProductionLot pl = deriveProductionLot(pldto);
				String processFlag = pldto.getProcessFlag();
				if(processFlag != null) {
					if(processFlag.equals("2")) {
						createEngines(pl);
					}
					if(processFlag.equals("1")) {
						createFramesAndStampingSequences(pl);
					}
				}
				getDao(ProductionLotDao.class).save(pl);
			}
		}
	}

	public void createPreProductionLots(Map<String, PreProductionLotDto> tailsByLocation,
			List<PreProductionLotDto> ppldtoList, Map<String, Double> sequences) {
		for(String siteProcessLocation : getPropertyBean().getProcessLocations()) {
			PreProductionLotDto ppldto = tailsByLocation.get(siteProcessLocation);
			String planCode = ppldto.getPlanCode().trim();
			if(sequences.containsKey(planCode)) {
				ppldto.setSequence(sequences.get(planCode) + 1);
			}
			ppldtoList.add(ppldto);
		}
	}

	public void savePreProductionLots(List<PreProductionLotDto> ppldtoList) {
		for(PreProductionLotDto ppldto : ppldtoList) {
			PreProductionLot ppl = ppldto.derivePreProductionLot();
			getDao(PreProductionLotDao.class).save(ppl);
		}
	}

	public void saveProductionLots(List<ProductionLotDto> pldtoList) {
		for(ProductionLotDto pldto : pldtoList) {
			ProductionLot pl = deriveProductionLot(pldto);
			String processFlag = pldto.getProcessFlag();
			if(processFlag != null) {
				if(processFlag.equals("2")) {
					createEngines(pl);
				}
				if(processFlag.equals("1")) {
					createFramesAndStampingSequences(pl);
				}
			}
			getDao(ProductionLotDao.class).save(pl);
		}
	}

	// Check if current demand type matches with any of the configured Hold Demand Types
	public boolean isNotHoldDemandType(String currentLotDemandType) {
		String demandTypesProperty = getPropertyBean().getNotHoldDemandTypes();
		String[] demandTypes = demandTypesProperty.split(":");
		for(String demandType:demandTypes){
			if(demandType.trim().equalsIgnoreCase(currentLotDemandType)) return true;
		}
		return false;
	}
	
	public Double findSequence(String planCode, Map<String, Double> sequences) { 
		Double maxSequence = 0d;
		if(sequences.containsKey(planCode)) {
			maxSequence = sequences.get(planCode);
			maxSequence += 1;
		} else {
			maxSequence = getDao(PreProductionLotDao.class).findMaxSequence(planCode);
			if(maxSequence == null || maxSequence == 0) {
				maxSequence = 0d;  
			} else {
				maxSequence += 1;
			}
		}
		return maxSequence;
	}
	
	public void createEngines(ProductionLot ppl) {
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = ppl.getLotSize();
		String startProductId = ppl.getStartProductId();
		if(startProductId == null) {
			getLogger().error("No StartProductId, Can not create Engines.");
			return;
		}
		String engineType = null;
		int serialNumber;
		try {
			engineType = startProductId.substring(0, 5);
			serialNumber = Integer.parseInt(startProductId.substring(5));
		} catch(IndexOutOfBoundsException ie) {
			getLogger().error("Failed to parse engine type or serialNumber from StartProductId: " +
					startProductId + " Can not create Engines.");
			return;
		}
		getLogger().info("Creating " + lotSize + " EINs. Starting from " + startProductId);
		for(int i = 0; i < lotSize; i++) {
			String productId = engineType + String.format("%07d", serialNumber++); 
			Engine engine = createEngine(ppl, productId);
			engines.add(engine);
			getLogger().info("Engine: " + engine.toString());
		}
		getDao(EngineDao.class).saveAll(engines);
		getLogger().info("" + lotSize + " EINs are created");
	}
	
	public void createFramesAndStampingSequences(ProductionLot pl) {
		List<Frame> frames = new ArrayList<Frame>();
		List<ProductStampingSequence> stampingSequences = new ArrayList<ProductStampingSequence>();
		int lotSize = pl.getLotSize();
		String startProductId = pl.getStartProductId();
		boolean isJapaneseVIN = ProductNumberDef.VIN_JPN.isNumberValid(startProductId);
		boolean isVIN = ProductNumberDef.VIN.isNumberValid(startProductId);
		if(!isVIN && !isJapaneseVIN) {
			getLogger().error("StartProductId is incorrect: " + startProductId + ", cannot create Frames and Stamping Sequences.");
			return;
		}
		StringBuilder sbStartVin = new StringBuilder(startProductId.substring(0, isJapaneseVIN ? 5 : 11));
		String startVin = sbStartVin.toString().toUpperCase();
		int intVin = 0;
		if(isJapaneseVIN) {
			intVin = Integer.parseInt(startProductId.substring(5,11));
		} else {
			intVin = Integer.parseInt(startProductId.substring(11,17));
		}
		getLogger().info("Creating " + lotSize + " VINs. Starting from " + startProductId);
		String productSpecCode = pl.getProductSpecCode();
		FrameSpec spec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(productSpecCode);
		if(spec==null) {
			getLogger().error("No FrameSpec for ProductSpecCode. " + productSpecCode + ".");
		}
		
		boolean isCheckDigitNeeded = MsipUtil.isCheckDigitNeeded(startProductId);
		if(!isCheckDigitNeeded){
			if(!checkDigitValidity(startProductId)) {
				getLogger().error("Invalid Check Digit Character. " + productSpecCode );
				return;
			}
		}
			
		for(int i = 0; i < lotSize; i++) {
			String strVin = startVin + String.format("%06d", intVin++);
			if(!isJapaneseVIN){
				StringBuilder sbVin = new StringBuilder(strVin);				
				if(isCheckDigitNeeded){
					char vinCheckDigit = ProductDigitCheckUtil.calculateVinCheckDigit(strVin);
					sbVin.setCharAt(8, vinCheckDigit);
				}				
				strVin = sbVin.toString();
			}
			Frame frame = createFrame(pl, strVin,isJapaneseVIN);
			frames.add(frame);
			getLogger().info("Frame: " + frame.toString());
			ProductStampingSequence stampingSequence = createStampingSequence(pl, i, strVin);
			stampingSequences.add(stampingSequence);
			getLogger().info("StampingSequence: " + stampingSequence.toString());
		}
		getDao(FrameDao.class).saveAll(frames);
		getLogger().info("" + lotSize + " VINs created");
		getDao(ProductStampingSequenceDao.class).saveAll(stampingSequences);
		getLogger().info("" + lotSize + " stampingSequences created");
	}
}
