package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiEntryScreenDeptId;
import com.honda.galc.service.IDaoService;

public interface QiEntryScreenDeptDao extends IDaoService<QiEntryScreenDept, QiEntryScreenDeptId> {
	public List<String> findDepartmentsForEntryScreen(String screenName);
	public void removeAllEntryScreenDepartment(List<QiEntryScreenDept> inOldButNotInNewList,String oldEntryScreenName);
	public void updateAllEntryScreenDept(List<QiEntryScreenDept> qiEntryScreenDeptList, QiEntryScreenDto oldQiEntryScreenDto);
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel);
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel,short isUsed);
	public List<QiEntryScreenDept> findAllByEntryModelAndIsUsed(String entryModel, short version);
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion);
	public void removeByEntryModelAndVersion(String entryModel, short version);
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version);
	List<Division> findAllDivisionByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel);
}
