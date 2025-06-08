package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiLocalThemeDao;
import com.honda.galc.entity.qi.QiLocalTheme;

/**
 * 
 * <h3>LocalThemeModel Class description</h3>
 * <p>
 * LocalThemeModel is used to maintain Tracking Code
 * </p>
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
 * @author L&TInfotech<br>
 *        May 11, 2017
 * 
 */

public class LocalThemeModel extends QiModel{

	public List<QiLocalTheme> findAllLocalThemesByFilter(String filter) {
		return getDao(QiLocalThemeDao.class).findAllByFilter(filter);
	}
	

	public List<QiLocalTheme> findAllLocalThemesByFilterAndStatus(String filter, short status) {
		return getDao(QiLocalThemeDao.class).findAllByFilterAndStatus(filter, status);
	}


	public void saveLocalTheme(QiLocalTheme qiLocalTheme) {
		 getDao(QiLocalThemeDao.class).save(qiLocalTheme);
	}


	public void updateLocalTheme(QiLocalTheme qiLocalTheme) {
		getDao(QiLocalThemeDao.class).updateLocalTheme(qiLocalTheme);
		
	}


	public void updateLocalTheme(QiLocalTheme qiLocalTheme, String localThemeName) {
		getDao(QiLocalThemeDao.class).updateLocalTheme(qiLocalTheme, localThemeName);
		
	}


	public QiLocalTheme findLocalThemeByName(String localThemeName) {
		return getDao(QiLocalThemeDao.class).findByKey(localThemeName);
	}


	public boolean isLocalThemeInUseByLocalDefect(String localThemeName) {
		return getDao(QiLocalDefectCombinationDao.class).isLocalThemeInUseByLocalDefect(localThemeName);
	}
	

	public void updateLocalThemeForLocalDefects(String newLocalThemeName, String userId, String oldLocalThemeName) {
		getDao(QiLocalDefectCombinationDao.class).updateLocalThemeForLocalDefects(newLocalThemeName, userId, oldLocalThemeName);
	}


	
}
