package com.honda.galc.oif.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dto.PddaUnitOfOperation;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

/**
 * @author Suriya Sena
 * @date 18 Sept 2014
 */
public class PushTimeSchedulerTask extends OifAbstractTask implements IEventTaskExecutable {
	
	private static final int EXPIRY_HOURS=48;
	private static final int MAX_ADJUSTMENT_MINUTES=60;
	private static final int MIN_ADJUSTMENT_MINUTES=0;
	private static final int DEFAULT_PUSH_TIME=55;
	private static final String SHIFT="01";

	public PushTimeSchedulerTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		
       int pushTimeMinutes = getPushTimeMinutes();
       float minWorkcompletedMinutes = Float.MAX_VALUE;
       DailyDepartmentScheduleUtil dailyDepartmentSchedule=null;
       String lastProcessPointId = null;
       
       for (String processPointId : getPushTimerProcessPoints()) {
          minWorkcompletedMinutes = Math.min(calculateWorkCompleted(processPointId),minWorkcompletedMinutes);
          lastProcessPointId = processPointId;
       }
       
       int pushAdjustment = pushTimeMinutes - (int)minWorkcompletedMinutes;
       
       // All process points that use the PushTimerWidget use the same schedule
	   ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
	   ProcessPoint p = processPointDao.findById(lastProcessPointId);
	   p.setProcessPointId(lastProcessPointId);
       dailyDepartmentSchedule = new DailyDepartmentScheduleUtil(p);
       DailyDepartmentSchedule schedule = dailyDepartmentSchedule.getFirstWorkPeriodStartTime(SHIFT);
       Calendar startTime = new GregorianCalendar();
       startTime.setTime(schedule.getStartTime());
       
       Calendar today = new GregorianCalendar();
       today.set(Calendar.HOUR_OF_DAY,startTime.get(Calendar.HOUR_OF_DAY));
       today.set(Calendar.MINUTE,startTime.get(Calendar.MINUTE));
       today.set(Calendar.SECOND,startTime.get(Calendar.SECOND));
       today.set(Calendar.MILLISECOND,0);
       
       updateAdjustmentList(pushAdjustment,today.getTime());
	}

	
	public void updateAdjustmentList(int pushAdjustment, Date scheduleStartTime) {
		ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		ComponentPropertyId id = new ComponentPropertyId();
		id.setComponentId("DEFAULT_VIOS");
		id.setPropertyKey("PUSH_TIMER_ADJUSTMENT");
		
		ComponentProperty componentProperty = componentPropertyDao.findByKey(id);
		
	    SortedMap<Date, Integer> adjustments = parseAdjustmentList(componentProperty.getPropertyValue());
	       	    
		adjustments.put(scheduleStartTime, pushAdjustment);
		   
		String adjustmentList = formatAdjustmentList(adjustments);
		   
		componentProperty.setPropertyValue(adjustmentList);
		
		componentPropertyDao.save(componentProperty);
	}
	
	
	
	private SortedMap<Date, Integer> parseAdjustmentList(String adjustmentList) {
		SortedMap<Date,Integer>  adjustments = new TreeMap<Date, Integer>(dateComparator);
		
		String dateFormat = "MM/d/yy HH:mm";
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);

		for (String adj : adjustmentList.split(",")) {
			adj = adj.trim();
			try {
				Date start = df.parse(adj);
				int mins = Integer.parseInt(adj.substring(dateFormat.length() + 1).trim());
				if (mins > MIN_ADJUSTMENT_MINUTES && mins < MAX_ADJUSTMENT_MINUTES) {
					adjustments.put(start, mins);
				} else {
					logger.debug(String.format("Adjustment out of range! Value ignored '%s' list='%s'",adj,adjustmentList));
				}
			} catch (ParseException e) {
				logger.debug(String.format("Adjustment out of range! Value ignored '%s' list='%s'",adj,adjustmentList));
			}
		}
		return adjustments;
	}
	
	
	public String formatAdjustmentList( SortedMap<Date, Integer> adjustments) {
		Calendar cutoffDate = new GregorianCalendar();
		cutoffDate.add(Calendar.HOUR, EXPIRY_HOURS * (-1));
		
		StringBuilder sb = new StringBuilder();
		
		for (Date date : adjustments.keySet()) {
			if (date.after(cutoffDate.getTime())) {
				int minutes = adjustments.get(date);
			    String adjustment = String.format("%tD %1$tR %02d",date.getTime(),minutes);				
				sb.append(adjustment);
				sb.append(",");
			}
		}
		
		return sb.toString();
	}

	
	private static Comparator<Date> dateComparator = new Comparator<Date>() {
	    public int compare( Date date1, Date date2) {
	    	return  date1.compareTo(date2);
	    }
	};

	
	private int getPushTimeMinutes() {
		int pushTimeMinutes = DEFAULT_PUSH_TIME;
		
		ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		
		ComponentPropertyId id = new ComponentPropertyId();
		id.setComponentId("DEFAULT_VIOS");
		id.setPropertyKey("PUSH_TIME_MINS");
		
		ComponentProperty componentProperty = componentPropertyDao.findByKey(id);
		
		if (componentProperty != null) {
		  try {
		     pushTimeMinutes = Integer.parseInt(componentProperty.getPropertyValue());
		  } catch (NumberFormatException ex) {
		  }
		}
		
		return pushTimeMinutes;
	}
	
	private List<String> getPushTimerProcessPoints() {
		ArrayList<String> processPointList =  new ArrayList<String> ();
		
		ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
	    List<ComponentProperty>  componentPropertyList = componentPropertyDao.findAllByPropertyKey("WIDGETS");
	    		
	    for (ComponentProperty componentProperty : componentPropertyList)  {
	    	if (componentProperty.getPropertyValue().indexOf("PUSH_TIMER_WIDGET") >= 0) {
	    		processPointList.add(componentProperty.getId().getComponentId());
	    	}
	    }
	    return processPointList;
	}
	
	
	
	private float calculateWorkCompleted(String processPointId) {
		float workCompleted = 0.0f;
		String productId;
		
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		InstalledPart installedPart = installedPartDao.findLatestInstalledByProcessPoint(processPointId);

		if (installedPart != null) {
			productId = installedPart.getProductId();

			MCStructureDao mcStructureDao = ServiceFactory.getDao(MCStructureDao.class);
			List<PddaUnitOfOperation> unitOfOperationList = mcStructureDao.getAllOperationsForProcessPointAndProduct(productId,processPointId);

			Collections.sort(unitOfOperationList, pddaUnitOfOperationComparator);

			List<InstalledPart> installedPartList = installedPartDao.findAllByProductIdAndProcessPoint(productId,processPointId);

			for (InstalledPart part : installedPartList) {
				if (part.isStatusOk()) {
					PddaUnitOfOperation key = new PddaUnitOfOperation();
					key.setOperationName(part.getPartName());
					int index = Collections.binarySearch(unitOfOperationList,key, pddaUnitOfOperationComparator);
					if (index >= 0) {
						PddaUnitOfOperation unit = unitOfOperationList.get(index);
						workCompleted += unit.getUnitTotalTime();
					}
				}
			}
		}
		
		return workCompleted;
	}


	private static Comparator<PddaUnitOfOperation> pddaUnitOfOperationComparator = new Comparator<PddaUnitOfOperation>() {
	    public int compare( PddaUnitOfOperation operation1, PddaUnitOfOperation operation2) {
	    	return operation1.getOperationName().trim().compareTo(operation2.getOperationName().trim());
	    }
	};
}
