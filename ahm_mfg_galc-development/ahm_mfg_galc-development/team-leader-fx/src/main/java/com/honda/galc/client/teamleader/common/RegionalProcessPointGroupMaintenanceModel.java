package com.honda.galc.client.teamleader.common;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.dao.conf.RegionalCodeDao;
import com.honda.galc.dao.conf.RegionalProcessPointGroupDao;
import com.honda.galc.dao.qi.QiSiteDao;
import com.honda.galc.dto.RegionalProcessPointGroupDto;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.qi.constant.QiConstant;

public class RegionalProcessPointGroupMaintenanceModel extends QiModel {

	public RegionalProcessPointGroupMaintenanceModel() {
		super();
	}
	
	public List<RegionalCode> findAllCategoryCode() {
		return getDao(RegionalCodeDao.class).findRegionalValueByCode(RegionalCodeName.CATEGORY_CODE.getName());
	}
	
	public List<String> findAllSite() {
		return getDao(QiSiteDao.class).findAllSiteName();
	}
	
	public List<RegionalCode> findAvailableProcessPointGroupByFilter(short categoryCode, String site, String filter) {
		return getDao(RegionalCodeDao.class).findAvailableProcessPointGroupByFilter(categoryCode, site, filter);
	}
	
	public List<RegionalProcessPointGroupDto> findAssignedProcessPointGroup(short categoryCode, String site) {
		return getDao(RegionalCodeDao.class).findAssignedGroupsByCodeAndSite(categoryCode, site);
	}
	
	public void createRegionalProcessPointGroups(List<RegionalProcessPointGroup> regionalProcessPointGroupList) {
		getDao(RegionalProcessPointGroupDao.class).saveAll(regionalProcessPointGroupList);
	}
	
	public void deleteRegionalProcessPointGroups(List<RegionalProcessPointGroup> regionalProcessPointGroupList) {
	 	getDao(RegionalProcessPointGroupDao.class).removeAll(regionalProcessPointGroupList);
	}
}
