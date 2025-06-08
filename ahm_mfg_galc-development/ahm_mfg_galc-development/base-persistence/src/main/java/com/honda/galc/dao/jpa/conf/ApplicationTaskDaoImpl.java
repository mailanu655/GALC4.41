package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.service.Parameters;


/**
 * 
 * <h3>ApplicationTaskDaoImpl Class description</h3>
 * <p> ApplicationTaskDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 29, 2010
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class ApplicationTaskDaoImpl extends BaseDaoImpl<ApplicationTask,String> implements ApplicationTaskDao {
	
	private final String FIND_HEADLESS_TASK_NAME = "SELECT T.TASK_NAME FROM galadm.gal243tbx T WHERE T.APPLICATION_ID =?1 AND SEQUENCE_NO =?2 FOR READ ONLY";
	
    @Autowired
    private TerminalDao terminalDao;

    public ApplicationTaskDaoImpl() {
    	super();
    }
    
    
    public List<ApplicationTask> findAllByProcessPointId(String processPointId) {
        
        Terminal terminal = terminalDao.findFirstByProcessPointId(processPointId);
        if(terminal == null) return new ArrayList<ApplicationTask>();
        
        return findAllByHostName(terminal.getHostName());
        
    }


    public List<ApplicationTask> findAllByHostName(String hostName) {
        
        return findAll(Parameters.with("id.hostName", hostName));
        
    }


	public String findHeadlessTaskName(String processPointId) {
		Parameters params = Parameters.with("1", processPointId);
		params.put("2", 1);
		return findFirstByNativeQuery(FIND_HEADLESS_TASK_NAME, params, String.class);
	}


	public String findApplicationId(String taskName) {
		ApplicationTask task = findFirst(Parameters.with("id.taskName", taskName));
		return task == null ? null : task.getId().getApplicationId();
	}

  
}
