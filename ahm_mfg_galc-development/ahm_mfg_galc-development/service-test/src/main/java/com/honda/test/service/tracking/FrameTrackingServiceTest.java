package com.honda.test.service.tracking;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.CounterByModelGroupDao;
import com.honda.galc.dao.product.CounterByProductSpecDao;
import com.honda.galc.dao.product.CounterByProductionLotDao;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.CounterByModelGroup;
import com.honda.galc.entity.product.CounterByProductSpec;
import com.honda.galc.entity.product.CounterByProductionLot;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FrameTrackingServiceTest {

	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/tracking/frame_tracking_service.sql");
		
		TestUtils.startWebServer();
	}
	
	/*
	 * isProcessCount = true;
	 * isProcessCountByModel=true;
	 * isProcessCountByProductionLot=true;
	 * isCreateNewThreadForTracking = true;
	 * process point 1AF1D1202 TrackingPointFlag = 0
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test01TrackingWithCounts() {
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A52KH750450");
		assertNotNull(frame);
		getTrackingService().track(frame, "1AF1D1202");
		
		//need to sleep since tracking is running in the separate thread
		TestUtils.sleep(1500);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A52KH750450", "1AF1D1202");
		assertEquals(1,productResults.size());
		
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(0,inProcessProducts.size());
				
		frame = getDao(FrameDao.class).findByKey("2HGFC3A52KH750450");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(1,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(1,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(1,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(1,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = true;
	 * isProcessCountByModel=true;
	 * isProcessCountByProductionLot=true;
	 * isCreateNewThreadForTracking = true;
	 * process point 1AF1D1202 TrackingPointFlag = 0
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test02TrackingWithCounts() {
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A50KH750429");
		assertNotNull(frame);
		getTrackingService().track(frame, "1AF1D1202");
		
		//need to sleep since tracking is running in the separate thread
		TestUtils.sleep(1500);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A50KH750429", "1AF1D1202");
		assertEquals(1,productResults.size());
		
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(0,inProcessProducts.size());
				
		frame = getDao(FrameDao.class).findByKey("2HGFC3A50KH750429");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test03TrackingWithoutCountsAndInSameThread() {
		disableCounts();
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A50KH750432");
		assertNotNull(frame);
		getTrackingService().track(frame, "1AF1D1202");
		
		// no sleep since in the same thread
		// TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A50KH750432", "1AF1D1202");
		assertEquals(1,productResults.size());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(1,inProcessProducts.size());
		
		frame = getDao(FrameDao.class).findByKey("2HGFC3A50KH750432");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test04TrackingWithProductTypeAndProductId() {
		disableCounts();
		getTrackingService().track(ProductType.FRAME,"2HGFC3A50KH750446", "1AF1D1202");
		
		// no sleep since in the same thread
		// TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A50KH750446", "1AF1D1202");
		assertEquals(1,productResults.size());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(2,inProcessProducts.size());
		
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A50KH750446");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test05TrackingWithProductTypeAndProductIdAndDeviceId() {
		disableCounts();
		getTrackingService().track(ProductType.FRAME,"2HGFC3A51KH750424", "1AF1D1202","TestDevice");
		
		// no sleep since in the same thread
		// TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A51KH750424", "1AF1D1202");
		assertEquals(1,productResults.size());
		assertEquals("TestDevice",productResults.get(0).getDeviceId());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(3,inProcessProducts.size());
		
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A51KH750424");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test06TrackingWithProductAndDeviceId() {
		disableCounts();
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A52KH750433");
		assertNotNull(frame);
		
		getTrackingService().track(frame,"1AF1D1202","TestDevice");
		
		// no sleep since in the same thread
		// TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A52KH750433", "1AF1D1202");
		assertEquals(1,productResults.size());
		assertEquals("TestDevice",productResults.get(0).getDeviceId());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(4,inProcessProducts.size());
		frame = getDao(FrameDao.class).findByKey("2HGFC3A52KH750433");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
		
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 */
	@Test
	public void test08TrackingWithProductTypeAndProductHistory() {
	
		ProductResult productResult = new ProductResult("2HGFC3A51KH750438", "1AF1D1202", ServiceFactory.getDao(FrameDao.class).getDatabaseTimeStamp());
		productResult.setAssociateNo("user Id");
		getTrackingService().track(ProductType.FRAME, productResult);
		
		// no sleep since in the same thread
		// TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A51KH750438", "1AF1D1202");
		assertEquals(1,productResults.size());
		assertEquals("user Id",productResults.get(0).getAssociateNo());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(6,inProcessProducts.size());
		
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A51KH750438");
		assertEquals("1AF1D1202", frame.getLastPassingProcessPointId());
		
		List<Counter> counters = getDao(CounterDao.class).findAll();
		assertEquals(1,counters.size());
		assertEquals(2,counters.get(0).getPassingCounter());
		
		List<CounterByModelGroup> CounterByModelGroups = getDao(CounterByModelGroupDao.class).findAll();
		assertEquals(1,CounterByModelGroups.size());
		assertEquals(2,CounterByModelGroups.get(0).getPassingCounter());
		
		List<CounterByProductSpec> CounterByProductSpecs = getDao(CounterByProductSpecDao.class).findAll();
		assertEquals(1,CounterByProductSpecs.size());
		assertEquals(2,CounterByProductSpecs.get(0).getPassingCounter());
		
		List<CounterByProductionLot> CounterByProductionLots = getDao(CounterByProductionLotDao.class).findAll();
		assertEquals(1,CounterByProductionLots.size());
		assertEquals(2,CounterByProductionLots.get(0).getPassingCounter());
	}
	
	
	
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	 * process point 1AF1D1202 PassingCountFlag = 1
	 * process point 1AF1D1202 RecoveryPointFlag = 1
	 */
	@Test
	public void test07TrackingWithBackfill() {
		// set backfill process point
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey("1AF1D1202");
		assertNotNull(processPoint);
		
		processPoint.setBackFillProcessPointId("1PA1D6001");
		processPoint.setRecoveryPointFlag((short)1);
		getDao(ProcessPointDao.class).save(processPoint);
		
		// set recovery flag
		processPoint = getDao(ProcessPointDao.class).findByKey("1PA1D6001");
		assertNotNull(processPoint);
			
		processPoint.setRecoveryPointFlag((short)1);
		getDao(ProcessPointDao.class).save(processPoint);
				
		
		Frame frame = getDao(FrameDao.class).findByKey("2HGFC3A51KH750441");
		assertNotNull(frame);
		getTrackingService().track(frame, "1AF1D1202");
		
		//backfill process "1PA1D6001" runs on separate thread
		TestUtils.sleep(1000);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A51KH750441", "1AF1D1202");
		assertEquals(1,productResults.size());
		
		productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A51KH750441", "1PA1D6001");
		assertEquals(1,productResults.size());
		
		// one record in GAL176TBX
		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findAll();
		assertEquals(5,inProcessProducts.size());
				
				
	}
	
	/*
	 * isProcessCount = false;
	 * isProcessCountByModel=false;
	 * isProcessCountByProductionLot=false;
	 * isCreateNewThreadForTracking = false;
	 * process point 1AF1D1202 TrackingPointFlag = 1
	
	*/
	private void disableCounts() {
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		properties.add(new ComponentProperty("1AF1D1202","PROCESS_COUNT","false"));
		properties.add(new ComponentProperty("1AF1D1202","PROCESS_COUNT_BY_MODEL","false"));
		properties.add(new ComponentProperty("1AF1D1202","PROCESS_COUNT_BY_PRODUCTION_LOT","false"));
		properties.add(new ComponentProperty("1AF1D1202","PROCESS_COUNT_BY_PRODUCT_SPEC","false"));
		properties.add(new ComponentProperty("1AF1D1202","CREATE_NEW_THREAD_FOR_TRACKING","false"));
		
		
		getDao(ComponentPropertyDao.class).saveAll(properties);
		PropertyService.reset();
		
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey("1AF1D1202");
		assertNotNull(processPoint);
		
		processPoint.setTrackingPointFlag((short)1);
		getDao(ProcessPointDao.class).save(processPoint);
		
	}
	
	private TrackingService getTrackingService() {
		return getService(TrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
}
