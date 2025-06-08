package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiPddaResponsibleLoadTriggerDao;
import com.honda.galc.dto.qi.QiDepartmentDto;
import com.honda.galc.dto.qi.QiPddaResponsibleLoadTriggerDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTrigger;

/**
 * 
 * <h3>ResponsibleLoadTriggeredModel Class description</h3>
 * <p> ResponsibleLoadTriggeredModel use to get and update data on Responsible Load Trigger Screen </p>
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
public class ResponsibleLoadTriggeredModel extends QiModel {
	
	public ResponsibleLoadTriggeredModel() {
		super();
	}
	/*
	 * this method is used to find all Resp company values
	 */
	public List<String> findAllRespCompanyByAdminConfirmedFix(short adminConfirmed) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findAllRespCompanyByAdminConfirmedFix(adminConfirmed);
	}
	/*
	 * this method is used to find all Resp plants values by company
	 */
	public List<String> findAllPlantsByCompany(String company, short adminConfirmed) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findAllPlantsByCompany(company,adminConfirmed);		
	}
	/*
	 * this method is used to find all product types by Resp company and plant
	 */
	public List<String> findAllProductsByCompanyAndPlant(String plant, String company, short adminConfirmed) {
		return 	getDao(QiPddaResponsibleLoadTriggerDao.class).findAllProductsByCompanyAndPlant(plant,company,adminConfirmed);	
	}
	/*
	 * this method is used to find all Resp departments values by company plant and product
	 */
	public List<String> findAllDepartmentsByCompanyAndPlantAndProduct(String company, String plant, String product, short adminConfirmed) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findAllDepartmentsByCompanyAndPlantAndProduct(company,plant,product,adminConfirmed);
	}
	/*
	 * this method is used to find all Resp departments values by company plant and product
	 */
	public List<QiDepartmentDto> findAllQiDepartmentByCompanyAndPlantAndProduct(String company, String plant, String product, short adminConfirmed) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findAllQiDepartmentByCompanyAndPlantAndProduct(company,plant,product,adminConfirmed);
	}
	/*
	 * this method is used to find all pddaResponsibilityData by company,plant,product and department
	 */
	public List<QiPddaResponsibleLoadTriggerDto> findPddaResponsibleTriggerData(String company, String plant, String product,String department, short adminConfirmed) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findAllByCompanyPlantProductAndDepartment(company,plant,product,department,adminConfirmed);
	}
	/*
	 * this method is used to QiPddaResponsibleLoadTrigger by pddaResponsibilityId
	 */
	public QiPddaResponsibleLoadTrigger findPddaResponsibleTriggerDataById(Integer id) {
		return getDao(QiPddaResponsibleLoadTriggerDao.class).findByPddaResponsibilityId(id);
	}
	/*
	 * this method is used to update QiPddaResponsibleLoadTrigger data
	 */
	public void updatePddaResponsibleLoadtriggerData(QiPddaResponsibleLoadTrigger qiPddaResponsibleLoadTrigger) {
		getDao(QiPddaResponsibleLoadTriggerDao.class).save(qiPddaResponsibleLoadTrigger);
	}
}
