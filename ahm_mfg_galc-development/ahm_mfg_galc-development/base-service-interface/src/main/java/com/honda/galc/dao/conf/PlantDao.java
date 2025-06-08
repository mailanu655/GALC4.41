package com.honda.galc.dao.conf;


import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.PlantId;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface PlantDao extends IDaoService<Plant, PlantId> {
 
    public List<Plant> findAllBySite(String siteName); 

    public Plant findById(String siteName, String plantName);
    
    public void removeById(String siteName);

    public void removeById(String siteName, String plantName);

    public List<Object[]> findPlantForSite();

    
}
