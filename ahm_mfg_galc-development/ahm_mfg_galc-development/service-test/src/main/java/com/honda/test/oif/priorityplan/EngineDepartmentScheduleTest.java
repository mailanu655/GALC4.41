package com.honda.test.oif.priorityplan;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.HostPriorityPlanDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.HostPriorityPlan;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.task.AsyncTaskExecutorService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

public class EngineDepartmentScheduleTest {

	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9082/BaseWeb/HttpServiceHandler");
		
	}
	
	@Test
	public void test1() {
		// getOifService().execute("OIF_RGALC_DEPT_SCHEDULE", null, "");
		
		getOifService().execute("OIF_PRUNE_ENGINE", null, "");
		TestUtils.sleep(3000);
		
//		List<HostPriorityPlan> hostPriorityPlans = getDao(HostPriorityPlanDao.class).findAll();
//		assertEquals(2, hostPriorityPlans.size());
//		assertEquals("Y", hostPriorityPlans.get(0).getRowProcessed());
//		assertEquals("Y", hostPriorityPlans.get(1).getRowProcessed());
//		
//		List<PreProductionLot> preProductionLots = getDao(PreProductionLotDao.class).findAllForProcessLocation("AE");
//		assertEquals(5, preProductionLots.size());
//		assertEquals("HCM 03AE201810190120", preProductionLots.get(0).getProductionLot());
//		assertEquals("HCM 03AE201810190130", preProductionLots.get(1).getProductionLot());
//		assertEquals("HCM 03AE201810190140", preProductionLots.get(2).getProductionLot());
//		assertEquals("HCM 03AE201811020700", preProductionLots.get(3).getProductionLot());
//		assertEquals("HCM 03AE201811020710", preProductionLots.get(4).getProductionLot());
//		
//		assertEquals("HCM 03AE201810190130", preProductionLots.get(0).getNextProductionLot());
//		assertEquals("HCM 03AE201810190140", preProductionLots.get(1).getNextProductionLot());
//		assertEquals("HCM 03AE201811020700", preProductionLots.get(2).getNextProductionLot());
//		assertEquals("HCM 03AE201811020710", preProductionLots.get(3).getNextProductionLot());
//		assertNull(preProductionLots.get(4).getNextProductionLot());
//		
//		List<ProductionLot> productionLots = getDao(ProductionLotDao.class).findAll();
//		assertEquals(5, productionLots.size());
//		
//		List<Engine> engines = getDao(EngineDao.class).findAllByProductionLot("HCM 03AE201811020700");
//		assertEquals(30, engines.size());
//		
//		engines = getDao(EngineDao.class).findAllByProductionLot("HCM 03AE201811020710");
//		assertEquals(30, engines.size());
//		
//		List<ProductResult> productResults = getDao(ProductResultDao.class).findAll();
//		assertEquals(60, productResults.size());
//		
//		List<InProcessProduct> inProcessProducts = getDao(InProcessProductDao.class).findSequenceListByDivision("ME");
//		assertEquals(60, inProcessProducts.size());
//		
	}
	
	private AsyncTaskExecutorService getOifService() {
		return getService(AsyncTaskExecutorService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
	//	DBUtils.resetData();
	}
}
