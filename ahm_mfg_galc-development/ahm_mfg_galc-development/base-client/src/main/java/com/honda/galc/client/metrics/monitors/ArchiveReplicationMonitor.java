/**
 * 
 */
package com.honda.galc.client.metrics.monitors;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.entity.conf.MetricGroupType;

/**
 * @author Subu Kathiresan
 * Sep 12, 2011
 */
public class ArchiveReplicationMonitor<S extends MetricGroupType> extends AbstractBaseMetricsMonitor<S> {

	private String _monitorName = "ArchiveReplicationMonitor";
	
	@Override
	public boolean activate() {
		return false;
	}

	@Override
	public boolean deActivate() {
		return false;
	}

	@Override
	public String getMonitorName() {
		return _monitorName;
	}

	@Override
	public void setMonitorName(String monitorName) {
		_monitorName = monitorName;
	}
	
	@Override
	public void run() {
		getLogger().info("Running ArchiveReplicationMonitor");
	}
}