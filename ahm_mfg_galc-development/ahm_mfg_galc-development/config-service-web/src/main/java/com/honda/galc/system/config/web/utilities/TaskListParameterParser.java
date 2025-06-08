
package com.honda.galc.system.config.web.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.honda.galc.common.exception.ConfigurationServicesException;
import com.honda.galc.entity.conf.ApplicationTask;


/**
 * <h3></h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Feb 13, 2007</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * </TR>
 * </TABLE>
 */
public class TaskListParameterParser {

    public TaskListParameterParser() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public List<ApplicationTask> buildTaskByApplicationList(String applicationID, HttpServletRequest request)
        throws ConfigurationServicesException 
    {
        List<ApplicationTask> result= new ArrayList<ApplicationTask>(10);
        
        int idx = 0;
        
        do {
          idx++;
          
          String inputEventFlagParam = request.getParameter("inputFlag"+idx);
          String beginFlagParam = request.getParameter("beginFlag"+idx);
          String commitFlagParam = request.getParameter("commitFlag"+idx);
          String taskNameParam = request.getParameter("taskName"+idx);
          String taskArgsParam = request.getParameter("taskArgs"+idx);
          String jndiNameParam = request.getParameter("jndiName"+idx);
          String statefulFlagParam = request.getParameter("statefulFlag"+idx);
          
          if (taskNameParam == null || taskNameParam.length() == 0)
          {
              break;
          }
          
          ApplicationTask data = new ApplicationTask(applicationID,taskNameParam);
          
          data.setArgument(taskArgsParam);
          data.setBeginFlag((short)(Boolean.valueOf(beginFlagParam) ? 1: 0));
          data.setCommitFlag((short)(Boolean.valueOf(commitFlagParam)? 1:0));
          data.setInputEventExistFlag((short)(Boolean.valueOf(inputEventFlagParam)? 1:0));
          data.setSequenceNo(idx);
          data.setJndiName(jndiNameParam);
          data.setStatefullSessionBeanFlag((short)(Boolean.valueOf(statefulFlagParam)? 1: 0));
          
          result.add(data);
        } while (true);
        
        return result;
    }

}
