package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.common.message.Message;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsClientListDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsLaneDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;
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
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.notification.service.IGtsNotificationService;
import com.honda.galc.service.gts.GtsPbs1TrackingService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.SimulatorSocketServer;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P1PBSTest {
	
	private static String P1PBS = "P1PBS";
	private static String PA_CARRIER= "PA_CARRIER";
	private static String PBS_TERMINAL_NAME = "P1PBS_View";
	
	private static SimulatorSocketServer socketServer = new SimulatorSocketServer();
	
	private static SimulatorSocketServer clientSocketServer;
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/gts/p1pbs.sql");
		
		TestUtils.startWebServer();
		
		socketServer.startServer();
		
		
		startClientSocketServer();
	}
	
	@Test
	public void test01ReceiveCarrierPresent() {
		issueIndicatorChange("CP-qA",1);
		TestUtils.assertIndicator(P1PBS, "CP-qA",1);
		
		issueIndicatorChange("CP-qA",0);
		TestUtils.assertIndicator(P1PBS, "CP-qA",0);
	}

	@Test
	public void test02ReceiveConveyorStatus() {
		testIndicator("CS-PAONCommStatus");
	}
	
	@Test
	public void test03ReceiveControlBox() {
		testIndicator("CB-B34P11");
	}
	
	@Test
	public void test04ReceiveLaneFull() {
		testIndicator("LF-wA");
	}
	
	@Test
	public void test05ReceiveCPLF(){
		testIndicator("CPLF-pQ");
	}
	
	@Test
	public void test06CarrierVinAssociation() {
		testCarrierFrameAssociation(1001,"2HGFC3A50KH750429");
		testCarrierFrameAssociation(1002,"2HGFC3A50KH750429");
		testCarrierFrameAssociation(1002,"2HGFC3A52KH750450");
		testCarrierFrameAssociation(1081,"2HGFC2F82KH507590");
		
	}
	
	@Test
	public void test07ReceiveReader() {
		testReceiveReader("tA",1001);
		
	}
	
	@Test
	public void test08MIP() {
		issueIndicatorChange("MP-tA-tY", 1);
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tA");
		assertEquals(0,lcs.size());
		lcs = findLaneCarriers("tY");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
	}
	
	@Test
	public void test09AddCarrierByUser() {
		getPbsService().addCarrierByUser("tH", 1, "1005");
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tH");
		assertEquals(1,lcs.size());
		assertEquals("1005",lcs.get(0).getCarrierId());
	}
	
	@Test
	public void test10CorrectCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1PBS,"tH"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tH");
		assertEquals(1,lcs.size());
		assertEquals("1005",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getPbsService().correctCarrierByUser(lc, "1008");
		
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("tH");
		assertEquals(1,lcs.size());
		assertEquals("1008",lcs.get(0).getCarrierId());
		
	}
	
	@Test
	public void test11RemoveCarrierByUser() {
		
		GtsLane lane = getDao(GtsLaneDao.class).findByKey(new GtsLaneId(P1PBS,"tH"));
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tH");
		assertEquals(1,lcs.size());
		assertEquals("1008",lcs.get(0).getCarrierId());
		
		GtsLaneCarrier lc = lcs.get(0);
		lc.setLane(lane);
		
		getPbsService().removeCarrierByUser(lc, 1);
		TestUtils.sleep(300);
		
		lcs = findLaneCarriers("tH");
		assertEquals(0,lcs.size());
		
	}
	
	@Test
	public void test12ToggleGateStatus() {
		toggleGate("EXIT-tD"); // open the gate
		toggleGate("ENTRY-tD"); // open the gate
		
		toggleGate("EXIT-tD");  // close
		toggleGate("ENTRY-tD"); // close
	}
	
	@Test
	public void test13ToggleGateEntrypTOpen() {
		toggleGate("ENTRY-pT"); // open
		TestUtils.sleep(100);
		DataContainer dc = socketServer.getReceivedData("P1PBS.GR-ENTRY-pT");
		assertNotNull(dc);
		assertEquals("true", dc.getString("GR-ENTRY-pT"));
		
		issueIndicatorChange("GS-ENTRY-pT", 1);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pT");
		
		assertEquals(true, changedNode.isGateOpen());
		
	}

	@Test
	public void test14ToggleGateEntrypTClose() {
		
		toggleGate("ENTRY-pT"); // close
		
		TestUtils.sleep(100);
		
		DataContainer dc = socketServer.getReceivedData("P1PBS.GR-ENTRY-pT");
		
		assertNotNull(dc);
		assertEquals("false", dc.getString("GR-ENTRY-pT"));
		
		
		issueIndicatorChange("GS-ENTRY-pT", 0);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pT");
		
		assertEquals(false, changedNode.isGateOpen());
		
	}
	
	@Test
	public void test15ToggleGateEntrypTWithExitpROpen() {
		toggleGate("EXIT-pR"); // open
		
		clientSocketServer.getReceivedRequestList().clear();
		
		issueIndicatorChange("GS-ENTRY-pT", 1);
		
		TestUtils.sleep(200);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pT");
		
		assertEquals(false, changedNode.isGateOpen());
		
		NotificationRequest request = clientSocketServer.getRequest(IGtsNotificationService.class, "message");
		
		assertNotNull(request);
		assertTrue(request.getParams().length == 1);
		Message message = (Message)request.getParams()[0];
		
		assertTrue(message.getMessage().startsWith("Cannot open gate 'ENTRY-pT' when gate 'EXIT-pR' is open --"));
		
		// close EXIT-pR and test again
		toggleGate("EXIT-pR"); // close
		
		clientSocketServer.getReceivedRequestList().clear();
		
		issueIndicatorChange("GS-ENTRY-pT", 1);
		
		TestUtils.sleep(100);
		
		changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pT");
		
		assertEquals(true, changedNode.isGateOpen());
		
		request = clientSocketServer.getRequest(IGtsNotificationService.class, "message");
		
		assertNull(request);
		
	}
	
	@Test
	public void test16ToggleGateExitpR_Open() {
		toggleGate("ENTRY-pR"); // open
		
		TestUtils.sleep(100);
		
		DataContainer dc = socketServer.getReceivedData("P1PBS.NGR-ENTRY-pR");
		assertNotNull(dc);
		assertEquals("false", dc.getString("NGR-ENTRY-pR"));
		
		dc = socketServer.getReceivedData("P1PBS.ChangeControlIntoPR");
		assertNotNull(dc);
		assertEquals("true", dc.getString("ChangeControlIntoPR"));
		
		issueIndicatorChange("GS-ENTRY-pR", 0);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pR");
		
		assertEquals(true, changedNode.isGateOpen());
		
		
	}
	
	@Test
	public void test17ToggleGateExitpR_Close() {
		
		toggleGate("ENTRY-pR"); // close
		
		TestUtils.sleep(100);
		DataContainer dc = socketServer.getReceivedData("P1PBS.NGR-ENTRY-pR");
		assertNotNull(dc);
		assertEquals("true", dc.getString("NGR-ENTRY-pR"));
		
		dc = socketServer.getReceivedData("P1PBS.ChangeControlIntoPR");
		assertNotNull(dc);
		assertEquals("true", dc.getString("ChangeControlIntoPR"));
		
		issueIndicatorChange("GS-ENTRY-pR", 1);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pR");
		
		assertEquals(false, changedNode.isGateOpen());
		
	}
	
	@Test
	public void test18ToggleGateExitpRWithEntrypPOpen() {
		// "Entry-pT" is open
		
		clientSocketServer.getReceivedRequestList().clear();
		
		toggleGate("EXIT-pR"); // open
		
		TestUtils.sleep(300);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "EXIT-pR");
		
		assertEquals(false, changedNode.isGateOpen());
		
		NotificationRequest request = clientSocketServer.getRequest(IGtsNotificationService.class, "message");
		
		assertNotNull(request);
		assertTrue(request.getParams().length == 1);
		Message message = (Message)request.getParams()[0];
		
		assertTrue(message.getMessage().startsWith("Cannot open gate 'EXIT-pR' when gate 'ENTRY-pT' is open --"));
		
		clientSocketServer.getReceivedRequestList().clear();
		
		// turn off ENTRY-p
		issueIndicatorChange("GS-ENTRY-pT", 0);
		
		TestUtils.sleep(100);
		
		changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "ENTRY-pT");
		
		assertEquals(false, changedNode.isGateOpen());
		
		//try to turn on EXIT-pR again
		toggleGate("EXIT-pR"); 
		
		changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, "EXIT-pR");
		
		assertEquals(true, changedNode.isGateOpen());
		
		request = clientSocketServer.getRequest(IGtsNotificationService.class, "message");
		
		assertNull(request);
		
	}

	@Test
	public void test19ProcessCarrierLeave() {
		testCarrierFrameAssociation(1011,"2HGFC3A52KH750450");
		getPbsService().addCarrierByUser("pJ", 1, "1011");
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
		
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC3A52KH750450"));
		assertNull(product);
		
		TestUtils.assertIndicator(P1PBS, "MP-pJ-pP",1);
		
		// reset
		issueIndicatorChange("LOAD-VEHICLE", 0);
		
		TestUtils.assertIndicator(P1PBS, "MP-pJ-pP",0);
	
	}
	
	@Test
	public void test20DeadRails() {
		getPbsService().addCarrierByUser("qG", 1, "1025");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qL", 1, "1026");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qL", 2, "1028");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qP", 1, "1030");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qP", 2, "1031");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qR", 1, "1032");
		TestUtils.sleep(100);
		getPbsService().addCarrierByUser("qR", 2, "1033");
		TestUtils.sleep(100);
		
		// move from head of lane qG to head of lane qL
		issueIndicatorChange("MP-qG-qL", 1);
		TestUtils.sleep(300);
		
		issueIndicatorChange("MP-qG-qL", 0);
				
		List<GtsLaneCarrier> lcs = findLaneCarriers("qG");
		assertEquals(0,lcs.size());

		lcs = findLaneCarriers("qL");
		assertEquals(3,lcs.size());
		assertEquals("1025",lcs.get(0).getCarrierId());
		assertEquals("1026",lcs.get(1).getCarrierId());
		assertEquals("1028",lcs.get(2).getCarrierId());
		
		// move from head of lane qL to head of lane qG
		issueIndicatorChange("MP-qL-qG", 1);
		TestUtils.sleep(300);
		issueIndicatorChange("MP-qL-qG", 0);
		
		lcs = findLaneCarriers("qG");
		assertEquals(1,lcs.size());
		assertEquals("1025",lcs.get(0).getCarrierId());
		
		lcs = findLaneCarriers("qL");
		assertEquals(2,lcs.size());
		assertEquals("1026",lcs.get(0).getCarrierId());
		assertEquals("1028",lcs.get(1).getCarrierId());
		
		//move from end of lane qL to Exit qQ
		issueIndicatorChange("MP-qL-qQ", 1);
		TestUtils.sleep(300);
		issueIndicatorChange("MP-qL-qQ", 0);
		
		lcs = findLaneCarriers("qL");
		assertEquals(1,lcs.size());
		assertEquals("1026",lcs.get(0).getCarrierId());
		
		lcs = findLaneCarriers("qQ");
		assertEquals(0,lcs.size());
		
		//move from end of lane qP to head of lane qL
		issueIndicatorChange("MP-qP-qL", 1);
		TestUtils.sleep(300);
		issueIndicatorChange("MP-qP-qL", 0);
		
		lcs = findLaneCarriers("qL");
		assertEquals(2,lcs.size());
		assertEquals("1031",lcs.get(0).getCarrierId());
		assertEquals("1026",lcs.get(1).getCarrierId());
		
		lcs = findLaneCarriers("qP");
		assertEquals(1,lcs.size());
		assertEquals("1030",lcs.get(0).getCarrierId());
		
		//move from end of lane qR to head of lane qL
		issueIndicatorChange("MP-qR-qL", 1);
		TestUtils.sleep(300);
		issueIndicatorChange("MP-qR-qL", 0);
		
		lcs = findLaneCarriers("qL");
		assertEquals(3,lcs.size());
		assertEquals("1033",lcs.get(0).getCarrierId());
		assertEquals("1031",lcs.get(1).getCarrierId());
		assertEquals("1026",lcs.get(2).getCarrierId());
		
		lcs = findLaneCarriers("qR");
		assertEquals(1,lcs.size());
		assertEquals("1032",lcs.get(0).getCarrierId());
		
		

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
	
	@Test 
	public void test22Heartbeat() {
		
		issueIndicatorChange("HB-PLCHeartBeat",1);
		TestUtils.sleep(100);
		
		DataContainer dc = socketServer.getReceivedData("P1PBS.REPLY-PLCHeartBeat");
		assertNotNull(dc);
		assertEquals("true", dc.getString("PLCHeartBeat"));
	
		issueIndicatorChange("HB-PLCHeartBeat",0);
		
		TestUtils.sleep(100);
		
		dc = socketServer.getReceivedData("P1PBS.REPLY-PLCHeartBeat");
		assertNotNull(dc);
		assertEquals("false", dc.getString("PLCHeartBeat"));
	
	}
	
	@Test
	public void test23ReceiveReaderAtLaneTv() {
		//reader at entry lane Tv , no reader discrepancy
		testReceiveReader("tV",1081,0);
	}
	
	@Test
	public void test24ReceiveCarrierVinAssociation() {
		DataContainer dc = null;
		//carrier not in table , vin was not associated, vin was not paint on
		dc = issueCarrierVinAssociation("P101", "2HGFC4A84KH300878");
		assertNotNull(dc);
		assertEquals(dc.get("ALREADY_TL3"), 0);
		assertEquals(dc.get("PRE_PAINT_ON"), 1);
		assertEquals(dc.get("DOLLY_ASSIGNED"), 0);
		assertEquals(dc.get("INVALID_VIN"), 0);
		assertEquals(dc.get("DOLLY_ID"), "P101");
		assertEquals(dc.get("FINAL_RESULT"), 0);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByKey(new GtsCarrierId(PA_CARRIER, "P101"));
		
		assertNotNull(carrier);
		assertEquals("2HGFC4A84KH300878", carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC4A84KH300878"));
		assertNotNull(product);
		
		
	}
	
	@Test
	public void test25ReceiveCarrierVinAssociation() {
		DataContainer dc = null;
		//carrier in the table , vin was associated, vin was not paint on
		dc = issueCarrierVinAssociation("P101", "2HGFC4A84KH300878");
		assertNotNull(dc);
		assertEquals(dc.get("ALREADY_TL3"), 1);
		assertEquals(dc.get("PRE_PAINT_ON"), 1);
		assertEquals(dc.get("DOLLY_ASSIGNED"), 1);
		assertEquals(dc.get("INVALID_VIN"), 0);
		assertEquals(dc.get("DOLLY_ID"), "P101");
		assertEquals(dc.get("FINAL_RESULT"), 0);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByKey(new GtsCarrierId(PA_CARRIER, "P101"));
		
		assertNotNull(carrier);
		assertEquals("2HGFC4A84KH300878", carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC4A84KH300878"));
		assertNotNull(product);
		
	}
	
	@Test
	public void test26ReceiveCarrierVinAssociation() {
		DataContainer dc = null;
		//carrier not in the table , vin not associated, vin  has paint on
		dc = issueCarrierVinAssociation("P102", "2HGFC2F82KH507606");
		assertNotNull(dc);
		assertEquals(dc.get("ALREADY_TL3"), 0);
		assertEquals(dc.get("PRE_PAINT_ON"), 0);
		assertEquals(dc.get("DOLLY_ASSIGNED"), 0);
		assertEquals(dc.get("INVALID_VIN"), 0);
		assertEquals(dc.get("DOLLY_ID"), "P102");
		assertEquals(dc.get("FINAL_RESULT"), 1);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByKey(new GtsCarrierId(PA_CARRIER, "P102"));
		
		assertNotNull(carrier);
		assertEquals("2HGFC2F82KH507606", carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,"2HGFC2F82KH507606"));
		assertNotNull(product);
		
	}
	
	@Test
	public void test27RefreshProductDefectStatus() {
		String VIN = "2HGFC3A59KH750431";
		testCarrierFrameAssociation(1022,VIN);
		List<DefectResult> items = getDao(DefectResultDao.class).findAll();
		List<DefectResult> defectResults = new ArrayList<DefectResult>();
		for(DefectResult result : items) {
			if(result.getProductId().equals(VIN)) defectResults.add(result);
		}
		assertEquals(1,defectResults.size());
		defectResults.get(0).setDefectStatus(DefectStatus.OUTSTANDING);
		getDao(DefectResultDao.class).saveAll(defectResults);
		
		getPbsService().refreshProductDefectStatus(VIN);

		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,VIN));
		assertNotNull(product);
		assertEquals(DefectStatus.OUTSTANDING.getId(), product.getDefectStatus().getId());
	}
	
	@Test
	public void test28RefreshProductDefectStatusByMIP() {
		String VIN = "2HGFC3A59KH750445";
		testCarrierFrameAssociation(1023,VIN);
		getPbsService().addCarrierByUser("wP", 1, "1023");
		TestUtils.sleep(100);
		List<GtsLaneCarrier> lcs = findLaneCarriers("wP");
		assertEquals(1,lcs.size());
		assertEquals("1023",lcs.get(0).getCarrierId());
		
		// change defect status to outstanding
		List<DefectResult> items = getDao(DefectResultDao.class).findAll();
		List<DefectResult> defectResults = new ArrayList<DefectResult>();
		for(DefectResult result : items) {
			if(result.getProductId().equals(VIN)) defectResults.add(result);
		}
		assertEquals(1,defectResults.size());
		defectResults.get(0).setDefectStatus(DefectStatus.OUTSTANDING);
		getDao(DefectResultDao.class).saveAll(defectResults);

		//move from end of lane qP to head of lane qL
		issueIndicatorChange("MP-wP-wD", 1);
		TestUtils.sleep(100);
		issueIndicatorChange("MP-wP-wD", 0);

		lcs = findLaneCarriers("wP");
		assertEquals(0,lcs.size());
		
		lcs = findLaneCarriers("wD");
		assertEquals(1,lcs.size());
		assertEquals(DefectStatus.OUTSTANDING.getId(),lcs.get(0).getProduct().getDefectStatusValue());



	}
	
	@Test
	public void test29UpdateProductInspectionStatus() {
		String VIN ="2HGFC2F81KH507581";
		
		testCarrierFrameAssociation(1024,VIN);

		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,VIN));
		assertNotNull(product);
		assertNotEquals(GtsInspectionStatus.DELAYED.getId(), product.getInspectionStatus().getId());

		getPbsService().updateProductInspectionStatus(VIN, GtsInspectionStatus.DELAYED.getId());
		
		product = getDao(GtsProductDao.class).findByKey(new GtsProductId(PA_CARRIER,VIN));
		assertNotNull(product);
		assertEquals(GtsInspectionStatus.DELAYED.getId(), product.getInspectionStatus().getId());

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
		assertEquals(carrier.getProductId(),lcs.get(0).getProductId());
		assertEquals(discrepancyStatus,lcs.get(0).getDiscrepancyStatus());
		
	}
	
	private static void startClientSocketServer() {
		Terminal terminal = getDao(TerminalDao.class).findByKey(PBS_TERMINAL_NAME);
		assertNotNull(terminal);
		clientSocketServer = new SimulatorSocketServer(terminal.getPort(),true);
		clientSocketServer.startServer();
		
		subscribe(terminal);
	}
	
	private static void subscribe(Terminal terminal) {
    	GtsClientList client = new GtsClientList();
    	GtsClientListId id = new GtsClientListId();
    	id.setTrackingArea(P1PBS);
    	id.setClientIp(terminal.getIpAddress());
    	id.setClientPort(terminal.getPort());
    	client.setId(id);
    	client.setClientName(terminal.getHostName());
    	getDao(GtsClientListDao.class).save(client);
    }
	
	private void testCarrierFrameAssociation(int carrierId, String vin) {
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		getPbsService().changeAssociation(Integer.toString(carrierId), vin);
		
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
		GtsNode node = getDao(GtsNodeDao.class).findByNodeName(P1PBS, gateName);
		assertNotNull(node);
		
		getPbsService().toggleGateStatus(node);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(P1PBS, gateName);
		
		assertEquals(node.isGateOpen(), changedNode.isGateOpen());
	}
	
	private void testIndicator(String indicatorName) {
		issueIndicatorChange(indicatorName,1);
		TestUtils.assertIndicator(P1PBS, indicatorName,1);
		
		issueIndicatorChange(indicatorName,0);
		TestUtils.assertIndicator(P1PBS, indicatorName,0);
	}
	
	private void issueIndicatorChange(String indicatorName, int value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID("P1PBS.UNSOLICITED_DATA");
		dc.put(indicatorName, value);
		getPbsService().execute(dc);
	}
	
	private DataContainer issueCarrierVinAssociation(String carrierId, String vin) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID("P1PBS.CARRIER_VIN_ASSOCIATION");
		dc.put("PRODUCT_ID", vin);
		dc.put("CARRIER_ID", carrierId);
		
		return getPbsService().execute(dc);
	}

	private void issueReader(String readerName, String value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(P1PBS+"."+readerName);
		dc.put(readerName, value);
		dc.put("VIN", "TEST VIN");
		dc.put("DELIMITER", "%");
		getPbsService().execute(dc);
	}
	
	private List<GtsLaneCarrier> findLaneCarriers(String laneId) {
		return getDao(GtsLaneCarrierDao.class).findAll(P1PBS,laneId,PA_CARRIER);
	}
	
	private GtsPbs1TrackingService getPbsService() {
		return getService(GtsPbs1TrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
		socketServer.stopRunning();
	}

}


