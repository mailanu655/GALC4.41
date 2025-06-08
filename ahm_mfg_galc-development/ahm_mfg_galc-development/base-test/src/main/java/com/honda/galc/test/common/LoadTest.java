package com.honda.galc.test.common;

import java.util.Calendar;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

public class LoadTest {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadConfig();
		//testTracking();
		//testSave(args);
		test1();
		//loadTestTracking(args);
		//testBlock();
		//loadTestSchedule();
	}

	private static void test1() {
		InstalledPartDao dao = ServiceFactory.getService(InstalledPartDao.class);
		InstalledPartId id = new InstalledPartId();
		id.setProductId("R18A14800954");
		id.setPartName("6X30 CHAIN CASE");
		InstalledPart part = dao.findByKey(id);
		part.getId().setPartName("TEST");
		part.setPartSerialNumber(null);
		dao.save(part);
		
	}

	private static void testBlock() {
		String block="R1A0HC1301A10001D";
		TrackingService service = ServiceFactory.getService(TrackingService.class);
		service.track(ProductType.BLOCK, block, "AE0EN10801");
		
		
	}

	public static void loadConfig() {

		ApplicationContextProvider.loadFromClassPathXml("application.xml");

	}

	private static void loadTestTracking(String[] args) {
		int einNumber = 480;
		String einPrefix="R18A147";
		String processPointId = "AE0EN12501";
		int counter = 1000;
		
		if(args.length > 0)
			processPointId = args[0];
		if(args.length > 1)
			counter = Integer.parseInt(args[1]);
		if(args.length > 2)
			einNumber = Integer.parseInt(args[2]);
		
		
		int count = 0;
		while (count < counter) {
			String ein = einPrefix + String.format("%1$05d", einNumber);
			long start = System.currentTimeMillis();
			
//			EngineDao dao = ServiceFactory.getDao(EngineDao.class);
//			Engine engine = dao.findByKey(ein);
			TrackingService service = ServiceFactory.getService(TrackingService.class);
			service.track(ProductType.ENGINE, ein, processPointId);
			System.out.println("--------counter:" + count + " productId:" + ein + " time:" + (System.currentTimeMillis() - start));
			einNumber++;
			count++;
		}

	}
	
	private static void testSave(String[] args) {
		int test = Integer.parseInt(args[3]);
		int counter = test;
		InProcessProductDao dao = ServiceFactory.getService(InProcessProductDao.class);
		while (counter < (1000 + test)) {
			
			//sleep(10);
			
			long start = System.currentTimeMillis();
			//InProcessProduct p = dao.findByKey("---TEST");
//					InProcessProduct p = new InProcessProduct();
//					p.setProductId("---TEST_"+ counter);
//			p.setNextProductId(null);
//			p.setLineId("AE1BL1");
//			p.setProductSpecCode("RNA");
//			p.setProductionLot("testLog");
//			p.setLastPassingProcessPointId("MY-LAST-PP");
//			p.setPlanOffDate(new Date(System.currentTimeMillis()));
//			p.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
//			dao.save(p);
//			dao.findByNextProductId(p.getProductId());
			InProcessProduct product = dao.findLastForLine("AE1BL1");
			System.out.println("--------------------Last in process product:" + product.getProductId());
//			p.setProductId("-INSERT_" +counter);
//			p.setLineId(args[0]);
//			dao.save(p);
			System.out.println("------" + counter + " total:" + (System.currentTimeMillis() - start));
			counter++;
			
		}		
	}
	

	public static void testTracking() {
		String ein = "R18A14800909";
		EngineDao dao = ServiceFactory.getDao(EngineDao.class);
		Engine engine = dao.findByKey(ein);
		TrackingService service = ServiceFactory.getService(TrackingService.class);
		service.track(engine, "AE0EN12501");
		//service.track(ProductType.ENGINE, ein, "AE0EN12501");
	}
	
	public static void loadTestSchedule() {
		for(int i = 0; i < 10000; i++){
			long timeInMillis = Calendar.getInstance().getTimeInMillis();
			System.out.println("counter: " + i + " time:" + timeInMillis);
		}
	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}