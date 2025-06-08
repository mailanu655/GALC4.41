/**
 * 
 */
package com.honda.galc.client.metrics.monitors;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.dao.conf.MetricGroupDao;
import com.honda.galc.dao.conf.MetricGroupTypeDao;
import com.honda.galc.entity.conf.Metric;
import com.honda.galc.entity.conf.MetricGroup;
import com.honda.galc.entity.conf.MetricGroupType;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author Subu Kathiresan
 * Aug 31, 2011
 * @param <S>
 */
public abstract class AbstractBaseMetricsMonitor<S extends MetricGroupType> extends Thread implements IMetricsMonitor  {

	private Hashtable<String, Metric> _metricMap = new Hashtable<String, Metric>();
	private Hashtable<String, MetricGroup> _metricGroupMap = new Hashtable<String, MetricGroup>();
	private BigInteger _scheduleId = new BigInteger("-1");
	private List<S> _groupTypes = new ArrayList<S>();
	
	private static volatile boolean _singleInstanceOnly = false;
	private volatile boolean _running = false;
	
	/**
	 * Adding the AnnotationProcessor in the abstract class allows us to
	 * enable all monitors for Event subscription 
	 */
	@SuppressWarnings("unchecked")
	protected AbstractBaseMetricsMonitor () {
		AnnotationProcessor.process(this);
		_running = true;
		try {
			_groupTypes = (List<S>) ServiceFactory.getDao(MetricGroupTypeDao.class).findAllByStrategy(this.getClass().getCanonicalName());	
		} catch (Exception ex) {
			getLogger().error("Could not retrieve Metric Group types for monitor " + this.getClass().getCanonicalName());
		}
		createMetricMaps();
	}
	
	/**
	 * creates a map containing all the metric/metric groups related to this metric group type
	 */
	private void createMetricMaps() {
		for(S groupType: getMetricGroupTypes()) {
			try {
				setMetricMaps(ServiceFactory.getDao(MetricGroupDao.class).findAllByMetricGroupTypeId(groupType.getMetricGroupTypeId()));
			} catch(Exception ex) {
				getLogger().warn("Could not add Metric/MetricGroups for MetricGroupType " + groupType.getMetricGroupTypeName());
			}
		}
		getLogger().info("Finished creating map of Metric/Metric Groups for " + this.getClass().getCanonicalName());
	}
	
	/**
	 * Create maps for the MetricGroup list provided
	 * 
	 * @param groups
	 * @return
	 */
	private void setMetricMaps(List<MetricGroup> groups) {
		// create a map of unique metric groups
		for(MetricGroup metricGroup: groups) {
			if (!getMetricGroupMap().containsKey(metricGroup.getMetricGroupName())) {
				getMetricGroupMap().put(metricGroup.getMetricGroupName(), metricGroup);
			}
		}
	}
	
	protected void setMetricGroupMap(Hashtable<String, MetricGroup> metricGroupMap) {
		_metricGroupMap = metricGroupMap;
	}

	protected Hashtable<String, MetricGroup> getMetricGroupMap() {
		return _metricGroupMap;
	}
	
	protected Hashtable<String, Metric> getMetricMap() {
		return _metricMap;
	}
	
	protected List<S> getMetricGroupTypes() {
		return _groupTypes;
	}
		
	public void setMetricGroupTypes(List<S> groupTypes) {
		_groupTypes = groupTypes;
	}
	
	public boolean isRunning() {
		return _running;
	}
	
	protected void setRunning(boolean flag) {
		_running = flag;
	}
	
	public static boolean isSingleInstanceOnly() {
		return _singleInstanceOnly;
	}
	
	protected static void setSingleInstanceOnly(boolean flag) {
		_singleInstanceOnly = flag;
	}

	private void setScheduleId(BigInteger scheduleId) {
		_scheduleId = scheduleId;
	}

	private BigInteger getScheduleId() {
		return _scheduleId;
	} 
	
	public abstract String getMonitorName();
	
	public abstract void setMonitorName(String monitorName);
	
	public abstract boolean activate(); 

	public abstract boolean deActivate();
}
