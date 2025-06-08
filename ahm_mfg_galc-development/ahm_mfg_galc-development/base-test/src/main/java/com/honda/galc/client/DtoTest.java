package com.honda.galc.client;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.dto.DtoFactory;
import com.honda.galc.dto.DtoUtil;
import com.honda.galc.dto.EngineNumberingDto;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;

public class DtoTest {
	
	@Before
	public void loadConfig() {
		
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		HttpServiceProvider.setUrl("http://localhost:9080/BaseWeb/HttpServiceHandler");
		
	}
	
	@Test
	public void test(){
		Map<String,Object> data1 = new HashMap<String,Object>();
		data1.put("PRODUCTION_LOT", "lot 1");
		data1.put("PRODUCT_SPEC_CODE", "FMZA");
		data1.put("LOT_SIZE", 30);
		EngineNumberingDto dto = DtoFactory.getDto(EngineNumberingDto.class, data1);
		Map<String,String> formats = new HashMap<String,String>();
		formats.put("PRODUCTION_LOT", "0,12");
		formats.put("SPEC", "12,15");
		
		String outputData = DtoUtil.output(EngineNumberingDto.class,dto, formats, 30);
		System.out.println("outdata = " + outputData);
	}
	
	@Test
	public void test1(){
		List<EngineNumberingDto> dtos = 
			ServiceFactory.getDao(EngineDao.class).findAllRecentStampedEngines("AE0EN11001", "AE0EN12001");
		
		for(EngineNumberingDto dto: dtos) {
			System.out.println(dto.getProductId());
		}
	}
	
	@Test
	public void test2(){
		Date date= new Date(System.currentTimeMillis());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Map<String,Object> data1 = new HashMap<String,Object>();
		data1.put("PRODUCTION_LOT", "lot 1");
		data1.put("INT", 20);
		data1.put("DOUBLE", 30.0);
		data1.put("DATE", date);
		data1.put("TIME", timestamp);
		
		TestDto testDto = DtoFactory.getDto(TestDto.class, data1);

		System.out.println("testString " + testDto.getTestString());
		System.out.println("testInteger " + testDto.getTestInteger());
		System.out.println("testDouble " + testDto.getTestDouble());
		System.out.println("testDate " + testDto.getTestDate());
		System.out.println("testTimestamp " + testDto.getTestTimestamp());
	}
		
}
