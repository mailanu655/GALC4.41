package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/28/12 Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public enum AutoArchiveTime {

	NEVER(0), SECONDS_30(0.5), MINUTES_1(1), MINUTES_5(5), MINUTES_10(10), MINUTES_30(30), HOURS_1(60), HOURS_4(240),
	HOURS_8(480), HOURS_12(720), HOURS_24(1440), HOURS_48(2880);

	double timeInMin;

	AutoArchiveTime(double time) {
		this.timeInMin = time;
	}

	public double getTimeInMin() {
		return this.timeInMin;
	}

	public static AutoArchiveTime findByTime(double timeInMin) {
		AutoArchiveTime[] autoArchiveTimes = AutoArchiveTime.values();

		AutoArchiveTime requestedAutoArchiveTime = NEVER;
		for (AutoArchiveTime autoArchiveTime : autoArchiveTimes) {
			if (autoArchiveTime.getTimeInMin() == timeInMin) {
				requestedAutoArchiveTime = autoArchiveTime;
				break;
			}
		}
		return requestedAutoArchiveTime;
	}
}
