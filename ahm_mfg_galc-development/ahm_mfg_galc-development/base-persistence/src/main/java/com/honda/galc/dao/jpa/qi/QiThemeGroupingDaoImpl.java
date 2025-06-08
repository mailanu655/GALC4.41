package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiThemeGroupingDao;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;
import com.honda.galc.entity.qi.QiThemeGroupingId;
import com.honda.galc.service.Parameters;

public class QiThemeGroupingDaoImpl extends BaseDaoImpl<QiThemeGrouping, QiThemeGroupingId> implements QiThemeGroupingDao {

	private final String GET_THEME_ASSOCIATION = "SELECT e FROM QiThemeGrouping e WHERE ";
	
	private final String UPDATE_THEME_ASSOCIATION = "UPDATE QI_THEME_GROUPING_TBX SET THEME_NAME = ?1 WHERE THEME_NAME = ?2";
	
	private final String UPDATE_THEME_GROUP_ASSOCIATION = "UPDATE QI_THEME_GROUPING_TBX SET THEME_GROUP_NAME = ?1 WHERE THEME_GROUP_NAME = ?2";
	
	public List<QiThemeGrouping> findThemeGrouping(List<String> themeGroupNameList, List<String> themeNameList) {
		String fetchThemeAssociation = GET_THEME_ASSOCIATION;
		boolean isGroupExist = themeGroupNameList != null && !themeGroupNameList.isEmpty();
		Parameters param = new Parameters();
		if(isGroupExist) {
			fetchThemeAssociation = fetchThemeAssociation.concat(" e.id.themeGroupName in ( :groupNames ) ");
			param.put("groupNames", themeGroupNameList);
		}
		if(themeNameList != null && !themeNameList.isEmpty()) {
			fetchThemeAssociation = fetchThemeAssociation.concat(isGroupExist ? " and " : "");
			fetchThemeAssociation = fetchThemeAssociation.concat(" e.id.themeName in ( :themeName )");
			param.put("themeName", themeNameList);
		}
		return findAllByQuery(fetchThemeAssociation, param);
	}
	
	@Transactional
	public void deleteGroupAssociation(List<QiThemeGrouping> qiThemeGrouping) {
		for (QiThemeGrouping q : qiThemeGrouping) {
			Parameters params = Parameters.with("id.themeGroupName",
					q.getThemeGroupName()).put("id.themeName", q.getThemeName());
			delete(params);
		}
	}
	
	@Transactional
	public void updateThemeAssociation(String newThemeName, String oldThemeName) {
		Parameters params = Parameters.with("1", newThemeName).put("2", oldThemeName);
		executeNativeUpdate(UPDATE_THEME_ASSOCIATION, params);
	}
	
	@Transactional
	public void updateThemeGroupAssociation(String newThemeGroupName, String oldThemeGroupName) {
		Parameters params = Parameters.with("1", newThemeGroupName).put("2", oldThemeGroupName);
		executeNativeUpdate(UPDATE_THEME_GROUP_ASSOCIATION, params);
	}
	

	@Transactional
	public void deleteAllByThemeGroups(List<QiThemeGroup> qiThemeGroups) {
		for (QiThemeGroup themeGroup : qiThemeGroups) {
			Parameters params = Parameters.with("id.themeGroupName", themeGroup.getThemeGroupName());
			delete(params);
		}
	}
	
	@Transactional
	public void deleteAllByThemes(List<QiTheme> qiThemes) {
		for (QiTheme theme : qiThemes) {
			Parameters params = Parameters.with("id.themeName", theme.getThemeName());
			delete(params);
		}
	}
}
