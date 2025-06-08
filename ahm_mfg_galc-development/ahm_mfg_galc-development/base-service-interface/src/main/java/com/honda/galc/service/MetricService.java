package com.honda.galc.service;

import com.honda.galc.dao.conf.MetricDao;
import com.honda.galc.dao.conf.MetricGroupDao;
import com.honda.galc.dao.conf.MetricGroupHistoryDao;
import com.honda.galc.dao.conf.MetricGroupMetricsDao;
import com.honda.galc.dao.conf.MetricGroupMetricsHistoryDao;
import com.honda.galc.dao.conf.MetricHistoryDao;
import com.honda.galc.dao.conf.MetricTypeDao;
import com.honda.galc.dao.conf.MetricTypeParamDao;

/**
 * @author Todd Roling
 */
public interface MetricService extends IService {
	public MetricDao getMetricDao();
	public MetricHistoryDao getMetricHistoryDao();
	public MetricGroupDao getMetricGroupDao();
	public MetricGroupHistoryDao getMetricGroupHistoryDao();
	public MetricGroupMetricsDao getMetricGroupMetricsDao();
	public MetricGroupMetricsHistoryDao getMetricGroupMetricsHistoryDao();
	public MetricTypeDao getMetricTypeDao();
	public MetricTypeParamDao getMetricTypeParamDao();
}
