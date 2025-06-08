package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.service.property.PropertyService;

public class NglcChecker extends RestServiceChecker<BaseProductCheckerData> {

	private static final String NGLC_CHECKER_CLIENT_ID = "NGLC_CHECKER_CLIENT_ID";
	private static final String NGLC_CHECKER_URL = "NGLC_CHECKER_URL";
	private static final String PRODUCT_ID = "productID";
	private static final String RESULT = "result";
	private static final String REASON = "reason";
	private static final String OK = "Y";

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public CheckerType getType() {
		return CheckerType.Application;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public List<CheckResult> executeCheck(BaseProductCheckerData inputData) {
		String clientId = PropertyService.getProperty(inputData.getCurrentProcessPoint(), NGLC_CHECKER_CLIENT_ID, null);
		if (!setDevice(clientId)) {
			List<CheckResult> checkResults = new ArrayList<CheckResult>();
			checkResults.add(createCheckResult("Cannot find the Device info for " + clientId));
			return checkResults;
		}

		DataContainer data = buildDataContainer(inputData.getProductId());
		if (data == null) {
			List<CheckResult> checkResults = new ArrayList<CheckResult>();
			checkResults.add(createCheckResult("Failed to build DataContainer for " + inputData.getProductId() + " using Device " + clientId));
			return checkResults;
		}

		String json = DataContainerJSONUtil.convertToRawJSON(data);
		String url = PropertyService.getProperty(inputData.getCurrentProcessPoint(), NGLC_CHECKER_URL, null);
		DataContainer response = getRestRequest(json, url);

		return evaluateResponse(response);
	}

	private DataContainer buildDataContainer(String productId) {
		DataContainer data = new DefaultDataContainer();
		data.put(PRODUCT_ID, productId);
		return makePopulatedDataContainer(data);
	}

	private List<CheckResult> evaluateResponse(DataContainer response) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		if (response == null) {
			checkResults.add(createCheckResult("Error contacting NGLC checker REST service."));
			return checkResults;
		}

		String result = response.get(RESULT).toString();
		if (result == null || !result.equals(OK)) {
			StringBuilder sb = new StringBuilder();
			sb.append("Specified VIN has failed one or more NGLC checks.");
			checkResults.add(createCheckResult(evaluateResponseReasons(response, sb)));
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
