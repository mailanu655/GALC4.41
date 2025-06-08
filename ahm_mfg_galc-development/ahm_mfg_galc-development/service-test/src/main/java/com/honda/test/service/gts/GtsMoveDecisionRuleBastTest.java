package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;

import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.gts.GtsClientListDao;
import com.honda.galc.dao.gts.GtsLaneCarrierDao;
import com.honda.galc.dao.gts.GtsMoveDao;
import com.honda.galc.dao.gts.GtsNodeDao;
import com.honda.galc.dao.gts.GtsProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsClientList;
import com.honda.galc.entity.gts.GtsClientListId;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveId;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsProductId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.gts.IBodyTrackingService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.SimulatorSocketServer;
import com.honda.test.util.TestUtils;

public abstract class GtsMoveDecisionRuleBastTest {
	public static String TRACKING_AREA;
	public static String CARRIER_AREA;
	public static String CLIENT_TERMINAL_NAME;
	public static String UNSOLICITED_DATA;
	
	protected static SimulatorSocketServer socketServer = new SimulatorSocketServer();
	
	protected static SimulatorSocketServer clientSocketServer;
	
	
	protected static void  loadConfig(String filePath) {
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile(filePath);
		
		//allow log4j to load the log4j.property dynamically based on the machine host name or preferred suffix
		ServerSideLoggerConfig.configLog4j();

		
		TestUtils.startWebServer();
		
		socketServer.startServer();
		
		
		startClientSocketServer();
	}
	
	private static void startClientSocketServer() {
		Terminal terminal = getDao(TerminalDao.class).findByKey(CLIENT_TERMINAL_NAME);
		assertNotNull(terminal);
		clientSocketServer = new SimulatorSocketServer(terminal.getPort(),true);
		clientSocketServer.startServer();
		
		subscribe(terminal);
	}
	
	private static void subscribe(Terminal terminal) {
    	GtsClientList client = new GtsClientList();
    	GtsClientListId id = new GtsClientListId();
    	id.setTrackingArea(TRACKING_AREA);
    	id.setClientIp(terminal.getIpAddress());
    	id.setClientPort(terminal.getPort());
    	client.setId(id);
    	client.setClientName(terminal.getHostName());
    	getDao(GtsClientListDao.class).save(client);
    }
	
	protected void testCarrierFrameAssociation(int carrierId, String vin) {
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		getGtsService().changeAssociation(Integer.toString(carrierId), vin);
		
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(CARRIER_AREA, carrierId);
		
		assertNotNull(carrier);
		assertEquals(vin, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(CARRIER_AREA,vin));
		assertNotNull(product);
		assertEquals(frame.getProductionLot(), product.getLotNumber());
		assertEquals(GtsDefectStatus.DIRECT_PASS,product.getDefectStatus());
		assertEquals(frame.getShortVin(),product.getShortProdId());
	}
	
	protected void testCarrierFrameAssociation(int carrierId, String vin, GtsProductStatus holdStatus) {
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		assertNotNull(frame);
		
		getGtsService().changeAssociation(Integer.toString(carrierId), vin);
		
		TestUtils.sleep(100);
		GtsCarrier carrier = getDao(GtsCarrierDao.class).findByCarrierId(CARRIER_AREA, carrierId);
		
		assertNotNull(carrier);
		assertEquals(vin, carrier.getProductId());
		GtsProduct product = getDao(GtsProductDao.class).findByKey(new GtsProductId(CARRIER_AREA,vin));
		assertNotNull(product);
		assertEquals(frame.getProductionLot(), product.getLotNumber());
		assertEquals(holdStatus,product.getProductStatus());
		assertEquals(frame.getShortVin(),product.getShortProdId());
	}
	
	protected void issueIndicatorChange(String indicatorName, int value) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(UNSOLICITED_DATA);
		dc.put(indicatorName, value);
		getGtsService().execute(dc);
		
	}
	
	protected void toggleGate(String gateName) {
		GtsNode node = getDao(GtsNodeDao.class).findByNodeName(TRACKING_AREA, gateName);
		assertNotNull(node);
		
		getGtsService().toggleGateStatus(node);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(TRACKING_AREA, gateName);
		
		assertEquals(node.isGateOpen(), changedNode.isGateOpen());
	}
	
	protected void toggleGate(boolean isOn,String... gateNames) {
		for(String gateName : gateNames) {
			toggleGate(gateName,isOn);
		}
	}
	
	protected void toggleGate(String gateName,boolean isOn) {
		GtsNode node = getDao(GtsNodeDao.class).findByNodeName(TRACKING_AREA, gateName);
		assertNotNull(node);
		
		if(node.isGateOpen() == isOn) return;
		
		getGtsService().toggleGateStatus(node);
		
		GtsNode changedNode = getDao(GtsNodeDao.class).findByNodeName(TRACKING_AREA, gateName);
		
		assertEquals(node.isGateOpen(), changedNode.isGateOpen());
	}
	
	
	protected GtsMove assertMove(String srcLane, String destLane, GtsMoveStatus... mvStatusList) {
		GtsMoveId id = new GtsMoveId(TRACKING_AREA,srcLane,destLane);
		GtsMove move = getDao(GtsMoveDao.class).findByKey(id);
		assertNotNull(move);
		assertTrue(isMoveStatus(move.getMoveStatus(),mvStatusList));
		return move;
	}
	
	protected void resetMoveRequest(String srcLane, String destLane) {
		GtsMoveId id = new GtsMoveId(TRACKING_AREA,srcLane,destLane);
		GtsMove move = getDao(GtsMoveDao.class).findByKey(id);
		assertNotNull(move);
		move.setMoveStatus(GtsMoveStatus.FINISHED);
		getDao(GtsMoveDao.class).update(move);
		
	}
	
	protected void removeLaneCarrier(String laneName, String carrierId,int pos) {
		GtsLaneCarrier lc = new GtsLaneCarrier(TRACKING_AREA,laneName);
		lc.setLaneCarrier(carrierId);
		getGtsService().removeCarrierByUser(lc, pos);
	}
	
	protected void addLaneCarrier(String laneName, String carrierId,int pos) {
		getGtsService().addCarrierByUser(laneName, pos, carrierId);
		TestUtils.sleep(10);
	}
	
	private boolean isMoveStatus(GtsMoveStatus mvStatus, GtsMoveStatus[] mvStatusList) {
		for(GtsMoveStatus status : mvStatusList) {
			if(mvStatus.equals(status)) return true;
		}
		return false;
	}
	
	protected List<GtsLaneCarrier> findLaneCarriers(String laneId) {
		return getDao(GtsLaneCarrierDao.class).findAll(TRACKING_AREA,laneId,CARRIER_AREA);
	}

	
	public abstract IBodyTrackingService getGtsService();
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
		socketServer.stopRunning();
	}

}
