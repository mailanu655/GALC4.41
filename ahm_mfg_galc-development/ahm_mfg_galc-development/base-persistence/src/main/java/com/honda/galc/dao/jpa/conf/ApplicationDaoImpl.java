package com.honda.galc.dao.jpa.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.service.Parameters;


/**
 * 
 * <h3>ApplicationDaoImpl Class description</h3>
 * <p> ApplicationDaoImpl description </p>
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
 * Jan 31, 2010
 *
 */

public class ApplicationDaoImpl extends BaseDaoImpl<Application,String> implements ApplicationDao {
    
	private static final String FIND_ALL_BY_APP_TYPES = "select e from Application e where e.applicationTypeId in (:applicationTypeId) order by e.applicationId";
	private static final String FIND_ALL_BY_APP_TYPES_AND_CONTAIN = "select e from Application e where e.applicationTypeId in (:applicationTypeId) and " +
																	"(UPPER(e.applicationId) LIKE :applicationFilter OR UPPER(e.applicationName) LIKE :applicationFilter OR "+
																	"UPPER(e.applicationDescription) LIKE :applicationFilter OR UPPER(e.screenId) LIKE :applicationFilter) "+
																	"order by e.applicationId";
	
    @Autowired
    private TerminalDao terminalDao;
    
    @Autowired
    private ApplicationByTerminalDao appByTerminalDao;
    
    public ApplicationDaoImpl() {
    	super();
    }

    public List<Application> findAllByApplicationTypeId(int type) {
        return findAll(Parameters.with("applicationTypeId", type),new String[] {"applicationId"},true);
    }
    
    public List<Application> findAllByApplicationType(ApplicationType type) {
		return this.findAllByApplicationTypeId(type.getId());
	}
    
    public List<Application> findAllByApplicationTypes(List<ApplicationType> types) {
 		if (types == null || types.isEmpty()) {
 			return new ArrayList<Application>();
 		}
 		List<Integer> typeIds =  ApplicationType.getApplicationTypeIds(types); 
        return findAllByQuery(FIND_ALL_BY_APP_TYPES, Parameters.with("applicationTypeId", typeIds));
    }
    
    public List<Application> findAllByApplicationTypes(List<ApplicationType> types, String filter) {
 		List<Integer> typeIds =  ApplicationType.getApplicationTypeIds(types);
 		Parameters parameters = Parameters.with("applicationTypeId", typeIds);
 		parameters.put("applicationFilter", "%"+filter.toUpperCase()+"%");
        return findAllByQuery(FIND_ALL_BY_APP_TYPES_AND_CONTAIN, parameters);
    }
    
    public Application findAppByProcessPointId(String processPointId) {
        
        Terminal terminal = terminalDao.findFirstByProcessPointId(processPointId);
        if(terminal == null) return null;
        
        ApplicationByTerminal appByTerminal = appByTerminalDao.findDefaultApplication(terminal.getHostName());
        
        if(appByTerminal == null) return null;
        
        return findByKey(appByTerminal.getId().getApplicationId());   
    }

    public List<Application> findAllByApplicationTypeId(int applicationTypeId, String textSearch) {
		Parameters parameters = Parameters.with("applicationTypeId", applicationTypeId);
 		parameters.put("applicationFilter", "%" + textSearch.toUpperCase() + "%");
        return findAllByQuery(FIND_ALL_BY_APP_TYPES_AND_CONTAIN, parameters);
    }

}
