package com.honda.galc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.service.property.PropertyService;

public class LegacyUtil extends RestServiceUtil {
	private static final String NGLC_CHECKER_CLIENT_ID = "NGLC_CHECKER_CLIENT_ID";
	private static final String NGLC_CHECKER_URL = "NGLC_CHECKER_URL";
	private static final String PRODUCT_ID = "productID";
	private static final String RESULT = "result";
	private static final String REASON = "reason";
	private static final String OK = "Y";
	
	private static final String NGLC_CHECKER_METHOD = "NGLC_CHECKER_METHOD";
	private static final String NGLC_BASE_URL = "NGLC_BASE_URL";

	@Deprecated
	public List<String> executeCheck(String productId, String currentProcessPoint) {
		String clientId = PropertyService.getProperty(currentProcessPoint, NGLC_CHECKER_CLIENT_ID, null);
		if (!setDevice(clientId)) {
			List<String> checkResults = new ArrayList<String>();
			checkResults.add("Cannot find the Device info for " + clientId);
			return checkResults;
		}

		DataContainer data = buildDataContainer(productId);
		if (data == null) {
			List<String> checkResults = new ArrayList<String>();
			checkResults.add("Failed to build DataContainer for " + productId + " using Device " + clientId);
			return checkResults;
		}
	
		String json = DataContainerJSONUtil.convertToRawJSON(data);
		String url = PropertyService.getProperty(currentProcessPoint, NGLC_CHECKER_URL, null);
		DataContainer response = getRestRequest(json, url);

		return evaluateResponse(response);
	}
	
	public List<String> executeCheck(DataContainer data, String currentProcessPoint, String checkType) {

		Map<String, String> clientMap = PropertyService.getPropertyMap(currentProcessPoint, NGLC_CHECKER_CLIENT_ID);
		String clientId = clientMap.get(checkType);		
		Map<String, String> methodMap = PropertyService.getPropertyMap(currentProcessPoint, NGLC_CHECKER_METHOD);
		String legacyMethod = methodMap.get(checkType);		
		if (!setDevice(clientId)) {
			List<String> checkResults = new ArrayList<String>();
			checkResults.add("Cannot find the Device info for " + clientId);
			return checkResults;
		}
		
		String json = DataContainerJSONUtil.convertToRawJSON(makePopulatedDataContainer(data));
		String url = PropertyService.getProperty(currentProcessPoint, NGLC_BASE_URL, null).concat(legacyMethod);
		DataContainer response = null;
		try {
			response = getRestRequest(URLEncoder.encode(json,"UTF-8"), url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Logger.getLogger().error("Error generating the json "+e.getMessage());
		}

		return evaluateResponse(response);
	}
	
	private DataContainer buildDataContainer(String productId) {
		DataContainer data = new DefaultDataContainer();
		data.put(PRODUCT_ID, productId);
		return makePopulatedDataContainer(data);
	}

	private List<String> evaluateResponse(DataContainer response) {
		List<String> checkResults = new ArrayList<String>();
		if (response == null) {
			checkResults.add("Error contacting NGLC checker REST service.");
			return checkResults;
		}

		String result = response.get(RESULT).toString();
		if (result == null || !result.equals(OK)) {
			StringBuilder sb = new StringBuilder();
			//sb.append("Specified VIN has failed one or more NGLC checks.");
			checkResults.add(evaluateResponseReasons(response, sb));
		}
		return checkResults;
	}

	@SuppressWarnings("unchecked")
	private String evaluateResponseReasons(DataContainer response, StringBuilder sb) {
		if (response == null) return sb.toString();

		List<String> reasons = (List<String>) response.get(REASON);
		if (reasons == null || reasons.isEmpty()) return sb.toString();

		for (String reason : reasons)
			sb.append("\n" + reason.trim());
		return sb.toString();
	}
}
