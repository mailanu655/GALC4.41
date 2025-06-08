package com.honda.galc.client.engine.aeon;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.EngineNumberingDto;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;

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
public class EngineNumberingModel {
	
	private static final String BLOCK_MC = "BLOCK MC";
	private static final String OIF_TRACKING_STATUS ="OIF_TRACKING_STATUS";
	private static final String PPID_BLOCK_TRANSFER = "PPID_BLOCK_TRANSFER";
	private static final String CHECK_BLOCK_REFERENCE_NUMBER = "CHECK_BLOCK_REFERENCE_NUMBER";
	
	private ApplicationContext applicationContext;
	
	private Engine expectedEngine = null;
	private PreProductionLot currentPreProdLot = null;
	
	private BuildAttributeCache buildAttributeCache = new BuildAttributeCache();
	
	public EngineNumberingModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public Engine getExpectedEngine() {
		return expectedEngine;
	}

	public void setExpectedEngine(Engine expectedEngine) {
		this.expectedEngine = expectedEngine;
	}

	public PreProductionLot getCurrentPreProdLot(String productionLot) {
		if(currentPreProdLot != null && currentPreProdLot.getProductionLot().equals(productionLot)) return currentPreProdLot;
		currentPreProdLot = getDao(PreProductionLotDao.class).findByKey(productionLot);
		return currentPreProdLot;
	}

	public void loadData() {
		ExpectedProduct engine = getDao(ExpectedProductDao.class).findByKey(applicationContext.getProcessPointId());
		if(engine == null) throw new TaskException("Expected Product is null");
		expectedEngine = findEngine(engine.getProductId()); 
	}
	
	public List<BlockLoad> getLoadedBlocks() {
		List<BlockLoad> blockLoads = getDao(BlockLoadDao.class).findAllNonStampedBlocks();
		Collections.reverse(blockLoads);
		return blockLoads;
	}
	
	public BlockLoad findPreviousBlock(List<BlockLoad> blockLoads, BlockLoad currentBlockLoad) {
		int ref = currentBlockLoad.getReferenceNumber();
		if(ref > 1) {
			
			for(BlockLoad blockLoad : blockLoads) {
				if(blockLoad.getProductionLot().equals(currentBlockLoad.getProductionLot()) && 
				   blockLoad.getReferenceNumber() == currentBlockLoad.getReferenceNumber() -1) return blockLoad;
			}
			
			return null;
		}else {
			PreProductionLot previousProductionLot = getDao(PreProductionLotDao.class).findParent(currentBlockLoad.getProductionLot());
			
			if(previousProductionLot == null) return null;
			
			for(BlockLoad blockLoad : blockLoads) {
				if(blockLoad.getProductionLot().equals(previousProductionLot.getProductionLot()) && 
						   blockLoad.getReferenceNumber() == previousProductionLot.getLotSize()) return blockLoad;
			}
		}
		
		return null;
		
	}
	
	public List<EngineNumberingDto> getProcessedEngines() {
		return getDao(EngineDao.class).findAllRecentStampedEngines(applicationContext.getProcessPointId(),getPPIDForBlockTransfer());
	}
	
	public Block findBlock(String blockNumber) {
		Block block = getDao(BlockDao.class).findByMCDCNumber(blockNumber);
		if(block == null) {
			// assume all the outside blocks will be imported into block table first
			throw new TaskException("block " + blockNumber + " does not exist in block table");
		}
		return block;
	}
	
	public void saveInstalledPart(String ein, Block block) {
		
		InstalledPart part = new InstalledPart(ein,BLOCK_MC);
		part.setPartSerialNumber(block.getMcSerialNumber());
		part.setInstalledPartStatus(InstalledPartStatus.OK);
		block.setEngineSerialNumber(ein);
		getDao(BlockDao.class).save(block, part);
	}
	
	public void moveInstalledPartsToEngine(String ein, String mcNumber) {
		// if a EIN is going through engine numbering, any existing build results would be invalid and should be deleted.
		// this change will not only support the addition of Knock Bolt install on piston line but it will
		// also support Inline engine rebuild.
		
		getDao(MeasurementDao.class).deleteProdIds(new ArrayList<String>(Arrays.asList(new String[]{ein})));
		getDao(InstalledPartDao.class).deleteProdIds(new ArrayList<String>(Arrays.asList(new String[]{ein})));
		
		getDao(InstalledPartDao.class).moveAllData(ein, mcNumber);
	}
	
	public void updateBlockLoadStatus(String mcNumber,BlockLoadStatus status) {
				getDao(BlockLoadDao.class).updateStatus(mcNumber, status);
	}
	
	public void updateBlockLoad(BlockLoad blockLoad) {
		getDao(BlockLoadDao.class).save(blockLoad);
	}
	
	public void invokeTracking(String ein) {
		getService(TrackingService.class).track(ProductType.ENGINE, ein, applicationContext.getProcessPointId());
	}
	
	public void issueProductOnNotification(PreProductionLot preProductionLot) {
		ServiceFactory.getNotificationService(IProductOnNotification.class)
			.execute(	preProductionLot.getProductionLot(), 
							preProductionLot.getProcessLocation(), 
							preProductionLot.getStampedCount());
	}
	
	public BlockLoad getBlockLoad(String mcNumber) {
		return getDao(BlockLoadDao.class).findByKey(mcNumber);
	}
	
	public void updateExpectedProduct(String ein) {
		expectedEngine = findNextProduct(ein);
		if(expectedEngine == null) throw new TaskException("Could not find next engine");
		ExpectedProduct expectedProduct = new ExpectedProduct(expectedEngine.getProductId(),applicationContext.getProcessPointId());
		getDao(ExpectedProductDao.class).update(expectedProduct);
	}
	
