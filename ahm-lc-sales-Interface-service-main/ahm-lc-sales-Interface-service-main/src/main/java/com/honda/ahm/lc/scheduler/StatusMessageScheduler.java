package com.honda.ahm.lc.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honda.ahm.lc.task.ReceivingTransactionTask;
import com.honda.ahm.lc.util.PropertyUtil;

@Component
public class StatusMessageScheduler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ReceivingTransactionTask receivingTransactionTask;
	
	@Autowired
	PropertyUtil propertyUtil;

	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private boolean isEnabled = false;

	@Scheduled(cron = "${status.scheduledTasks.cron.expression}")
	public void cronJobSch() {

		Date now = new Date();
		String strDate = sdf.format(now);

		logger.info("status.scheduledTasks.cron.expression executed at {}", strDate);

		isEnabled = propertyUtil.statusJobEnable();


		if (isEnabled) {
			receivingTransactionTask.execute();
		} else {
			logger.info("Scheduled Jobs Not Enabled - Property JOBS_ENABLED = " + isEnabled);
		}

	}
}
