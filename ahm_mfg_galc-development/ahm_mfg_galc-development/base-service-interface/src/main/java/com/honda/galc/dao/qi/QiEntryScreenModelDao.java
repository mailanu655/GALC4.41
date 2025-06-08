package com.honda.galc.dao.qi;


import com.honda.galc.entity.qi.QiEntryScreenModel;
import com.honda.galc.entity.qi.QiEntryScreenModelId;
import com.honda.galc.service.IDaoService;

public interface QiEntryScreenModelDao extends IDaoService<QiEntryScreenModel, QiEntryScreenModelId> {

	/**
	 * This method is used to update Entry Model Name
	 * @param newEntryModelName
	 * @param upadteUser
	 * @param oldEntryModelName
	 */
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName);
	public void removeEntryScreenModel(String oldEntryScreenName);	
	public void updateEntryScreenModel(QiEntryScreenModel qiEntryScreenModel,String oldEntryScreenName);
}
