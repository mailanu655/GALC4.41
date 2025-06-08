package com.honda.galc.dao.jpa.qi;

import java.util.List;

import javax.persistence.Column;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiTextEntryMenuDao;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.service.Parameters;

public class QiTextEntryMenuDaoImpl extends BaseDaoImpl<QiTextEntryMenu,QiTextEntryMenuId> implements QiTextEntryMenuDao{

	private static final String UPDATE_ENTRY_SCREEN_AND_MODEL_NAME = "update QiTextEntryMenu e "
			+ "set e.id.entryScreen=:newEntryScreen, e.id.entryModel=:newEntryModel, e.updateUser=:updateUser "
			+ "where e.id.entryScreen=:oldEntryScreen and e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";
	
	private static final String UPDATE_ENTRY_MODEL_NAME = "update QiTextEntryMenu e set e.id.entryModel=:newEntryModel, e.updateUser=:updateUser where e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";

	private static final String UPDATE_TEXT_ENTRY_MENU = "update  GALADM.QI_TEXT_ENTRY_MENU_TBX "
			+ "set TEXT_ENTRY_MENU = ?1, TEXT_ENTRY_MENU_DESC = ?2, UPDATE_USER=?3 "
			+ "where ENTRY_SCREEN = ?4 AND TEXT_ENTRY_MENU =  ?5 AND ENTRY_MODEL = ?6 AND IS_USED = ?7";
	
	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_TEXT_ENTRY_MENU_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";
	
	private static final String  SELECT_ALL_TEXT_ENTRY_MENU_BY_ENTRY_MODEL = "SELECT * FROM GALADM.QI_TEXT_ENTRY_MENU_TBX "
			+ "WHERE ENTRY_MODEL = ?1";
	
	public List<QiTextEntryMenu> findAllMenuDetailsByIsUsedAndEntryScreen(String entryScreen, String entryModel, short version) {
		 return findAll(Parameters.with("id.entryScreen", entryScreen)
				 .put("id.entryModel", entryModel)
				 .put("id.isUsed", version));
	}
	

	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRES_NEW)
	public void  updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu) {
		if(qiTextEntryMenu == null)  return;
		QiTextEntryMenuId id = qiTextEntryMenu.getId();
		Parameters idParams = Parameters.with("id.entryScreen", id.getEntryScreen())
				.put("id.textEntryMenu", id.getTextEntryMenu())
				.put("id.entryModel", id.getEntryModel())
				.put("id.isUsed", id.getIsUsed());

		 update(Parameters.with("textEntryMenuDesc", qiTextEntryMenu.getTextEntryMenuDesc()).put("updateUser", qiTextEntryMenu.getUpdateUser()),
				 idParams);
	}
	
	@Transactional
	public void  updateTextEntryMenu(QiTextEntryMenu qiTextEntryMenu,String oldTextEntryMenu) {
		Parameters params =  Parameters.with("1", qiTextEntryMenu.getId().getTextEntryMenu())
				.put("2", qiTextEntryMenu.getTextEntryMenuDesc())
				.put("3", qiTextEntryMenu.getUpdateUser())
				.put("4",  qiTextEntryMenu.getId().getEntryScreen())
				.put("5", oldTextEntryMenu)
				.put("6", qiTextEntryMenu.getId().getEntryModel())
				.put("7", qiTextEntryMenu.getId().getIsUsed());
		 executeNativeUpdate(UPDATE_TEXT_ENTRY_MENU, params);		
	}

	@Transactional
	public void removeSelectedTextEntry(QiTextEntryMenu selectedComb) {
		if(selectedComb == null)  return;
		QiTextEntryMenuId id = selectedComb.getId();
		Parameters idParams = Parameters.with("id.entryScreen", id.getEntryScreen())
				.put("id.textEntryMenu", id.getTextEntryMenu())
				.put("id.entryModel", id.getEntryModel())
				.put("id.isUsed", id.getIsUsed());

		 delete(idParams);
		
	}

	@Transactional
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short version, String userId) {
		Parameters params = Parameters.with("newEntryScreen", newEntryScreen.getEntryScreen())
				.put("newEntryModel", newEntryScreen.getEntryModel())
				.put("updateUser", userId)
				.put("oldEntryScreen", oldEntryScreen)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", version);
		executeUpdate(UPDATE_ENTRY_SCREEN_AND_MODEL_NAME, params);
	}
	
	public List<QiTextEntryMenu> findAllByEntryScreenAndModel(String entryScreen, String entryModel, short version) {
		return findAll(Parameters.with("id.entryScreen", entryScreen)
				.put("id.entryModel", entryModel).put("id.isUsed", version));
	}
	
	@Transactional
	public void removeAllMenusByEntryScreenAndModel(String entryScreen, String entryModel, short version) {
		delete(Parameters.with("id.entryScreen", entryScreen)
				.put("id.entryModel", entryModel).put("id.isUsed", version));
	}
	
	@Transactional
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed) {
		Parameters params = Parameters.with("newEntryModel", newEntryModel)
				.put("updateUser", userId)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", isUsed);
		executeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	public List<QiTextEntryMenu> findAllByEntryModelAndIsUsed(String entryModel, short version){
	    return findAll(Parameters.with("id.entryModel", entryModel).put("id.isUsed", version));	
	}
	
	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("id.entryModel",entryModel).put("id.isUsed", version));
	}
	
	@Transactional
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version) {
		delete(Parameters.with("id.entryModel", entryModel).put("id.entryScreen", entryScreen)
				.put("id.isUsed", version));
	}

	public List<QiTextEntryMenu> findAllTextEntryMenuByEntryModel(String entryModel) {
		return  findAll(Parameters.with("id.entryModel", entryModel));		
	}
	
	@Override
	public long countTextEntryMenuByEntryModel(String entryModel) {
		return  count(Parameters.with("id.entryModel", entryModel));		
	}
}
