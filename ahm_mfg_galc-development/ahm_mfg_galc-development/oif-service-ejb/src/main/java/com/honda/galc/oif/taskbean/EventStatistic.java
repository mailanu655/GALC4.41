package com.honda.galc.oif.taskbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>EventStatistics</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Feb 17, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class EventStatistic {

	private static Map<String, EventStatistic> statistics;
	private static Map<String, List<EventStatistic>> statisticsHistory;
	private static int statisticCapacity = 10;

	static {
		statistics = new HashMap<String, EventStatistic>();
		statisticsHistory = new HashMap<String, List<EventStatistic>>();
	}

	private String eventName;
	private long startExecutionTime;
	private long endExecutionTime;

	// === static api === //
	public static Map<String, EventStatistic> getStatistics() {
		return statistics;
	}

	public static EventStatistic getStatistic(String eventName) {
		return getStatistics().get(eventName);
	}

	public static Map<String, List<EventStatistic>> getStatisticsHistory() {
		return statisticsHistory;
	}

	public static EventStatistic registerStatistic(String eventName, long startExecutionTime, long endExecutionTime) {
		if (eventName == null) {
			return null;
		}
		EventStatistic statistic = createEventStatistic(eventName, startExecutionTime, endExecutionTime);
		registerStatistic(statistic);
		return statistic;
	}

	public static Long getAvgExecutionTime(String eventName) {
		Long time = 0L;
		List<EventStatistic> eventStatistic = statisticsHistory.get(eventName);
		if (eventStatistic == null || eventStatistic.size() == 0) {
			return time;
		}
		Long totalTime = 0L;
		for (EventStatistic stat : eventStatistic) {
			totalTime = totalTime + stat.getExecutionTime();
		}

		time = totalTime / eventStatistic.size();
		return time;
	}

	public static List<Long> getExecutionTimes(String eventName) {
		List<Long> times = new ArrayList<Long>();
		List<EventStatistic> eventStatistic = statisticsHistory.get(eventName);
		if (eventStatistic == null || eventStatistic.size() == 0) {
			return times;
		}
		for (EventStatistic stat : eventStatistic) {
			times.add(stat.getExecutionTime());
		}
		return times;
	}

	private static void registerStatistic(EventStatistic statistic) {

		if (statistic == null || statistic.getEventName() == null) {
			return;
		}

		statistics.put(statistic.getEventName(), statistic);

		List<EventStatistic> eventStatistics = statisticsHistory.get(statistic.getEventName());

		if (eventStatistics == null) {
			eventStatistics = new ArrayList<EventStatistic>(statisticCapacity);
			statisticsHistory.put(statistic.getEventName(), eventStatistics);
		}

		if (eventStatistics.size() > statisticCapacity - 1) {
			eventStatistics.remove(0);
		}
		eventStatistics.add(statistic);
	}

	private static EventStatistic createEventStatistic(String eventName, long startExecutionTime, long endExecutionTime) {
		EventStatistic statistic = new EventStatistic();
		statistic.setEventName(eventName);
		statistic.setStartExecutionTime(startExecutionTime);
		statistic.setEndExecutionTime(endExecutionTime);
		return statistic;
	}

	// === get/set === //
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public long getEndExecutionTime() {
		return endExecutionTime;
	}

	public void setEndExecutionTime(long endExecutionTime) {
		this.endExecutionTime = endExecutionTime;
	}

	public long getStartExecutionTime() {
		return startExecutionTime;
	}

	public void setStartExecutionTime(long startExecutionTime) {
		this.startExecutionTime = startExecutionTime;
	}

	// === calc api === //
	public Long getExecutionTime() {
		return getEndExecutionTime() - getStartExecutionTime();
	}

	public Long getAvgExecutionTime() {
		return getAvgExecutionTime(getEventName());
	}

	public List<Long> getExecutionTimes() {
		return getExecutionTimes(getEventName());
	}
}
