package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dao.qi.QiThemeDao;
import com.honda.galc.dao.qi.QiThemeGroupDao;
import com.honda.galc.dao.qi.QiThemeGroupingDao;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;


/**
 * 
 * <h3>ThemeMaintenanceModel Class description</h3>
 * <p> ThemeMaintenanceModel description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 * 
 *
 */ 
public class ThemeMaintenanceModel extends QiModel {

	public ThemeMaintenanceModel(){
		super();
	}
	
	/**
	 * @return
	 */
	public List<QiThemeGroup> findAllThemeGroup(List<Short> statusList){
		return getDao(QiThemeGroupDao.class).findAllThemeGroup(statusList);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<QiTheme> findAllTheme(List<Short> statusList){
		return getDao(QiThemeDao.class).findAllTheme(statusList);
		
	}
	/**
	 * This method gets called when user clicks on Create Theme
	 * @param theme
	 */ 
	public QiTheme createTheme(QiTheme theme) {
		return getDao(QiThemeDao.class).insert(theme);
	}
	
	/**
	 * Checks if is theme exists.
	 *
	 * @param theme the theme
	 * @return true, if is theme exists
	 */
	public boolean isThemeExists(String theme) {
		return getDao(QiThemeDao.class).findByKey(theme) != null;
	}
	/**
	 * This method gets called when user clicks on Update Theme
	 * @param theme
	 * @param oldThemeName
	 */
	public void updateTheme(QiTheme theme, String oldThemeName) {
		getDao(QiThemeDao.class).updateTheme(theme,oldThemeName);
	}
	
	/**
	 * This method gets called when user clicks on reactivate/inactivate menu item to change theme status
	 * @param name
	 * @param active
	 */
	public void updateThemeStatus(List<QiTheme> updateList) {
		getDao(QiThemeDao.class).updateAll(updateList);
	}
	
	/**
	 * This method gets called when user clicks on Create Theme Group
	 * @param themeGroup
	 */ 
	public QiThemeGroup createThemeGroup(QiThemeGroup themeGroup) {
		return getDao(QiThemeGroupDao.class).insert(themeGroup);
	}
	
	/**
	 * This method gets called when user clicks on Update Theme Group
	 * @param themeGroup
	 * @param oldThemeGroupName
	 */
	public void updateThemeGroup(QiThemeGroup themeGroup, String oldThemeGroupName) {
		getDao(QiThemeGroupDao.class).updateThemeGroup(themeGroup,oldThemeGroupName);
	}
	
	/**
	 * Checks if is theme group exists.
	 *
	 * @param themeGroup the theme group
	 * @return true, if is theme group exists
	 */
	public boolean isThemeGroupExists(String themeGroup) {
		return getDao(QiThemeGroupDao.class).findByKey(themeGroup) != null;
	}
	
	/**
	 * This method gets called when user clicks on reactivate/inactivate menu item to change theme group status
	 * @param name
	 * @param active
	 */
	public void updateThemeGroupStatus(List<QiThemeGroup> updateThemeGroupList) {
		getDao(QiThemeGroupDao.class).updateAll(updateThemeGroupList);
	}
	
	/**
	 * Adds the to group association.
	 *
	 * @param qiThemeGrouping the qi theme grouping
	 */
	public void addToGroupAssociation(QiThemeGrouping qiThemeGrouping) {
		getDao(QiThemeGroupingDao.class).insert(qiThemeGrouping);
	}
	
	/**
	 * Delete group association by theme groups.
	 *
	 * @param qiThemeGroups the qi theme groups
	 */
	public void deleteGroupAssociationByThemeGroups(List<QiThemeGroup> qiThemeGroups) {
		getDao(QiThemeGroupingDao.class).deleteAllByThemeGroups(qiThemeGroups);
	}

	/**
	 * Delete group association.
	 *
	 * @param qiThemeGrouping the qi theme grouping
	 */
	public void deleteGroupAssociation(List<QiThemeGrouping> qiThemeGrouping) {
		getDao(QiThemeGroupingDao.class).deleteGroupAssociation(qiThemeGrouping);
	}
	
	/**
	 * Find theme grouping.
	 *
	 * @param themeGroupName the theme group name
	 * @param themeName the theme name
	 * @return the list
	 */
	public List<QiThemeGrouping> findThemeGrouping(List<String> themeGroupNameList, List<String> themeNameList) {
		return getDao(QiThemeGroupingDao.class).findThemeGrouping(themeGroupNameList, themeNameList);
	}

	/**
	 * Update theme association.
	 *
	 * @param newThemeName the new theme name
	 * @param oldThemeName the old theme name
	 */
	public void updateThemeAssociation(String newThemeName, String oldThemeName) {
		getDao(QiThemeGroupingDao.class).updateThemeAssociation(newThemeName, oldThemeName);
	}
	
	/**
	 * Update theme group association.
	 *
	 * @param newThemeGroupName the new theme group name
	 * @param oldThemeGroupName the old theme group name
	 */
	public void updateThemeGroupAssociation(String newThemeGroupName, String oldThemeGroupName) {
		getDao(QiThemeGroupingDao.class).updateThemeGroupAssociation(newThemeGroupName, oldThemeGroupName);
	}
	
	/**
	 * Delete group association by themes.
	 *
	 * @param qiThemes the qi themes
	 */
	public void deleteGroupAssociationByThemes(List<QiTheme> qiThemes) {
		getDao(QiThemeGroupingDao.class).deleteAllByThemes(qiThemes);
	}
	
	/**
	 * Find all by theme name.
	 *
	 * @param themeName the theme name
	 * @return the list
	 */
	public List<QiPartDefectCombination> findAllByThemeName(String themeName) {
		return getDao(QiPartDefectCombinationDao.class).findAllByThemeName(themeName);
	}
	
	/**
	 * Update part defect combination.
	 *
	 * @param pdcListToUpdate the pdc list to update
	 */
	public void updatePartDefectCombination(List<QiPartDefectCombination> pdcListToUpdate) {
		getDao(QiPartDefectCombinationDao.class).updateAll(pdcListToUpdate);
	}
}
