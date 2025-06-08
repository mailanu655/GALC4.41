package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.entity.qi.QiEntryScreenId;

/**
 * 
 * <h3>QIIqsDao Class description</h3>
 * <p> QIIqsDao description </p>
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
 * July 27 2016
 *
 *
 */

public interface QiTextEntryMenuDao extends IDaoService<QiTextEntryMenu, QiTextEntryMenuId> {

	void updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu, String oldTextEntryMenu);

	void removeSelectedTextEntry(QiTextEntryMenu selectedComb);

	void updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu);
	
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short version, String userId);
	
	public List<QiTextEntryMenu> findAllByEntryScreenAndModel(String entryScreen, String entryModel, short version);
	
	public void removeAllMenusByEntryScreenAndModel(String entryScreen, String entryModel, short version);
	
	public List<QiTextEntryMenu> findAllByEntryModelAndIsUsed(String entryModel, short version);

	void updateVersionValue(String entryModel, short oldVersion, short newVersion);
	public List<QiTextEntryMenu> findAllMenuDetailsByIsUsedAndEntryScreen(String entryScreen, String entryModel, short version);
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed);
	public void removeByEntryModelAndVersion(String entryModel, short version);
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version);

	List<QiTextEntryMenu> findAllTextEntryMenuByEntryModel(String entryModel);

	long countTextEntryMenuByEntryModel(String entryModel);
	
}
