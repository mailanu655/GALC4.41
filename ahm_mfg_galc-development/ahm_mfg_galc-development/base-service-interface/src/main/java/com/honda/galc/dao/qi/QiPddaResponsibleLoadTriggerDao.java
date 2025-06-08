package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiDepartmentDto;
import com.honda.galc.dto.qi.QiPddaResponsibleLoadTriggerDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTrigger;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTriggerId;
import com.honda.galc.service.IDaoService;

public interface QiPddaResponsibleLoadTriggerDao extends IDaoService<QiPddaResponsibleLoadTrigger, QiPddaResponsibleLoadTriggerId> {

	/*
	 * this method is used to find all Resp company values
	 */
	public List<String> findAllRespCompanyByAdminConfirmedFix(short adminConfirmed);
	/*
	 * this method is used to find all Resp plants values by company
	 */
	public List<String> findAllPlantsByCompany(String company, short adminConfirmed);
	/*
	 * this method is used to find all product types by Resp company and plant
	 */
	public List<String> findAllProductsByCompanyAndPlant(String plant, String company, short adminConfirmed);
	/*
	 * this method is used to find all Resp departments values by company plant and product
	 */
	public List<String> findAllDepartmentsByCompanyAndPlantAndProduct(String company, String plant, String product,short adminConfirmed);
	/*
	 * this method is used to find all pddaResponsibilityData by company,plant,product and department
	 */
	public List<QiPddaResponsibleLoadTriggerDto> findAllByCompanyPlantProductAndDepartment(String company, String plant,
			String product, String department, short adminConfirmed);
	/*
	 * this method is used to QiPddaResponsibleLoadTrigger by pddaResponsibilityId
	 */
	public QiPddaResponsibleLoadTrigger findByPddaResponsibilityId(Integer id);
	
	public List<QiPddaResponsibleLoadTrigger> findAllByAdminConfirmedFix();
	List<QiDepartmentDto> findAllQiDepartmentByCompanyAndPlantAndProduct(String company, String plant, String product, short adminConfirmed);

}
