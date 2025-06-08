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

import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsClientListDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsCarrierId;
import com.honda.galc.entity.gts.GtsClientList;
import com.honda.galc.entity.gts.GtsClientListId;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneId;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.service.gts.GtsTbs1TrackingService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.SimulatorSocketServer;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P1TBSTest {
	
	private static String P1TBS = "P1TBS";
	private static String PA_CARRIER= "PA_CARRIER";
	private static String TBS_TERMINAL_NAME = "P1TBS_View";
	
	private static SimulatorSocketServer socketServer = new SimulatorSocketServer();
	
	private static SimulatorSocketServer clientSocketServer;
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/gts/P1TBS.sql");
		
		TestUtils.startWebServer();
		
		socketServer.startServer();
		
		
		startClientSocketServer();
	}
	
	@Test
	public void test01ReceiveCarrierPresent() {
		issueIndicatorChange("CP-tsJ",1);
		TestUtils.assertIndicator(P1TBS, "CP-tsJ",1);
		
		issueIndicatorChange("CP-tsJ",0);
		TestUtils.assertIndicator(P1TBS, "CP-tsJ",0);
	}

	@Test
	public void test02ReceiveConveyorStatus() {
		testIndicator("CS-P13-SD1");
	}
	
	@Test
	public void test03ReceiveControlBox() {
		testIndicator("CB-P13-B5P13");
	}
	
	@Test
	public void test04ReceiveLaneFull() {
		testIndicator("LF-tsQ");
	}
	
	@Test
	public void test07ReceiveReader() {
		testReceiveReader("toC",1001);
		
	}
	
	@Test
	public void test08MIP() {
		issueIndicatorChange("MP-toC-tsA", 1);
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("toC");
		assertEquals(0,lcs.size());
		lcs = findLaneCarriers("tsA");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
	}
	
	@Test
	public void test09AddCarrierByUser() {
		getTbsService().addCarrierByUser("toB", 1, "1005");
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("toB");
		assertEquals(1,lcs.size());
		assertEquals("1005",lcs.get(0).getCarrierId());
	}
	
	@Test
	public void test10CorrectCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1TBS,"toB"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("toB");
		assertEquals(1,lcs.size());
		assertEquals("1005",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getTbsService().correctCarrierByUser(lc, "1008");
		
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("toB");
		assertEquals(1,lcs.size());
		assertEquals("1008",lcs.get(0).getCarrierId());
		
	}
	
	@Test
	public void test11RemoveCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1TBS,"toB"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("toB");
		assertEquals(1,lcs.size());
		assertEquals("1008",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getTbsService().removeCarrierByUser(lc, 1);
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("toB");
		assertEquals(0,lcs.size());
		
	}
	
	@Test
	public void test12ToggleGateStatus() {
		toggleGate("EXIT-tsL"); // open the gate
		toggleGate("ENTRY-tsH"); // open the gate
		
		toggleGate("EXIT-tsL");  // close
		toggleGate("ENTRY-tsH"); // close
	}
	
	@Test
	public void test13ReceiveReader_Entrace() {
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByKey(new GtsCarrierId(PA_CARRIER,"1009"));
		assertNotNull(carrier);
		assertEquals("2HGFC4A80KH300859", carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1TBS,"2HGFC4A80KH300859"));
		assertNull(product);
		
		testReceiveReader("ssZ",1009,0);

		product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC4A80KH300859"));
		assertNotNull(product);
		
	}
	
	
	public void test19ProcessCarrierLeave() {
		testCarrierFrameAssociation(1011,"2HGFC3A52KH750450");
		getTbsService().addCarrierByUser("pJ", 1, "1011");
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("pJ");
		assertEquals(1,lcs.size());
		assertEquals("1011",lcs.get(0).getCarrierId());
		
		// move vehicle to AFON
		issueIndicatorChange("LOAD-VEHICLE", 1);
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("pJ");
		assertEquals(0,lcs.size());
		
		lcs = findLaneCarriers("pX");
		assertEquals(0,lcs.size());
		
		lcs = findLaneCarriers("pP");
		assertEquals(1,lcs.size());
		assertEquals("1011",lcs.get(0).getCarrierId());
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(PA_CARRIER, 1011);
		assertNotNull(carrier);
		assertNull(carrier.getProductId());
		
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(P1TBS,"2HGFC3A52KH750450"));
		assertNull(product);
		
		TestUtils.assertIndicator(P1TBS, "MP-pJ-pP",1);
		
		// reset
		issueIndicatorChange("LOAD-VEHICLE", 0);
		
		TestUtils.assertIndicator(P1TBS, "MP-pJ-pP",0);
	
	}
		
	@Test
	public void test21FrameHoldRelease() {
		
		testCarrierFrameAssociation(1335,"2HGFC2F83KH507601");
	
		HoldResult holdResult= new HoldResult("2HGFC2F83KH507601", HoldResultType.HOLD_NOW.getId());
		getDao(HoldResultDao.class).save(holdResult);
		
		TestUtils.sleep(100);
		
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC2F83KH507601"));
		assertNotNull(product);
		assertEquals(GtsProductStatus.HOLD,product.getProductStatus());
		
		holdResult.setReleaseFlag((short)1);
		getDao(HoldResultDao.class).save(holdResult);
		
		TestUtils.sleep(100);
		
		product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC2F83KH507601"));
		assertNotNull(product);
		assertEquals(GtsProductStatus.RELEASE,product.getProductStatus());
		
	}
	
	
		
	private void testReceiveReader(String laneId,int carrierId) {
		testReceiveReader(laneId,carrierId,GtsLaneCarrier.READER_DISCREPANCY);
	}	
	
	private void testReceiveReader(String laneId,int carrierId,int discrepancyStatus) {
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(PA_CARRIER, carrierId);
		assertNotNull(carrier);
		
		issueReader("RDR-"+laneId,Integer.toString(carrierId));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers(laneId);
		assertEquals(1,lcs.size());
		assertEquals(discrepancyStatus,lcs.get(0).getDiscrepancyStatus());
		
	}
	
	private static void startClientSocketServer() {
		Terminal terminal = getDao(TerminalDao.class).findByKey(TBS_TERMINAL_NAME);
		assertNotNull(terminal);
		clientSocketServer = new SimulatorSocketServer(terminal.getPort(),true);
		clientSocketServer.startServer();
		
		subscribe(terminal);
	}
	
	private static void subscribe(Terminal terminal) {
    	GtsClientList client = new GtsClientList();
    	GtsClientListId id = new GtsClientListId();
    	id.setTrackingArea(P1TBS);
    	id.setClientIp(terminal.getIpAddress());
    	id.setClientPort(terminal.getPort());
    	client.setId(id);
    	client.setClientName(terminal.getHostName());
    	getDao(GtsClientListDao.class).save(client);
    }
	
	private void testCarrierFrameAssociation(int carrierId, String vin) {
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		getTbsService().changeAssociation(Integer.toString(carrierId), vin);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(PA_CARRIER, carrierId);
		
		assertNotNull(carrier);
		assertEquals(vin, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,vin));
		assertNotNull(product);
		assertEquals(frame.getProductionLot(), product.getLotNumber());
		assertEquals(GtsDefectStatus.DIRECT_PASS,product.getDefectStatus());
		assertEquals(frame.getShortVin(),product.getShortProdId());
	}
	
	private void toggleGate(String gateName) {
		GtsNode node = getDao(GtsNodeDao.class).findByNodeName(P1TBS, gateName);
		assertNotNull(node);
		
		getTbsService().toggleGateStatus(node);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1TBS, gateName);
		
		assertEquals(node.isGateOpen(), changedNode.isGateOpen());
	}
	
	private void testIndicator(String indicatorName) {
		issueIndicatorChange(indicatorName,1);
		TestUtils.assertIndicator(P1TBS, indicatorName,1);
		
		issueIndicatorChange(indicatorName,0);
		TestUtils.assertIndicator(P1TBS, indicatorName,0);
	}
	
	private void issueIndicatorChange(String indicatorName, int value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID("P1TBS.UNSOLICITED_DATA");
		dc.put(indicatorName, value);
		getTbsService().execute(dc);
	}
	
	private void issueReader(String readerName, String value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(P1TBS+"."+readerName);
		dc.put(readerName, value);
		dc.put("VIN", "TEST VIN");
		dc.put("DELIMITER", "%");
		getTbsService().execute(dc);
	}
	
	private List<GtsLaneCarrier> findLaneCarriers(String laneId) {
		return getDao(GtsLaneCarrierDao.class).findAll(P1TBS,laneId,PA_CARRIER);
	}
	
	private GtsTbs1TrackingService getTbsService() {
		return getService(GtsTbs1TrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
		socketServer.stopRunning();
	}

}


