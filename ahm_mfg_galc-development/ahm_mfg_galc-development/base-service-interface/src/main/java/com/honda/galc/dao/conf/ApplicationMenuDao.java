package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.ApplicationMenuEntryId;
import com.honda.galc.service.IDaoService;

import java.util.List;


public interface ApplicationMenuDao extends IDaoService<ApplicationMenuEntry, ApplicationMenuEntryId> {

    public ApplicationMenuEntry findById(String clientId, Integer nodeNumber);

    /**
     * get all application menu entries of a terminal. 
     * @return  the list is sorted by parent node number and current node number
     */
    public List<ApplicationMenuEntry> findAllByClientId(String clientId);

    public void removeById(String clientId, Integer nodeNumber);
}
