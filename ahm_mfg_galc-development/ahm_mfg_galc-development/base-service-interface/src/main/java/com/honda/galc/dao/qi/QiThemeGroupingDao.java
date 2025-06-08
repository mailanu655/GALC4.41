package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;
import com.honda.galc.entity.qi.QiThemeGroupingId;
import com.honda.galc.service.IDaoService;

public interface QiThemeGroupingDao extends IDaoService<QiThemeGrouping, QiThemeGroupingId> {

	/**
	 * Find theme grouping.
	 *
	 * @param themeGroupName the theme group name
	 * @param themeName the theme name
	 * @return the list
	 */
	public List<QiThemeGrouping> findThemeGrouping(List<String> themeGroupNameList, List<String> themeNameList);
	
	/**
	 * Delete group association.
	 *
	 * @param qiThemeGrouping the qi theme grouping
	 */
	public void deleteGroupAssociation(List<QiThemeGrouping> qiThemeGrouping);
	
	/**
	 * Update theme association.
	 *
	 * @param newThemeName the new theme name
	 * @param oldThemeName the old theme name
	 */
	public void updateThemeAssociation(String newThemeName, String oldThemeName);

	/**
	 * Update theme group association.
	 *
	 * @param newThemeGroupName the new theme group name
	 * @param oldThemeGroupName the old theme group name
	 */
	public void updateThemeGroupAssociation(String newThemeGroupName, String oldThemeGroupName);
	
	/**
	 * Delete group association by themes.
	 *
	 * @param qiThemes the qi themes
	 */
	public void deleteAllByThemes(List<QiTheme> qiThemes);
	
	/**
	 * Delete group association by theme groups.
	 *
	 * @param qiThemeGroups the qi theme groups
	 */
	public void deleteAllByThemeGroups(List<QiThemeGroup> qiThemeGroups);
}
