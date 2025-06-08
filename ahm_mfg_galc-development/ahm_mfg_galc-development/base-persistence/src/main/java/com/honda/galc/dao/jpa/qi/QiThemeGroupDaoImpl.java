package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiThemeGroupDao;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.service.Parameters;

public class QiThemeGroupDaoImpl extends BaseDaoImpl<QiThemeGroup, String> implements QiThemeGroupDao  {
	

	private static String UPDATE_THEME = "update QI_THEME_GROUP_TBX  set ACTIVE = ?1, THEME_GROUP_NAME= ?2 , " +
			"THEME_GROUP_DESCRIPTION = ?3, UPDATE_USER = ?4 where THEME_GROUP_NAME= ?5";
	
	private static String FIND_THEME_GROUP= "select e from QiThemeGroup e where e.active in (:statusList) order by e.themeGroupName";
	
	@Transactional
	public void updateThemeGroup(QiThemeGroup qiThemeGroup, String oldThemeGroupName) {
		Parameters params = Parameters.with("1", qiThemeGroup.getActiveValue())
				.put("2", qiThemeGroup.getThemeGroupName()).put("3",qiThemeGroup.getThemeGroupDescription())
				.put("4", qiThemeGroup.getUpdateUser()).put("5",oldThemeGroupName);
		executeNativeUpdate(UPDATE_THEME, params);
	}
	
	public List<QiThemeGroup> findAllThemeGroup(List<Short> statusList) {
		Parameters params = Parameters.with("statusList", statusList);
		return findAllByQuery(FIND_THEME_GROUP, params);
	}
}
