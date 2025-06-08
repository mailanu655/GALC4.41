package com.honda.galc.service.partmarkrequest;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.service.PartMarkRequestService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class,PropertyService.class}) 
public class PartMarkRequestServiceImplTest {
	
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 18,2019
	 * Tests getPartMarks method in PartMarkRequestService.java
	 * ModelYearCode And PartNumbers Match Found
	 */
	@Test
	public void getPartMarks_WhenModelYearCodeAndPartNumbersMatchFound(){
		PowerMockito.mockStatic(ServiceFactory.class);
		BuildAttributeByBomDao buildAttributeByBomDaoMock =  PowerMockito.mock(BuildAttributeByBomDao.class);
		when(ServiceFactory.getDao(BuildAttributeByBomDao.class)).thenReturn(buildAttributeByBomDaoMock);
		when(buildAttributeByBomDaoMock.findAllAttributeForPartNoAndModelYear(Matchers.anyString(),Matchers.anyString(), Matchers.anyString())).thenReturn(getPartMarks());
		PartMarkRequestService partMarkRequestService = new PartMarkRequestServiceImpl();
		//DataContainer dataContainer = partMarkRequestService.getPartMarks("2018", getPartNumbers());
		
		//assertEquals(0,((Integer)dataContainer.get("RETURN_CODE")).intValue());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 18,2019
	 * Tests getPartMarks method in PartMarkRequestService.java
	 * ModelYearCode And PartNumbers Match Not Found
	 */
	@Test
	public void getPartMarks_WhenModelYearCodeAndPartNumbersMatchNotFound(){
		PowerMockito.mockStatic(ServiceFactory.class);
		BuildAttributeByBomDao buildAttributeByBomDaoMock =  PowerMockito.mock(BuildAttributeByBomDao.class);
		when(ServiceFactory.getDao(BuildAttributeByBomDao.class)).thenReturn(buildAttributeByBomDaoMock);	
		when(buildAttributeByBomDaoMock.findAllAttributeForPartNoAndModelYear(Matchers.anyString(),Matchers.anyString(), Matchers.anyString())).thenReturn(new ArrayList<Object[]>());
		PartMarkRequestService partMarkRequestService = new PartMarkRequestServiceImpl();
		//DataContainer dataContainer = partMarkRequestService.getPartMarks("2018", getPartNumbers());
		
		//assertEquals(0,((Integer)dataContainer.get("RETURN_CODE")).intValue());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 18,2019
	 * Tests getPartMarks method in PartMarkRequestService.java
	 * on Exception
	 */
	@Test
	public void getPartMarks_OnError(){
		PowerMockito.mockStatic(ServiceFactory.class);
		BuildAttributeByBomDao buildAttributeByBomDaoMock =  PowerMockito.mock(BuildAttributeByBomDao.class);
		when(ServiceFactory.getDao(BuildAttributeByBomDao.class)).thenReturn(buildAttributeByBomDaoMock);
		when(buildAttributeByBomDaoMock.findAllAttributeForPartNoAndModelYear(Matchers.anyString(),Matchers.anyString(), Matchers.anyString())).thenThrow(new NullPointerException());
		PartMarkRequestService partMarkRequestService = new PartMarkRequestServiceImpl();
		//DataContainer dataContainer = partMarkRequestService.getPartMarks("2018", getPartNumbers());
		
		//assertEquals(1,((Integer)dataContainer.get("RETURN_CODE")).intValue());
	}
	
	private List<String> getPartNumbers() {
		List<String> partNumbers = new ArrayList<String>();
		partNumbers.add("73300TJB B0");
		partNumbers.add("73300TJB A1");
		
		return partNumbers;
	}

	private List<Object[]> getPartMarks() {
		List<Object[]> partMarks = new ArrayList<Object[]>();
		
		for(int i=0;i<2;i++) {
			String[] result = new String[] {"partNo"+i,"partMark"+i};
			
			partMarks.add(result);
		}
		return partMarks;
	}
}
