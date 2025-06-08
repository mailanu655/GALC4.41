package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationByTerminalId;
import com.honda.galc.service.Parameters;


public class ApplicationByTerminalDaoImpl extends BaseDaoImpl<ApplicationByTerminal,ApplicationByTerminalId> implements ApplicationByTerminalDao {
    private static final long serialVersionUID = 1L;

    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public ApplicationByTerminal findById(String applicationId, String hostName) {
        return findByKey(new ApplicationByTerminalId(applicationId, hostName));
    }

    @Transactional
    public void removeById(String applicationId, String hostName) {

    	remove(findById(applicationId,hostName));
    	
    }
    
    // remove all application of terminal "hostname"
    @Transactional
    public void removeById(String hostName) {

    	delete(Parameters.with("id.hostName", hostName));
    	
    }

    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<ApplicationByTerminal> findAllByTerminal(String hostName) {
		
		return findAll(Parameters.with("id.hostName", hostName));
		
	}


    @Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public ApplicationByTerminal findDefaultApplication(String hostName) {
        
        return findFirst(Parameters.with("id.hostName", hostName)
                             .put("defaultApplicationFlag", (short)1));
        
    }

    @Override
	@Transactional
	public void updateTerminalApplication(String hostName, String applicationId) {
		// remove all application of terminal "hostname"
		removeById(hostName);
		
		//add application "applicationId" to terminal "hostName"
		ApplicationByTerminal applicationByTerminal = new ApplicationByTerminal(applicationId,hostName);
		applicationByTerminal.setDefaultApplicationFlag((short)1);
		save(applicationByTerminal);
	}



	
}
