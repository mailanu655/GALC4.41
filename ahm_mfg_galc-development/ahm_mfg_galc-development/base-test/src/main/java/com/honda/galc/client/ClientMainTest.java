package com.honda.galc.client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.property.TestBean;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.ApplicationMenuDao;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.Site;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.net.SocketRequestDispatcher;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.data.ProductType;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ClientMainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadConfig();
//		findApplications();
//		findDevices();
//		testInprocessProduct();
		// findApplicationMenus();
//		testPropertyBean();
//		findRules();
		//testUpdateProperty();
//		findDeviceFormats();
	//	findPlants();
	//	findRules();
	//	startSocketServer();
//		findSite();
//		findPlant();
//		findPartName();
	//	findEngine();
	//	loadDepartmentSchedule();
		
		createDefect();
	//	testHistoryDao();
		
	}
	private static void testHistoryDao() {
		ProductHistoryDao<? extends ProductHistory,?> productHistoryDao = 
    		ProductTypeUtil.getProductHistoryDao(ProductType.valueOf("ENGINE"));
		
		productHistoryDao.isProductProcessed("R18Z11929831", "AE0EN12801", new Timestamp(2012, 07, 16, 0, 0,0,0).toString());
		
	}

	private static void createDefect() {
		InstalledPart part = new InstalledPart("R18Z11929831", "B-CAP");
		part.setInstalledPartStatusId(0);
		Measurement m = new Measurement("R18Z11929831", "B-CAP", 1);
		m.setMeasurementStatusId(0);
		part.getMeasurements().add(m);
		
		List<InstalledPart> l = new ArrayList<InstalledPart>();
		l.add(part);
		
		ServiceFactory.getService(QicsService.class).update("AE0EN12801", ProductType.valueOf("ENGINE"), l);
		
	}

	
	public static void getTaskList() {
		
		List<ApplicationTask> taskSpecs = ServiceFactory.getDao(ApplicationTaskDao.class).findAll();
		
		int i = 0;
		
	}
	
	public static void findApplicationMenus() {
		List<ApplicationMenuEntry> menus = ServiceFactory.getDao(ApplicationMenuDao.class).findAll();
		int i = 0;
	}
	
	public static void testInprocessProduct() {
		InProcessProductDao dao = ServiceFactory.getDao(InProcessProductDao.class);
		for(int i = 0;i<10; i++)
			dao.findLastForLine("AE1BL1");
	}
	public static void findApplications() {
		ApplicationDao dao = ServiceFactory.getDao(ApplicationDao.class);
		List<Application> applications = dao.findAll();
		for(Application app : applications) {
			System.out.println("Application: " + app.getApplicationId() + " name: = " + app.getApplicationName());
		}
		Application app = dao.findByKey("ConHldPrtBySN_TL");

		System.out.println("this App" + app.getApplicationName());

		System.out.println(" count = " + dao.count());
	}

	public static void findDevices() {
		List<Device> devices = ServiceFactory.getDao(DeviceDao.class).findAll();
		
		int i = 0;
	}
	
	public static void findDeviceFormats(){

		DeviceFormatDao deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		List<DeviceFormat> deviceFormats = deviceFormatDao.findAll();

		for(DeviceFormat df : deviceFormats) {
			System.out.println("DeviceFormat: " + df.getTagName() + " tag value: = " + df.getTagValue());
		}

	}

	public static void findPlants() {
		PlantDao plantDao = ServiceFactory.getDao(PlantDao.class);
		List<Plant> plants = plantDao.findAll();
		for(Plant plant: plants) {
			System.out.println("Plant : " + plant.getId().getPlantName() + plant.getId().getSiteName());

			for (Division division : plant.getDivisions()) {
				System.out.println("Division: " + division.getDivisionId() + "name: " + division.getDivisionName());
			}
		}
	}

	public static void findRules() {

		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		PartNameDao partNameDao = ServiceFactory.getDao(PartNameDao.class);
		List<LotControlRule> rules = lotControlRuleDao.findAllByProcessPoint("AE0EN14501");
		Map<String, PartName> partNames = new HashMap<String,PartName>();
		for(LotControlRule rule: rules) {

			String partName = rule.getId().getPartName();
			if(partNames.containsKey(partName)) 	rule.setPartName(partNames.get(partName)) ;
			else 	{
				PartName partNameObj = partNameDao.findPartNameByLotCtrRule(rule);
				partNames.put(partName, partNameObj);
				rule.setPartName(partNameObj);
			}

		}
	}

	public static void findSite() {
		SiteDao dao = ServiceFactory.getDao(SiteDao.class);
	    PlantDao plantDao =    ServiceFactory.getDao(PlantDao.class);
		Site site1 = new Site();
		site1.setSiteName("test");
		site1.setSiteDescription("message");
		dao.save(site1);

		 List<Site> sites= dao.findAll();

	   //     for(Site site : sites)
	   //         site.setPlants(plantDao.findAllBySiteWithChildren(site.getSiteName()));


	}
	
	public static void loadDepartmentSchedule() {
		
		List<DailyDepartmentSchedule> schedules = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).findAll();
		
		
	}

//	public static void findPlant() {
//		PlantDao plantDao =    ServiceFactory.getDao(PlantDao.class);
//		List<Plant> plants  = plantDao.findById("HCM");
//	}

	public static void findPartName() {
		PartNameDao dao = ServiceFactory.getDao(PartNameDao.class);
//		PartDao partDao = ClientDaoFactory.getDao(PartDao.class);
		PartName partName = dao.findByKey("MISSION");
//		partName.setParts(partDao.findAllByPartName("MISSION"));
	}

	public static void findEngine() {
		EngineDao dao = ServiceFactory.getDao(EngineDao.class);
		Engine engine = dao.findByKey("R18A14759410");
		System.out.println(engine.getProductId());
	}
	public static void testPropertyBean() {
		TestBean testBean = PropertyService.getPropertyBean(TestBean.class, "ESTS Client");

		int height = testBean.getImageWidth();
		System.out.println(" height = " + height);
	}

	public static void startSocketServer() {
		SocketRequestDispatcher dispatcher = new SocketRequestDispatcher(3030,null);
		try {
			dispatcher.accept();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void loadConfig() {

		ApplicationContextProvider.loadFromClassPathXml("application.xml");

	}

	public static void testUpdateProperty() {
	    ComponentPropertyId id = new ComponentPropertyId();
	    id.setComponentId("test");
	    id.setPropertyKey("test");
	    ComponentProperty cp = ServiceFactory.getDao(ComponentPropertyDao.class).findByKey(id);
	    cp.setPropertyValue("PROPERTY CHANGEd");
	    ServiceFactory.getDao(ComponentPropertyDao.class).save(cp);
	}

}
