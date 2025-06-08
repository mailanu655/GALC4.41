package com.honda.galc.dao.jpa.qi;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiPddaResponsibilityDao;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.service.Parameters;

public class QiPddaResponsibilityDaoImpl extends BaseDaoImpl<QiPddaResponsibility, Integer> implements QiPddaResponsibilityDao {

	private static final String FIND_ALL_MODEL_YEAR = "select distinct e.modelYear from QiPddaResponsibility e order by e.modelYear"; 
	
	private static final String FIND_ALL_BY_MODEL_YEAR = "select distinct e.vehicleModelCode from QiPddaResponsibility e where e.modelYear = :modelYear order by e.vehicleModelCode";
	
	private static final String FIND_ALL_BY_MODEL_YEAR_AND_VMC = "select distinct e.processNumber from QiPddaResponsibility e where e.modelYear = :modelYear and e.vehicleModelCode = :vehicleModelCode order by e.processNumber";
	
	private static final String FIND_ALL_MODEL_YEAR_VMC_AND_PROCESS_NO = "select distinct e.unitNumber from QiPddaResponsibility e where e.modelYear = :modelYear and e.vehicleModelCode = :vehicleModelCode  and e.processNumber = :processNumber";

	private static final String FIND_ALL_MODEL_YEAR_BY_SITE_PLANT_DEPT = "select distinct e.modelYear from QiPddaResponsibility e " +
			" where e.responsibleSite=:responsibleSite and e.responsiblePlant=:responsiblePlant and e.responsibleDept=:responsibleDept " +
			" and e.responsibleLevel1 =:responsibleLevel1 and e.pddaLine=:pddaLine"+
			" order by e.modelYear";

	private static final String FIND_ALL_VMC_BY_SITE_PLANT_DEPT_MODEL_YEAR = "select distinct e.vehicleModelCode from QiPddaResponsibility e " +
			" where  e.responsibleSite=:responsibleSite and e.responsiblePlant=:responsiblePlant and e.responsibleDept=:responsibleDept " +
			"  and e.responsibleLevel1 =:responsibleLevel1 and e.pddaLine=:pddaLine and e.modelYear=:modelYear "+
			" order by e.vehicleModelCode";

	private static final String FIND_PROCESS_NO_BY_SITE_PLANT_DEPT_MODEL_YEAR_VMC = "select distinct e.PROCESS_NUMBER || ' - ' || e.PROCESS_NAME as  PROCESS_NUMBER from galadm.QI_PDDA_RESPONSIBILITY_TBX e "+
					" where e.RESP_SITE=?1 and e.RESP_PLANT=?2 and e.RESP_DEPT=?3 "+
					" and e.RESP_LEVEL1 =?4 and e.PDDA_LINE=?5 and e.MODEL_YEAR=?6 and e.VEHICLE_MODEL_CODE=?7 "+
					" order by PROCESS_NUMBER";

	private static final String FIND_ALL_UNIT_NO_BY_SITE_PLANT_DEPT_MODEL_YEAR_VMC_PROCESS_NO = "select distinct e.UNIT_NUMBER || ' - ' || e.UNIT_DESC as UNIT_NUMBER from galadm.QI_PDDA_RESPONSIBILITY_TBX e "+
			" where e.RESP_SITE=?1 and e.RESP_PLANT=?2 and e.RESP_DEPT=?3 "+
			" and e.RESP_LEVEL1 =?4 and e.PDDA_LINE=?5 and e.MODEL_YEAR=?6 and e.VEHICLE_MODEL_CODE=?7  and e.PROCESS_NUMBER=?8 "+
			" order by UNIT_NUMBER";
	
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
	
	private final static String FIND_BY_NEW_MODLE_YEAR = "SELECT * FROM GALADM.QI_PDDA_RESPONSIBILITY_TBX WHERE " 
			+ "MODEL_YEAR=?1 AND RESP_COMPANY=?2 AND RESP_SITE=?3 AND RESP_PLANT=?4 AND RESP_DEPT=?5 AND "
			+ "VEHICLE_MODEL_CODE=?6 AND UNIT_NUMBER=?7";

