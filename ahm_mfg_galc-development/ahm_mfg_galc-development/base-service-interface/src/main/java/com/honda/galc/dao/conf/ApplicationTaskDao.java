package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.ApplicationTask;
import com.honda.galc.service.IDaoService;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface ApplicationTaskDao extends IDaoService<ApplicationTask, String> {
    
    List<ApplicationTask> findAllByHostName(String hostName);
    
    List<ApplicationTask> findAllByProcessPointId(String processPointId);
    
    /**
     * Find task name for head less data collection
     * Only one task per headless process point. This will always retrieve the 1st task for headless process point
     * 
     * @param processPointId
     * @return
     */
    String findHeadlessTaskName(String processPointId);
    
    String findApplicationId(String taskName);
}
