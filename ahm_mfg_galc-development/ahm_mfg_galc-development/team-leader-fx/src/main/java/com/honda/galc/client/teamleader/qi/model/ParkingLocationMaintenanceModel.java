package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDepartmentDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairAreaRowDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.dto.qi.QiRepairAreaSapceAssignmentDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>ParkingLocationMaintenanceModel Class description</h3>
 * <p> ParkingLocationMaintenanceModel description </p>
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
 * </Table>
 *   
 * @author L&T Infotech<br>
 */

public class ParkingLocationMaintenanceModel extends QiModel{	
	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
	
	public List<Plant> findAllPlantBySite(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}
	
	public List<Division> findAllDivisionBySiteAndPlant(String siteName, String plantName) {
		return getDao(DivisionDao.class).findById(siteName,plantName);
	}
	
	/**
	 * This method is used to create the RepairAreaRow and checked the duplicate RepairAreaRow
	 * @param qiRepairAreaRow
	 * @return
	 */
	public QiRepairAreaRow createRepairAreaRow(QiRepairAreaRow qiRepairAreaRow) {
		return((QiRepairAreaRow)getDao(QiRepairAreaRowDao.class).insert(qiRepairAreaRow));
	}
	/**
	 * This method checks whether same RowData exists in the database
	 * @param partName
	 */
	public boolean isRepairAreaRowExists(QiRepairAreaRowId repairAreaRowId) {
		boolean flag=getDao(QiRepairAreaRowDao.class).findByKey(repairAreaRowId) != null;
		return flag;
	}
	
	public boolean isRepairSpaceExists(QiRepairAreaSpace repairAreaSpace) {
		return getDao(QiRepairAreaSpaceDao.class).findByKey(repairAreaSpace.getId()) != null;
	}
	
	public void createRepairAreaSpace(QiRepairAreaSpace method) {
		getDao(QiRepairAreaSpaceDao.class).insert(method);
	}
	/**
	 * Update RowData
	 */
	public QiRepairAreaRow updateRepairAreaRow(QiRepairAreaRow qiRepairAreaRow) {
		return (QiRepairAreaRow) getDao(QiRepairAreaRowDao.class).save(qiRepairAreaRow);
	}
	
	public QiRepairAreaSpace updateRepairAreaSpace(QiRepairAreaSpace qiRepairAreaSpace) {
		return (QiRepairAreaSpace) getDao(QiRepairAreaSpaceDao.class).save(qiRepairAreaSpace);
	}
	
	public void deleteRepairArea(QiRepairArea qiRepairArea){
		getDao(QiRepairAreaDao.class).removeByKey(qiRepairArea.getId());
	}
	
	public void deleteRepairAreaRow(QiRepairAreaRow qiRepairAreaRow){
		getDao(QiRepairAreaRowDao.class).removeByKey(qiRepairAreaRow.getId());
	}
	
	public void deleteRepairAreaSpace(QiRepairAreaSpace qiRepairAreaSpace){
		getDao(QiRepairAreaSpaceDao.class).removeByKey(qiRepairAreaSpace.getId());
	}
	
	public List<QiRepairArea> findAllBySiteAndPlant(String siteName, String plantName) {
		return getDao(QiRepairAreaDao.class).findAllBySiteAndPlant(siteName,plantName);
	}
	
	public boolean isRepairAreaExists(String method) {
		return getDao(QiRepairAreaDao.class).findByKey(method) != null;
	}
	
	public void createRepairArea(QiRepairArea method) {
		getDao(QiRepairAreaDao.class).insert(method);
	}
	
	public void updateRepairArea(QiRepairArea method, String oldRepairAreaName) {
		getDao(QiRepairAreaDao.class).updateRepairArea(method,oldRepairAreaName);
	}
	
	public List<QiRepairAreaRow> findAllByRepairAreaName(String repairAreaName){
		return getDao(QiRepairAreaRowDao.class).findAllByRepairAreaName(repairAreaName);
	}
	
