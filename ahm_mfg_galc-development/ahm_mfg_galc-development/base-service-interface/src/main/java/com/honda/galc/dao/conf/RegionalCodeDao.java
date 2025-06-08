package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.RegionalProcessPointGroupDto;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalCodeId;
import com.honda.galc.service.IDaoService;

public interface RegionalCodeDao extends IDaoService<RegionalCode, RegionalCodeId> {
	
	public List<RegionalCode> findAllRegionalCode();
		
	public List<RegionalCode> findRegionalValueByCode(String regionalCodeName);
	
	public void updateRegionalValue(String oldRegionalCodeName, String oldValue, String value, String name, 
			String abbreviation, String description, String updateUser);
	
	public void updateRegionalCode(String oldRegionalCodeName, String regionalCodeName, String updateUser);
	
	public void removeByRegionalCodeName(String regionalCodeName);
	
	public List<RegionalCode> findAllByRegionalCodeName(String name);
	public List<RegionalCode> findAllByNameAndMatchingText(String name, String text);
    public List<RegionalProcessPointGroupDto> findAllGroupsByCode(short code);
    public List<RegionalProcessPointGroupDto> findAllGroupsByCodeAndText(short code, String searchText);
    public List<RegionalProcessPointGroupDto> findAssignedGroupsByCodeAndSite(short code, String site); 
    public List<RegionalCode> findAvailableProcessPointGroupByFilter(short code, String site, String filter);
}


















