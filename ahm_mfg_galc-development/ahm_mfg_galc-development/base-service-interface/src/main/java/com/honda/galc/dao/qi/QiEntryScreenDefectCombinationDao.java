package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.service.IDaoService;

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

public interface QiEntryScreenDefectCombinationDao extends IDaoService<QiEntryScreenDefectCombination, QiEntryScreenDefectCombinationId> {

	void updateEntryScreenNameAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion, String userId);

	void updateTextEntryMenuName(QiTextEntryMenuId newTextEntryMenuId, String oldTextEntryMenu, String userId);
	
	public List<QiEntryScreenDefectCombination> findAllByEntryScreen(String entryScreen, String entryModel, short version);
	
	public String fetchAuditPrimaryKeyValue(int regionalDefectCombinationId, String entryScreenName, String entryModel);
			
    public Long findCountByEntryScreenName(String entryScreen);
	
	List<QiEntryScreenDefectCombination> findAllByRegionalDefectCombinationId(Integer regionalDefectCombinationId);	
	
	List<QiEntryScreenDefectCombination> findAllEntryScreensByPartDefectId(List<Integer> partDefectIdList);

	List<QiEntryScreenDefectCombination> findAllEntryScreensByPartLocationId(List<Integer> partLocationIdList);
	
	void removeByTextEntryId(QiTextEntryMenuId textEntryMenu);
	
	public List<QiEntryScreenDefectCombination> findAllByEntryModelAndVersion(String entryModel, short version);

	void updateVersionValue(String entryModel, short oldVersion, short newVersion);
	
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel,short isUsed);
	
	public void removeByEntryModelAndVersion(String entryModel, short version);
	
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version);

	void deleteByEntryScreenModelAndVersion(String plantName, String entryScreen, String entryModel, short version);

	void deleteByEntryScreenModelMenuAndVersion(String plantName, String entryScreen, String entryModel, String menu, short version);

	List<QiEntryScreenDefectCombination> findAllByEntryModel(String entryModel);

	List<QiEntryScreenDefectCombination> findAllByEntryScreenAndEntryModel(String entryScreen, String entryModel);

	long countByEntryModelAndScreen(String entryScreen, String entryModel);

	long countByEntryModel(String entryModel);
	
}
