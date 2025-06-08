package com.honda.galc.client;

import java.util.Date;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.PushTimerStatusDao;
import com.honda.galc.entity.conf.PushTimerStatus;
import com.honda.galc.entity.conf.PushTimerStatusId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Aug 4, 2015
 */
public class PushTimeMonitor {

	public int opsPlanned;
	public int opsCompleted;
	public int statusUpdateInterval = -1;
	public long lastStatusUpdate = 0;

	public PushTimeMonitor() {}

	public void updateStatus(final String productId, final long progress) {
		Thread worker = new Thread() {
			public void run() {
				long now = new Date().getTime();
				if ((now - lastStatusUpdate) > getStatusUpdateInterval()) {
					lastStatusUpdate = now;
					try {
						PushTimerStatusId id = new PushTimerStatusId(getAppContext().getApplicationId(), getAppContext().getTerminalId());
						PushTimerStatus status = new PushTimerStatus(id);
						status.setProductId(productId);
						status.setAssociateNo(getAppContext().getUserId());
						status.setLastUpdated(new Date());
						status.setOpsPlanned(opsPlanned);
						status.setOpsCompleted(opsCompleted);
						status.setStatusInSecs(progress);

						getPushTimerStatusDao().save(status);

						getLogger().info("Updated push timer status: " + status.toString());
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		
		worker.setDaemon(false);
		worker.start();
	}

	private long getStatusUpdateInterval() {
		if (statusUpdateInterval == -1) {
			try {
				statusUpdateInterval = PropertyService.getPropertyBean(SystemPropertyBean.class).getPushTimerStatusUpdateInterval();
			} catch(Exception ex) {
				ex.printStackTrace();
				statusUpdateInterval = 30000;  // use 30 seconds as the default
				getLogger().error(ex, String.format("Using default 30000 milliseconds for Push Timer Status update interval."));
			}
		}
		return statusUpdateInterval;
	}

	public int getOpsPlanned() {
		return opsPlanned;
	}

	public void setOpsPlanned(int opsPlanned) {
		this.opsPlanned = opsPlanned;
	}

	public int getOpsCompleted() {
		return opsCompleted;
	}

	public void setOpsCompleted(int opsCompleted) {
		this.opsCompleted = opsCompleted;
	}

	public long getLastStatusUpdate() {
		return lastStatusUpdate;
	}

	public void setLastStatusUpdate(long lastStatusUpdate) {
		this.lastStatusUpdate = lastStatusUpdate;
	}

	public void setStatusUpdateInterval(int statusUpdateInterval) {
		this.statusUpdateInterval = statusUpdateInterval;
	}

	private PushTimerStatusDao getPushTimerStatusDao() {
		return ServiceFactory.getDao(PushTimerStatusDao.class);
	}

	private ApplicationContext getAppContext() {
		return ClientMainFx.getInstance().getApplicationContext();
	}
	
	private Logger getLogger() {
		Logger logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.getLogContext().setApplicationInfoNeeded(true);
		return logger;
	}
}
