package com.honda.galc.client.product.entry;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.client.CachedApplicationContext;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.service.ServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class})
public class SearchByDunnageModelTest {

	@Mock
	private DunnageDao dunnageDaoMock;
	
	@Mock
	private DunnageContentDao dunnageContentDaoMock;

	@Mock
	private CachedApplicationContext contextMock;

	@InjectMocks
	private SearchByDunnageModel searchByDunnageModel = new SearchByDunnageModel(contextMock);
	
	List<Dunnage> dunnageList;
	
	List<String> productList;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(ServiceFactory.class);
		dunnageList = new ArrayList<Dunnage>();
		productList = new ArrayList<String>(); 
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 02, 2019
	 * 
	 * Test searching products that are assigned to a particular dunnage.
	 * 
	 * Test for when no products are found in dunnage.
	 * 
	 * dunnage id = DUNNAGEID1
	 */
	@Test
	public void getProductsByDunnage_NoProductsFound() {
		when(ServiceFactory.getDao(DunnageContentDao.class)).thenReturn(dunnageContentDaoMock);
		when(dunnageContentDaoMock.findAllProductIdsInDunnage("DUNNAGEID1")).thenReturn(new ArrayList<String>());

		searchByDunnageModel.getProductsByDunnage("DUNNAGEID1");
		assertTrue("Test failed, expected 0 results, actual : " + searchByDunnageModel.getProducts().size(), searchByDunnageModel.getProducts().size() == 0);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date May 02, 2019
	 * 
	 * Test searching products that are assigned to a particular dunnage.
	 * 
	 * Test for when two products are assigned to dunnage.
	 * 
	 * dunnage id = DUNNAGEID1
	 * product Ids = LG93291019T, LG93291017R
	 */
	@Test
	public void getProductsByDunnage_MultipleProductsFound() {
		productList.add("LG93291019T");
		productList.add("LG93291017R");

		when(ServiceFactory.getDao(DunnageContentDao.class)).thenReturn(dunnageContentDaoMock);
		when(dunnageContentDaoMock.findAllProductIdsInDunnage("DUNNAGEID1")).thenReturn(productList);

		searchByDunnageModel.getProductsByDunnage("DUNNAGEID1");
		assertTrue("Test failed, expected 2 results, actual : " + searchByDunnageModel.getProducts().size(), searchByDunnageModel.getProducts().size() == 2);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by supplying a substring of a dunnage Id.
	 * All dunnage records where the substring in contained in the dunnage Id should be returned.
	 * 
	 * Test for when 11 results are found matching "AGEID1"
	 * 
	 * dunnage id = 1DUNNAGEID,2DUNNAGEID,3DUNNAGEID,4DUNNAGEID,5DUNNAGEID,6DUNNAGEID,7DUNNAGEID,8DUNNAGEID,9DUNNAGEID,10DUNNAGEID,11DUNNAGEID
	 */
	@Test
	public void findByDunnage_Return11Results() {
		dunnageList.add(createDunnage("", 12, "RV0"));
		dunnageList.add(createDunnage("2DUNNAGEID", 12, "RV0"));
		dunnageList.add(createDunnage("3DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("4DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("5DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("6DUNNAGEID", 12, "RV0"));
		dunnageList.add(createDunnage("7DUNNAGEID", 12, "RV0"));
		dunnageList.add(createDunnage("8DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("9DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("10DUNNAGEID", 12, "R70"));
		dunnageList.add(createDunnage("110DUNNAGEID", 12, "R70"));

		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialDunnage("AGEID")).thenReturn(dunnageList);
		searchByDunnageModel.findByDunnage("AGEID");
		assertTrue("Test failed, expected 11 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 11);
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by supplying a substring of a dunnage Id.
	 * All dunnage records where the substring in contained in the dunnage Id should be returned.
	 * 
	 * Test for when 0 results are found matching "AGEID99"
	 * 
	 * dunnage id = AGEID99
	 */
	@Test
	public void findByDunnage_NoDunnagesFound() {
		dunnageList.add(createDunnage("DUNNAGEID1", 12, "RV0"));
		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialDunnage("AGEID99")).thenReturn(new ArrayList<Dunnage>());
		searchByDunnageModel.findByDunnage("AGEID99");
		assertTrue("Test failed, expected 0 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 0);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by supplying a substring of a dunnage Id.
	 * All dunnage records where the substring in contained in the dunnage Id should be returned.
	 * 
	 * Test for when 1 record is found with an exact match"
	 * 
	 * dunnage id = DUNNAGEID1
	 */
	@Test
	public void findByDunnage_ExactMatchFound() {
		dunnageList.add(createDunnage("DUNNAGEID1", 12, "RV0"));

		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialDunnage("DUNNAGEID1")).thenReturn(dunnageList);
		searchByDunnageModel.findByDunnage("DUNNAGEID1");
		assertTrue("Test failed, expected 1 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 1);
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by product Id
	 * 
	 * Test for when no dunnages are found with the substring provided"
	 * 
	 * Product Id = DUNNAGEID17
	 */
	@Test
	public void findAllByProductId_NoDunnagesFound() {

		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialProductId("DUNNAGEID1")).thenReturn(new ArrayList<Dunnage>());
		searchByDunnageModel.findAllByProductId("DUNNAGEID1");
		assertTrue("Test failed, expected 0 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 0);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by product Id
	 * 
	 * Test for when the product Id sent is assigned to a dunnage"
	 * 
	 * dunnage id = DUNNAGEID1
	 * product Id = LG93291019T
	 */
	@Test
	public void findAllByProductId_ExactMatchFound() {
		dunnageList.add(createDunnage("DUNNAGEID1", 12, "RV0"));

		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialProductId("LG93291019T")).thenReturn(dunnageList);
		searchByDunnageModel.findAllByProductId("LG93291019T");
		assertTrue("Test failed, expected 1 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 1);
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @date Mar 31, 2019
	 * 
	 * Test searching for dunnages by supplying a substring of a dunnage Id.
	 * All dunnage records where the substring in contained in the dunnage Id should be returned.
	 * 
	 * Test for when 2 records is found with an exact match"
	 * 
	 * dunnage id = DUNNAGEID17
	 */
	@Test
	public void findByProductSpecCode_ExactMatchFound() {
		dunnageList.add(createDunnage("DUNNAGEID1", 12, "RV0"));
		dunnageList.add(createDunnage("DUNNAGEID2", 12, "RV0"));
		dunnageList.add(createDunnage("DUNNAGEID3", 12, "R70"));
		dunnageList.add(createDunnage("DUNNAGEID4", 12, "R70"));
		dunnageList.add(createDunnage("DUNNAGEID5", 12, "R70"));
		
		when(ServiceFactory.getDao(DunnageDao.class)).thenReturn(dunnageDaoMock);
		when(dunnageDaoMock.findAllByPartialMtoc("RV0")).thenReturn(dunnageList.subList(0,2));
		searchByDunnageModel.findByProductSpecCode("RV0");
		assertTrue("Test failed, expected 2 results, actual : " + searchByDunnageModel.getDunnages().size(), searchByDunnageModel.getDunnages().size() == 2);
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(0)));
		assertTrue("Incorrect result " + searchByDunnageModel.getDunnages().toString(), searchByDunnageModel.getDunnages().contains(dunnageList.get(1)));
	}
	
	private Dunnage createDunnage(String dunnageId, int expectedQty, String productSpecCode) {
		Dunnage dunnage = new Dunnage();
		dunnage.setDunnageId(dunnageId);
		dunnage.setExpectedQty(expectedQty);
		dunnage.setProductSpecCode(productSpecCode);
		return dunnage;
	}
}
