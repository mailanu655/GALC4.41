package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.service.IDaoService;

public interface QiLocalThemeDao extends IDaoService<QiLocalTheme, String> {

	public List<QiLocalTheme> findAllActiveLocalTheme();

	public List<QiLocalTheme> findAllByFilter(String filter);

	public List<QiLocalTheme> findAllByFilterAndStatus(String filter, short status);

	public void updateLocalTheme(QiLocalTheme qiLocalTheme);
	
	public void updateLocalTheme(QiLocalTheme qiLocalTheme, String oldLocalTheme);
}
