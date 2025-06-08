package com.honda.galc.client.teamleader.common;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.dao.conf.RegionalCodeDao;
import com.honda.galc.dao.conf.RegionalProcessPointGroupDao;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalCodeId;

public class RegionalDataMaintenanceModel extends QiModel {
	
	RegionalCode selectedRegionalCode = null;
	RegionalCode selectedRegionalValue = null;
	
	public RegionalDataMaintenanceModel() {
		super();
	}
	
	public List<RegionalCode> findAllDistinctRegionalCode() {
		List<RegionalCode> allRegionalCodeList = getDao(RegionalCodeDao.class).findAllRegionalCode();
		List<RegionalCode> allDistinctRegionalCodeList = new ArrayList<RegionalCode>();
		Iterator<RegionalCode> regionalCodeIterator = allRegionalCodeList.iterator();
		String currentRegionalCodeName = "";
		
		while (regionalCodeIterator.hasNext()) {
			RegionalCode regionalCode = regionalCodeIterator.next();
			if (!currentRegionalCodeName.equals(regionalCode.getId().getRegionalCodeName())) {
				allDistinctRegionalCodeList.add(regionalCode);
				currentRegionalCodeName = regionalCode.getId().getRegionalCodeName();
			}
		}
		
		return allDistinctRegionalCodeList;
	}
	
	public List<RegionalCode> findRegionalValueList(String regionalCodeName) {
		return getDao(RegionalCodeDao.class).findRegionalValueByCode(regionalCodeName);
	}
	
	public RegionalCode findRegionalCode(String regionalCodeName, String regionalValue) {
		return getDao(RegionalCodeDao.class).findByKey(new RegionalCodeId(regionalCodeName, regionalValue));
	}
	
	public RegionalCode createRegionalCode(RegionalCode regionalCode){
		return getDao(RegionalCodeDao.class).save(regionalCode);
	}
	
	public void updateRegionalValue(String oldRegionalCodeName, String oldValue, String value, String name, String abbreviation, String description, String updateUser) {
		getDao(RegionalCodeDao.class).updateRegionalValue(oldRegionalCodeName, oldValue, value, name, abbreviation, description, updateUser);
	}

	public void updateRegionalCode(String oldRegionalCodeName, String regionalCodeName, String updateUser) {
		getDao(RegionalCodeDao.class).updateRegionalCode(oldRegionalCodeName, regionalCodeName, updateUser);
	}
	
	public void deleteRegionalCode(String regionalCodeName) {
		getDao(RegionalCodeDao.class).removeByRegionalCodeName(regionalCodeName);
	}
	
	public void deleteRegionalValue(RegionalCode regionalCode) {
		getDao(RegionalCodeDao.class).remove(regionalCode);
	}
	
	public RegionalCode getSelectedRegionalCode() {
		return selectedRegionalCode;
	}

	public void setSelectedRegionalCode(RegionalCode selectedRegionalCode) {
		this.selectedRegionalCode = selectedRegionalCode;
	}

	public RegionalCode getSelectedRegionalValue() {
		return selectedRegionalValue;
	}

	public void setSelectedRegionalValue(RegionalCode selectedRegionalValue) {
		this.selectedRegionalValue = selectedRegionalValue;
	}
	
	public long findCountByCategoryCode(short categoryCode) {
		return getDao(RegionalProcessPointGroupDao.class).findCountByCategoryCode(categoryCode);
	}
	
	public long findCountByprocessPointGroupName(String processPointGroupName) {
		return getDao(RegionalProcessPointGroupDao.class).findCountByprocessPointGroupName(processPointGroupName);
	}
}
