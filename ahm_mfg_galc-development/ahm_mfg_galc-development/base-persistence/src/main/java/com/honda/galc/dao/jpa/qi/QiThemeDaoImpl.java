package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiThemeDao;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIThemeDaoImpl Class description</h3>
 * <p> QIThemeDaoImpl description </p>
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

public class QiThemeDaoImpl extends BaseDaoImpl<QiTheme, String> implements QiThemeDao {
	
	private static String UPDATE_THEME = "update QI_THEME_TBX  set ACTIVE = ?1, THEME_NAME= ?2 , " +
			"THEME_DESCRIPTION = ?3, UPDATE_USER = ?4 where THEME_NAME= ?5";
	
	private static String FIND_THEME= "select e from QiTheme e where e.active in (:statusList) order by e.themeName";
	
	/* (non-Javadoc)
	 * @see com.honda.galc.dao.qi.QiThemeDao#updateTheme(com.honda.galc.entity.qi.QiTheme, java.lang.String)
	 */
	@Transactional
	public void updateTheme(QiTheme qiTheme, String oldThemeName) {
		Parameters params = Parameters.with("1", qiTheme.getActiveValue())
				.put("2", qiTheme.getThemeName()).put("3",qiTheme.getThemeDescription())
				.put("4", qiTheme.getUpdateUser()).put("5",oldThemeName);
		executeNativeUpdate(UPDATE_THEME, params);
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.dao.qi.QiThemeDao#findAllTheme(java.util.List)
	 */
	public List<QiTheme> findAllTheme(List<Short> statusList) {
		Parameters params = Parameters.with("statusList", statusList);
		return findAllByQuery(FIND_THEME, params);
	}
	
	public List<QiTheme> findAllActiveThemes(){
		return findAll(Parameters.with("active", (short)1), new String[]{"themeName"}, true);
	}
}
