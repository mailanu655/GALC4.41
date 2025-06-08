package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.service.IDaoService;

public interface QiThemeGroupDao extends IDaoService<QiThemeGroup, String> {

	/**
	 * Find all theme group.
	 *
	 * @param statusList the status list
	 * @return the list
	 */
	public List<QiThemeGroup> findAllThemeGroup(List<Short> statusList);

	/**
	 * Update theme group.
	 *
	 * @param qiThemeGroup the qi theme group
	 * @param oldThemeGroupName the old theme group name
	 */
	public void updateThemeGroup(QiThemeGroup qiThemeGroup,	String oldThemeGroupName);
}
