package com.honda.test.engine;

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

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.Arguments;
import com.honda.galc.client.NonCachedApplicationContext;
import com.honda.galc.client.engine.shipping.EngineShippingModel;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.gts.GtsEngineBodyTrackingService;
import com.honda.galc.service.printing.PrintQueuePrinterDevice;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AEShippingTest {
	
	private static EngineShippingModel model = new EngineShippingModel(); 
	private static String PPID ="AE0EN16601";
	
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/engine/ae_shipping.sql");
		
		DBUtils.loadClientApplicationContext(19080);
		TestUtils.startWebServer();
		
		initModel();
			
	}
	
	@Test
	public void test01AssignTrailer() {
		
		List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		
		model.assignTrailer("1128", schedules, null);
		
		model.reloadShippingTrailerInfoList();
		assertEquals(1, model.getAllShippingTrailers().size());
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		assertEquals(60,info.getSchQty());
		assertEquals(0,info.getActQty());
		assertEquals(ShippingTrailerInfoStatus.WAITING, info.getStatus());
		
		model.reloadActiveQuorums();
		assertEquals(10, model.findAllScheduledQuorums().size());
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertEquals(ShippingQuorumStatus.ALLOCATING,quorum.getStatus());
		assertEquals(6,quorum.getQuorumSize());
		assertEquals(1,quorum.getTrailerId());
		assertEquals("1128",quorum.getTrailerNumber());
		assertEquals(1,quorum.getTrailerRow());
		
		List<ShippingQuorumDetail> details = quorum.getShippingQuorumDetails();
		assertEquals(6,details.size());
		ShippingQuorumDetail detail = details.get(0);
		assertEquals("HCM 01201810034502",detail.getKdLot());
		assertEquals("K5BBLF900",detail.getYmto());
		
		quorum = model.findAllScheduledQuorums().get(3);
		assertEquals(ShippingQuorumStatus.WAITING,quorum.getStatus());
		assertEquals(6,quorum.getQuorumSize());
		assertEquals(1,quorum.getTrailerId());
		assertEquals("1128",quorum.getTrailerNumber());
		assertEquals(4,quorum.getTrailerRow());
		
		details = quorum.getShippingQuorumDetails();
		assertEquals(6,details.size());
		detail = details.get(0);
		assertEquals("HCM 01201810034501",detail.getKdLot());
		assertEquals("K5BBAF900",detail.getYmto());
		
		assertEquals(1, model.findAllDelayedQuorums().size());
		
		schedules = model.findAllActiveVanningSchedules();
		assertEquals(1,(int)schedules.get(0).getTrailerId());
		assertEquals("1128",schedules.get(0).getTrailerNumber());
		
	}
	
	@Test
	public void test02DeassignTrailer() {
		
		model.reloadShippingTrailerInfoList();
		assertEquals(1, model.getAllShippingTrailers().size());
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		assertEquals(60,info.getSchQty());
		assertEquals(0,info.getActQty());
		assertEquals(ShippingTrailerInfoStatus.WAITING, info.getStatus());
		
		model.deassignTrailer(info.getTrailerId());
		
		model.reloadShippingTrailerInfoList();
		assertEquals(0, model.getAllShippingTrailers().size());
		
		List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		assertNull(schedules.get(0).getTrailerId());
		assertNull(schedules.get(1).getTrailerId());
		assertNull(schedules.get(2).getTrailerId());
		
		model.reloadActiveQuorums();
		assertEquals(0, model.findAllScheduledQuorums().size());
		
	}
	
	@Test
	public void test03AssignTrailerAgain() {
		
		List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		
		model.reloadActiveQuorums();
		
		model.assignTrailer("1128", schedules, null);
		
		model.reloadShippingTrailerInfoList();
		assertEquals(1, model.getAllShippingTrailers().size());
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		assertEquals(60,info.getSchQty());
		assertEquals(0,info.getActQty());
		assertEquals(ShippingTrailerInfoStatus.WAITING, info.getStatus());
		
		model.reloadActiveQuorums();
		assertEquals(10, model.findAllScheduledQuorums().size());
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertEquals(ShippingQuorumStatus.ALLOCATING,quorum.getStatus());
		assertEquals(6,quorum.getQuorumSize());
		assertEquals(1,quorum.getTrailerId());
		assertEquals("1128",quorum.getTrailerNumber());
		assertEquals(1,quorum.getTrailerRow());
		
		List<ShippingQuorumDetail> details = quorum.getShippingQuorumDetails();
		assertEquals(6,details.size());
		ShippingQuorumDetail detail = details.get(0);
		assertEquals("HCM 01201810034502",detail.getKdLot());
		assertEquals("K5BBLF900",detail.getYmto());
		
		quorum = model.findAllScheduledQuorums().get(3);
		assertEquals(ShippingQuorumStatus.WAITING,quorum.getStatus());
		assertEquals(6,quorum.getQuorumSize());
		assertEquals(1,quorum.getTrailerId());
		assertEquals("1128",quorum.getTrailerNumber());
		assertEquals(4,quorum.getTrailerRow());
		
		details = quorum.getShippingQuorumDetails();
		assertEquals(6,details.size());
		detail = details.get(0);
		assertEquals("HCM 01201810034501",detail.getKdLot());
		assertEquals("K5BBAF900",detail.getYmto());
		
		assertEquals(1, model.findAllDelayedQuorums().size());
		
		schedules = model.findAllActiveVanningSchedules();
		assertEquals(1,(int)schedules.get(0).getTrailerId());
		assertEquals("1128",schedules.get(0).getTrailerNumber());
		
	}
	
	
	
	@Test
	public void test04PrintVanningSheet() {
		
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		
		List<ShippingVanningSchedule> schedules = 
			model.findVanningSchedules(model.findAllActiveVanningSchedules(), info.getTrailerId());

		model.printVanningScheduleSheet(info,schedules);
		
		TestUtils.sleep(300);
		DataContainer dc = PrintQueuePrinterDevice.currentDC;
		assertNotNull(dc);
		
		assertEquals("LOT1-2|HCM 01201810034502|KLF9|18|LOT1-1|HCM 01201810034501|KAF9|12|LOT2|HCM 01201810034401|KAF9|30", dc.getString("LOTS_SUMMARY"));
		assertEquals("AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|AF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9|LF9",dc.getString("ENGINE_LOTS"));
		assertEquals("1128",dc.getString("TRAILER"));
		
	}
	
	@Test
	public void test05AddRepairQuorum() {
		ShippingQuorum quorum = new ShippingQuorum();
		quorum.setPalletType("AP4");
		quorum.setQuorumSize(3);
		
		ShippingQuorum beforeQuorum = model.findAllScheduledQuorums().get(0);
		model.createRepairQuorum(beforeQuorum, 3, "AP4");
		
		model.reloadActiveQuorums();
		ShippingQuorum repairQuorum = model.findAllScheduledQuorums().get(1);
		assertNotNull(repairQuorum);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_REPAIR, repairQuorum.getTrailerId());
		assertEquals(3,repairQuorum.getQuorumSize());
		assertEquals(3,repairQuorum.getShippingQuorumDetails().size());
	}
	
	@Test
	public void test06AddExceptionalQuorum() {
		List<ShippingQuorumDetail> details = new ArrayList<>();
		details.add(new ShippingQuorumDetail(null,0,1));
		details.add(new ShippingQuorumDetail(null,0,2));
		details.add(new ShippingQuorumDetail(null,0,3));
		details.get(0).setYmto("K5BBLF900");
		details.get(1).setYmto("K5BBAF900");
		details.get(2).setYmto("K5BBLF900");
		ShippingQuorum quorum = new ShippingQuorum();
		quorum.setPalletType("AP4");
		quorum.setQuorumSize(3);
		quorum.setShippingQuorumDetails(details);
		
		ShippingQuorum repairQuorum = model.findAllScheduledQuorums().get(1);
		model.createExceptionalQuorum(repairQuorum, quorum);
		model.reloadActiveQuorums();
		
		ShippingQuorum exceptQuorum = model.findAllScheduledQuorums().get(2);
		assertNotNull(exceptQuorum);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_EXCEPT, exceptQuorum.getTrailerId());
		assertEquals(3,exceptQuorum.getQuorumSize());
		assertEquals(3,exceptQuorum.getShippingQuorumDetails().size());
		assertEquals("K5BBLF900",exceptQuorum.getShippingQuorumDetails().get(0).getYmto());
		assertEquals("K5BBAF900",exceptQuorum.getShippingQuorumDetails().get(1).getYmto());
		assertEquals("K5BBLF900",exceptQuorum.getShippingQuorumDetails().get(2).getYmto());
		
	}
	
	
	@Test
	public void test07LoadEngineQuorum1() {
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertNotNull(quorum);
		quorum.setStatus(ShippingQuorumStatus.ALLOCATED);
		getDao(ShippingQuorumDao.class).save(quorum);
		
		model.updateQuorumStatus(quorum, ShippingQuorumStatus.LOADING);
		
		loadEngine("K20C24205994", "K5BBLF900",1,0);
		loadEngine("K20C24205995", "K5BBLF900",2,0);
		loadEngine("K20C24205996", "K5BBLF900",3,0);
		loadEngine("K20C24205997", "K5BBLF900",4,0);
		loadEngine("K20C24205998", "K5BBLF900",5,0);
		loadEngine("K20C24205999", "K5BBLF900",6,0);
			
	}
	
	@Test
	public void test08LoadRepairQuorum() {
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertNotNull(quorum);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_REPAIR, quorum.getTrailerId());
		
		quorum.setStatus(ShippingQuorumStatus.ALLOCATED);
		getDao(ShippingQuorumDao.class).save(quorum);
		
		model.updateQuorumStatus(quorum, ShippingQuorumStatus.LOADING);
		
		this.loadRepairEngine("K20C24205985", "K5BBLF900", 1);
		this.loadRepairEngine("K20C24205986", "K5BBLF900", 2);
		this.loadRepairEngine("K20C24205987", "K5BBLF900", 3);
		
	}
	
	@Test
	public void test09LoadExceptionalQuorum() {
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertNotNull(quorum);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_EXCEPT, quorum.getTrailerId());
		
		quorum.setStatus(ShippingQuorumStatus.ALLOCATED);
		getDao(ShippingQuorumDao.class).save(quorum);
		
		model.updateQuorumStatus(quorum, ShippingQuorumStatus.LOADING);
		
		this.loadExceptionalEngine("K20C24205988", "K5BBLF900", 1,false);
		this.loadExceptionalEngine("K20C24205989", "K5BBLF900", 2,true);
		this.loadExceptionalEngine("K20C24205990", "K5BBLF900", 3,false);
		
	}
	
	@Test
	public void test10LoadEngineQuorum2() {

		loadEngine("K20C24206075", "K5BBLF900",7,0);
		loadEngine("K20C24206076", "K5BBLF900",8,0);
		loadEngine("K20C24206077", "K5BBLF900",9,0);
		loadEngine("K20C24206078", "K5BBLF900",10,0);
		loadEngine("K20C24206079", "K5BBLF900",11,0);
		loadEngine("K20C24206080", "K5BBLF900",12,0);
	
	}
	
	@Test
	public void test11LoadEngineQuorum3() {
		
		loadEngine("K20C24206081", "K5BBLF900",13,0);
		loadEngine("K20C24206082", "K5BBLF900",14,0);
		loadEngine("K20C24206083", "K5BBLF900",15,0);
		loadEngine("K20C24206084", "K5BBLF900",16,0);
		loadEngine("K20C24206085", "K5BBLF900",17,0);
		loadEngine("K20C24206086", "K5BBLF900",18,0);
		
		
	
	}
	
	@Test
	public void test12CompleteQuorum4() {
		loadEngine("K20C24206003", "K5BBAF900",19,1);
		
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertEquals(4, quorum.getTrailerRow());
		
		model.completeQuorum(quorum);
		model.reloadActiveQuorums();
		
		ShippingQuorum newQuorum = getDao(ShippingQuorumDao.class).findByKey(quorum.getId());
		
		assertEquals(ShippingQuorumStatus.INCOMPLETE, newQuorum.getStatus());

	}

	@Test
	public void test13ManualLoadQuorum3() {
		ShippingQuorum quorum = findQuorum(1, 3);
		assertNotNull(quorum);
		assertEquals(ShippingQuorumStatus.COMPLETE, quorum.getStatus());
		List<String> eins = new ArrayList<>();
		eins.add("K20C24206084");
		eins.add("K20C24206085");
		eins.add("K20C24206086");
		eins.add("K20C24206075");
		eins.add("K20C24206076");
		eins.add("K20C24206077");
		
		model.saveManualLoadEngines(quorum, eins);
		
		ShippingQuorum newQuorum = getDao(ShippingQuorumDao.class).findByKey(quorum.getId());
		assertEquals(ShippingQuorumStatus.COMPLETE, newQuorum.getStatus());
		List<ShippingQuorumDetail> details = newQuorum.getShippingQuorumDetails();
		assertEquals("K20C24206084", details.get(0).getEngineNumber());
		assertEquals("K20C24206085", details.get(1).getEngineNumber());
		assertEquals("K20C24206086", details.get(2).getEngineNumber());
		assertEquals("K20C24206075", details.get(3).getEngineNumber());
		assertEquals("K20C24206076", details.get(4).getEngineNumber());
		assertEquals("K20C24206077", details.get(5).getEngineNumber());
		
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(0).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(1).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(2).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(3).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(4).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(5).getStatus());
		
		ShippingQuorum quorum2 = findQuorum(1, 2);
		details = quorum2.getShippingQuorumDetails();
		assertEquals(ShippingQuorumStatus.INCOMPLETE, quorum2.getStatus());
		
		assertNull(details.get(0).getEngineNumber());
		assertNull(details.get(1).getEngineNumber());
		assertNull(details.get(2).getEngineNumber());
		assertEquals("K20C24206078", details.get(3).getEngineNumber());
		assertEquals("K20C24206079", details.get(4).getEngineNumber());
		assertEquals("K20C24206080", details.get(5).getEngineNumber());
		
		
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();	

	    assertEquals(16, model.getAllShippingTrailers().get(0).getActQty());
	    
	    List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		assertEquals(15, schedules.get(0).getActQty());
		assertEquals(1,schedules.get(1).getActQty());
	}
	
	@Test
	public void test14ManualLoadQuorum2() {
		ShippingQuorum quorum = findQuorum(1, 2);
		assertNotNull(quorum);
		assertEquals(ShippingQuorumStatus.INCOMPLETE, quorum.getStatus());
		List<String> eins = new ArrayList<>();
		eins.add("K20C24206081");
		eins.add("K20C24206082");
		eins.add("K20C24206083");
		eins.add("K20C24206078");
		eins.add("K20C24206079");
		eins.add("K20C24206080");
		
		model.saveManualLoadEngines(quorum, eins);
		
		ShippingQuorum newQuorum = getDao(ShippingQuorumDao.class).findByKey(quorum.getId());
		assertEquals(ShippingQuorumStatus.COMPLETE, newQuorum.getStatus());
		List<ShippingQuorumDetail> details = newQuorum.getShippingQuorumDetails();
		assertEquals("K20C24206081", details.get(0).getEngineNumber());
		assertEquals("K20C24206082", details.get(1).getEngineNumber());
		assertEquals("K20C24206083", details.get(2).getEngineNumber());
		assertEquals("K20C24206078", details.get(3).getEngineNumber());
		assertEquals("K20C24206079", details.get(4).getEngineNumber());
		assertEquals("K20C24206080", details.get(5).getEngineNumber());
		
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(0).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(1).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(2).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(3).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(4).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(5).getStatus());
		
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();	

	    assertEquals(19, model.getAllShippingTrailers().get(0).getActQty());
	    
	    List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		assertEquals(18, schedules.get(0).getActQty());
		assertEquals(1,schedules.get(1).getActQty());
	}
	
	@Test
	public void test15ManualLoadQuorum4() {
		ShippingQuorum quorum = findQuorum(1, 4);
		assertNotNull(quorum);
		assertEquals(ShippingQuorumStatus.INCOMPLETE, quorum.getStatus());
		List<String> eins = new ArrayList<>();
		eins.add("K20C24206003");
		eins.add("K20C24206004");
		eins.add("K20C24206005");
		eins.add("K20C24206006");
		eins.add("K20C24206007");
		eins.add("K20C24206008");
		
		model.saveManualLoadEngines(quorum, eins);
		
		ShippingQuorum newQuorum = getDao(ShippingQuorumDao.class).findByKey(quorum.getId());
		assertEquals(ShippingQuorumStatus.COMPLETE, newQuorum.getStatus());
		List<ShippingQuorumDetail> details = newQuorum.getShippingQuorumDetails();
		assertEquals("K20C24206003", details.get(0).getEngineNumber());
		assertEquals("K20C24206004", details.get(1).getEngineNumber());
		assertEquals("K20C24206005", details.get(2).getEngineNumber());
		assertEquals("K20C24206006", details.get(3).getEngineNumber());
		assertEquals("K20C24206007", details.get(4).getEngineNumber());
		assertEquals("K20C24206008", details.get(5).getEngineNumber());
		
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(0).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(1).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(2).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(3).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(4).getStatus());
		assertEquals(ShippingQuorumDetailStatus.MANUAL_LOAD, details.get(5).getStatus());
		
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();	

	    assertEquals(24, model.getAllShippingTrailers().get(0).getActQty());
	    
	    List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		assertEquals(3, schedules.size());
		assertEquals(6,schedules.get(1).getActQty());
		
		
	}
	
	@Test
	public void test16MoveQuorumFromDelayedDifferentQuorumDate() {
		List<ShippingQuorum> scheduledQuorums = model.findAllScheduledQuorums();
		List<ShippingQuorum> delayedQuorums = model.findAllDelayedQuorums();
		assertTrue(delayedQuorums.size() > 0);
		ShippingQuorum delayedQuorum = delayedQuorums.get(0);
		
		for(ShippingQuorum quorum : scheduledQuorums) {
			model.updateQuorumStatus(quorum, ShippingQuorumStatus.DELAYED);
		}
		
		model.updateQuorumStatus(delayedQuorum, ShippingQuorumStatus.WAITING);
		
		
        model.reloadActiveQuorums();
        
        scheduledQuorums = model.findAllScheduledQuorums();
        assertEquals(1, scheduledQuorums.size());
        
        //BEFORE
        // ShippingQuorum(2018-11-16,450000,9,1) ALLOCATING
        // ShippingQuorum(currentDate, 250000,5,3) DELAYED
        // ShippingQuorum(currentDate, 300000,6,3) DELAYED
        
        delayedQuorums = model.findAllDelayedQuorums();
        ShippingQuorum currentQuorum = delayedQuorums.get(0);
        model.releaseDelayedQuorum(currentQuorum, scheduledQuorums.get(0));
        
        //AFTER Should be
        // ShippingQuorum(2018-11-16,450000,9,1) ALLOCATING
        // ShippingQuorum(currentDate, 250000,5,0) DELAYED
        // ShippingQuorum(currentDate, 300000,6,3) DELAYED
        
        model.reloadActiveQuorums();
        
        scheduledQuorums = model.findAllScheduledQuorums();
		assertEquals(2, scheduledQuorums.size());
		assertEquals(currentQuorum.getId().getQuorumDate(),scheduledQuorums.get(1).getId().getQuorumDate());
		assertEquals(currentQuorum.getId().getQuorumId(),scheduledQuorums.get(1).getId().getQuorumId());
			
	}
	
	@Test
	public void test17CompleteTrailer() {
		model.reloadShippingTrailerInfoList();
		assertEquals(1, model.getAllShippingTrailers().size());
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		
		model.completeTrailer(info);
		
		TestUtils.sleep(3000);
		
		model.reloadShippingTrailerInfoList();
		assertEquals(0, model.getAllShippingTrailers().size());

		info = getDao(ShippingTrailerInfoDao.class).findByKey(info.getTrailerId());
		assertEquals(ShippingTrailerInfoStatus.COMPLETE, info.getStatus());
		
		model.reloadActiveQuorums();
		List<ShippingQuorum> scheduledQuorums = model.findAllScheduledQuorums();
		List<ShippingQuorum> delayedQuorums = model.findAllDelayedQuorums();
		
		assertEquals(0, scheduledQuorums.size());
		assertEquals(0, delayedQuorums.size());
		
        Engine engine = getDao(EngineDao.class).findByKey("K20C24205994");		
        assertNotNull(engine);
        assertEquals("AE1SH1", engine.getTrackingStatus());
        assertEquals("AE0EN16601", engine.getLastPassingProcessPointId());
		
		
	}
	
		
	private ShippingQuorum findQuorum(int trailerId, int trailerRow) {
		List<ShippingQuorum> quorums  = model.findAllManualLoadQuorums();
		for(ShippingQuorum quorum : quorums) {
			if(quorum.getTrailerId() == trailerId && quorum.getTrailerRow() == trailerRow) return quorum;
		}
		return null;
	}
	
	
	private void loadRepairEngine(String ein, String mtoc, int seq) {
		
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_REPAIR, quorum.getTrailerId());
		
		
		getTrackingService().loadEngineProcess(ein, mtoc);
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();
		
		ShippingQuorum updatedQuorum = model.findAllScheduledQuorums().get(0);
		
		if(seq == quorum.getQuorumSize()) {
			assertEquals(ShippingQuorumStatus.ALLOCATING,updatedQuorum.getStatus());
		}else {
			assertEquals(seq, updatedQuorum.getLoadedCount());
			
			ShippingQuorumDetail detail = updatedQuorum.getShippingQuorumDetails().get(seq -1);
			assertEquals(ein,detail.getEngineNumber());
			assertEquals(ShippingQuorumStatus.LOADING,updatedQuorum.getStatus());
		}
		
	}
	
	private void loadExceptionalEngine(String ein, String mtoc, int seq, boolean isMissLoad) {
		
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		assertEquals(ShippingTrailerInfo.TRAILER_ID_EXCEPT, quorum.getTrailerId());
		
		
		getTrackingService().loadEngineProcess(ein, mtoc);
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();
		
		ShippingQuorum updatedQuorum = model.findAllScheduledQuorums().get(0);
		
		if(seq == quorum.getQuorumSize()) {
			assertEquals(ShippingQuorumStatus.ALLOCATING,updatedQuorum.getStatus());
		}else {
			assertEquals(seq, updatedQuorum.getLoadedCount());
			
			ShippingQuorumDetail detail = updatedQuorum.getShippingQuorumDetails().get(seq -1);
			assertEquals(ein,detail.getEngineNumber());
			if(isMissLoad) {
				assertNotEquals(mtoc, detail.getYmto());
				assertEquals(ShippingQuorumDetailStatus.MISS_LOAD,detail.getStatus());
			}else {
				assertEquals(mtoc, detail.getYmto());
				assertEquals(ShippingQuorumDetailStatus.AUTO_LOAD,detail.getStatus());

			}
			assertEquals(ShippingQuorumStatus.LOADING,updatedQuorum.getStatus());
		}
		
	}

	private void loadEngine(String ein, String mtoc, int count,int scheduleIndex) {
		int seq = count % 6;
			
		getTrackingService().loadEngineProcess(ein, mtoc);
		
		
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();
		
		ShippingQuorum quorum = model.findAllScheduledQuorums().get(0);
		
		if(seq == 0) {
			assertEquals(ShippingQuorumStatus.ALLOCATING,quorum.getStatus());
		}else {
			assertEquals((count / 6) + 1, quorum.getTrailerRow());
			assertEquals(seq, quorum.getLoadedCount());
			ShippingQuorumDetail detail = quorum.getShippingQuorumDetails().get(seq -1);
			assertEquals(ein,detail.getEngineNumber());
			assertEquals(ShippingQuorumStatus.LOADING,quorum.getStatus());
		}
		
		
		ShippingTrailerInfo info = model.getAllShippingTrailers().get(0);
		assertEquals(count, info.getActQty());
		
		List<ShippingVanningSchedule> schedules = model.findAllActiveVanningSchedules();
		int qty = 0;
		for(int i=0; i<scheduleIndex; i++) {
			qty += schedules.get(i).getActQty();
		}
		
		ShippingVanningSchedule schedule = schedules.get(scheduleIndex);
		assertEquals(count - qty,schedule.getActQty());
	}
 	
	private static void initModel() {
		List<String> args = new ArrayList<String>();
		args.add("rootdir");
		args.add("server url");
		args.add(PPID);
		Arguments arguments = Arguments.create(args);
		ApplicationContext appContext = NonCachedApplicationContext.create(arguments);
		model.setApplicationContext(appContext);
	}
	
	private GtsEngineBodyTrackingService getTrackingService() {
		return getService(GtsEngineBodyTrackingService.class);
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
	
	
	
}
