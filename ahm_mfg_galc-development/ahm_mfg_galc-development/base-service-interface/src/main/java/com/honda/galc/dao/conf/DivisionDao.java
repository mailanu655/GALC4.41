package com.honda.galc.dao.conf;


import com.honda.galc.entity.conf.Division;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface DivisionDao extends IDaoService<Division, String> {
	
    public Division findByDivisionId(String divisionId);

    public List<Division> findById(String siteName);

    public List<Division> findById(String siteName, String plantName);

    public List<Division> findAllByDivisionIds(List<String> divisionIds);
    
    public Division findByKeyWithChildren(String divisionId);

    public void removeByDivisionId(String divisionId);

    public void removeById(String siteName);

    public void removeById(String siteName, String plantName);

    public void removeById(String siteName, String plantName, String divisionId);
    
    public void updateId(String siteName, String plantName, String divisionId);

    public List<Division> findDept();
    
    public List<String> findPlantBySite(String siteName);
    
    public Division findWithChildren(String divisionId);
    
    public List<String> findAllPlantBySiteAndExternalSystemData(String site);
    
    public List<String> findAllDivisionId();

	public List<Object[]> findDivisionIdAndName();

	public List<Object[]> findDivisionForPlant();

	public String getDivisionID(String divisionName);
}
