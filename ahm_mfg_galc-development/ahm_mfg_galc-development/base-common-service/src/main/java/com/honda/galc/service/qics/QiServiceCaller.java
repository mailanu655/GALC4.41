package com.honda.galc.service.qics;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.dto.rest.RepairDefectDto;
import com.honda.galc.property.QiDefectServicePropertyBean;
import com.honda.galc.service.property.PropertyService;

public class QiServiceCaller {

	public static void sendDefectsToQicsService(List<QiCreateDefectDto> qiDefects)  {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
			QiDefectServicePropertyBean pb = PropertyService.getPropertyBean(QiDefectServicePropertyBean.class);
			String putServiceUrl = pb.getQiDefectServicePut();
			if (!StringUtils.isBlank(putServiceUrl)) {
				for(QiCreateDefectDto dto : qiDefects)  {
					String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);			
					HttpClient httpClient = HttpClientBuilder.create().build();
					HttpPut request = new HttpPut(putServiceUrl);
				    StringEntity params = new StringEntity(json);
				    request.addHeader("content-type", "application/json");
				    request.setEntity(params);
				    
				    HttpResponse response = httpClient.execute(request);
				    String reason = response.getStatusLine().getReasonPhrase();
				    Logger.getLogger().info("The rest service call to QiDefectService/Put is " + reason);
				}
			}
		} catch (ClientProtocolException cpe) {
			Logger.getLogger().error(cpe, "ClientProtocolException occurred");
		} catch (IOException ioe) {
			Logger.getLogger().error(ioe, "IOException occurred");
		}		
		
	}
	
	public static void sendRepairsToQicsService(List<QiRepairDefectDto> qiRepairs)  {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);		
			QiDefectServicePropertyBean pb = PropertyService.getPropertyBean(QiDefectServicePropertyBean.class);
			String repairServiceUrl = pb.getQiDefectServiceRepair();
			if (!StringUtils.isBlank(repairServiceUrl)) {
				for(QiRepairDefectDto dto : qiRepairs)  {
					String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);			
					HttpClient httpClient = HttpClientBuilder.create().build();
					HttpPut request = new HttpPut(repairServiceUrl);
				    StringEntity params = new StringEntity(json);
				    request.addHeader("content-type", "application/json");
				    request.setEntity(params);
				    
				    HttpResponse response = httpClient.execute(request);
				    String reason = response.getStatusLine().getReasonPhrase();
				    Logger.getLogger().info("The rest service call to QiDefectService/Repair is " + reason);
				}
			}
		} catch (ClientProtocolException cpe) {
			Logger.getLogger().error(cpe, "ClientProtocolException occurred");
		} catch (IOException ioe) {
			Logger.getLogger().error(ioe, "IOException occurred");
		}
	}
		
	public static void sendRepairToLotControl(RepairDefectDto repair)  {
		if (repair == null)  return;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(repair);			
			HttpClient httpClient = HttpClientBuilder.create().build();
			QiDefectServicePropertyBean pb = PropertyService.getPropertyBean(QiDefectServicePropertyBean.class);
			String repairServiceUrl = pb.getLotControlServiceRepair();
			if (!StringUtils.isBlank(repairServiceUrl)) {
				HttpPut request = new HttpPut(repairServiceUrl);
			    StringEntity params = new StringEntity(json);
			    request.addHeader("content-type", "application/json");
			    request.setEntity(params);
			    
			    HttpResponse response = httpClient.execute(request);
			    String reason = response.getStatusLine().getReasonPhrase();
			    Logger.getLogger().info("The rest service call to QiDefectService/Repair is " + reason);
			}
		} catch (ClientProtocolException cpe) {
			Logger.getLogger().error(cpe, "ClientProtocolException occurred");
		} catch (IOException ioe) {
			Logger.getLogger().error(ioe, "IOException occurred");
		}		
	}
	
	public static void sendDeleteToQicsService(String extSysName, Long extSysKey) {
		try {
			QiDefectServicePropertyBean pb = PropertyService.getPropertyBean(QiDefectServicePropertyBean.class);
			String deleteServiceUrl = pb.getQiDefectServiceDelete();
			if (!StringUtils.isBlank(deleteServiceUrl)) {
				HttpClient httpClient = HttpClientBuilder.create().build(); 
				URIBuilder builder = new URIBuilder(deleteServiceUrl);
				builder.setParameter("extSysName", extSysName).setParameter("extSysKey", extSysKey.toString());
				HttpDelete request = new HttpDelete(builder.build());
				request.addHeader("content-type", "text/html");
				
				HttpResponse response = httpClient.execute(request);
				String reason = response.getStatusLine().getReasonPhrase();
				Logger.getLogger().info("The rest service call to QiDefectService/Delete is " + reason);
			}
		} catch (ClientProtocolException cpe) {
			Logger.getLogger().error(cpe, "ClientProtocolException occurred");
		} catch (IOException ioe) {
			Logger.getLogger().error(ioe, "IOException occurred");
		} catch (URISyntaxException ue) {
			Logger.getLogger().error(ue, "URISyntaxException occurred");
		}		
	}
}
