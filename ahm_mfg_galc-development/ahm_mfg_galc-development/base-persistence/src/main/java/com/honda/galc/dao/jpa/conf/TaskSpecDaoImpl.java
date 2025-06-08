package com.honda.galc.dao.jpa.conf;


import java.util.List;


import com.honda.galc.dao.conf.TaskSpecDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.TaskSpec;
import com.honda.galc.service.Parameters;


public class TaskSpecDaoImpl extends BaseDaoImpl<TaskSpec,String> implements TaskSpecDao {

    private static String FINDALL= "select t from TaskSpec t order by t.taskName asc";
	public List<TaskSpec> findAllByProcessPointId(String processPointId) {

        return findAll(Parameters.with("processPointId", processPointId));
        
    }
    
    /**
     * find All and order by application task name
     */
    public List<TaskSpec> findAll() {
    	
    	return super.findAllByQuery(FINDALL);
    	
    }
     
     
    
 

}
