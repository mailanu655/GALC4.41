package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.RegionalCodeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.RegionalProcessPointGroupDto;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalCodeId;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.service.Parameters;

public class RegionalCodeDaoImpl extends BaseDaoImpl<RegionalCode, RegionalCodeId> implements RegionalCodeDao {
	
	private static final String UPDATE_REGIONAL_CODE_NAME = "UPDATE GALADM.REGIONAL_CODE_TBX SET REGIONAL_CODE_NAME = ?1, " +
			"UPDATE_USER = ?2 WHERE REGIONAL_CODE_NAME = ?3";
	
	private static final String UPDATE_REGIONAL_VALUE = "UPDATE GALADM.REGIONAL_CODE_TBX SET REGIONAL_VALUE = ?1, " +
			"REGIONAL_VALUE_NAME = ?2, REGIONAL_VALUE_ABBR = ?3, REGIONAL_VALUE_DESC = ?4, UPDATE_USER =  ?5 " +
			"WHERE REGIONAL_CODE_NAME = ?6 AND REGIONAL_VALUE = ?7";
	
	private static final String DELETE_REGIONAL_CODE_BY_CODE_NAME = "DELETE FROM GALADM.REGIONAL_CODE_TBX WHERE REGIONAL_CODE_NAME = ?1";
	 
    private static final String FIND_ALL_BY_NAME_AND_MATCHING_TEXT = "select c from RegionalCode c where c.id.regionalCodeName = :name" +
    				" and (upper(c.id.regionalValue) like :text or upper(c.regionalValueName) like :text or upper(c.regionalValueDesc) like :text)";
    
	private static final String FIND_ALL_GROUPS_BY_CODE = "select rpg.site, rc.regional_value, rc.regional_value_name, rc.regional_value_abbr, rc.regional_value_desc " + 
			"from galadm.regional_process_point_group_tbx rpg left join galadm.regional_code_tbx rc " + 
			"on rpg.process_point_group_name = rc.regional_value " +
			"where rpg.category_code = ?1 and rc.regional_code_name = ?2";
	
	private static final String FIND_ALL_GROUPS_BY_CODE_AND_TEXT = "select rpg.site, rc.regional_value, rc.regional_value_name, rc.regional_value_abbr, rc.regional_value_desc " + 
			"from galadm.regional_process_point_group_tbx rpg left join galadm.regional_code_tbx rc " + 
			"on rpg.process_point_group_name = rc.regional_value " +
			"where rpg.category_code = ?1 and rc.regional_code_name = ?2 " + 
			"and (upper(rc.regional_value_name) like ?3 or upper(rc.regional_value_desc) like ?3)";
	
	private static final String FIND_ASSIGNED_GROUPS_BY_CODE_AND_SITE = "select rpg.category_code, rpg.site, rc.regional_value, rc.regional_value_name, rc.regional_value_abbr, rc.regional_value_desc " + 
			"from galadm.regional_process_point_group_tbx rpg left join galadm.regional_code_tbx rc " + 
			"on rpg.process_point_group_name = rc.regional_value " +
			"where rpg.category_code = ?1 and rc.regional_code_name = ?2 " + 
			"and rpg.site = ?3 ORDER BY rc.regional_value_name ASC";
	
	private static final String FIND_AVAILABLE_GROUPS_BY_FILTER = "select REGIONAL_CODE_NAME, REGIONAL_VALUE, REGIONAL_VALUE_NAME, REGIONAL_VALUE_ABBR, REGIONAL_VALUE_DESC " + 
			"from galadm.regional_code_tbx where REGIONAL_CODE_NAME = ?1 and " + 
			"UPPER(REGIONAL_VALUE concat REGIONAL_VALUE_NAME concat REGIONAL_VALUE_ABBR concat REGIONAL_VALUE_DESC) like ?2 " +
			"except	select REGIONAL_CODE_NAME, REGIONAL_VALUE, REGIONAL_VALUE_NAME, REGIONAL_VALUE_ABBR, REGIONAL_VALUE_DESC " + 
			"from galadm.regional_code_tbx a, galadm.regional_process_point_group_tbx b where a.REGIONAL_VALUE = b.PROCESS_POINT_GROUP_NAME " + 
			"and a.REGIONAL_CODE_NAME = ?1 and b.SITE = ?3 and b.CATEGORY_CODE = ?4 ORDER BY REGIONAL_VALUE_NAME ASC";