	public List<BigDecimal> findAllModelYear(){
		return findByQuery(FIND_ALL_MODEL_YEAR, BigDecimal.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByModelYear(BigDecimal modelYear){
		Parameters params = Parameters.with("modelYear", modelYear);
		return findResultListByQuery(FIND_ALL_BY_MODEL_YEAR, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByModelYearAndVMC(String modelYear, String vmc){
		Parameters params = Parameters.with("modelYear", modelYear).put("vehicleModelCode", vmc);
		return findResultListByQuery(FIND_ALL_BY_MODEL_YEAR_AND_VMC, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByModelYearVMCAndProcessNumber(String modelYear, String vmc, String processNo){
		Parameters params = Parameters.with("modelYear", modelYear).put("vehicleModelCode", vmc).put("processNumber", processNo);
		return findResultListByQuery(FIND_ALL_MODEL_YEAR_VMC_AND_PROCESS_NO, params);
	}
	
	public QiPddaResponsibility findByModelYearVMCProcessNoAndUnitNumber(String modelYear, String vmc, String processNo, String unitNo){
		Parameters params = Parameters.with("modelYear", modelYear).put("vehicleModelCode", vmc).put("processNumber", processNo).put("unitNumber", unitNo);
		return findFirst(params);
	}

	/** This method is used to return existing pddaresponsibility for selected data.
	 * @param qipddaresponsibility
	 * @return QiPddaResponsibility
	 */
	public QiPddaResponsibility findByCompanyPlantDeptModelYearVMCUnitNoPddaLine(QiPddaResponsibility qipddaresponsibility){
		Parameters params = Parameters.with("responsibleCompany", qipddaresponsibility.getResponsibleCompany()).put("entryPlant", qipddaresponsibility.getEntryPlant())
				.put("rowType", "P").put("responsiblePlant", qipddaresponsibility.getResponsiblePlant())
				.put("pddaDeptCode", qipddaresponsibility.getPddaDeptCode().equals("")?null:qipddaresponsibility.getPddaDeptCode())
				.put("vehicleModelCode", qipddaresponsibility.getVehicleModelCode())
				.put("modelYear", qipddaresponsibility.getModelYear())
				.put("unitNumber", qipddaresponsibility.getUnitNumber())
				.put("pddaLine", qipddaresponsibility.getPddaLine());
		return findFirst(params);
	}
	
	/** This method is used to return list of model year based on entryPlant,entrySite,productType,respPlant,respSite,respLevel1,respDept,basePartNo,pddaLine.
	 * @param respPlant
	 * @param respSite
	 * @param respDept
	 * @param respLevel1
	 * @param pddaLine
	 * @return List<BigDecimal>
	 */
	public List<BigDecimal> findAllModelYearByPlantSiteDeptLine(String respPlant, String respSite, String respDept,String respLevel1, String pddaLine) {
		Parameters params = Parameters.with("responsibleSite", respSite).put("responsiblePlant", respPlant)
				.put("responsibleDept", respDept)
				.put("pddaLine", pddaLine)
				.put("responsibleLevel1", respLevel1);
		return findResultListByQuery(FIND_ALL_MODEL_YEAR_BY_SITE_PLANT_DEPT, params);
		
	}
	
	/** This method is used to return list of vehicle model code based on entryPlant,entrySite,productType,respPlant,respSite,respLevel1,respDept,basePartNo,pddaLine,modelYear.
	 * @param respPlant
	 * @param respSite
	 * @param respDept
	 * @param respLevel1
	 * @param pddaLine
	 * @param modelYear
	 * @return List<BigDecimal>
	 */
	public List<String> findAllVehicleModelCodeByPlantSiteDeptLineModelYear(String respPlant, String respSite, String respDept,
			String respLevel1, String pddaLine,BigDecimal modelYear) {
		Parameters params = Parameters.with("responsibleSite", respSite).put("responsiblePlant", respPlant)
				.put("responsibleDept", respDept)
				.put("pddaLine", pddaLine)
				.put("responsibleLevel1", respLevel1)
				.put("modelYear", modelYear);
		return findResultListByQuery(FIND_ALL_VMC_BY_SITE_PLANT_DEPT_MODEL_YEAR, params);
		
	}
	
	/** This method is used to return list of process number based on entryPlant,entrySite,productType,respPlant,respSite,respLevel1,respDept,basePartNo,pddaLine.modelyear,vehiclemodelCode.
	 * @param respPlant
	 * @param respSite
	 * @param respDept
	 * @param respLevel1
	 * @param pddaLine
	 * @param modelYear
	 * @param vehicleModelCode
	 * @return List<BigDecimal>
	 */
	public List<String> findAllProcessNoByPlantSiteDeptLineModelYearVMC(String respPlant, String respSite, String respDept,
			String respLevel1, String pddaLine,BigDecimal modelYear,String vehicleModelCode) {
		Parameters params = Parameters.with("1", respSite).put("2", respPlant)
				.put("3", respDept)
				.put("5", pddaLine)
				.put("4", respLevel1)
				.put("6", modelYear)
				.put("7", vehicleModelCode);
		return findResultListByNativeQuery(FIND_PROCESS_NO_BY_SITE_PLANT_DEPT_MODEL_YEAR_VMC, params);
		
	}
	
	/** This method is used to return list of unit number based on entryPlant,entrySite,productType,respPlant,respSite,respLevel1,respDept,basePartNo,pddaLine,modelYear,veicleModelCode,processNo.
	 * @param respPlant
	 * @param respSite
	 * @param respDept
	 * @param respLevel1
	 * @param pddaLine
	 * @param modelYear
	 * @param vehicleModelCode
	 * @param processNumber
	 * @return List<String>
	 */
	public List<String> findAllUnitNoByPlantSiteDeptLineModelYearVMCProcessNo(String respPlant, String respSite, String respDept,
			String respLevel1, String pddaLine,BigDecimal modelYear,String vehicleModelCode,String processNumber ) {
		Parameters params = Parameters.with("1", respSite).put("2", respPlant)
				.put("3", respDept)
				.put("5", pddaLine)
				.put("4", respLevel1)
				.put("6", modelYear)
				.put("7", vehicleModelCode)
				.put("8", processNumber);
		return findResultListByNativeQuery(FIND_ALL_UNIT_NO_BY_SITE_PLANT_DEPT_MODEL_YEAR_VMC_PROCESS_NO, params);
		
	}
	
	/** This method is used to return pddaResponsibility based on entryPlant,entrySite,productType,respPlant,respSite,respLevel1,respDept,basePartNo,pddaLine,modelYear,vehicleModelCode,processNumber,unitNumber.
	 * @param respPlant
	 * @param respSite
	 * @param respDept
	 * @param respLevel1
	 * @param pddaLine
	 * @param modelYear
	 * @param vehicleModelCode
	 * @param processNumber
	 * @param unitNumber
	 * @return List<BigDecimal>
	 */
	public QiPddaResponsibility findByPlantSiteDeptLineModelYearVMCProcessNoUnitNo(String respPlant, String respSite, String respDept,
			String respLevel1, String pddaLine,BigDecimal modelYear,String vehicleModelCode,String processNumber,String unitNumber) {
		Parameters params = Parameters.with("responsibleSite", respSite).put("responsiblePlant", respPlant)
				.put("responsibleDept", respDept)
				.put("pddaLine", pddaLine)
				.put("responsibleLevel1", respLevel1)
				.put("modelYear", modelYear)
				.put("vehicleModelCode", vehicleModelCode)
				.put("processNumber", processNumber)
				.put("unitNumber", unitNumber);
		return findFirst(params);
		
	}
	
	/**
	 * This method is used to find all Resp company values
	 * @param adminConfirmed
	 * @return List<String>
	 */
	public List<String> findAllRespCompanyByAdminConfirmedFix(short adminConfirmed) {
		StringBuilder query= new StringBuilder(FIND_ALL_COMPANY);
		Parameters params = new Parameters();
		if(adminConfirmed<2){
		    params.put("1", adminConfirmed);
		    query.append("and PRLT.ADMIN_CONFIRMED_FIX = ?1");
		}
		return findResultListByNativeQuery(query.toString(),params);
	}
	
	/**
	 * This method is used to find all Resp plants values by company
	 *  @param adminConfirmed
	 *  @param company
	 * @return List<String>
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
	
	/**
	 * This method is used to find all product types by Resp company and plant
	 * @param plant
	 * @param company
	 * @param adminConfirmed
	 * @return List<String>
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
	
	/**
	 * This method is used to find all Resp departments values by company plant and product
	 * @param plant
	 * @param company
	 * @param product
	 * @param adminConfirmed
	 * @return List<String>
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
	
	/**
	 * This method is used to find all PDDA responsibility by new model year and old PDDA responsibility
	 * @param modelYear
	 * @param oldQiPddaResponsibility
	 * @return List<QiPddaResponsibility>
	 */
	public List<QiPddaResponsibility> findByNewModelYear(BigDecimal modelYear, QiPddaResponsibility oldQiPddaResponsibility) {
		Parameters params = Parameters.with("1", modelYear)
				.put("2", oldQiPddaResponsibility.getResponsibleCompany())
				.put("3", oldQiPddaResponsibility.getResponsibleSite())
				.put("4", oldQiPddaResponsibility.getResponsiblePlant())
				.put("5", oldQiPddaResponsibility.getResponsibleDept())
				.put("6", oldQiPddaResponsibility.getVehicleModelCode())
				.put("7", oldQiPddaResponsibility.getUnitNumber());
		return findAllByNativeQuery(FIND_BY_NEW_MODLE_YEAR, params);
	}
}
