package com.honda.galc.service.productattribute;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.ProductAttributeDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ProductAttribute;
import com.honda.galc.entity.product.ProductAttributeId;
import com.honda.galc.service.ProductAttributeService;
import com.honda.galc.service.ServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class, ProductAttributeDao.class}) 
public class ProductAttributeServiceImplTest {
	
	
	/**
	 * @author Ambica Gawarla
	 * @date Dec 18,2018
	 * Tests getProductAttribute method in ProductAttributeService.java
	 * Product And Attribute Match Found
	 */
	@Test
	public void testGetProductAttributeWhenProductAndAttributeMatchFound(){
		String productId = "5J8YD4H53KL013580";
		String attribute = "PA_RELINK";
		PowerMockito.mockStatic(ServiceFactory.class);  
		ProductAttributeDao productAttributeDaoMock =  PowerMockito.mock(ProductAttributeDao.class);			
		when(ServiceFactory.getDao(ProductAttributeDao.class)).thenReturn(productAttributeDaoMock);
		when( productAttributeDaoMock.findByKey((ProductAttributeId)Matchers.any())).thenReturn(getProductAttribute(productId, attribute));
		
		ProductAttributeService productAttributeService = new ProductAttributeServiceImpl();
		DataContainer dataContainer = productAttributeService.getProductAttributes(productId, attribute);
		
		assertEquals(0,((Integer)dataContainer.get(TagNames.ERROR_CODE)).intValue());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Dec 18,2018
	 * Tests getProductAttribute method in ProductAttributeService.java
	 * Product And Attribute Match Not Found
	 */
	@Test
	public void testGetProductAttributeWhenProductAndAttributeMatchNotFound(){
		String productId = "5J8YD4H53KL0135900";
		String attribute = "PA_RELINK";
		PowerMockito.mockStatic(ServiceFactory.class);  
		ProductAttributeDao productAttributeDaoMock =  PowerMockito.mock(ProductAttributeDao.class);			
		when(ServiceFactory.getDao(ProductAttributeDao.class)).thenReturn(productAttributeDaoMock);
		when( productAttributeDaoMock.findByKey((ProductAttributeId)Matchers.any())).thenReturn(null);
		
		ProductAttributeService productAttributeService = new ProductAttributeServiceImpl();
		DataContainer dataContainer = productAttributeService.getProductAttributes(productId, attribute);
		
		assertEquals(1,((Integer)dataContainer.get(TagNames.ERROR_CODE)).intValue());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Dec 18,2018
	 * Tests getProductAttribute method in ProductAttributeService.java
	 * on Exception
	 */
	@Test
	public void testGetProductAttributeOnError(){
		String productId = "5J8YD4H53KL013580";
		String attribute = "PA_RELINKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		PowerMockito.mockStatic(ServiceFactory.class);  
		ProductAttributeDao productAttributeDaoMock =  PowerMockito.mock(ProductAttributeDao.class);			
		when(ServiceFactory.getDao(ProductAttributeDao.class)).thenReturn(productAttributeDaoMock);
		when( productAttributeDaoMock.findByKey((ProductAttributeId)Matchers.any())).thenThrow(new NullPointerException());
		
		ProductAttributeService productAttributeService = new ProductAttributeServiceImpl();
		DataContainer dataContainer = productAttributeService.getProductAttributes(productId, attribute);
		
		assertEquals(2,((Integer)dataContainer.get(TagNames.ERROR_CODE)).intValue());
	}
	
	private ProductAttribute getProductAttribute(String productId, String attribute){
		ProductAttributeId productAttributeId = new ProductAttributeId();
		productAttributeId.setProductId(productId);
		productAttributeId.setAttribute(attribute);
		
		ProductAttribute productAttribute = new ProductAttribute();
		productAttribute.setId(productAttributeId);
		productAttribute.setAttributeValue("TRUE");
		
		return productAttribute;
	}

}
