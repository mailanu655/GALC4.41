package com.honda.galc.dao.jpa.qi;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPddaResponsibleLoadTriggerDao;
import com.honda.galc.dto.qi.QiDepartmentDto;
import com.honda.galc.dto.qi.QiPddaResponsibleLoadTriggerDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTrigger;
import com.honda.galc.entity.qi.QiPddaResponsibleLoadTriggerId;
import com.honda.galc.service.Parameters;

public class QiPddaResponsibleLoadTriggerDaoImpl extends BaseDaoImpl<QiPddaResponsibleLoadTrigger,QiPddaResponsibleLoadTriggerId> implements QiPddaResponsibleLoadTriggerDao {

	private final static String FIND_ALL_COMPANY ="Select distinct PR.RESP_COMPANY "
			+ " from galadm.QI_PDDA_RESPONSIBILITY_TBX PR join galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT "
			+ " on PR.PDDA_RESPONSIBILITY_ID =PRLT.PDDA_RESPONSIBILITY_ID ";
	
	private final static String FIND_ALL_PLANTS_BY_COMPANY= "Select distinct PR.RESP_PLANT from "
			+ " galadm.QI_PDDA_RESPONSIBILITY_TBX PR join galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT "
			+ " on PR.PDDA_RESPONSIBILITY_ID =PRLT.PDDA_RESPONSIBILITY_ID and PR.RESP_COMPANY=?1 ";
	
	private final static String FIND_ALL_PRODUCTS_BY_PLANT_AND_COMPANY= "Select distinct PR.PRODUCT_TYPE "
			+ " from galadm.QI_PDDA_RESPONSIBILITY_TBX PR join galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT "
			+ " on PR.PDDA_RESPONSIBILITY_ID =PRLT.PDDA_RESPONSIBILITY_ID and PR.RESP_PLANT=?1 and PR.RESP_COMPANY=?2 ";
	
	private final static String FIND_ALL_DEPARTMENTS_BY_PRODUCT_AND_PLANT_AND_COMPANY= "Select distinct PR.RESP_DEPT "
			+ " from galadm.QI_PDDA_RESPONSIBILITY_TBX PR join galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT "
			+ " on PR.PDDA_RESPONSIBILITY_ID =PRLT.PDDA_RESPONSIBILITY_ID and PR.RESP_COMPANY=?1 and PR.RESP_PLANT=?2 and PR.PRODUCT_TYPE=?3 ";

	private final static String FIND_ALL_QIDEPT_BY_PRODUCT_AND_PLANT_AND_COMPANY=
			"select distinct d.DEPT,d.DEPT_NAME,d.DEPT_DESCRIPTION from QI_PDDA_RESPONSIBILITY_TBX r "
			+ " join QI_PDDA_RESP_LOAD_TRIGGER_TBX t on r.PDDA_RESPONSIBILITY_ID = t.PDDA_RESPONSIBILITY_ID"
			+ " join QI_DEPT_TBX d on r.RESP_DEPT=d.DEPT"
			+ " where r.RESP_COMPANY=?1 and r.RESP_PLANT=?2 and r.PRODUCT_TYPE=?3";

