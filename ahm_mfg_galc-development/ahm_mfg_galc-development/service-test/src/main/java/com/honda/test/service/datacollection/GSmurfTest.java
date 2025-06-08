package com.honda.test.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.property.PropertyService;
import com.honda.test.util.DBUtils;
import com.honda.test.util.TestUtils;

public class GSmurfTest {
	@BeforeClass
	public static void  loadConfig() {
		
		DBUtils.loadConfig();
		DBUtils.readAndExecuteSqlFromFile("sql/service/datacollection/GSmurf.sql");

		PropertyService.refreshComponentProperties("System_Info");
		
		TestUtils.startWebServer();
	}
	
	@Test
	public void testMapByRule(){
		
		DataCollectionService service = getService(DataCollectionService.class);
		
		DataContainer dc = new DefaultDataContainer();
		
		
		for(int i=1;i<=18;i++) {
			dc.put("ANGLE"+i, 137.0);
			dc.put("RESULT"+i, "true");
			dc.put("STRING_VALUE"+i, "1");
			dc.put("MNAME"+i, "1");
			dc.put("METHOD"+i, 100);
			dc.put("VALUE"+i, 106.0);
		}
		dc.put("PRODUCT_ID", "2HGFC3A52KH750450");
		dc.put("SN1", "JA122018072141L3");
		dc.put("SN2", "2");
//		dc.put("STATUS2","TRUE");
		
		DataContainer data = TestUtils.prepareDataContainer("1AF1D1202_VIN",dc);
		
		data.put("PROCESS_POINT_ID", "1AF1D1202");
		
		
		service.execute(data);
		
		List<InstalledPart> installedParts = getDao(InstalledPartDao.class).findAllByProductIdAndProcessPoint("2HGFC3A52KH750450", "1AF1D1202");
		assertEquals(20,installedParts.size());
		for(InstalledPart installedPart:installedParts) {
			if(installedPart.getPartName().equalsIgnoreCase("EQUIP GSMURF MODEL")) {
				assertEquals(InstalledPartStatus.OK, installedPart.getInstalledPartStatus());
			}
		}
		
		TestUtils.sleep(300);
		List<ProductResult> productResults = getDao(ProductResultDao.class).findAllByProductAndProcessPoint("2HGFC3A52KH750450", "1AF1D1202");
		assertEquals(1,productResults.size());
		
	}
	
	@AfterClass
	public static void  cleanup() {
		DBUtils.resetData();
	}
}
