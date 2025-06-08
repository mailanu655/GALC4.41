package com.honda.galc.dao.jpa.conf;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.MCProcessAssignmentDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.MCProcessAssignment;
import com.honda.galc.entity.conf.MCProcessAssignmentId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author vfc01778
 * @date December 09, 2014
 */
public class MCProcessAssignmentDaoImpl extends BaseDaoImpl<MCProcessAssignment, MCProcessAssignmentId> implements MCProcessAssignmentDao {

	
	private final String VALIDATE_ASSIGNMENT = " SELECT * FROM MC_PROCESS_ASSIGNMENT_TBX WHERE ASSOCIATE_NO = ?1 AND PROCESS_POINT_ID =?2 AND PRODUCTION_DATE= ?3 AND SHIFT= ?4 " ; 
	
	@Autowired
	private GpcsDivisionDao gpcsDivisionDao;
	public boolean validateUserAssignment(String userid, String processPoint) {
		
		Parameters params = new Parameters();
		params.put("1", userid.trim());
		params.put("2", processPoint.trim());
		
		DailyDepartmentSchedule period = getProductiondate(processPoint);   	
		//convert to 12:00 AM format
		Calendar cal = Calendar.getInstance();    
		if(period != null)
		cal.setTime(period.getId().getProductionDate()); 	
		
		
		cal.set(Calendar.HOUR_OF_DAY, 0);          
		cal.set(Calendar.MINUTE, 0);                
		cal.set(Calendar.SECOND, 0);                 
		cal.set(Calendar.MILLISECOND, 0);           
		Date prodDate = cal.getTime(); 
		
		params.put("3", prodDate);
		if(period != null)
		params.put("4", period.getId().getShift());
		
		
		List<MCProcessAssignment> resultList = findAllByNativeQuery(VALIDATE_ASSIGNMENT, params);		
		return resultList.size() > 0;
	}
	
	private DailyDepartmentSchedule getProductiondate(String pp) {
		
		long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(pp);
		GpcsDivision gpcsDivision = gpcsDivisionDao.findByKey(processPoint
				.getDivisionId());

		DailyDepartmentSchedule currentPeriod = ServiceFactory.getDao(DailyDepartmentScheduleDao.class)
				.findByActualTime(gpcsDivision.getGpcsLineNo(),
						gpcsDivision.getGpcsProcessLocation(),
						gpcsDivision.getGpcsPlantCode(),
						new Timestamp(currentTimeInMillis));	
		
		return currentPeriod;	
		
	}
}
