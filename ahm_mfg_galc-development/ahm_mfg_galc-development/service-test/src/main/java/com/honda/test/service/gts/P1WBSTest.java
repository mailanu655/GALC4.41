package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.GtsCarrierStatus;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneId;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.gts.GtsWbs1TrackingService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.SimulatorSocketServer;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P1WBSTest {
	
	private static String P1WBS = "P1WBS";
	
	private static SimulatorSocketServer socketServer = new SimulatorSocketServer();
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/gts/p1wbs.sql");
		socketServer.startServer();
	}
	
	@Test
	public void test01CarrierVinAssociation() {
		testCarrierFrameAssociation(2,"2HGFC3A50KH750429");
		
	}
	
	@Test
	public void test02DeviceCarrierVinAssociation() {
		
		String vin = "2HGFC3A50KH750446";
		int carrierId = 10;
		
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID("P1WBS.CARRIER_VIN_ASSOCIATION");
		dc.put("CARRIER_ID", carrierId);
		dc.put("PRODUCT_ID",vin);
		
		getWbsService().execute(dc);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(P1WBS, carrierId);
		
		assertNotNull(carrier);
		assertEquals(vin, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin));
		assertNotNull(product);
		assertEquals(frame.getProductionLot(), product.getLotNumber());
		assertEquals(GtsDefectStatus.DIRECT_PASS,product.getDefectStatus());
	}
	
	@Test
	public void test03ReceiveBodyCountS1() {
		DataContainer dc = new DefaultDataContainer();
		dc.put("BODY-COUNT", 2);
		dc.put("BP-S1-1", 2);
		dc.put("BP-S1-2", 10);
		dc.setClientID("P1WBS.BP-S1");
		socketServer.add(dc);
		
		issueIndicatorChange("BC-S1", 2);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("S1");
		assertEquals(2,lcs.size());
		assertEquals("002",lcs.get(0).getCarrierId());
		assertEquals("010",lcs.get(1).getCarrierId());
		assertEquals("2HGFC3A50KH750429",lcs.get(0).getProductId());
		
	}
	
	@Test
	public void test04ReceiveBodyPositionChange() {
		DataContainer rdc = new DefaultDataContainer();
		rdc.put("BP-R1-1", 15);
		rdc.setClientID("P1WBS.BP-R1");
		socketServer.add(rdc);
		
		issueIndicatorChange("UBP-R1-1", 15);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("R1");
		assertEquals(1,lcs.size());
		assertEquals("015",lcs.get(0).getCarrierId());
		
		TestUtils.assertIndicator(P1WBS,"UBP-R1-1", 15);
		
		issueIndicatorChange("UBP-R1-1", 0);
		
		lcs = findLaneCarriers("R1");
		assertEquals(0,lcs.size());
		
		TestUtils.assertIndicator(P1WBS,"UBP-R1-1", 0);
		
		
	}
	
	@Test
	public void test05ReceiveActiveLane() {
		
		issueIndicatorChange("AL-ENTRY_STORAGE", 2);
		
		TestUtils.assertIndicator(P1WBS,"MV-ENTRY_STORAGE_2", 1);
		TestUtils.assertIndicator(P1WBS,"AL-ENTRY_STORAGE", 2);
		
		issueIndicatorChange("AL-ENTRY_STORAGE", 4);
		
		TestUtils.assertIndicator(P1WBS,"MV-ENTRY_STORAGE_2", 0);
		TestUtils.assertIndicator(P1WBS,"MV-ENTRY_STORAGE_4", 1);
		TestUtils.assertIndicator(P1WBS,"AL-ENTRY_STORAGE", 4);
		
	}
	
	@Test
	public void test06ReceiveMoveStatus() {
		issueIndicatorChange("MV-TR1", 1);
		TestUtils.assertIndicator(P1WBS,"MV-TR1", 1);
		
		issueIndicatorChange("MV-TR1", 0);
		TestUtils.assertIndicator(P1WBS,"MV-TR1", 0);
		
	}
	
	@Test
	public void test07ReceivePaintPallet() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("PALLET_CARRIER-1", 18);
		dc.put("PALLET_CARRIER-2", 21);
		dc.setClientID("P1WBS.PALLET_CARRIER");
		socketServer.add(dc);
	
		issueIndicatorChange("U-LS-PaintPallet-CARRIER", 1);
		
		assertCarrierStatus("018", GtsCarrierStatus.PALLET);
		assertCarrierStatus("021", GtsCarrierStatus.PALLET);
		
		issueIndicatorChange("U-LS-PaintPallet-CARRIER", 0);
		
		dc = new DefaultDataContainer();
		dc.put("PALLET_CARRIER-1", 24);
		dc.put("PALLET_CARRIER-2", 26);
		dc.setClientID("P1WBS.PALLET_CARRIER");
		socketServer.add(dc);
		
		issueIndicatorChange("U-LS-PaintPallet-CARRIER", 1);
		
		assertCarrierStatus("018", GtsCarrierStatus.NORMAL);
		assertCarrierStatus("021", GtsCarrierStatus.NORMAL);
		assertCarrierStatus("024", GtsCarrierStatus.PALLET);
		assertCarrierStatus("026", GtsCarrierStatus.PALLET);
		
		issueIndicatorChange("U-LS-PaintPallet-CARRIER", 0);
		
	}
	
	@Test
	public void test08AddCarrierByUser() {
		getWbsService().addCarrierByUser("S2", 1, "25");
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("S2");
		assertEquals(1,lcs.size());
		assertEquals("25",lcs.get(0).getCarrierId());
		
		DataContainer dc = socketServer.getReceivedData("P1WBS.BP-S2");
		assertNotNull(dc);
		assertEquals("1", dc.getString("BODY-COUNT"));
		assertEquals("25",dc.getString("BP-S2-1"));
	}
	
	@Test
	public void test09CorrectCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1WBS,"S2"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("S2");
		assertEquals(1,lcs.size());
		assertEquals("25",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getWbsService().correctCarrierByUser(lc, "33");
		
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("S2");
		assertEquals(1,lcs.size());
		assertEquals("33",lcs.get(0).getCarrierId());
		
		DataContainer dc = socketServer.getReceivedData("P1WBS.BP-S2");
		assertNotNull(dc);
		assertEquals("1", dc.getString("BODY-COUNT"));
		assertEquals("33",dc.getString("BP-S2-1"));
	}
	
	@Test
	public void test10RemoveCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1WBS,"S2"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("S2");
		assertEquals(1,lcs.size());
		assertEquals("33",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getWbsService().removeCarrierByUser(lc, 1);
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("S2");
		assertEquals(0,lcs.size());
		
		DataContainer dc = socketServer.getReceivedData("P1WBS.BP-S2");
		assertNotNull(dc);
		assertEquals("0", dc.getString("BODY-COUNT"));
	}

	@Test
	public void test11CarrierIntoRepair() {
		// if GTS_PRODUCT_TBX is not populated , populate the product
		
		String vin1 = "2HGFC3A57KH750430"; 
		String vin2 = "2HGFC3A57KH750444"; 
		
		GtsProduct product1 = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin1));
		assertNull(product1);
		GtsProduct product2 = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin1));
		assertNull(product2);
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("BODY-COUNT", 1);
		dc.put("BP-CTR-1", 22);
		dc.put("BP-CTR-2", 23);
		dc.setClientID("P1WBS.BP-CTR");
		socketServer.add(dc);
		
		issueIndicatorChange("UBP-CTR-1", 1);
		
		product1 = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin2));
		assertNotNull(product1);
		
		product2 = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin2));
		assertNotNull(product2);
		
	}
	
	@Test
	public void test12CarrierReturn() {
		DataContainer dc = new DefaultDataContainer();
		dc.put("BODY-COUNT", 1);
		dc.put("BP-CR-1", 2);
		dc.setClientID("P1WBS.BP-CR");
		socketServer.add(dc);
		
		issueIndicatorChange("BC-CR", 2);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("CR");
		assertEquals(1,lcs.size());
		assertEquals("002",lcs.get(0).getCarrierId());
		assertNull(lcs.get(0).getProductId());
	}
	
	@Test
	public void test13RefreshPLCIndicators() {
		
		DataContainer dc = new DefaultDataContainer();
		dc.put("BC-S1", 2);
		dc.put("BP-S2", 1);
		dc.setClientID("P1WBS.UNSOLICITED_DATA");
		socketServer.add(dc);
		
		dc = new DefaultDataContainer();
		dc.put("BODY-COUNT", 2);
		dc.put("BP-S1-1", 2);
		dc.put("BP-S1-2", 10);
		dc.setClientID("P1WBS.P1WBS.BP-S1");
		socketServer.add(dc);
		
		dc = new DefaultDataContainer();
		dc.put("BODY-COUNT", 1);
		dc.put("BP-S2-1", 16);
		dc.setClientID("P1WBS.P1WBS.BP-S2");
		socketServer.add(dc);
		
		
		getWbsService().refreshPLCIndictors();
	}
	
	
	private void issueIndicatorChange(String indicatorName, int value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID("P1WBS.UNSOLICITED_DATA");
		dc.put(indicatorName, value);
		getWbsService().execute(dc);
		
	}
	
	private void assertCarrierStatus(String carrierId, GtsCarrierStatus carrierStatus) {
		GtsCarrierId id = new GtsCarrierId();
		id.setTrackingArea(P1WBS);
		id.setCarrierNumber(carrierId);
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByKey(id);
		assertNotNull(carrier);
		assertEquals(carrierStatus,carrier.getStatus());
	}
	
	private void testCarrierFrameAssociation(int carrierId, String vin) {
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		getWbsService().changeAssociation(Integer.toString(carrierId), vin);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(P1WBS, carrierId);
		
		assertNotNull(carrier);
		assertEquals(vin, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1WBS,vin));
		assertNotNull(product);
		assertEquals(frame.getProductionLot(), product.getLotNumber());
		assertEquals(GtsDefectStatus.DIRECT_PASS,product.getDefectStatus());
	
	}
	
	private List<GtsLaneCarrier> findLaneCarriers(String laneId) {
		return getDao(GtsLaneCarrierDao.class).findAll(P1WBS,laneId,P1WBS);
	}
	
	private GtsWbs1TrackingService getWbsService() {
		return getService(GtsWbs1TrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
		socketServer.stopRunning();
	}
}