	public List<QiRepairAreaSpace> findAllByRepairAreaNameAndRow(String repairAreaName, int repairAreaRow){
		return getDao(QiRepairAreaSpaceDao.class).findAllByRepairAreaNameAndRow(repairAreaName,repairAreaRow);
	}
	
	public List<String> findAllDeptBySiteAndPlant(String siteName,String plantName){		
		return getDao(QiDepartmentDao.class).findAllActiveDepartmentsBySiteAndPlant(siteName,plantName);
	}
	
	public List<QiRepairAreaSapceAssignmentDto> findAllSpaceAssignmentByRepairAreaNameAndRow(QiRepairAreaRowId areaRowId){
		return getDao(QiRepairAreaSpaceDao.class).findAllSpaceAssignmentByRepairAreaNameAndRow(areaRowId);
	}
	public void saveAllRepairAreaRow(List<QiRepairAreaRow> repairAreaRowsList){
		getDao(QiRepairAreaRowDao.class).saveAll(repairAreaRowsList);
	}
	
	public void saveAllRepairAreaSpace(List<QiRepairAreaSpace> repairAreaSpaceList){
		getDao(QiRepairAreaSpaceDao.class).saveAll(repairAreaSpaceList);
	}

	public void updateRepairAreaSpaceStatus(QiRepairAreaSpace qiRepairAreaSpace){
		getDao(QiRepairAreaSpaceDao.class).update(qiRepairAreaSpace);
	}
	
	public boolean isRepairAreaUsedInLocalDefectCombination(String repairMethodName) {
		return getDao(QiLocalDefectCombinationDao.class).isRepairAreaUsed(repairMethodName);
	}
	
	public boolean isRepairAreaUsedInRepairAreaRow(String repairMethodName) {
		return getDao(QiRepairAreaRowDao.class).isRepairAreaUsed(repairMethodName);
	}
	
	public boolean isRepairAreaUsedInRepairAreaSpace(String repairMethodName) {
		return getDao(QiRepairAreaSpaceDao.class).isRepairAreaUsed(repairMethodName);
	}
		
	public void updateAllByRepairAreaName(String repairAreaName,String oldRepairAreaName,String updateUser) {
		getDao(QiLocalDefectCombinationDao.class).updateAllByRepairArea(repairAreaName,oldRepairAreaName,updateUser);
		getDao(QiRepairAreaRowDao.class).updateAllByRepairArea(repairAreaName,oldRepairAreaName,updateUser);
		getDao(QiRepairAreaSpaceDao.class).updateAllByRepairArea(repairAreaName,oldRepairAreaName,updateUser);
		getDao(QiDefectResultDao.class).updateAllByRepairArea(repairAreaName,oldRepairAreaName,updateUser);
	}
	
	public void clearRepairAreaSpace(QiRepairAreaSpaceId id, String updateUser) {
		QiRepairAreaSpace qiRepairAreaSpace = getDao(QiRepairAreaSpaceDao.class).findByKey(id);
		String productId = qiRepairAreaSpace.getProductId();
		Long defectResultId = qiRepairAreaSpace.getDefectResultId();
		if (productId != null || defectResultId != null) {
			getDao(QiRepairAreaSpaceDao.class).clearRepairAreaSpace(id, updateUser);
		}
		
		if (productId != null) {
			//remove repair area result from GAL177TBX if configured, primary key is product_id
			if (PropertyService.getPropertyBean(QiPropertyBean.class, getApplicationId()).isReplicateRepairAreaResult()) {
				getDao(InRepairAreaDao.class).removeByKey(productId);	
			}
		}
	}

	public boolean isRepairAreaUsedInDefectResult(String repairAreaName) {
		return getDao(QiDefectResultDao.class).isRepairAreaUsed(repairAreaName);
	}

	public boolean isIntransitRepairAreaExist(String siteName,String plantName) {
		return null != getDao(QiRepairAreaDao.class).findBySiteAndPlant(siteName,plantName);
	}
}
