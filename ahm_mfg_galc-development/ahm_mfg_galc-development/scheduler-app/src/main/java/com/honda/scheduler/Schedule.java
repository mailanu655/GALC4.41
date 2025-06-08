package com.honda.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobKey;

import com.honda.util.Property;
import com.honda.util.PropertyDao;

/**
 * Schedule : This class provides a convenient way to
 * access properties, essentially the equivalent to the EventSchedulePropertyBean 
 *  
 * @author      Suriya Sena
 * Date         3/17/2016
 */

public class Schedule  {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(Schedule.class.getName());	
	private PropertyDao propertyDao;
	private Map<String, String> propertyMap = new HashMap<String, String>();
	
	private final String COMPONENT_ID = "EVENT_SCHEDULE";
	private final String IBM_EVENT_LIST = "EVENT_LIST";         
	private final String QRTZ_EVENT_LIST = "EVENT_LIST_V2";   
	private final String EVENT_SCHEDULE_ENABLED = "EVENT_SCHEDULE_ENABLED";
	private final String EVENT_SCHEDULE = "EVENT_SCHEDULES{%s}";
	private final String EVENT_TASKS = "EVENT_TASKS{%s}";

	public void setPropertyDao(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	public boolean isEventServiceEnabled() {
		return (Boolean) getProperty(EVENT_SCHEDULE_ENABLED, Boolean.class,"false");
	}
	
	public String getSchedule(String eventName) {
		return (String) getProperty(String.format(EVENT_SCHEDULE,eventName), String.class,"");
	}
	
	public List<String> getEventList() {
		List<String> ibmEventList = getPropertiesAsList(IBM_EVENT_LIST);
		List<String> qrtzEventList = getPropertiesAsList(QRTZ_EVENT_LIST);
		List<String> normalizedQrtzEventList = new ArrayList<String>(); 
		
		for (String event:qrtzEventList) {
			if (ibmEventList.contains(event)) {
				log.error(String.format("The event %s has been removed since it is defined in both %s and %s and could potentially cause the jobs to run twice!!!",event, QRTZ_EVENT_LIST,IBM_EVENT_LIST));
			} else {
				normalizedQrtzEventList.add(event);
			}
		}
		return normalizedQrtzEventList;
	}


	public List<String> getTaskList(String eventName) {
		return getPropertiesAsList(String.format(EVENT_TASKS,eventName));
	}

	
	private List<String> getPropertiesAsList(String propertyName) {
		String events = (String) getProperty(propertyName, String.class,"");
		
		if (events.compareTo("")==0) {
			return new ArrayList<String>();
		} else {
		    return Arrays.asList(events.split(","));
		}
	}
	
	
	public HashMap<JobKey,String> getScheduleAsMap() {
		HashMap<JobKey,String> map =  new HashMap<JobKey,String>();
		List<String> eventList = getEventList();
		for (String group : eventList) {
			List<String> taskList = getTaskList(group);
			for (String name: taskList) {
				map.put(new JobKey(name,group),getSchedule(group));
			}
		}
		return map;
	}
	
	private Object getProperty(String key, Class<?> type, String defaultValue) {
		String value = propertyMap.get(key);
		value = (value != null ? value : defaultValue);

		if (type == Boolean.class) {
			return Boolean.valueOf(value);
		} else {
			return value;
		}
	}
	
	public void reload() {
        load();		
	}

	public void load() {
		List<Property> propertyList = propertyDao.getProperty(COMPONENT_ID);

		propertyMap.clear();
		for (Property p : propertyList) {
			propertyMap.put(p.getKey(), p.getValue());
		}
	}

}
