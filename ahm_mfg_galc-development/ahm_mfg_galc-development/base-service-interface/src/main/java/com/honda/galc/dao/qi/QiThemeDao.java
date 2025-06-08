package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.service.IDaoService;

public interface QiThemeDao extends IDaoService<QiTheme, String> {

	/**
	 * Find all theme.
	 *
	 * @param statusList the status list
	 * @return the list
	 */
	public List<QiTheme> findAllTheme(List<Short> statusList);

	/**
	 * Update theme.
	 *
	 * @param qiTheme the qi theme
	 * @param oldThemeName the old theme name
	 */
	public void updateTheme(QiTheme qiTheme, String oldThemeName);
	
	/**
	 * Find all active themes.
	 *
	 * @return the list
	 */
	public List<QiTheme> findAllActiveThemes();

}
