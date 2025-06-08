package com.honda.test.engine;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.Arguments;
import com.honda.galc.client.NonCachedApplicationContext;
import com.honda.galc.client.engine.aeon.BlockLoadModel;
import com.honda.galc.client.engine.aeon.EngineNumberingModel;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.BlockHistoryDao;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockHistory;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AEBlockLoadEngineNumberingTest {
	
	static BlockLoadModel blockLoadModel;
	
	static EngineNumberingModel engineNumberingModel;
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/engine/ae_block_load.sql");
		TestUtils.startWebServer();
		
	}
	
	@Test
	public void Test1StartBlockLoad() {
		List<String> args = new ArrayList<String>();
		args.add("rootdir");
		args.add("server url");
		args.add("AE0EN10801");
		Arguments arguments = Arguments.create(args);
		ApplicationContext appContext = NonCachedApplicationContext.create(arguments);
		blockLoadModel = new BlockLoadModel(appContext);
		
		blockLoadModel.loadData();
		
		assertNotNull(blockLoadModel.getCurrentPreProdLot());
		assertEquals("HCM 03AE201810190120", blockLoadModel.getCurrentPreProdLot().getProductionLot());
		
		assertNotNull(blockLoadModel.getNextPreProdLots());
		assertEquals(2,blockLoadModel.getNextPreProdLots().size());
		assertEquals("HCM 03AE201810190130", blockLoadModel.getNextPreProdLots().get(0).getProductionLot());
		assertEquals("HCM 03AE201810190140", blockLoadModel.getNextPreProdLots().get(1).getProductionLot());
		
		assertEquals(1,blockLoadModel.getNextReferenceNumber());
	}
	
	@Test
	public void Test2LoadBlock1() {
		//receive block
		Block block = blockLoadModel.validateMCNumber("5BA0HC690710598Q");
		assertNotNull(block);
		
		blockLoadModel.createCurrentBlockLoad(block);
		
		//save block data
		blockLoadModel.saveBlockData();
		
		BlockLoad blockLoad = getDao(BlockLoadDao.class).findByKey("5BA0HC690710598Q");
		assertNotNull(blockLoad);
		assertEquals(1,blockLoad.getReferenceNumber());
		assertEquals(BlockLoadStatus.LOADED,blockLoad.getStatus());
		
		// update block tracking
		blockLoadModel.invokeTracking();
		
		blockLoadModel.resetData();
		
		blockLoadModel.loadData();
		
		
		TestUtils.sleep(300);
		
		List<BlockHistory> blockHistoryList = getDao(BlockHistoryDao.class).findAllByProductId(block.getBlockId());
		assertEquals(1,blockHistoryList.size());
		
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(1,inProcessProducts.size());
		
		
	}
	
	@Test
	public void Test3LoadBlock2() {
		//receive block
		Block block = blockLoadModel.validateMCNumber("5BA0HC690710590I");
		assertNotNull(block);
		
		blockLoadModel.createCurrentBlockLoad(block);
		
		//save block data
		blockLoadModel.saveBlockData();
		
		BlockLoad blockLoad = getDao(BlockLoadDao.class).findByKey("5BA0HC690710590I");
		assertNotNull(blockLoad);
		assertEquals(2,blockLoad.getReferenceNumber());
		assertEquals(BlockLoadStatus.LOADED,blockLoad.getStatus());
		
		// update block tracking
		blockLoadModel.invokeTracking();
		
		blockLoadModel.resetData();
		
		blockLoadModel.loadData();
		
		
		TestUtils.sleep(300);
		List<BlockHistory> blockHistoryList = getDao(BlockHistoryDao.class).findAllByProductId(block.getBlockId());
		assertEquals(1,blockHistoryList.size());
		
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(2,inProcessProducts.size());
		
	}
	
	@Test
	public void Test4StartEngineNumbering() {
		List<String> args = new ArrayList<String>();
		args.add("rootdir");
		args.add("server url");
		args.add("AE0EN11001");
		Arguments arguments = Arguments.create(args);
		ApplicationContext appContext = NonCachedApplicationContext.create(arguments);
		engineNumberingModel = new EngineNumberingModel(appContext);
		
		engineNumberingModel.loadData();
		
		assertNotNull(engineNumberingModel.getExpectedEngine());
		assertEquals("K20C24205985", engineNumberingModel.getExpectedEngine().getProductId());
	}
	
	@Test
	public void Test5EngineNumberingReceivingMCNumber() {
		Block block = null; 
		try{
			block = engineNumberingModel.findBlock("5BA0HC690710598Q");
		}catch(Exception ex) {
		}
		
		assertNotNull(block);
		
		BlockLoad blockLoad = null;
		try{
			blockLoad = engineNumberingModel.checkBlockLoaded(block);
		}catch(Exception ex) {
		}
		
		assertNotNull(blockLoad);
		
		try{
			engineNumberingModel.checkBlockInstalled(block);
		}catch(Exception ex) {
			assertTrue(false);
		}
		
	}
	
	@Test
	public void Test6EngineNumberingEngineStamped() {
		Block block = null;
		BlockLoad blockLoad = null;
		
		try {
		 block = engineNumberingModel.findBlock("5BA0HC690710598Q");
		 blockLoad = engineNumberingModel.checkBlockLoaded(block);
		}catch(Exception ex) {
		}
		
		assertNotNull(block);
		assertNotNull(blockLoad);
		
		Engine engine = null;
		
		try {
			engine = engineNumberingModel.findEngine("K20C24205985");
			engineNumberingModel.checkEngineStamped(engine);
			engineNumberingModel.checkBlockModelType(engine, block);
			engineNumberingModel.checkBlockReferenceNumber(engine,blockLoad);
			engineNumberingModel.matchNextExpectedEngine(engine.getProductId());
		}catch(Exception ex) {
			assertTrue(false);
		}
		assertNotNull(engine);
		
		try{
			engineNumberingModel.moveInstalledPartsToEngine("K20C24205985", block.getMcSerialNumber());
			engineNumberingModel.saveInstalledPart("K20C24205985", block);
			engineNumberingModel.updateBlockLoadStatus(block.getMcSerialNumber(), BlockLoadStatus.STAMPED);
			PreProductionLot preProductionLot = engineNumberingModel.updateStampCount(engine);
			
//			if(preProductionLot != null && preProductionLot.getStampedCount() == 1) {
//				ShippingVanningSchedule schedule = model.createShippingVanningSchedule(preProductionLot);
//				if(schedule != null) {
//					getLogger().info("Created Shipping Vanning Schedule " + schedule);
//				}else {
//					// a new lot passed AE ON but no vanning schedule is created. This situation most likes does not happen
//				}
//			}
			engineNumberingModel.updateExpectedProduct("K20C24205985");
			engineNumberingModel.invokeTracking("K20C24205985");
		}catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
		
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByProductId("K20C24205985");
		assertEquals(1, installedParts.size());
		assertEquals("BLOCK MC",installedParts.get(0).getPartName());
		
		PreProductionLot preProdLot = getDao(PreProductionLotDao.class).findByKey("HCM 03AE201810190120");
		assertEquals(1, preProdLot.getStampedCount());
		assertEquals(1, preProdLot.getSendStatusId());
		
		BlockLoad blockLoad1 = getDao(BlockLoadDao.class).findByKey("5BA0HC690710598Q");
		assertEquals("9", blockLoad1.getStatusId());
		
		Block block1 = getDao(BlockDao.class).findByMCSerialNumber("5BA0HC690710598Q");
		assertEquals("K20C24205985",block1.getEngineSerialNumber());
		
		ExpectedProduct expectedProduct = getDao(ExpectedProductDao.class).findByKey("AE0EN11001");
		assertEquals("K20C24205986", expectedProduct.getProductId());
		
		TestUtils.sleep(300);
		
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("K20C24205985", "AE0EN11001");
		assertEquals(1,productResults.size());
		
		InProcessProduct inProcessProduct = getDao(InProcessProductDao.class).findByKey("K20C24205985");
		assertNotNull(inProcessProduct);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
}
