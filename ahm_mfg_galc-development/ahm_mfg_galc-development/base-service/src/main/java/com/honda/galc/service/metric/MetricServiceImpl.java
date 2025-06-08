package com.honda.galc.service.metric;

import com.honda.galc.dao.conf.MetricDao;
import com.honda.galc.dao.conf.MetricGroupDao;
import com.honda.galc.dao.conf.MetricGroupHistoryDao;
import com.honda.galc.dao.conf.MetricGroupMetricsDao;
import com.honda.galc.dao.conf.MetricGroupMetricsHistoryDao;
import com.honda.galc.dao.conf.MetricHistoryDao;
import com.honda.galc.dao.conf.MetricTypeDao;
import com.honda.galc.dao.conf.MetricTypeParamDao;
import com.honda.galc.service.MetricService;
import com.honda.galc.service.ServiceFactory;

public class MetricServiceImpl implements MetricService {
	private MetricDao metricDao;
	private MetricHistoryDao metricHistoryDao;
	private MetricGroupDao metricGroupDao;
	private MetricGroupHistoryDao metricGroupHistoryDao;
	private MetricGroupMetricsDao metricGroupMetricsDao;
	private MetricGroupMetricsHistoryDao metricGroupMetricsHistoryDao;
	private MetricTypeDao metricTypeDao;
	private MetricTypeParamDao metricTypeParamDao;

	public MetricDao getMetricDao() {
		if (metricDao == null)
			metricDao = ServiceFactory.getDao(MetricDao.class);
		return metricDao;
	}

	public MetricHistoryDao getMetricHistoryDao() {
		if (metricHistoryDao == null)
			metricHistoryDao = ServiceFactory.getDao(MetricHistoryDao.class);
		return metricHistoryDao;
	}

	public MetricGroupDao getMetricGroupDao() {
		if (metricGroupDao == null)
			metricGroupDao = ServiceFactory.getDao(MetricGroupDao.class);
		return metricGroupDao;
	}

	public MetricGroupHistoryDao getMetricGroupHistoryDao() {
		if (metricGroupHistoryDao == null)
			metricGroupHistoryDao = ServiceFactory
					.getDao(MetricGroupHistoryDao.class);
		return metricGroupHistoryDao;
	}

	public MetricGroupMetricsDao getMetricGroupMetricsDao() {
		if (metricGroupMetricsDao == null)
			metricGroupMetricsDao = ServiceFactory
					.getDao(MetricGroupMetricsDao.class);
		return metricGroupMetricsDao;
	}

	public MetricGroupMetricsHistoryDao getMetricGroupMetricsHistoryDao() {
		if (metricGroupMetricsHistoryDao == null)
			metricGroupMetricsHistoryDao = ServiceFactory
					.getDao(MetricGroupMetricsHistoryDao.class);
		return metricGroupMetricsHistoryDao;
	}

	public MetricTypeDao getMetricTypeDao() {
		if (metricTypeDao == null)
			metricTypeDao = ServiceFactory.getDao(MetricTypeDao.class);
		return metricTypeDao;
	}

	public MetricTypeParamDao getMetricTypeParamDao() {
		if (metricTypeParamDao == null)
			metricTypeParamDao = ServiceFactory
					.getDao(MetricTypeParamDao.class);
		return metricTypeParamDao;
	}
}
