package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationByTerminalId;
import com.honda.galc.service.IDaoService;

import java.util.List;


public interface ApplicationByTerminalDao extends IDaoService<ApplicationByTerminal, ApplicationByTerminalId> {

    public ApplicationByTerminal findById(String appByTermId, String hostName);

    public List<ApplicationByTerminal> findAllByTerminal(String hostName);

    public void removeById(String appByTermId, String hostName);
    
    public void removeById(String hostName);
    
    public ApplicationByTerminal findDefaultApplication(String hostName);
    
    public void updateTerminalApplication(String hostName, String applicationId);
}
