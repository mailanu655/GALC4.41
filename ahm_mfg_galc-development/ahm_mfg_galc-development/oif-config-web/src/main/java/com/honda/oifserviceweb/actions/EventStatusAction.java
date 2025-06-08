package com.honda.oifserviceweb.actions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.oif.sched.EventSchedulePropertyBean;
import com.honda.galc.oif.taskbean.EventStatistic;
import com.honda.galc.oif.values.LeaseValue;
import com.honda.galc.oif.web.actions.AbstractAction;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.service.property.PropertyService;
import com.ibm.websphere.scheduler.TaskStatus;

/**
 * @version 	1.0
 * @author
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class EventStatusAction extends AbstractAction
{
	private static final String GALDB_DS = "java:comp/env/jdbc/SchedulerDatasource";
	private static final String ATTR_LEASES = "leases";
	private static final String ALL_TASKS_PATTERN = "%";
	private static final String ATTR_EVENT_TASKS = "eventTasks";
	private static final String ATTR_EVENTS = "events";
	private static final String ATTR_SCHEDULER = "scheduler";
	private static PropertyHelper propertyHelper = new PropertyHelper("OIF_SYSTEM_PROPERTIES");
	private static final String SCHED_NAME = propertyHelper.getProperty("SCHED_NAME");
	private static final String SCHED_LEASE_INFO = "SELECT LEASEOWNER, LEASE_EXPIRE_TIME, DISABLED FROM " + 
			propertyHelper.getProperty("SCHED_TABLE_PREFIX") + "LMGR FOR READ ONLY";
	
	
	public static Map<Object, String> statusMap = new HashMap<Object, String>();
	
	static {
		statusMap.put(TaskStatus.CANCELLED, "CANCELLED");
		statusMap.put(TaskStatus.COMPLETE, "COMPLETE");
		statusMap.put(TaskStatus.INVALID, "INVALID");
		statusMap.put(TaskStatus.RUNNING, "RUNNING");
		statusMap.put(TaskStatus.SCHEDULED, "SCHEDULED");
		statusMap.put(TaskStatus.SUSPENDED, "SUSPENDED");
	}
		
    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		ActionErrors errors = new ActionErrors();
		ActionForward forward = mapping.findForward("result"); // return value

		try {
			
			javax.naming.InitialContext initialContext = new javax.naming.InitialContext();
			// TODO: Re-factor business logic into helper class(es)
			com.ibm.websphere.scheduler.Scheduler scheduler = (com.ibm.websphere.scheduler.Scheduler)
			  initialContext.lookup(SCHED_NAME);
			
			request.setAttribute(ATTR_SCHEDULER, SCHED_NAME);
			request.setAttribute(ATTR_EVENTS, scheduler.findTasksByName(ALL_TASKS_PATTERN));
			
			EventSchedulePropertyBean propBean = PropertyService.getPropertyBean(EventSchedulePropertyBean.class);
			
			request.setAttribute(ATTR_EVENT_TASKS, propBean.getEventTasks());
			
			List<LeaseValue> leases = getLeases(initialContext);
			
			request.setAttribute(ATTR_LEASES, leases);
			
			request.setAttribute("statusMap", statusMap);
			request.setAttribute("statistics", EventStatistic.getStatistics());
			
		} catch (Exception e) {

			// Report the error using the appropriate name and ID.
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", e));

		}

		// If a message is required, save the specified key(s)

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		// Finish with
		return (forward);
    }
    

	/**
	 * @param initialContext 
	 * @return
	 * @throws NamingException 
	 * @throws GALCSQLException
	 * @throws IllegalSQLRequestException
	 * @throws NamingException 
	 * @throws SQLException 
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	private List<LeaseValue> getLeases(InitialContext initialContext) throws NamingException, SQLException {
		List<LeaseValue> leases = new ArrayList<LeaseValue>();
		
		DataSource ds = (DataSource) initialContext.lookup(GALDB_DS);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SCHED_LEASE_INFO);
			
			while (rs.next()) {
				long expireLong = rs.getLong(2);
				Date expire = new Date(expireLong);
				LeaseValue value = new LeaseValue(rs.getString(1), expire, rs.getString(3));
				leases.add(value);
			}
			
			conn.commit();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// do nothing
				}
			}			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// do nothing
				}
			}			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// do nothing
				}
			}			
		}
		
		return leases;
	}
}
