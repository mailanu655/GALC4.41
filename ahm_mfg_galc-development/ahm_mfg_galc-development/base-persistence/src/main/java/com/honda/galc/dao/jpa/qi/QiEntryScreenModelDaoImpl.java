package com.honda.galc.dao.jpa.qi;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryScreenModelDao;
import com.honda.galc.entity.qi.QiEntryScreenModel;
import com.honda.galc.entity.qi.QiEntryScreenModelId;
import com.honda.galc.service.Parameters;
public class QiEntryScreenModelDaoImpl extends BaseDaoImpl<QiEntryScreenModel, QiEntryScreenModelId> implements QiEntryScreenModelDao  {
	private static final String UPDATE_ENTRY_MODEL_NAME = "update GALADM.QI_ENTRY_SCREEN_MODEL_TBX e set e.ENTRY_MODEL = ?1 , e.UPDATE_USER =?2 where e.ENTRY_MODEL = ?3";
	
	private static final String UPDATE_REPAIR_METHOD = "update GALADM.QI_ENTRY_SCREEN_MODEL_TBX  set ENTRY_SCREEN= ?1 , ENTRY_MODEL= ?2 ," +			
			" UPDATE_USER = ?3, UPDATE_TIMESTAMP = ?4 where ENTRY_SCREEN= ?5";
	/**
	 * This method is used to update Entry Model Name
	 * @param newEntryModelName
	 * @param upadteUser
	 * @param oldEntryModelName
	 */
	@Transactional
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName) {
		Parameters params = Parameters.with("1", newEntryModelName)
				.put("2", userId)
				.put("3", oldEntryModelName);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	@Transactional
	public void removeEntryScreenModel(String oldEntryScreenName) {		
		delete(Parameters.with("id.entryScreen", oldEntryScreenName));
	}
	
	
		
	@Transactional
	public void updateEntryScreenModel(QiEntryScreenModel qiEntryScreenModel,String oldEntryScreenName) {		
		Parameters params = Parameters.with("1", qiEntryScreenModel.getId().getEntryScreen())
				.put("2", qiEntryScreenModel.getId().getEntryModel())				
				.put("3", qiEntryScreenModel.getUpdateUser()).put("4",new Date()).put("5",oldEntryScreenName);
			executeNativeUpdate(UPDATE_REPAIR_METHOD, params);		
	}

}
