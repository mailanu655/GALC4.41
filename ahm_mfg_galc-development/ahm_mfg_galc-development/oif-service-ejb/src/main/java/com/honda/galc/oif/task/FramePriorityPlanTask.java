package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.GPP305DTO;
import com.honda.galc.oif.dto.PreProductionLotDTO;
import com.honda.galc.oif.dto.ProductionLotDTO;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
import com.honda.galc.util.PriorityPlanUtils;

/**
 * 
 * <h3>FramePriorityPlanTask Class description</h3>
 * <p> FramePriorityPlanTask description </p>
 * Frame Priority Plan task is an OIF task for frame plants, which executes every day 
 * It retrieves data from incoming interface N--GPC#HMAGAL#GPP305 to get the priority production plans. 
 * Every priority plan is converted to production lot in GAL217TBX and preproduction lot in GAL212TBX.  
 * It also builds Engine(GAL131TBX), Frame(GAL143TBX) 
 * and FrameStampingSequence(GAL216TBX) objects
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
 * @author Larry Karpov 
 * Jan 29, 2014
 *
 *
 */
public class FramePriorityPlanTask extends BasePriorityPlanTask<GPP305DTO> implements IEventTaskExecutable{
	
	int failedCount = 0,successCount=0;;
	
	public FramePriorityPlanTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processFramePriorityPlan();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			setIncomingJobCount(successCount, failedCount, receivedFileList);
			errorsCollector.sendEmail();
		}
	}
	
	private void processFramePriorityPlan() {
		logger.info("start to process priority plan");
		
		int batchCount = 0;
		
		refreshProperties();
		initData(GPP305DTO.class);
		boolean initializeData = getPropertyBoolean(OIFConstants.INITIALIZE_TAIL, false);
		if(initializeData) {
			if(getPriorityPlans() == 0) {
				logger.error("No received files.");
				errorsCollector.error("No received files.");
				setIncomingJobStatus(OifRunStatus.NO_FILE_RECEIVED);
				return;
			}
			initTails();
		}
		Map<String, PreProductionLotDTO> tailsByLocation = getTailsByProcessLocation();
		if(tailsByLocation == null || tailsByLocation.size() == 0) {
			logger.emergency("Tails are not correct for at least one process location: " + siteProcessLocations);
			errorsCollector.emergency("Tails are not correct for at least one process location: " + siteProcessLocations);
			setIncomingJobStatus(OifRunStatus.TAIL_NOT_FOUND);
			return;
		}
		if(!initializeData && getPriorityPlans() == 0) {
			logger.error("No received files.");
			errorsCollector.error("No received files.");
			return;
		}
		for (int count=0; count<receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}
			List<PreProductionLotDTO> ppldtoList = new ArrayList<PreProductionLotDTO>(); 
			List<ProductionLotDTO> pldtoList = new ArrayList<ProductionLotDTO>(); 
			PreProductionLotDTO previousPplDto = null;
			Map<String, Double> sequences = new HashMap<String, Double>();
			
			boolean isAutoHold = getPropertyBoolean(OIFConstants.AUTO_HOLD, false);
			for(String receivedRecord : receivedRecords) {
				GPP305DTO plan305 = new GPP305DTO();
				simpleParseHelper.parseData(plan305, receivedRecord);
				if(!validateCurrentRecord(plan305)) {
					continue;
				}
				
				String strProductionLot = plan305.generateProductionLot();
				PreProductionLot ppl = getDao(PreProductionLotDao.class).findByKey(strProductionLot);
				if(ppl == null) {
					PreProductionLotDTO ppldto = new PreProductionLotDTO(plan305);
						
					// Check logic about sequence list confirm on condition for Hold Lot
					// Check if lot to be placed on hold based on demand type
					if  ( isAutoHold && (!isNotHoldDemandType(plan305.getDemandType()))){
						ppldto.setHoldStatus(0);
						String planCode = ppldto.getPlanCode().trim();
						Double maxSequence = findSequence(planCode, sequences);
						ppldto.setSequence(maxSequence);
						sequences.put(planCode, maxSequence);
						ppldtoList.add(ppldto);
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
					successCount++;
				} else {
					logger.error("A pre production lot record already exists. No production lot will be created for this lot "
							+ ppl);
					errorsCollector.error("A pre production lot record already exists. No production lot will be created for this lot "
							+ ppl);
					failedCount++;
					setIncomingJobStatus(OifRunStatus.LOT_ALREADY_EXISTS);
					
				}
			}
			
			if(ppldtoList.size() > 0) {
				for(String siteProcessLocation : siteProcessLocations) {
					PreProductionLotDTO ppldto = tailsByLocation.get(siteProcessLocation);
					String planCode = ppldto.getPlanCode().trim();
					if(sequences.containsKey(planCode)) {
						ppldto.setSequence(sequences.get(planCode) + 1);
					}
					ppldtoList.add(ppldto);
					
					
				}
				for(PreProductionLotDTO ppldto : ppldtoList) {
					PreProductionLot ppl = ppldto.derivePreProductionLot();
					
					ProductionLotDTO pldto = findProductLotDto(pldtoList, ppl.getProductionLot());
					PreProductionLot preProdLot  = getDao(PreProductionLotDao.class).findByKey(ppl.getProductionLot());
				
					if(pldto != null && preProdLot == null) {
						ProductionLot pl = pldto.deriveProductionLot();
						String processFlag = pldto.getProcessFlag();
						if(processFlag != null) {
							if(processFlag.equals("2")) {
								createEngines(pl);
								batchCount += pl.getLotSize();
							}
							if(processFlag.equals("1")) {
								createFramesAndStampingSequences(pl);
								batchCount += pl.getLotSize();
							}
						}
						getDao(ProductionLotDao.class).save(pl);
					}
					
					if(preProdLot == null ||!StringUtils.equalsIgnoreCase(preProdLot.getNextProductionLot(), ppl.getNextProductionLot())) {
					   getDao(PreProductionLotDao.class).save(ppl);
					}
					
					if(getBatchProcessCount() > 0 && batchCount >=getBatchProcessCount()) {
						ppl.setNextProductionLot(null);
  					    getDao(PreProductionLotDao.class).save(ppl);
						String message = "Batch Processing Count reached. Batch Count = " + batchCount + " Currently Processing Pre production Lot " + ppl;
						logger.emergency(message);
						errorsCollector.emergency(message);
						//save current TempFiles
						updateTempFiles(receivedFileList);
						return;
					}
					
							
				}
				logger.info("File " + (count+1) + " (" + receivedFile + ") from " + receivedFileList.length + " total file(s) is processed.");
			}
		}
		
		this.clearTempFiles();
		
		logger.info("Priority Production plan processed.");
	}
	
	private ProductionLotDTO findProductLotDto(List<ProductionLotDTO> pldtoList,String productionLot) {
		for(ProductionLotDTO pldto : pldtoList) {
			if(pldto.getProductionLot().equals(productionLot)) return pldto;
		}
		
		return null;
	}

	// Check some assumptions about the data
	private boolean validateCurrentRecord(GPP305DTO currentPlan305) { 
		boolean result = true;
		String planCode = currentPlan305.getPlanCode();
		if(planCode == null) {
			logger.error("planCode not found in record, skipping record.");
			errorsCollector.error("planCode not found in record, skipping record.");
			result = false;
		} else {
			String plantCode = currentPlan305.getPlantCode().trim();
			if(plantCode == null) {
				logger.error("plantCode not found in planCode: " + planCode + " skipping record.");
				errorsCollector.error("plantCode not found in planCode: " + planCode + " skipping record.");
				result = false;
			} else {
				if(!plantCode.equals(siteName)) {
					logger.debug("Not current plant: " + plantCode + "; skipping record.");
					result = false;
				} else {
					String lineNo = currentPlan305.getLineNo();
					if(lineNo == null) {
						logger.error("lineNo not found in planCode, skipping record.");
						errorsCollector.error("lineNo not found in planCode, skipping record.");
						result = false;
					} else {
						if(!lineNo.equals(siteLineId)) {
							logger.debug("Not current line: " + lineNo + "; skipping record.");
							result = false;
						} else {
							String processLocation = currentPlan305.getProcessLocation();
							if(!siteProcessLocations.contains(processLocation)) {
								logger.debug("Different process location: " + processLocation + "; skipping record.");
								result = false;
							}
						}
					}
				}
			}
		}
		return result;
	}

	private void createEngines(ProductionLot ppl) {
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = ppl.getLotSize();
		String startProductId = ppl.getStartProductId();
		if(startProductId == null) {
			logger.error("No StartProductId, Can not create Engines.");
			errorsCollector.error("No StartProductId, Can not create Engines.");
			setIncomingJobStatus(OifRunStatus.CREATE_ENGINE_FAIL);
			return;
		}
		String engineType = null;
		int serialNumber;
		try {
			engineType = startProductId.substring(0, 5);
			serialNumber = Integer.parseInt(startProductId.substring(5));
		} catch(IndexOutOfBoundsException ie) {
			logger.error(ie, "Failed to parse engine type or serialNumber from StartProductId: " +
					startProductId + " Can not create Engines.");
			errorsCollector.error(ie, "Failed to parse engine type or serialNumber from StartProductId: " +
					startProductId + " Can not create Engines.");
			setIncomingJobStatus(OifRunStatus.FAIL_TO_PARSE_ENGINE);
			return;
		}
		logger.info("Creating " + lotSize + " EINs. Starting from " + startProductId);
		for(int i = 0; i < lotSize; i++) {
			String productId = engineType + String.format("%07d", serialNumber++); 
			Engine engine = createEngine(ppl, productId);
			engines.add(engine);
			logger.info("Engine: " + engine.toString());
		}
		getDao(EngineDao.class).saveAll(engines);
		logger.info("" + lotSize + " EINs are created");
		
		trackEngines(engines);
	}
	
	private void createFramesAndStampingSequences(ProductionLot pl) {
		List<Frame> frames = new ArrayList<Frame>();
		List<ProductStampingSequence> stampingSequences = new ArrayList<ProductStampingSequence>();
		int lotSize = pl.getLotSize();
		String startProductId = pl.getStartProductId();
		boolean isJapaneseVIN = ProductNumberDef.VIN_JPN.isNumberValid(startProductId);
		boolean isVIN = ProductNumberDef.VIN.isNumberValid(startProductId);
		if(!isVIN && !isJapaneseVIN) {
			logger.error("StartProductId is incorrect: " + startProductId + ", cannot create Frames and Stamping Sequences.");
			errorsCollector.error("StartProductId is incorrect: " + startProductId + ", cannot create Frames and Stamping Sequences.");
			setIncomingJobStatus(OifRunStatus.CREATE_FRAME_FAIL);
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
		logger.info("Creating " + lotSize + " VINs. Starting from " + startProductId);
		String productSpecCode = pl.getProductSpecCode();
		FrameSpec spec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(productSpecCode);
		if(spec==null) {
			logger.error("No FrameSpec for ProductSpecCode. " + productSpecCode + ".");
			errorsCollector.error("No FrameSpec for ProductSpecCode. " + productSpecCode + ".");
			setIncomingJobStatus(OifRunStatus.NO_FRAME_SPEC);
		}
		
		boolean isCheckDigitNeeded = PriorityPlanUtils.isCheckDigitNeeded(startProductId);
		if(!isCheckDigitNeeded){
			if(!checkDigitValidity(startProductId)) {
				logger.error("Invalid Check Digit Character. " + productSpecCode );
				errorsCollector.error("Invalid Check Digit Character. " + productSpecCode );
				setIncomingJobStatus(OifRunStatus.INVALID_DIGIT_CHARACTER);
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
			logger.info("Frame: " + frame.toString());
			ProductStampingSequence stampingSequence = createStampingSequence(pl, i, strVin);
			stampingSequences.add(stampingSequence);
			logger.info("StampingSequence: " + stampingSequence.toString());
		}
		getDao(FrameDao.class).saveAll(frames);
		logger.info("" + lotSize + " VINs created");
		getDao(ProductStampingSequenceDao.class).saveAll(stampingSequences);
		logger.info("" + lotSize + " stampingSequences created");
		
		trackFrames(frames);
	}
	
	private boolean checkDigitValidity(String productId) {
		OifTaskPropertyBean OifPropertyBean = PropertyService.getPropertyBean(OifTaskPropertyBean.class);
		String[] validDigits = OifPropertyBean.getValidCheckDigits();
		for(String validDigit:validDigits){
			if(validDigit.trim().equalsIgnoreCase(Character.toString(productId.charAt(8)))) return true;
		}
		return false;
	}

	private Frame createFrame(ProductionLot pl, String productId, boolean isJapaneseVIN) {
		Frame frame = new Frame(productId);
		frame.setProductionLot(pl.getProductionLot());
		frame.setKdLotNumber(pl.getKdLotNumber());
		frame.setProductSpecCode(pl.getProductSpecCode());
		frame.setPlanOffDate(pl.getPlanOffDate());
		frame.setProductionDate(pl.getProductionDate());
		if(isJapaneseVIN) {
			frame.setShortVin(productId); 
		} else {
			frame.setShortVin(	ProductNumberDef.VIN.getModel(productId) +
								ProductNumberDef.VIN.getYear(productId) + 
								ProductNumberDef.VIN.getPlant(productId) +
								ProductNumberDef.VIN.getSequence(productId));
		}
		return frame;
	}

	private ProductStampingSequence createStampingSequence(ProductionLot pl, int i, String productId) {
		ProductStampingSequenceId stampingSequenceId = new ProductStampingSequenceId(pl.getProductionLot(), productId);
		ProductStampingSequence stampingSequence = new ProductStampingSequence();
		stampingSequence.setId(stampingSequenceId);
		stampingSequence.setStampingSequenceNumber(i+1);
		stampingSequence.setSendStatus(0);
		return stampingSequence;
	}

	private ProductionLotDTO addPLDTO(GPP305DTO plan305) { 
		ProductionLotDTO pldto = new ProductionLotDTO(plan305);
		String strProductionLot = plan305.generateProductionLot();
		pldto.setProductionLot(strProductionLot);
		pldto.setProdLotKd(strProductionLot);
		return pldto;
	}

//	Search incoming message and use appropriate data as initial record for PreProductionSchedule 
	private void initTails() {
		List<String> emptyRecords = new ArrayList<String>();
		for(String processLocation : siteProcessLocations) {
			if(getDao(PreProductionLotDao.class).countByPlantLineLocation(siteName, siteLineId, processLocation) == 0) {
				emptyRecords.add(processLocation);
			}
		}
		if(emptyRecords.isEmpty()) {
			return;
		}
		for (int count=0; count<receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}
			for(String receivedRecord : receivedRecords) {
				GPP305DTO plan305 = new GPP305DTO();
				simpleParseHelper.parseData(plan305, receivedRecord);
				if(validateCurrentRecord(plan305)) {
					String strProcLoc = plan305.getProcessLocation();
					if(emptyRecords.contains(strProcLoc)) {
						PreProductionLotDTO ppldto = new PreProductionLotDTO(plan305);
						PreProductionLot ppl = ppldto.derivePreProductionLot();
						getDao(PreProductionLotDao.class).save(ppl);
						ProductionLotDTO pldto = addPLDTO(plan305);
						ProductionLot pl = pldto.deriveProductionLot();
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
						emptyRecords.remove(strProcLoc);
						if(emptyRecords.isEmpty()) {
							return;
						}
					}
				}
			}
		}
	}
	
private void trackFrames(List<Frame> frames) {
		
		if(StringUtils.isEmpty(getProcessPointId())) return;
		
		for(Frame frame : frames) {
			ServiceFactory.getService(TrackingService.class)
				.track(ProductType.FRAME, frame.getProductId(), getProcessPointId());
		}
	}
	
	private void trackEngines(List<Engine> engines) {
		
		if(StringUtils.isEmpty(getProcessPointId())) return;
		
		for(Engine engine : engines) {
			ServiceFactory.getService(TrackingService.class)
				.track(ProductType.ENGINE, engine.getProductId(), getProcessPointId());
		}
	}
	
	private String getProcessPointId() {
		return getProperty(OIFConstants.PROCESS_POINT_ID, "");
	}
	
	
	private Double findSequence(String planCode, Map<String, Double> sequences) { 
		Double maxSequence = 0d;
		if(sequences.containsKey(planCode)) {
			maxSequence = sequences.get(planCode);
			maxSequence += 1;
		} else {
			maxSequence = getDao(PreProductionLotDao.class).findMaxSequence(planCode);
			if(maxSequence == null || maxSequence == 0) {
				maxSequence = 0d;  
			} 
		}
		return maxSequence;
	}
	// Check if current demand type matches with any of the configured Hold Demand Types
	private boolean isNotHoldDemandType(String currentLotDemandType) {
		String demandTypesProperty = getProperty(OIFConstants.NOT_HOLD_DEMANDTYPES, "MP");
		String[] demandTypes = demandTypesProperty.split(":");
		for(String demandType:demandTypes){
			if(demandType.trim().equalsIgnoreCase(currentLotDemandType)) return true;
		}
		return false;
	}

}