	private final static String FIND_ALL_PPDA_RESPONSIBILITY_DATA_BY_COMPANY_PLANT_PRODUCT_AND_DEPARTMENT="Select PDDA_RESPONSIBILITY_ID ,PCL_TO_QICS_SEQ_KEY,'InProd' as DATA_LOCATION ,'' as ADMIN_CONFIRMED_FIX,UPDATE_TIMESTAMP as DATE_TIMESTAMP,PDDA_LINE as LINE,BASE_PART_NO as BASE_PART_NO,RESP_LEVEL2 as TEAM_GROUP_NO,RESP_LEVEL1 as TEAM_NO,RESP_LEVEL1_DESC as TEAM_NAME,PROCESS_NUMBER as PROCESS_NO,PROCESS_NAME as PROCESS_NAME,UNIT_NUMBER as UNIT_NO,UNIT_DESC as UNIT_DESC from GALADM.QI_PDDA_RESPONSIBILITY_TBX"
			+ " Where RESP_COMPANY = ?1 and RESP_PLANT = ?2 and PRODUCT_TYPE = ?3 and RESP_DEPT = ?4 and PDDA_RESPONSIBILITY_ID in"
			+ " (Select PRLT.PDDA_RESPONSIBILITY_ID from GALADM.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT join GALADM.QI_PDDA_RESPONSIBILITY_TBX PR"
			+ " on PRLT.PDDA_RESPONSIBILITY_ID = PR.PDDA_RESPONSIBILITY_ID ";
	private final static String FIND_ALL_PPDA_RESPONSIBILITY_LOAD_TRIGGER_DATA_BY_COMPANY_PLANT_PRODUCT_AND_DEPARTMENT=" Select PDDA_RESPONSIBILITY_ID ,PCL_TO_QICS_SEQ_KEY,'Prev' as DATA_LOCATION ,Case ADMIN_CONFIRMED_FIX when 0 then 'No' else 'Yes' end as ADMIN_CONFIRMED_FIX,CREATE_TIMESTAMP as DATE_TIMESTAMP,PREV_PDDA_LINE as LINE,PREV_BASE_PART_NO as BASE_PART_NO,'' as TEAM_GROUP_NO,"
			+ " PREV_RESP_LEVEL1 as TEAM_NO,PREV_RESP_LEVEL1_DESC as TEAM_NAME,PREV_PROCESS_NUMBER as PROCESS_NO,PREV_PROCESS_NAME as PROCESS_NAME,'' as UNIT_NO,PREV_UNIT_DESC as UNIT_DESC from galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT "
			+ " Where PDDA_RESPONSIBILITY_ID in "
			+ " (Select PR.PDDA_RESPONSIBILITY_ID from GALADM.QI_PDDA_RESPONSIBILITY_TBX PR join galadm.QI_PDDA_RESP_LOAD_TRIGGER_TBX PRLT on PRLT.PDDA_RESPONSIBILITY_ID = PR.PDDA_RESPONSIBILITY_ID"
			+ " and PR.RESP_COMPANY = ?1 and PR.RESP_PLANT = ?2 and PR.PRODUCT_TYPE = ?3 and PR.RESP_DEPT = ?4 ";
	/*
	 * this method is used to find all Resp company values
	 */
	public List<String> findAllCompanyNames(short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_COMPANY);
		Parameters params = new Parameters();
		if(adminConfirmed<2){
		    params.put("1", adminConfirmed);
		    query.append("and PRLT.ADMIN_CONFIRMED_FIX = ?1");
		}
		return findResultListByNativeQuery(query.toString(),params);
	}
	/*
	 * this method is used to find all Resp plants values by company
	 */
	public List<String> findAllPlantsByCompany(String company, short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_PLANTS_BY_COMPANY);
		Parameters params = new Parameters();
		params.put("1", company);
		if(adminConfirmed<2){
			params.put("2", adminConfirmed);
			query.append("and PRLT.ADMIN_CONFIRMED_FIX =?2");
		}
	    return findResultListByNativeQuery(query.toString(), params);
	}
	/*
	 * this method is used to find all product types by Resp company and plant
	 */
	public List<String> findAllProductsByCompanyAndPlant(String plant, String company, short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_PRODUCTS_BY_PLANT_AND_COMPANY);
		Parameters params = new Parameters();
		params.put("1", plant).put("2", company);
		if(adminConfirmed<2){
			params.put("3", adminConfirmed);
			query.append(" and PRLT.ADMIN_CONFIRMED_FIX =?3");
		}
		return findResultListByNativeQuery(query.toString(),params);
	}
	/*
	 * this method is used to find all Resp departments values by company plant and product
	 */
	public List<String> findAllDepartmentsByCompanyAndPlantAndProduct(String company, String plant, String product,short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_DEPARTMENTS_BY_PRODUCT_AND_PLANT_AND_COMPANY);
		Parameters params = new Parameters();
		params.put("1", company).put("2", plant).put("3", product);
		if(adminConfirmed<2){
			params.put("4", adminConfirmed);
			query.append(" and PRLT.ADMIN_CONFIRMED_FIX =?4");
		}
		return findResultListByNativeQuery(query.toString(), params);
	}
	/*
	 * this method is used to find all Resp departments values by company plant and product
	 */
	public List<QiDepartmentDto> findAllQiDepartmentByCompanyAndPlantAndProduct(String company, String plant, String product,short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_QIDEPT_BY_PRODUCT_AND_PLANT_AND_COMPANY);
		Parameters params = new Parameters();
		params.put("1", company).put("2", plant).put("3", product);
		if(adminConfirmed<2){
			String suffix = String.format(" and t.ADMIN_CONFIRMED_FIX=%d",adminConfirmed);
			query.append(suffix);
		}
		return findAllByNativeQuery(query.toString(), params,QiDepartmentDto.class);
	}
	/*
	 * this method is used to find all pddaResponsibilityData by company,plant,product and department
	 */
	public List<QiPddaResponsibleLoadTriggerDto> findAllByCompanyPlantProductAndDepartment(String company, String plant,String product, String department, short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_PPDA_RESPONSIBILITY_DATA_BY_COMPANY_PLANT_PRODUCT_AND_DEPARTMENT);
		String union= " ) Union ";
		String orderBy= " ) Order by PDDA_RESPONSIBILITY_ID ,DATA_LOCATION ,DATE_TIMESTAMP Desc";
		String adminConfirm="where ADMIN_CONFIRMED_FIX = ?5";
		Parameters params = new Parameters();
		params.put("1", company).put("2", plant).put("3", product).put("4", department);
		if(adminConfirmed<2){
			params.put("5", adminConfirmed);
			query.append(adminConfirm).append(union).append(FIND_ALL_PPDA_RESPONSIBILITY_LOAD_TRIGGER_DATA_BY_COMPANY_PLANT_PRODUCT_AND_DEPARTMENT)
			.append(adminConfirm).append(orderBy);
		}else{
			query.append(union).append(FIND_ALL_PPDA_RESPONSIBILITY_LOAD_TRIGGER_DATA_BY_COMPANY_PLANT_PRODUCT_AND_DEPARTMENT).append(orderBy);
		}
		return findAllByNativeQuery(query.toString(),params, QiPddaResponsibleLoadTriggerDto.class);
	}
	/*
	 * this method is used to QiPddaResponsibleLoadTrigger by pddaResponsibilityId
	 */
	public QiPddaResponsibleLoadTrigger findByPddaResponsibilityId(Integer id) {
		return findFirst(Parameters.with("id.pddaResponsibilityId", id));
	}
   /* 
    * @return List<QiPddaResponsibleLoadTrigger>
	*/
	public List<QiPddaResponsibleLoadTrigger> findAllByAdminConfirmedFix() {
		return findAll(Parameters.with("adminConfirmedFix", (short)0));
		
	}
	public List<String> findAllRespCompanyByAdminConfirmedFix(
			short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_COMPANY);
		Parameters params = new Parameters();
		if(adminConfirmed<2){
		    params.put("1", adminConfirmed);
		    query.append("and PRLT.ADMIN_CONFIRMED_FIX = ?1");
		}
		return findResultListByNativeQuery(query.toString(),params);
	}

}
