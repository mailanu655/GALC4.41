package com.honda.ahm.lc.service;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.enums.GalcDataType;
import com.honda.ahm.lc.model.AuditEntry;
import com.honda.ahm.lc.util.PropertyUtil;

public abstract class BaseGalcService<E extends AuditEntry, K> {
	private static final Object REST_SERVIVE_PATH = "/RestWeb/";
	protected Class<E> entityClass;

	@Autowired
	private PropertyUtil propertyUtil;

	abstract protected Logger getLogger();

	@SuppressWarnings("unchecked")
	protected BaseGalcService() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
	}

	public E sendToExternalSystem(String url, GalcDataType galcDataType, E entity, String method) {
		try {
			return getRestTemplate().postForObject(getExternalSystemUrl(url, galcDataType.getDao(), method),
					createPayload(galcDataType.getGalcPackage(), entity), entityClass);
		} catch (Exception e) {
			getLogger().error("Error while sending data to external system: {}, {} , {}", url, galcDataType.getDao(),
					e.getMessage());
		}
		return null;

	}

	protected RestTemplate getRestTemplate() {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

		return restTemplate;
	}

	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {

	    SimpleClientHttpRequestFactory clientHttpRequestFactory  = new SimpleClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(propertyUtil.getConnectTimeout());
	    clientHttpRequestFactory.setReadTimeout(propertyUtil.getReadTimeout());
	    return clientHttpRequestFactory;
	}
	private Object createPayload(String galcPackage, Object entity) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(galcPackage, entity);

		return map;
	}

	protected String getExternalSystemUrl(String url, String daoString, String method) {
		StringBuffer sb = new StringBuffer(url);
		sb.append(REST_SERVIVE_PATH);
		sb.append(daoString).append("/").append(method);
		return sb.toString();
	}

	public E queryExternalSystem(String url, GalcDataType galcDataType, String objPackage, Object id, String method) {
		try {
			return getRestTemplate().postForObject(getExternalSystemUrl(url, galcDataType.getDao(), method),
					createPayload(objPackage, id), entityClass);
		} catch (Exception e) {
			getLogger().error("Error while sending data to external system: {}, {} , {}", url, galcDataType.getDao(),
					e.getMessage());
		}
		return null;

	}

	public void trackProduct(String galcUrl, String processPointId, String productId) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("com.honda.galc.data.ProductType", getProductType());
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("java.lang.String", productId);
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("java.lang.String", processPointId);

			jsonObjects.add(jsonObject1);
			jsonObjects.add(jsonObject2);
			jsonObjects.add(jsonObject3);

			getRestTemplate().postForLocation(
					getExternalSystemUrl(galcUrl, GalcDataType.TRACKING_SERVICE.getDao(), "track"),
					jsonObjects.toString());
			getLogger().info(" Tracking completed - VIN - ProcessPointId : ", productId +" - "+processPointId);
		} catch (Exception e) {
			getLogger().error("Error Tracking - VIN - ProcessPointId : ", productId +" - "+processPointId);
			getLogger().error(e.getMessage());
		}
	}

	protected E findByProductId(String galcUrl, String productId, GalcDataType galcDataType) {

		try {
			E entity = queryExternalSystem(galcUrl, galcDataType, "java.lang.String", productId, "findByKey");
			return entity;
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	protected E save(String galcUrl, E entity, GalcDataType galcDataType) {

		try {
			return sendToExternalSystem(galcUrl, galcDataType, entity, "save");

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	protected E deleteByProductId(String galcUrl, String productId, GalcDataType galcDataType) {
		try {
			return queryExternalSystem(galcUrl, galcDataType, "java.lang.String", productId, "removeByKey");
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		return null;
	}

	private String getGalcUrl(String productId, GalcDataType galcDataType, String[] urls) {

		String url = null;
		for (String tempUrl : urls) {
			E entity = queryExternalSystem(tempUrl, galcDataType, "java.lang.String", productId, "findByKey");
			if (entity != null) {
				url = tempUrl;
				break;
			}
		}
		return url;
	}

	public String getGalcUrl(String productId, String lineId) {
		lineId = (lineId== null || lineId.isEmpty())?"1":lineId;
		Integer lineNo = Integer.parseInt(lineId);
		String[] urls = propertyUtil.getGALCUrl().split(",");
		return urls.length >= lineNo ? urls[lineNo - 1] : getGalcUrl(productId, GalcDataType.FRAME, urls);
	}

	protected String getProductType() {
		return propertyUtil.getProductType();
	}

	public boolean isOK(String result) {
		return result.equalsIgnoreCase("OK");
	}
}
