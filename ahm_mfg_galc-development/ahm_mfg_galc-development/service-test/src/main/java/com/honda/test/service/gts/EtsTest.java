package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.common.message.Message;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsIndicatorDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsMoveDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.ShippingQuorumDetailDto;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsIndicatorId;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveId;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.gts.GtsEngineBodyTrackingService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EtsTest {
	
	private static String ESTS="ESTS";
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/gts/ests_engine.sql");
		
		startWebServer();
		
		
	}
	
	
	/*
	 * This allows to access the db from a web browser : 
	 * localhost:8082
	 * 
	 * JDBC URL : jdbc:h2:mem:play;MODE=DB2
	 * user name /pass : sa/sa
	 */
	private static void startWebServer() {
		try {
			Server webServer = Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
			Server server = Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092").start();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	@Test
	public void test01AllocateQorum() {
		
		// product with different MTOC K5BBAF900
		testCarrierEngineAssociation(3,"K20C24206004");
		testDL3_Z14(3);
		
		testCarrierEngineAssociation(2,"K20C24205985");
		testDL3_Z14(2);
		
		testCarrierEngineAssociation(10,"K20C24205986");
		testDL3_Z14(10);
		
		//outstanding defect
		testCarrierEngineAssociation(8,"K20C24205987",GtsDefectStatus.OUTSTANDING);
		testDL3_Z14(8);
		
		testCarrierEngineAssociation(5,"K20C24205988");
		testDL3_Z14(5);
		
		//outstanding defect
		testCarrierEngineAssociation(12,"K20C24205989",GtsDefectStatus.OUTSTANDING);
		testDL3_Z14(12);
		
		testCarrierEngineAssociation(15,"K20C24205990");
 		testDL3_Z14(15);
 		
 		testCarrierEngineAssociation(18,"K20C24205991");
 		testDL3_Z14(18);
		
 		testCarrierEngineAssociation(20,"K20C24205992",GtsDefectStatus.OUTSTANDING);
 		testDL3_Z14(20);
 		
 		testCarrierEngineAssociation(22,"K20C24205993");
 		testDL3_Z14(22);
 		
 		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(ShippingQuorumStatus.ALLOCATED,quorum.getStatus());
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("Z14");
		assertEquals(10,lcs.size());
		
		assertEquals(0,lcs.get(0).getProductSeq());
		assertEquals(1,lcs.get(1).getProductSeq());
		assertEquals(2,lcs.get(2).getProductSeq());
		assertEquals(0,lcs.get(3).getProductSeq());
		assertEquals(3,lcs.get(4).getProductSeq());
		assertEquals(0,lcs.get(5).getProductSeq());
		assertEquals(4,lcs.get(6).getProductSeq());
		assertEquals(5,lcs.get(7).getProductSeq());
		assertEquals(0,lcs.get(8).getProductSeq());
		assertEquals(6,lcs.get(9).getProductSeq());
		
	}
	
	@Test
	public void test02MoveDecision() {
		toggleGate("ENTRY-Z15",true); // on
		toggleGate("EXIT-Z15",true); // on
		toggleGate("ENTRY-Z16",true); // on
		toggleGate("EXIT-Z14",true); // on
		
		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(550000, quorum.getId().getQuorumId());
		
		//simulate load qourum button
		getDao(ShippingQuorumDao.class).
			updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), ShippingQuorumStatus.LOADING);
		
		setGtsIndicator("CP-Z14","1");
		
		moveEngine("Z14","Z15",3, "K20C24206004");
		
		loadEngine("Z14",2, "K20C24205985",1,quorum);
		
		loadEngine("Z14",10, "K20C24205986",2,quorum);
		
		moveEngine("Z14","Z15",8, "K20C24205987");
			
		loadEngine("Z14",5, "K20C24205988",3,quorum);
		
		moveEngine("Z14","Z15",12, "K20C24205989");
		
		loadEngine("Z14",15, "K20C24205990",4,quorum);
		loadEngine("Z14",18, "K20C24205991",5,quorum);
		
		moveEngine("Z14","Z15",20, "K20C24205992");
		
		loadEngine("Z14",22, "K20C24205993",6,quorum);
		
		quorum = getDao(ShippingQuorumDao.class).findByKey(quorum.getId());
		assertNotNull(quorum);
		assertEquals(ShippingQuorumStatus.COMPLETE,quorum.getStatus());
		
	}
	
	@Test
	public void test03AllocateRepairQuorum() {
		//assert repair quorum
		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(570000, quorum.getId().getQuorumId());
		assertEquals(ShippingQuorumStatus.ALLOCATED,quorum.getStatus());
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("Z15");
		assertEquals(4,lcs.size());
		
		assertEquals(0,lcs.get(0).getProductSeq());
		assertEquals(1,lcs.get(1).getProductSeq());
		assertEquals(2,lcs.get(2).getProductSeq());
		assertEquals(3,lcs.get(3).getProductSeq());
	}

	@Test
	public void test04RepairQuorumMoveDecision() {
		// all gates are still open now
		
		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(570000, quorum.getId().getQuorumId());
		
		//simulate load qourum button
		getDao(ShippingQuorumDao.class).
			updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), ShippingQuorumStatus.LOADING);
		
		setGtsIndicator("CP-Z15","1");
		
		// circle buffer
		moveEngine("Z15","Z15",3, "K20C24206004");
		
		loadEngine("Z15",8,"K20C24205987",1,quorum);
		loadEngine("Z15",12,"K20C24205989",2,quorum);
		loadEngine("Z15",20,"K20C24205992",3,quorum);
	
	}
	
	@Test
	public void test05AllocateExceptionalQuorum() {
		//assert exceptional quorum
		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(590000, quorum.getId().getQuorumId());
		assertEquals(ShippingQuorumStatus.ALLOCATED,quorum.getStatus());
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("Z15");
		assertEquals(1,lcs.size());
		
		assertEquals(1,lcs.get(0).getProductSeq());

	}
	
	@Test
	public void test06ExceptionalQuorumMoveDecision() {
		// all gates are still open now
		
		ShippingQuorum quorum = getDao(ShippingQuorumDao.class).findCurrentShippingQuorum();
		assertNotNull(quorum);
		assertEquals(590000, quorum.getId().getQuorumId());
		
		//simulate load qourum button
		getDao(ShippingQuorumDao.class).
			updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), ShippingQuorumStatus.LOADING);
		
		setGtsIndicator("CP-Z14","0");
		
		loadEngine("Z15",3,"K20C24206004",1,quorum);
			
	}
	
	
	
	@Test
	public void test07AddRemoveCarrier() {
		GtsEngineBodyTrackingService service = getService(GtsEngineBodyTrackingService.class);
		service.addCarrierByUser("Z15", 1, "06");
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("Z15");
		assertEquals(1,lcs.size());
		assertEquals("06",lcs.get(0).getCarrierId());
		
		service.addCarrierByUser("Z15", 1, "13");
		
		lcs = findLaneCarriers("Z15");
		assertEquals(2,lcs.size());
		
		assertEquals("13",lcs.get(0).getCarrierId());
		assertEquals("06",lcs.get(1).getCarrierId());
		
		service.correctCarrierByUser(lcs.get(0), "21");
		
		lcs = findLaneCarriers("Z15");
		assertEquals(2,lcs.size());
		
		assertEquals("21",lcs.get(0).getCarrierId());
		assertEquals("06",lcs.get(1).getCarrierId());
		
		service.removeCarrierByUser(lcs.get(0), 1);
		
		lcs = findLaneCarriers("Z15");
		assertEquals(1,lcs.size());
		
		assertEquals("06",lcs.get(0).getCarrierId());
		
		
	}
	
	@Test
	public void test08ManualMove() {
		
		// check move request Z14 to Z16 is created
		GtsMove move = assertMove("Z15","Z16",GtsMoveStatus.FINISHED);
		Message message = getGtsService().checkMovePossible(move);
		
		assertEquals("Entry gate of lane Z16 is open",message.getMessage());
		
		toggleGate("ENTRY-Z16",false); // off
		toggleGate("EXIT-Z15",false); // off
		
		message = getGtsService().checkMovePossible(move);
		
		assertNull(message);
		
		getGtsService().createMoveRequest(move);
		
		assertMove("Z15","Z16",GtsMoveStatus.CREATED);
		
		
	}
	
	@Test
	public void test09PhoteEye() {
		
		testCarrierEngineAssociation(37,"K20C24205999");
 		
		//trigger READER DL3
		DataContainer dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"RDR-DL3","37");
		getGtsService().execute(dc);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("DL3");
		assertEquals(1,lcs.size());
		assertEquals("K20C24205999",lcs.get(0).getProductId());
		
		dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"PE-DL3", "0");
		getGtsService().execute(dc);
		
		lcs = findLaneCarriers("DL3");
		assertEquals(1,lcs.size());
		assertTrue(lcs.get(0).isPhotoEyeDiscrepancy());
			
	}
	
	@Test
	public void test10Heartbeat() {
		
		DataContainer dc = setGtsIndicator("HB-ATW_OVERHEAD_PLC","1");
		
		assertNotNull(dc);
		assertEquals("ESTS.REPLY-ATW_OVERHEAD_PLC", dc.getClientID());
		assertEquals("true", dc.getString("AE1_600_DRESS_REPAIR_PLC.DRESS_LINE_CONVEYOR.TRACKING.REPLY-ATW_OVERHEAD_PLC"));
		
		dc = setGtsIndicator("HB-ATW_OVERHEAD_PLC","0");
		
		assertNotNull(dc);
		assertEquals("ESTS.REPLY-ATW_OVERHEAD_PLC", dc.getClientID());
		assertEquals("false", dc.getString("AE1_600_DRESS_REPAIR_PLC.DRESS_LINE_CONVEYOR.TRACKING.REPLY-ATW_OVERHEAD_PLC"));
		
	}
	
	private void moveEngine(String sourceLane, String destLane,int carrierId,String ein) {
		
		String mip = "MP-"+sourceLane+"-"+destLane;
		
		// check move request is created
		assertMove(sourceLane,destLane,GtsMoveStatus.CREATED);
		
		// trigger MIP
		
		setGtsIndicator(mip,"1");
				
		// check move request is Started
		assertMove(sourceLane,destLane,GtsMoveStatus.STARTED);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers(destLane);
		assertTrue(lcs.size() > 0);
		assertEquals(ein,lcs.get(lcs.size() -1).getProductId());
		
		// reset MIP = 0
		setGtsIndicator(mip,"0");
		// check move request is FINISHED
		// next Move Request is created for next product 
		assertMove(sourceLane,destLane,GtsMoveStatus.CREATED,GtsMoveStatus.FINISHED);
			
	}
	
	private void loadEngine(String sourceLane,int carrierId,String ein, int seq, ShippingQuorum quorum) {
		String destLane = "Z16";
		
		moveEngine(sourceLane,destLane,carrierId,ein);
		
		//trigger READER DL3
		DataContainer dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"RDR-Z16",Integer.toString(carrierId));
		GtsEngineBodyTrackingService service = getService(GtsEngineBodyTrackingService.class);
		service.execute(dc);
		
		//trigger load engine
		
		dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"LOAD-ENGINE","1");
		service.execute(dc);
		
		assertEngineLoaded(carrierId, ein,seq,quorum);
		
		//trigger load engine
		
		dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"LOAD-ENGINE","0");
		service.execute(dc);
				
		// trigger MP-Z16-EXIT = 1
		setGtsIndicator("MP-Z16-EXIT","1");
		List<GtsLaneCarrier> lcs = findLaneCarriers("Z16");
		assertEquals(0,lcs.size());
		
		// trigger MP-Z16-EXIT = 0
		setGtsIndicator("MP-Z16-EXIT","0");
								
	}
	
	private GtsMove assertMove(String srcLane, String destLane, GtsMoveStatus... mvStatusList) {
		GtsMoveId id = new GtsMoveId(ESTS,srcLane,destLane);
		GtsMove move = getDao(GtsMoveDao.class).findByKey(id);
		assertNotNull(move);
		assertTrue(isMoveStatus(move.getMoveStatus(),mvStatusList));
		return move;
	}
	
	private boolean isMoveStatus(GtsMoveStatus mvStatus, GtsMoveStatus[] mvStatusList) {
		for(GtsMoveStatus status : mvStatusList) {
			if(mvStatus.equals(status)) return true;
		}
		return false;
	}
	
	private void assertEngineLoaded(int carrierId, String ein,int seq,ShippingQuorum quorum) {
		// carrier product deassociation
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(ESTS, carrierId);
		assertNotNull(carrier);
		assertNull(carrier.getProductId());
		
		// engine is removed from GTSProduct table
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(ESTS,ein));
		assertNull(product);
		
		List<ShippingQuorumDetailDto> details = getDao(ShippingQuorumDetailDao.class).findAllDetailsByEngineNumber(ein);
		assertEquals(1,details.size());
		assertEquals(seq,details.get(0).getQuorumSeq());
		
		// there is no trailer info and vanning schedule for repair/exceptional quorum
		if(quorum.isRepairQuorum() || quorum.isExceptionalQuorum()) return;
		
	    ShippingTrailerInfo info = getDao(ShippingTrailerInfoDao.class).findByKey(quorum.getTrailerId());
	    assertNotNull(info);
	    assertEquals(seq,info.getActQty());
		
	    List<ShippingVanningSchedule> schedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(quorum.getTrailerId(), details.get(0).getKdLot());
	    assertEquals(1,schedules.size());
	    assertEquals(seq,schedules.get(0).getActQty())	;	
	    
	}
	
	private void testCarrierEngineAssociation(int carrierId, String ein) {
		testCarrierEngineAssociation(carrierId,ein,GtsDefectStatus.DIRECT_PASS);
	}
	
	private void testCarrierEngineAssociation(int carrierId, String ein,GtsDefectStatus defectStatus) {
		Engine engine = getDao(EngineDao.class).findByKey(ein);
		assertNotNull(engine);
		
		getGtsService().changeAssociation(Integer.toString(carrierId), ein);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(ESTS, carrierId);
		
		assertNotNull(carrier);
		assertEquals(ein, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(ESTS,ein));
		assertNotNull(product);
		assertEquals(engine.getProductionLot(), product.getLotNumber());
		assertEquals(defectStatus,product.getDefectStatus());
	
	}
	
	/**
	 * trigger reader at DL3 and move the carrier to Z14
	 * @param carrierId
	 */
	private void testDL3_Z14(int carrierId) {
		
		DataContainer dc; 
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(ESTS, carrierId);
		assertNotNull(carrier);
		
		//trigger READER DL3
		dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,"RDR-DL3",Integer.toString(carrierId));
		getGtsService().execute(dc);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("DL3");
		assertEquals(1,lcs.size());
		assertEquals(carrier.getProductId(),lcs.get(0).getProductId());
		//entry lane , does not create reader discrepancy
		assertEquals(0,lcs.get(0).getDiscrepancyStatus());
		
		// trigger MP-DL3-Z14 = 1
		
		setGtsIndicator("MP-DL3-Z14","1");
		
		lcs = findLaneCarriers("DL3");
		assertEquals(0,lcs.size());
	
		
		lcs = findLaneCarriers( "Z14");
		assertTrue(lcs.size() >=1);
		assertEquals(carrier.getProductId(),lcs.get(lcs.size() -1).getProductId());
		
		
		// Reset MP-DL3-Z14 = 0
		setGtsIndicator("MP-DL3-Z14","0");
		
			
	}
	
	private DataContainer setGtsIndicator(String indicatorName, String value ) {
		
		DataContainer dc = TestUtils.prepareGtsIndicatorDataContainer(ESTS,indicatorName,value);
		DataContainer returnDC = getGtsService().execute(dc);
		
		GtsIndicatorId id = new GtsIndicatorId();
		id.setTrackingArea(ESTS);
		id.setIndicatorName(indicatorName);
		GtsIndicator indicator= getDao(GtsIndicatorDao.class).findByKey(id);
		
		assertNotNull(indicator);
		assertEquals(Integer.parseInt(value),indicator.getStatus());
		
		return returnDC;	
	}
	
	private List<GtsLaneCarrier> findLaneCarriers(String laneId) {
		return getDao(GtsLaneCarrierDao.class).findAll(ESTS,laneId,ESTS);
	}
	
	private void toggleGate(String gateName,boolean isOpen) {
		GtsNode node = getDao(GtsNodeDao.class).findByNodeName(ESTS, gateName);
		assertNotNull(node);
		
		getGtsService().toggleGateStatus(node);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(ESTS, gateName);
		
		assertEquals(isOpen, changedNode.isGateOpen());
	}
	
	
	
	private GtsEngineBodyTrackingService getGtsService() {
		return getService(GtsEngineBodyTrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
	
	
		
}
