package com.honda.galc.service.kickout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.common.exception.InputValidationException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.KickoutPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class, Logger.class, ServiceFactory.class})
public class KickoutServiceImplTest {

	@Mock
	KickoutPropertyBean kickoutPropertyBeanMock;

	@Mock
	HeadLessPropertyBean headlessPropertyBeanMock;
	
	@Mock
	KickoutDao kickoutDaoMock = PowerMockito.mock(KickoutDao.class);

	@InjectMocks
	private KickoutServiceImpl kickoutService = new KickoutServiceImpl();

	@Before
	public void setup() {
		PowerMockito.mockStatic(ServiceFactory.class);
		
		when(ServiceFactory.getDao(KickoutDao.class)).thenReturn(kickoutDaoMock);

		MockitoAnnotations.initMocks(this);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test isMultipleKickout to determine if multiple kickouts are allows on a product at the same time.
	 * Test case for when multiple kickout are not allowed.
	 * {ApplicationId = "AppId"}
	 */
	@Test
	public void isMultipleKickoutTest_false() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		String applicationId = "AppId";
		boolean isMultipleKickoutExpected = false;
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(false);
		boolean isMultipleKickoutResult = kickoutService.isMultipleKickout(applicationId);
		assertFalse(methodName + " failed: expected isMultipleKickout : " 
				+ isMultipleKickoutExpected + " actual : " +  isMultipleKickoutResult, isMultipleKickoutResult);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test isMultipleKickout to determine if multiple kickouts are allows on a product at the same time.
	 * Test case for when multiple kickout are allowed.
	 * {ApplicationId = "AppId"}
	 */
	@Test
	public void isMultipleKickoutTest_true() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		String applicationId = "AppId";
		boolean isMultipleKickoutExpected = true;
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(true);
		boolean isMultipleKickoutResult = kickoutService.isMultipleKickout(applicationId);
		assertTrue(methodName + " failed: expected isMultipleKickout : " 
				+ isMultipleKickoutExpected + " actual : " +  isMultipleKickoutResult, isMultipleKickoutResult);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test isMultipleKickout to determine if multiple kickouts are allows on a product at the same time.
	 * Test case for when the application id is missing.
	 * {ApplicationId = null}
	 */

	@Test(expected = InputValidationException.class)
	public void isMultipleKickoutTest_nullApplicationId() {
		String applicationId = null;
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(true);
		kickoutService.isMultipleKickout(applicationId);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 0
	 * {resultSize = 0}
	 */
	@Test
	public void getBatchSize_ResultSize0() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 0;
		int expectedResultSize = 0;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize 
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount + ".  "
				, expectedResultSize, batchSize);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 1
	 * {resultSize = 1}
	 */
	@Test
	public void getBatchSize_ResultSize1() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 1;
		int expectedResultSize = 1;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize 
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount + ".  "
				, expectedResultSize, batchSize);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 5
	 * {resultSize = 5}
	 */
	@Test
	public void getBatchSize_ResultSize5() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 5;
		int expectedResultSize = 4;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount + ".  "
				, expectedResultSize, batchSize);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 20
	 * {resultSize = 20}
	 */
	@Test
	public void getBatchSize_ResultSize20() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 20;
		int expectedResultSize = 11;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize 
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount 
				+ ".  ", expectedResultSize, batchSize);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 75
	 * {resultSize = 75}
	 */
	@Test
	public void getBatchSize_ResultSize75() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 75;
		int expectedResultSize = 51;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize 
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount 
				+ ".  ", expectedResultSize, batchSize);
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getBatchSize to get the number of products to include in a query. The number of products is 200
	 * {resultSize = 200}
	 */
	@Test
	public void getBatchSize_ResultSize200() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		int productIdCount = 200;
		int expectedResultSize = 101;
		int batchSize = kickoutService.getBatchSize(productIdCount);
		assertEquals(methodName + " failed: Expected batch size of : " + expectedResultSize 
				+ " actual : " + batchSize + ", the productIdCount was " + productIdCount 
				+ ".  ", expectedResultSize, batchSize);
	}
	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getProductListForQuery to get List of product Ids to search for based on a batch size.
	 * Test getting the first 4 results of a List of 5 product Ids.
	 * 
	 */
	@Test
	public void getProductListForQuery_GetFirst4Results() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		List<String> productIdList = new ArrayList<String>();
		int productCount = 5;
		int batchSize = 4;
		int currentLocation = 0;
		for(int x = 0; x < productCount; x++) {
			StringBuilder productId = new StringBuilder("PRODUCT");
			productId.append(x);
			StringBuilder mcSerialNumber = new StringBuilder("MCNUMBER");
			//productId.append(x);
			productIdList.add(productId.toString());
		}
		List<String> queryProductResults = kickoutService.getProductIdListForQuery(productIdList, batchSize, currentLocation);
		assertEquals(methodName + " failed: Expected product count : " + batchSize 
				+ " actual : " + queryProductResults.size() + ", the productIdCount was " + productIdList.size() 
				+ ".  ", queryProductResults.size(), batchSize);
		assertTrue("methodName" + " failed : Product : " + productIdList.get(0) + " was not returned. ", queryProductResults.contains(productIdList.get(0)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(1) + " was not returned. ", queryProductResults.contains(productIdList.get(1)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(2) + " was not returned. ", queryProductResults.contains(productIdList.get(2)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(3) + " was not returned. ", queryProductResults.contains(productIdList.get(3)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(4) + " was returned and should not have. ", !queryProductResults.contains(productIdList.get(4)));
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test getProductListForQuery to get List of product Ids to search for based on a batch size.
	 * Test getting the last 4 results of a List of 5 product Ids.
	 * 
	 */
	@Test
	public void getProductListForQuery_GetLast4Results() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		List<String> productIdList = new ArrayList<String>();
		int productCount = 5;
		int batchSize = 4;
		int currentLocation = 1;
		for(int x = 0; x < productCount; x++) {
			StringBuilder productId = new StringBuilder("PRODUCT");
			productId.append(x);
			StringBuilder mcSerialNumber = new StringBuilder("MCNUMBER");
			//productId.append(x);
			productIdList.add(productId.toString());
		}	
		List<String> queryProductResults = kickoutService.getProductIdListForQuery(productIdList, batchSize, currentLocation);
		assertEquals(methodName + " failed: Expected product count : " + batchSize 
				+ " actual : " + queryProductResults.size() + ", the productIdCount was " + productIdList.size() 
				+ ".  ", batchSize, queryProductResults.size());
		assertTrue("methodName" + " failed : Product : " + productIdList.get(1) + " was not returned. ", queryProductResults.contains(productIdList.get(1)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(2) + " was not returned. ", queryProductResults.contains(productIdList.get(2)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(3) + " was not returned. ", queryProductResults.contains(productIdList.get(3)));
		assertTrue("methodName" + " failed : Product : " + productIdList.get(0) + " was returned and should have. ", !queryProductResults.contains(productIdList.get(0)));
	}

	/**
	 * @author Bradley Brown
	 * @date Sep 13, 2019
	 * 
	 * Test validateProductsForKickout that verifies that the products are in the correct state to the kickedout.
	 * Test for when the Multiple Kickout flag is true and all products do not have a current active kickout.
	 * 
	 */
	@Test
	public void validateProductsForKickout_NoExistingKickout() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		DefaultDataContainer data = new DefaultDataContainer();
		int productCount = 5;
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(true);
		when(kickoutDaoMock.findProductsWithKickout(any(List.class))).thenReturn(new ArrayList<KickoutDto>());

		for(int x = 0; x < productCount; x++) {
			StringBuilder productId = new StringBuilder("PRODUCT");
			productId.append(x);
			StringBuilder mcSerialNumber = new StringBuilder("MCNUMBER");
			productId.append(x);
			productList.add(createBlockProduct(productId.toString(), mcSerialNumber.toString(), "BLOCK", "5GO", "BLOCKON"));
		}
		data.put(DataContainerTag.PRODUCT, productList);
		data.put(DataContainerTag.APPLICATION_ID, "AppId");
		DataContainer retData = kickoutService.validateProductsForKickout(data);
		String requestResult = (String) retData.get(DataContainerTag.REQUEST_RESULT);
		assertEquals(methodName + " failed : Expected REQUEST_RESULT = " + LineSideContainerValue.COMPLETE
				+ " actual = " + requestResult, LineSideContainerValue.COMPLETE, requestResult);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Sep 14, 2019
	 * 
	 * Test validateProductsForKickout that verifies that the products are in the correct state to the kickedout.
	 * Test for when the Multiple Kickout flag is true and products to have an existing kickout.
	 * 
	 */
	@Test
	public void validateProductsForKickout_MultipleKickoutTrue() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		DefaultDataContainer data = new DefaultDataContainer();
		int productCount = 5;
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		List<KickoutDto> existingKickoutProducts = new ArrayList<KickoutDto>();
		
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(true);
		when(kickoutDaoMock.findProductsWithKickout(any(List.class))).thenReturn(existingKickoutProducts);

		for(int x = 0; x < productCount; x++) {
			StringBuilder productId = new StringBuilder("PRODUCT");
			productId.append(x);
			StringBuilder mcSerialNumber = new StringBuilder("MCNUMBER");
			productId.append(x);
			productList.add(createBlockProduct(productId.toString(), mcSerialNumber.toString(), "BLOCK", "5GO", "BLOCKON"));
		}
		existingKickoutProducts.add(createKickoutDto(productList.get(2)));
		data.put(DataContainerTag.PRODUCT, productList);
		data.put(DataContainerTag.APPLICATION_ID, "AppId");
		DataContainer retData = kickoutService.validateProductsForKickout(data);
		String requestResult = (String) retData.get(DataContainerTag.REQUEST_RESULT);
		assertEquals(methodName + " failed : Expected REQUEST_RESULT = " + LineSideContainerValue.COMPLETE
				+ " actual = " + requestResult, LineSideContainerValue.COMPLETE, requestResult);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Sep 14, 2019
	 * 
	 * Test validateProductsForKickout that verifies that the products are in the correct state to the kickedout.
	 * Test for when the Multiple Kickout flag is false and all products do not have a current active kickout.
	 * 
	 */
	@Test
	public void validateProductsForKickout_NoExistingKickoutFlagFalse() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		DefaultDataContainer data = new DefaultDataContainer();
		int productCount = 5;
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		
		when(kickoutPropertyBeanMock.isMultipleKickout()).thenReturn(false);
		when(kickoutDaoMock.findProductsWithKickout(any(List.class))).thenReturn(new ArrayList<KickoutDto>());

		for(int x = 0; x < productCount; x++) {
			StringBuilder productId = new StringBuilder("PRODUCT");
			productId.append(x);
			StringBuilder mcSerialNumber = new StringBuilder("MCNUMBER");
			productId.append(x);
			productList.add(createBlockProduct(productId.toString(), mcSerialNumber.toString(), "BLOCK", "5GO", "BLOCKON"));
		}
		data.put(DataContainerTag.PRODUCT, productList);
		data.put(DataContainerTag.APPLICATION_ID, "AppId");
		DataContainer retData = kickoutService.validateProductsForKickout(data);
		String requestResult = (String) retData.get(DataContainerTag.REQUEST_RESULT);
		assertEquals(methodName + " failed : Expected REQUEST_RESULT = " + LineSideContainerValue.COMPLETE
				+ " actual = " + requestResult, LineSideContainerValue.COMPLETE, requestResult);
	}
	

	private BaseProduct createBlockProduct(String productId, String mcSerialNumber, String productType,
			String model, String trackingStatus) {
		Block product = new Block(productId);
		product.setMcSerialNumber(mcSerialNumber);
		product.setModelCode(model);
		product.setTrackingStatus(trackingStatus);
		return product;
	}
	
	private KickoutDto createKickoutDto(BaseProduct product) {
		KickoutDto kickoutDto = new KickoutDto();
		kickoutDto.setKickoutId(1L);
		kickoutDto.setProductId(product.getProductId());
		kickoutDto.setProductSpecCode(product.getProductSpecCode());
		kickoutDto.setLastPassingProcessPointId("LastPassId");
		kickoutDto.setLastPassingProcessPointName("LastPasName");
		kickoutDto.setDescription("Test Description");
		kickoutDto.setComment("Test Comment");
		kickoutDto.setKickoutStatus(KickoutStatus.OUTSTANDING.getId());
		kickoutDto.setProductType(ProductType.BLOCK.getProductName());
		
		return kickoutDto;
	}
}
