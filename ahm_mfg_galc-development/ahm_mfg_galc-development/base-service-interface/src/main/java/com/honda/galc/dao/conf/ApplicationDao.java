package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.service.IDaoService;

import java.util.List;


public interface ApplicationDao extends IDaoService<Application, String> {

    public List<Application> findAllByApplicationTypeId(int type);
    
    public List<Application> findAllByApplicationType(ApplicationType type);
    
    public List<Application> findAllByApplicationTypes(List<ApplicationType> types);
    
    public List<Application> findAllByApplicationTypes(List<ApplicationType> types, String applicationName);
    
    public Application findAppByProcessPointId(String processPointId);
    
    public List<Application> findAllByApplicationTypeId(int applicationTypeId, String textSearch);
}
