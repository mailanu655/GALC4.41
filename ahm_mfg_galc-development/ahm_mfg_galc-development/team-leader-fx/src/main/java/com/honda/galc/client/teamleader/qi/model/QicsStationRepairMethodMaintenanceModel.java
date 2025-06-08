package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import com.honda.galc.dao.qi.QiRepairMethodDao;
import com.honda.galc.dao.qi.QiStationRepairMethodDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiStationRepairMethod;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QicsStationRepairMethodMaintenanceModel Class description</h3>
 * <p> QicsStationRepairMethodMaintenanceModel description </p>
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
 *
 *
 */

public class QicsStationRepairMethodMaintenanceModel extends QiModel {

	public QicsStationRepairMethodMaintenanceModel() {
		super();
		
	}
	
	/**
	 * This method is used to find all Division based on Site and Plant.
	 * @param siteName,plantName.
	 * @return List of Division.
	 * **/
	  public List<Division> findById(String siteName, String plantName){
		return getDao(DivisionDao.class).findById(siteName,plantName);
	  }
	
	
	  /**
		 * This method is used to find all Qics Station(Process Point) based on Division.
		 * @param divisionId.
		 * @return List of Qics station(Process Point).
		 * **/
		public List<ProcessPoint> findQicsStationByApplicationComponentDivision(String divisionId) {
			return getDao(ProcessPointDao.class).findAllByApplicationComponentDivision(divisionId);
		}
	
	/**This method is used to find all Repair methods
	 * @return List of all repair method
	 */
	public List<QiRepairMethod> findAllActiveRepairMethod() {
		return getDao(QiRepairMethodDao.class).findAllActiveRepairMethods();
	}
	
	/**
	 * This method is used to find all Repair method based on Qics station(Process Point).
	 * @param qicsStaion.
	 * @return List of RepairMethod.
	 * **/
	public List<QiRepairMethod> findAllRepairMethodByQicsStation(String qicsStation) {
		return getDao(QiRepairMethodDao.class).findAllRepairMethodsByQicsStation(qicsStation);
	}
	
	/**
	 * This method is used to find all Assinged Repair method based on Division.
	 * @param qicsStaion.
	 * @return List of RepairMethod.
	 * **/
	public List<QiRepairMethod> findAllAssignedRepairMethodByDivision(String division) {
		return getDao(QiRepairMethodDao.class).findAllAssignedRepairMethodsByDivision(division);
	}
	
	/**This method is used to find all Station Repair method
	 * @return List of all station repair method
	 */
	public List<QiStationRepairMethod> findAllStationRepairMethod() {
		return getDao(QiStationRepairMethodDao.class).findAll();
		
	}
	
	/** 
	 * This method is used to find all plant by site name
	 * @param siteName
	 * @return list of plant
	 */
	public List<Plant> findAllBySite(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}
	
	
	/**
	 * Find filtered Repair methods
	 */
	public List<QiRepairMethod> findReairMethodByFilter(String repairMethodFilter) {
		return getDao(QiRepairMethodDao.class).findFilteredRepairMethods(repairMethodFilter);
	}
	
	/**
	 * Find filtered repair methods for (already assigned) Selected Repair methods
	 */
	public List<QiRepairMethod> findFilteredRepairMethodsForSelectedDivision(String repairMethodFilter,String division) {
		return getDao(QiRepairMethodDao.class).findFilteredRepairMethodsForSelectedDivision(repairMethodFilter,division);
	}
	
	
	/**This method is used to get site name
	 * @return
	 */
	public String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
		
	}
 
	/**
	 * This method is used to insert data into repair method process point
	 * @param qiStationRepairMethod
	 */
	public void createQiStationRepairMethod(QiStationRepairMethod qiStationRepairMethod) {
			 getDao(QiStationRepairMethodDao.class).insert(qiStationRepairMethod);
	}
	
	/**
	 * This method is used to remove data based on qiStationRepairMethod entity list
	 * @param qiStationRepairMethodList
	 */
	public void deleteQiStationRepairMethods(List<QiStationRepairMethod> qiStationRepairMethodList) {
	 	getDao(QiStationRepairMethodDao.class).removeAll(qiStationRepairMethodList);

	}
	
	
}
