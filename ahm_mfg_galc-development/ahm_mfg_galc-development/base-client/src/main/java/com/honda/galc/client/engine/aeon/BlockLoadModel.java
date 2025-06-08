package com.honda.galc.client.engine.aeon;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.openprotocol.model.LastTighteningResult;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * 
 * <h3>BlockLoadModel Class description</h3>
 * <p> BlockLoadModel description </p>
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
 * Mar 26, 2014
 *
 *
 */
public class BlockLoadModel {
	
	private ApplicationContext applicationContext;
	
	private List<BlockLoad> processedBlockLoads = new ArrayList<BlockLoad>();
	
	private BlockLoad lastBlockLoad = null;
	private BlockLoad currentBlockLoad = null;
	private BlockLoad remakeBlockLoad = null;
	
	private Block lastBlock = null;
	private Block currentBlock = null;
	
	private PreProductionLot currentPreProdLot = null;
	private List<PreProductionLot> nextPreProdLots;
	private long blockCountOfCurrentLot = 0;
	
	
	private List<LastTighteningResult> torqueResults = new ArrayList<LastTighteningResult>();
	private int torqueAttemptCount = 0;
	private int torqueIndex = 0;
	
	private List<LotControlRule> allRules;
	
	private Map<String,EngineSpec> engineSpecs = new HashMap<String, EngineSpec>();
	
	private BuildAttributeCache buildAttributeCache = new BuildAttributeCache();
	
	private static final String STARTER ="31200";
	private static final String BLOCK_MC = "BLOCK MC";
	private static final String BLOCK_MC_MODEL ="BLOCK_MC_MODEL";
	private static final String PPID_MC_OFF="PPID_MC_OFF";
	private static final String PROCESS_LOCATION="AE";
	private final String BLOCK_BORE_MEASURE = "BLOCK BORE MEASURE";
	protected final int BORE_MEASUREMENT_COUNT = 4;
	protected final String CRANK_JOURNAL = "CRANK JOURNAL";
	protected final int CRANK_JOURNAL_COUNT = 5;
	protected final boolean NOTIFY_STARTER_CHANGE;
	
	
	
