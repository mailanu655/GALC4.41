package com.honda.galc.client.product.entry;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.pane.SearchByDunnagePane;

public class SearchByDunnageControllerTest {

	@Mock
	SearchByDunnageModel searchByDunnageModelMock;

	@Mock
	SearchByDunnagePane searchByDunnagePaneMock;

	@Mock
	ApplicationContext applicationContextMock;

	@InjectMocks
	SearchByDunnageController searchByDunnageController = new SearchByDunnageController();;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test for searching for product ids with no results found and 
	 * a null result to returned from model
	 * 
	 * dunnage id = DUNNAGEID
	 *
	 **/
	@Test
	public void getProductsByDunnage_NoResultsFoundNullResult() {
		Mockito.when(searchByDunnageModelMock.getProductsByDunnage("DUNNAGEID")).thenReturn(null);
		Mockito.when(searchByDunnagePaneMock.getDunnageField()).thenReturn(null);
		Mockito.doNothing().when(searchByDunnagePaneMock).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
		List<String> dunnage = searchByDunnageController.getProductsByDunnage("DUNNAGEID");
		assertNull(dunnage);
		Mockito.verify(searchByDunnagePaneMock, Mockito.times(1)).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test for searching for product ids with no results found and 
	 * an empty list result to returned from model
	 *
	 * dunnage id = DUNNAGEID
	 *
	 **/
	@Test
	public void getProductsByDunnage_NoResultsFound() {
		Mockito.when(searchByDunnageModelMock.getProductsByDunnage("DUNNAGEID")).thenReturn(new ArrayList<String>());
		Mockito.when(searchByDunnagePaneMock.getDunnageField()).thenReturn(null);
		Mockito.doNothing().when(searchByDunnagePaneMock).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
		List<String> dunnage = searchByDunnageController.getProductsByDunnage("DUNNAGEID");
		assertTrue(dunnage.size() == 0);
		Mockito.verify(searchByDunnagePaneMock, Mockito.times(1)).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test for searching for product ids with 1 result found
	 *
	 * dunnage id = DUNNAGEID
	 *
	 **/
	@Test
	public void getProductsByDunnage_OneResultFound() {
		List<String> resultProductIds = new ArrayList<String>();
		resultProductIds.add("TESTPRODUCTID");
		Mockito.when(searchByDunnageModelMock.getProductsByDunnage("DUNNAGEID")).thenReturn(resultProductIds);
		Mockito.when(searchByDunnagePaneMock.getDunnageField()).thenReturn(null);
		Mockito.doNothing().when(searchByDunnagePaneMock).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
		List<String> dunnage = searchByDunnageController.getProductsByDunnage("DUNNAGEID");
		assertTrue(resultProductIds.size() == 1);
		assertTrue(dunnage.contains("TESTPRODUCTID"));
		Mockito.verify(searchByDunnagePaneMock, Mockito.times(0)).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
	}
	
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 31, 2019
	 * 
	 * Test for searching for product ids with 3 result found
	 *
	 * dunnage id = DUNNAGEID
	 *
	 **/
	@Test
	public void getProductsByDunnage_MultipleResultFound() {
		List<String> resultProductIds = new ArrayList<String>();
		resultProductIds.add("TESTPRODUCTID1");
		resultProductIds.add("TESTPRODUCTID2");
		resultProductIds.add("TESTPRODUCTID3");
		Mockito.when(searchByDunnageModelMock.getProductsByDunnage("DUNNAGEID")).thenReturn(resultProductIds);
		Mockito.when(searchByDunnagePaneMock.getDunnageField()).thenReturn(null);
		Mockito.doNothing().when(searchByDunnagePaneMock).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
		List<String> dunnage = searchByDunnageController.getProductsByDunnage("DUNNAGEID");
		assertTrue(resultProductIds.size() == 3);
		assertTrue(dunnage.contains("TESTPRODUCTID1"));
		assertTrue(dunnage.contains("TESTPRODUCTID2"));
		assertTrue(dunnage.contains("TESTPRODUCTID3"));
		Mockito.verify(searchByDunnagePaneMock, Mockito.times(0)).setErrorMessage(Matchers.anyString(), Matchers.anyObject());
	}
}
