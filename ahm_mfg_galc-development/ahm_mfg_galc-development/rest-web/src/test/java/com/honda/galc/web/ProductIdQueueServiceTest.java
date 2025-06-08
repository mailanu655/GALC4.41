/**
 * 
 */
package com.honda.galc.web;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.ServiceFactory;

/**
 * @author VCC44349
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class})
public class ProductIdQueueServiceTest {

	ProductIdQueueService restService = null;
	ArrayList<String> prodSeqJson = new ArrayList<String>();
	UriInfo uriInfo;
	@Mock ProductSequenceDao mockDao;
	Timestamp now = null;
	ArrayList<ProductSequence> prodSeqList = new ArrayList<ProductSequence>();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(ServiceFactory.class);
		when(mockDao.findAll(anyString())).thenReturn(prodSeqList);
		when(mockDao.save(any(ProductSequence.class))).thenReturn(null);
		when(ServiceFactory.getDao(ProductSequenceDao.class)).thenReturn(mockDao);
		StringBuilder sb = new StringBuilder(); 
		sb.append("{\n").append("\"ProductSequenceDto\" : {")
			    .append("\"productId\" : \"1HGCV1612JA608412\",")
			    .append("\"stationId\" : \"RFID_009\",")
			    .append("\"referenceTimestamp\" : 1541428748628,")
			    .append("\"associateNo\" : \"RFID_DEV\",")
			    .append("\"sequenceNumber\" : 1,")
			    .append("\"sourceSystemId\" : \"1HGCV1612JA608412\",")
			    .append("\"productType\" : \"FRAME\"").append("\n}\n}");
		prodSeqJson.add(sb.toString());
		uriInfo = mock(UriInfo.class);
		when(uriInfo.getAbsolutePath()).thenReturn(new URI("http://localhost:8005/RestWeb/v2/ProductIdQueueService"));
		
		for(int i = 0; i < 2; i++)  {
			now = new Timestamp(System.currentTimeMillis());
			ProductSequenceId id = new ProductSequenceId("  ProductId_123456" + i, " RFID_009 ");
			ProductSequence prodSeq = new ProductSequence(id);
			prodSeq.setAssociateNo("  vcc012345" + i);
			prodSeq.setProductType("   FRAME");
			prodSeq.setReferenceTimestamp(now);
			prodSeq.setSequenceNumber(i);
			prodSeq.setSourceSystemId(" ssId_" + i);
			prodSeqList.add(prodSeq);			
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductIdQueueService#ProductIdQueueService()}.
	 */
	@Test
	public void testProductIdQueueService() {
		restService = new ProductIdQueueService();
		assertNotNull(restService);
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductIdQueueService#resourceMethodGET(java.lang.String)}.
	 */
	@Test
	public void testResourceMethodGet() {
		restService = new ProductIdQueueService();
		Response resp = restService.resourceMethodGet("RFID_009");
		assertEquals(200, resp.getStatus());
		List<ProductSequenceDto> dtoList = null;
		// Creates the json object which will manage the information received 
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
		   public Date deserialize(JsonElement json, java.lang.reflect.Type  typeOfT, JsonDeserializationContext context) throws JsonParseException {
		      return new Date(json.getAsJsonPrimitive().getAsLong()); 
		   }

		});

		Gson gson = builder.create();
		String json = (String)resp.getEntity();
		dtoList = gson.fromJson(json, new TypeToken<List<ProductSequenceDto>>(){}.getType());
		for (ProductSequenceDto dto : dtoList)  {
			dto.toString();
		}
		assertNotNull(dtoList);
		assertEquals(dtoList.size(), 2);
		assertEquals(prodSeqList.get(0), dtoList.get(0).createProductSequence());
		assertEquals(prodSeqList.get(1), dtoList.get(1).createProductSequence());
		assertEquals(prodSeqList.get(0).hashCode(), dtoList.get(0).createProductSequence().hashCode());
		assertEquals(prodSeqList.get(1).hashCode(), dtoList.get(1).createProductSequence().hashCode());
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductIdQueueService#resourceMethodPUT(java.lang.String, javax.ws.rs.core.UriInfo)}.
	 */
	@Test
	public void testResourceMethodPut() {
		restService = new ProductIdQueueService();
		Response resp = restService.resourceMethodPut(prodSeqJson.get(0), uriInfo);
		assertEquals(201, resp.getStatus());
		verify(mockDao).save(any(ProductSequence.class));
	}

	/**
	 * Test method for {@link com.honda.galc.web.ProductIdQueueService#resourceMethodDELETE(java.lang.String)}.
	 */
	@Test
	public void testResourceMethodDelete() {
		restService = new ProductIdQueueService();
		Response resp = restService.resourceMethodDelete((prodSeqJson.get(0)));
		assertEquals(204, resp.getStatus());
		verify(mockDao).removeByKey(any(ProductSequenceId.class));
	}
}