	public List<RegionalCode> findAllRegionalCode(){
		return findAll(null, new String[]{"id.regionalCodeName", "id.regionalValue"}, true);
	}
	
	public List<RegionalCode> findRegionalValueByCode(String regionalCodeName) {
		Parameters params = Parameters.with("id.regionalCodeName", regionalCodeName);
		return findAll(params, new String[]{"id.regionalValue"}, true);
	}
	
	//use native query to update column which is in where clause
	@Transactional
	public void updateRegionalValue(String oldRegionalCodeName, String oldValue, String value, String name, String abbreviation, 
			String description, String updateUser) {
		Parameters params = Parameters.with("1", value).put("2", name).put("3", abbreviation).put("4",  description)
				.put("5", updateUser).put("6", oldRegionalCodeName).put("7", oldValue);
		executeNativeUpdate(UPDATE_REGIONAL_VALUE, params);
	}
	
	@Transactional
	public void updateRegionalCode(String oldRegionalCodeName, String regionalCodeName, String updateUser) {
		Parameters params = Parameters.with("1", regionalCodeName).put("2", updateUser).put("3", oldRegionalCodeName);
		executeNativeUpdate(UPDATE_REGIONAL_CODE_NAME, params);
	}
	
	@Transactional
	public void removeByRegionalCodeName(String regionalCodeName) {
		Parameters params = Parameters.with("1", regionalCodeName);
		executeNativeUpdate(DELETE_REGIONAL_CODE_BY_CODE_NAME, params);
	}

	public List<RegionalCode> findAllByRegionalCodeName(String name) {
    	return findAll(Parameters.with("id.regionalCodeName", name));
    }
	
    public List<RegionalCode> findAllByNameAndMatchingText(String name, String text) {
		Parameters params = Parameters.with("name", name)
										.put("text", "%" + text.toUpperCase() + "%");
    	return findAllByQuery(FIND_ALL_BY_NAME_AND_MATCHING_TEXT, params);
    }
    
	public List<RegionalProcessPointGroupDto> findAllGroupsByCode(short code) {
		Parameters params = Parameters.with("1", code).put("2", RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName());
		return findAllByNativeQuery(FIND_ALL_GROUPS_BY_CODE, params, RegionalProcessPointGroupDto.class);
	}
    
    public List<RegionalProcessPointGroupDto> findAllGroupsByCodeAndText(short code, String searchText) {
		Parameters params = Parameters.with("1", code)
									.put("2", RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName())
									.put("3", "%" + searchText.toUpperCase() + "%");
    	return findAllByNativeQuery(FIND_ALL_GROUPS_BY_CODE_AND_TEXT, params, RegionalProcessPointGroupDto.class);
    }
    
    public List<RegionalProcessPointGroupDto> findAssignedGroupsByCodeAndSite(short code, String site) {
		Parameters params = Parameters.with("1", code)
									.put("2", RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName())
									.put("3", site);
    	return findAllByNativeQuery(FIND_ASSIGNED_GROUPS_BY_CODE_AND_SITE, params, RegionalProcessPointGroupDto.class);
    }
    
    public List<RegionalCode> findAvailableProcessPointGroupByFilter(short code, String site, String filter) {
		Parameters params = Parameters.with("1", RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName())
									.put("2", "%" + filter.toUpperCase() + "%")
									.put("3", site)
									.put("4", code);
    	return findAllByNativeQuery(FIND_AVAILABLE_GROUPS_BY_FILTER, params, RegionalCode.class);
    }
}

