package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Date;
import java.util.List;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiRepairMethodDao;
import com.honda.galc.dao.qi.QiStationRepairMethodDao;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiStationRepairMethod;


public class RepairMethodMaintenanceModel extends QiModel {
	
	/**
	 * This method is used to create Repair Method and checked the duplicate Repair Method
	 * @param part
	 * @return
	 */
	public QiRepairMethod createMethod(QiRepairMethod repairMethodName) {

		QiRepairMethodDao dao = getDao(QiRepairMethodDao.class);
		if(dao.findByKey(repairMethodName.getRepairMethod()) != null){
			MessageDialog.showError("Already exists! Enter a different Repair Method Name");
			getApplicationContext().getLogger().error("Failed to add new Repair Method as the Repair Method "+repairMethodName.getRepairMethod()+" already exists!");
			return null;
		}
		else{
			repairMethodName.setAppCreateTimestamp(new Date());
			return((QiRepairMethod)dao.insert(repairMethodName));
		}
			
	}

	/**
	 * This method gets called when user clicks on reactivate/inactivate menu item
	 */

	public void updateRepairMethodStatus(List<QiRepairMethod> qiRepairMethod)
	{
		getDao(QiRepairMethodDao.class).updateAll(qiRepairMethod);
	}

	/**
	 * Update New Repair Method  
	 */
	public QiRepairMethod updateRepairMethod(QiRepairMethod qiRepairMethod) {
		return (QiRepairMethod) getDao(QiRepairMethodDao.class).save(qiRepairMethod);
	}
	
	public List<QiRepairMethod> findAllRepairMethods(){
		return getDao(QiRepairMethodDao.class).findAll();
	}
	
	/**
	 * @param filterValue
	 * @return
	 */
	public List<QiRepairMethod> findAllRepairMethods(String filterValue, List<Short> statusList){
		return getDao(QiRepairMethodDao.class).findRepairMethodsByFilter(filterValue, statusList);
	}
	
	
	
	public boolean isRepairMethodExists(String method) {
		return getDao(QiRepairMethodDao.class).findByKey(method) != null;
	}
	
	public QiRepairMethod createRepairMethod(QiRepairMethod method) {
		return (QiRepairMethod) getDao(QiRepairMethodDao.class).insert(method);
	}
	
	public void updateRepairMethod(QiRepairMethod method, String oldRepairMethodName) {
		getDao(QiRepairMethodDao.class).updateRepairMethod(method,oldRepairMethodName);
	}
	
	public List<QiStationRepairMethod> findAllAssociatedRepairMethodsByRepairMethod(String repairMethodName) {
		return getDao(QiStationRepairMethodDao.class).findAllByRepairMethod(repairMethodName);
	}
	
	public void removeAllQicsStationsByRepairMethod(String qiRepairMethodStationComb) {		
		getDao(QiStationRepairMethodDao.class).removeRepairMethodFromStations(qiRepairMethodStationComb);
	}
	
	public void updateAllRepairMethodsByRepairMethod(String repairMethodName,String oldRepairMethodName,String updateUser) {
		getDao(QiStationRepairMethodDao.class).updateAllByRepairMethod(repairMethodName,oldRepairMethodName,updateUser);		
	}
	
	public List<QiLocalDefectCombination> findAllAssociatedLocalDefectCombinationsByRepairMethod(String repairMethodName) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByRepairMethod(repairMethodName);
	}
	
	public void setAllLocalDefectCombinationsToUnassigned(String oldRepairMethodName) {		
		getDao(QiLocalDefectCombinationDao.class).setAllLocalDefectCombinationsToUnassigned(oldRepairMethodName, getUserId());
	}
	
	public void updateAllLocalDefectCombinationsByRepairMethod(String repairMethodName,String oldRepairMethodName,String updateUser) {
		getDao(QiLocalDefectCombinationDao.class).updateAllByRepairMethod(repairMethodName,oldRepairMethodName,updateUser);		
	}
}
