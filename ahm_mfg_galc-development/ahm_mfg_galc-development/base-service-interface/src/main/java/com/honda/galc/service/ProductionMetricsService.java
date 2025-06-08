package com.honda.galc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductionMetricsService extends IService {

	public List<Map<String, String>> getProductionMetrics(String productType, String trackingtPpt);

}
