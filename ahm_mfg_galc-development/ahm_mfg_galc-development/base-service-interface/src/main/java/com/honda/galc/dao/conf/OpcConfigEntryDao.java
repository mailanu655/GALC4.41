package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.service.IDaoService;


public interface OpcConfigEntryDao extends IDaoService<OpcConfigEntry, Long> {

    public List<OpcConfigEntry> findAllByOpcInstance(String opcInsName);
    
    public OpcConfigEntry findByDeviceId(String opcInsName, String deviceId);
    
    public List<OpcConfigEntry> findAllByDeviceId(String deviceId);
    
    public List<OpcConfigEntry> findAllByProcessPointId(String processPointId);
    
    public List<String> findAllOpcInstanceNames();
    
}