	public PreProductionLot updateStampCount(Engine engine) {
		PreProductionLot preProductionLot = getCurrentPreProdLot(engine.getProductionLot());
		if(preProductionLot.getStampedCount() < preProductionLot.getLotSize()){
			preProductionLot.setStampedCount(preProductionLot.getStampedCount() + 1);
			boolean isEndOfLot = preProductionLot.getStampedCount() == preProductionLot.getLotSize();
			preProductionLot.setSendStatus(isEndOfLot? PreProductionLotSendStatus.DONE : PreProductionLotSendStatus.INPROGRESS);
			getDao(PreProductionLotDao.class).save(preProductionLot);
			getDao(ProductionLotDao.class).updateLotStatus(preProductionLot.getProductionLot(), 1);
		}
		
		
		return preProductionLot;
	}
	
	public ShippingVanningSchedule createShippingVanningSchedule(PreProductionLot preProductionLot) {
		 return getDao(ShippingVanningScheduleDao.class).saveSchedule(preProductionLot);
	}
	
	public Engine findNextProduct(String ein) {
		Engine engine = getDao(EngineDao.class).findNextProduct(ein);
		if(engine != null) engine.setProdLot(getProductionLot(engine.getProductionLot()));
		
		return engine;
	}
	
	public BlockLoad checkBlockLoaded(Block block) {
		BlockLoad blockLoad = getDao(BlockLoadDao.class).findByKey(block.getMcSerialNumber());
		if(blockLoad == null) 
			throw new TaskException("block " + block.getMcSerialNumber() + " is not in the block load line");
		if(blockLoad.getStatus().equals(BlockLoadStatus.STAMPED)) 
			throw new TaskException("block " + block.getMcSerialNumber() + " has been stamped");
		if(blockLoad.getStatus().equals(BlockLoadStatus.REMOVE)) 
			throw new TaskException("block " + block.getMcSerialNumber() + " is in REMOVE status");
		return blockLoad;
	}
	
	public void checkBlockInstalled(Block block) {
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(BLOCK_MC, block.getMcSerialNumber());
		if(!installedParts.isEmpty())
			throw new TaskException("Block " + block.getMcSerialNumber() + " has been used by engine: " + installedParts.get(0).getProductId());
	}
	
	public Engine findEngine(String ein) {
		Engine engine = getDao(EngineDao.class).findByKey(ein);
		if(engine == null)
			throw new TaskException("EIN number " + ein + " does not exist");
		
		engine.setProdLot(getProductionLot(engine.getProductionLot()));
		return engine;
	}
	
	public Engine checkEngineStamped(Engine engine) {
		if(!engine.getTrackingStatus().equals(getEngineOifTrackingStatus()))
			throw new TaskException("EIN number " + engine.getProductId() + " was stamped");
		return engine;
	}
	
	public void checkBlockModelType(Engine engine,Block block){
		PreProductionLot prodLot = getDao(PreProductionLotDao.class).findByKey(engine.getProductionLot());
		String attributeStr = buildAttributeCache.findAttributeValue(prodLot.getProductSpecCode(), "BLOCK_MC_MODEL");
		String[] attributes = StringUtils.split(attributeStr, ",");
		for(String attribute : attributes){
			if(block.getModelCode().equalsIgnoreCase(StringUtils.trim(attribute))) return;
		}
		throw new TaskException("block " + block.getMcSerialNumber() + "'s model code does not match " + attributeStr);
	}
	
	public ProductionLot getProductionLot(String productionLot) {
		return getDao(ProductionLotDao.class).findByKey(productionLot);
	}
	
	public void checkBlockReferenceNumber(Engine engine, BlockLoad blockLoad) {
		
		if(!isCheckBlockReferenceNumber() || engine == null) return;
		
		PreProductionLot lot = getCurrentPreProdLot(engine.getProductionLot());
		engine.setProdLot(getProductionLot(lot.getProductionLot()));
		if(!engine.getProductionLot().equals(blockLoad.getProductionLot())) {
			throw new TaskException("Block " + blockLoad.getMcNumber() + "'s production lot " + blockLoad.getProductionLot() + " does not match \n" + 
					" engine " + engine.getProductId() + " 's production lot " + engine.getProductionLot());
		}else if(engine.getLotPosition() != blockLoad.getReferenceNumber()) {
			throw new TaskException("Block " + blockLoad.getMcNumber() + "'s reference number " + blockLoad.getReferenceNumber() + " does not match \n" + 
					" engine " + engine.getProductId() + " 's lot position " + engine.getLotPosition());
		}
	}
	
	public int checkBlock(Engine engine, BlockLoad blockLoad) {
		if(!isCheckBlockReferenceNumber() || engine == null) return 0;
		
		if(!engine.getProductionLot().equals(blockLoad.getProductionLot())) return 2;
		else if(engine.getLotPosition() != blockLoad.getReferenceNumber()) return 3;
		
		return 1;
	}
	
	public void matchNextExpectedEngine(String engineNumber) {
		if( expectedEngine == null || !expectedEngine.getProductId().equals(engineNumber))
			throw new TaskException("engine # " + engineNumber + " does not match the expected engine # " + ObjectUtils.toString(expectedEngine));
	}
	
	private String getEngineOifTrackingStatus() {
		return PropertyService.getProperty(applicationContext.getProcessPointId(),OIF_TRACKING_STATUS,"MEEN3");
	}
	
	private String getPPIDForBlockTransfer() {
		return PropertyService.getProperty(applicationContext.getProcessPointId(),PPID_BLOCK_TRANSFER,"AE0EN12001");
	}
	
	private boolean isCheckBlockReferenceNumber() {
		return PropertyService.getPropertyBoolean(applicationContext.getProcessPointId(),CHECK_BLOCK_REFERENCE_NUMBER,true);

	}
	
}