	public BlockLoadModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		NOTIFY_STARTER_CHANGE = PropertyService.getPropertyBoolean(this.applicationContext.getProcessPointId(), "NOTIFY_STARTER_CHANGE", false);
		allRules = getDao(LotControlRuleDao.class).findAllByProcessPoint(applicationContext.getProcessPointId());
	}
	
	
	public Block getLastBlock() {
		return lastBlock;
	}


	public void setLastBlock(Block lastBlock) {
		this.lastBlock = lastBlock;
	}


	public Block getCurrentBlock() {
		return currentBlock;
	}


	public void setCurrentBlock(Block currentBlock) {
		this.currentBlock = currentBlock;
	}
	
	public int getTorqueAttemptCount() {
		return torqueAttemptCount;
	}

	public void setTorqueAttemptCount(int torqueAttemptCount) {
		this.torqueAttemptCount = torqueAttemptCount;
	}

	public int getTorqueIndex() {
		return torqueIndex;
	}


	public void setTorqueIndex(int torqueIndex) {
		this.torqueIndex = torqueIndex;
	}


	public BlockLoad getCurrentBlockLoad() {
		return currentBlockLoad;
	}

	public void setCurrentBlockLoad(BlockLoad currentBlockLoad) {
		this.currentBlockLoad = currentBlockLoad;
	}

	public PreProductionLot getCurrentPreProdLot() {
		return currentPreProdLot;
	}

	public BlockLoad getRemakeBlockLoad() {
		return remakeBlockLoad;
	}

	public void setRemakeBlockLoad(BlockLoad remakeBlockLoad) {
		this.remakeBlockLoad = remakeBlockLoad;
		this.currentPreProdLot = getDao(PreProductionLotDao.class).findByKey(remakeBlockLoad.getProductionLot());
	}

	public void setCurrentPreProdLot(PreProductionLot currentPreProdLot) {
		this.currentPreProdLot = currentPreProdLot;
	}
	
	public List<PreProductionLot> getNextPreProdLots() {
		return nextPreProdLots;
	}

	public void setNextPreProdLots(List<PreProductionLot> nextPreProdLots) {
		this.nextPreProdLots = nextPreProdLots;
	}
	
	public String getCurrentLotSeq() {
			return "" + getNextReferenceNumber() + "/" + currentPreProdLot.getLotSize();
	}
	
	public int getNextReferenceNumber() {
		if(remakeBlockLoad != null) return remakeBlockLoad.getReferenceNumber();
		if(currentBlockLoad != null && 
				currentBlockLoad.getProductionLot().equalsIgnoreCase(currentPreProdLot.getProductionLot())) {
				if(blockCountOfCurrentLot < currentPreProdLot.getLotSize()) 
					return (int) blockCountOfCurrentLot + 1;
				else {
					// assume all the outside blocks will be imported into block table first
					throw new TaskException("Possible Out of Sync ! Call ISD. Current lot is fully loaded with blocks " );
				}
		}else return 1;
	}
	
	public void loadData() {
		
		lastBlock = null;
		lastBlockLoad = null;
		torqueResults.clear();
		
		currentBlockLoad = getDao(BlockLoadDao.class).findLastBlockLoad();
		
		processedBlockLoads = getDao(BlockLoadDao.class).findAllNonStampedBlocks();
	
		// check if a remake blockload is a next preproduction lot
		PreProductionLot preProdLot = getDao(PreProductionLotDao.class).findNext(currentBlockLoad.getProductionLot());
		BlockLoad remakeCurrentBlockLoad = findLastBlockLoad(preProdLot);
		if(remakeCurrentBlockLoad != null) currentBlockLoad = remakeCurrentBlockLoad;

		loadPreProductionLots(currentBlockLoad);

		if(currentBlockLoad != null)
			currentBlock = getDao(BlockDao.class).findByMCDCNumber(currentBlockLoad.getMcNumber());
		
		
		blockCountOfCurrentLot = getDao(BlockLoadDao.class).countByProductionLot(currentPreProdLot.getProductionLot());
	}
	
	private BlockLoad findLastBlockLoad(PreProductionLot preProdLot) {
		
		for(BlockLoad bl : processedBlockLoads) {
			if(bl.getProductionLot().equalsIgnoreCase(preProdLot.getProductionLot())) return bl;
		}
		
		return null;
	}
	
	private List<BlockLoad> findBlockLoads(List<BlockLoad> allBlockLoads, String prodLot) {
		List<BlockLoad> blockLoads = new ArrayList<BlockLoad>();
		if(StringUtils.isEmpty(prodLot)) return blockLoads;
		for(BlockLoad item : allBlockLoads) {
			if(item.getProductionLot().equals(prodLot)) blockLoads.add(item);
		}
		return blockLoads;
	}
	
	public List<BlockLoad> getProcessedBlocks() {
		return processedBlockLoads;
	}
	
	public List<LastTighteningResult> getTorqueResults() {
		return this.torqueResults;
	}
	
	public boolean isTorqueResultOk(LastTighteningResult result) {
		return result.getTorqueStatus()== 1 && result.getAngleStatus()== 1 && result.getTighteningStatus() == 1;
	}
	
	
	public void findLoadedBlocks() {
		processedBlockLoads.clear();
		List<BlockLoad> allBlockLoads = getDao(BlockLoadDao.class).findAllNonStampedBlocks();
		PreProductionLot preProductionLot = getDao(PreProductionLotDao.class).findCurrentProductLotAtBlockLoad();
		String prodLot = preProductionLot == null ? null : preProductionLot.getProductionLot();
		do{
			List<BlockLoad> blockLoads = findBlockLoads(allBlockLoads,prodLot);
			if(blockLoads.isEmpty()) break;
			processedBlockLoads.addAll(findBlockLoads(allBlockLoads,prodLot));
			PreProductionLot lot = getDao(PreProductionLotDao.class).findParent(prodLot);
			if(lot != null) prodLot = lot.getProductionLot(); else break;
		}while(true);
	}
	
	
	public Block validateMCNumber(String mcNumber) {
		Block block = getDao(BlockDao.class).findByMCDCNumber(mcNumber);
		if(block == null) {
			// assume all the outside blocks will be imported into block table first
			throw new TaskException("block " + mcNumber + " does not exist in block table");
		}
		
		lastBlock = currentBlock;
		currentBlock = block;
		
		// we have valid block here
		
		checkBlockDefectStatus(currentBlock);
		
		checkBlockHoldStatus(currentBlock);
		
		checkBlockModelType(currentBlock);
		
		checkBlockPassedMCOff(currentBlock);
		
		checkBlockLoaded(currentBlock);
		
		checkBlockInstalled(currentBlock);
		
		return currentBlock;
	}
	
	public BlockLoad createCurrentBlockLoad(Block block) {
		BlockLoad blockLoad = new BlockLoad();
		blockLoad.setMcNumber(block.getMcSerialNumber());
		blockLoad.setProductionLot(currentPreProdLot.getProductionLot());
		blockLoad.setLotSize(currentPreProdLot.getLotSize());
		blockLoad.setProductSpecCode(currentPreProdLot.getProductSpecCode());
		blockLoad.setReferenceNumber(getNextReferenceNumber());
		blockLoad.setStatus(BlockLoadStatus.LOADED);
		blockLoad.setKdLotNumber(currentPreProdLot.getKdLot());
		lastBlockLoad = currentBlockLoad;
		currentBlockLoad = blockLoad;
		return blockLoad;
	}
	
	public void updateBlockLoadStatus(BlockLoad blockLoad,BlockLoadStatus status) {
				blockLoad.setStatus(status);
				getDao(BlockLoadDao.class).save(blockLoad);
	}
	
	public String getStarter(String ymto) {
		return buildAttributeCache.findAttributeValue(ymto, STARTER);
	}
	
	private void checkBlockDefectStatus(Block block) {
		if(!block.isDirectPassStatus() && !block.isRepairedStatus()){
			throw new TaskException("block " + block.getMcSerialNumber()  + " has defect: " + block.getDefectStatus());
		};
	}
	
	private void checkBlockHoldStatus(Block block){
		if(block.getHoldStatus() != 0) {
			throw new TaskException("block " + block.getMcSerialNumber() + " is on hold");
		}
	}
	
	private void checkBlockPassedMCOff(Block block){
		String[] offPPIds = getMCBlockOffProcessPointIds();
		if(offPPIds.length <=0) return;
		for(String offPPId  : offPPIds) {
			if(!StringUtils.isEmpty(offPPId) && offPPId.equals(block.getLastPassingProcessPointId())) return;
		}
		throw new TaskException("block " + block.getMcSerialNumber() + " has not passed MC off process");
	}
	
	private void checkBlockModelType(Block block){
		String attributeStr = buildAttributeCache.findAttributeValue(currentPreProdLot.getProductSpecCode(), BLOCK_MC_MODEL);
		String[] attributes = StringUtils.split(attributeStr, ",");
		for(String attribute : attributes){
			if(block.getModelCode().equalsIgnoreCase(StringUtils.trim(attribute))) return;
		}
		throw new TaskException("block " + block.getMcSerialNumber() + "'s model code does not match " + attributeStr);
	}
	
	private void checkBlockLoaded(Block block) {
		BlockLoad blockLoad = getDao(BlockLoadDao.class).findByKey(block.getMcSerialNumber());
		if(blockLoad != null) throw new TaskException("block " + block.getMcSerialNumber() + " was loaded on the block load line");
	}
	
	private void checkBlockInstalled(Block block) {
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(BLOCK_MC, block.getMcSerialNumber());
		if(!installedParts.isEmpty())
			throw new TaskException("Block " + block.getMcSerialNumber() + "has been used by engine: " + installedParts.get(0).getProductId());
	}
	
	private String[] getMCBlockOffProcessPointIds() {
		String mcOff = PropertyService.getProperty(applicationContext.getProcessPointId(),PPID_MC_OFF);
		return StringUtils.isBlank(mcOff) ? new String[0] : mcOff.split(",");
	}
	
	private void loadPreProductionLots(BlockLoad lastBlockLoad) {
		if(lastBlockLoad == null)
			currentPreProdLot = getDao(PreProductionLotDao.class).findCurrentPreProductionLot(PROCESS_LOCATION);
		else {
			currentPreProdLot = getDao(PreProductionLotDao.class).findByKey(lastBlockLoad.getProductionLot());  
			if(currentPreProdLot != null && currentPreProdLot.getLotSize() <= loadedBlockCount(currentPreProdLot)) 
				currentPreProdLot = getDao(PreProductionLotDao.class).findNext(currentPreProdLot.getProductionLot());
		}
		if(currentPreProdLot == null) throw new TaskException("Could not retrieve the current pre production lot");
		nextPreProdLots = findNextLots(currentPreProdLot.getProductionLot(),3);
	}
	
	private long loadedBlockCount(PreProductionLot lot) {
		return getDao(BlockLoadDao.class).countByProductionLot(lot.getProductionLot());
	}
	
	private List<PreProductionLot> findNextLots(String startLot,int count) {
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		String prodLot = startLot;
		for(int i=0;i<count;i++) {
			PreProductionLot lot = getDao(PreProductionLotDao.class).findNext(prodLot);
			if(lot == null) break;
			lots.add(lot);
			prodLot = lot.getProductionLot();
		}
		return lots;
	}
	
	public LotControlRule getRule(String ymto) {
		EngineSpec spec  = getEngineSpec(ymto);
		if(spec == null) return null;
		List<LotControlRule> rules = LotControlPartUtil
			.getLotControlRuleByProductSpec(spec, allRules);
		return !rules.isEmpty()? rules.get(0):null;
	}
	
	public void saveBlockData() {
		LotControlRule rule = getRule(currentBlockLoad.getProductSpecCode());
		if(rule != null) {
			
			List<String> partNames = new ArrayList<String>();
			partNames.add(rule.getPartNameString());
			getDao(InstalledPartDao.class).deleteInstalledParts(currentBlockLoad.getMcNumber(), partNames);
			
			if(!getTorqueResults().isEmpty()) {
				List<InstalledPart> parts = new ArrayList<InstalledPart>();
				InstalledPart installedPart = createInstalledPart(rule);
				parts.add(installedPart);
				getDao(InstalledPartDao.class).saveAll(parts);
			}
		}
		if(remakeBlockLoad != null) {
			getDao(BlockLoadDao.class).remove(remakeBlockLoad);
			currentBlockLoad.setStatus(BlockLoadStatus.REMAKE);
		}
		getDao(BlockLoadDao.class).save(currentBlockLoad);
		
		currentBlock.setDunnage(null);
		getDao(BlockDao.class).save(currentBlock);
	}
	
	public void resetData(){
		if(remakeBlockLoad !=null) {
			lastBlockLoad = null;
			lastBlock = null;
			remakeBlockLoad = null;
		}else {
			lastBlockLoad = currentBlockLoad;
			lastBlock = currentBlock;
			currentBlockLoad = null;
			currentBlock = null;
		}
	}
	
	private InstalledPart createInstalledPart(LotControlRule rule) {
		int index = 1;

		InstalledPart installedPart = new InstalledPart(currentBlockLoad.getMcNumber(),rule.getPartNameString());
		installedPart.setInstalledPartStatusId(1);
		installedPart.setAssociateNo(applicationContext.getProcessPointId());
		installedPart.setProcessPointId(applicationContext.getProcessPointId());

		for(LastTighteningResult result: torqueResults) {

			boolean isTorqueOk = result.getTorqueStatus()== 1 && result.getAngleStatus()== 1 && result.getTighteningStatus() == 1;
			Measurement meas = new Measurement(currentBlockLoad.getMcNumber(),rule.getPartNameString(),index);
			meas.setMeasurementValue(result.getTorque());
			meas.setMeasurementStatus(isTorqueOk ? MeasurementStatus.OK: MeasurementStatus.NG);
			installedPart.getMeasurements().add(meas);
			index++;
		}
		return installedPart;
	}
	
	public void invokeTracking() {
		getService(TrackingService.class).track(currentBlock, applicationContext.getProcessPointId());
	}
	
	private EngineSpec getEngineSpec(String ymto) {
		if(!engineSpecs.containsKey(ymto)){
			EngineSpec engineSpec = getDao(EngineSpecDao.class).findByKey(ymto);
			if(engineSpec != null) engineSpecs.put(ymto, engineSpec);
		}	
		if(engineSpecs.containsKey(ymto))return engineSpecs.get(ymto);
		else return null;
	}
	
	public String getBlockBoreMeasurement() {
		if(currentBlock == null) return "";
		BlockBuildResult result = getDao(BlockBuildResultDao.class).findById(currentBlock.getBlockId(), BLOCK_BORE_MEASURE);
		return result == null ? "" : result.getResultValue();
	}
	
	public List<BlockBuildResult> getCrankJournal() {
		List<BlockBuildResult> results = new ArrayList<BlockBuildResult>();
		if(currentBlock == null) return results;
		for(int i=0;i<CRANK_JOURNAL_COUNT;i++) {
			String partName = CRANK_JOURNAL + " " + (i+1);
			BlockBuildResult result = getDao(BlockBuildResultDao.class).findById(currentBlock.getBlockId(), partName);
			if(result != null) results.add(result);
		}
		return results;
	}
	
	public String getCrankJournal(List<BlockBuildResult> crankJournals,int index) {
		String partName = CRANK_JOURNAL + " " + (index + 1);
		for(BlockBuildResult result : crankJournals) {
			if(result.getPartName().equalsIgnoreCase(partName)) return result.getResultValue();
		}
		return "";
	}


}


