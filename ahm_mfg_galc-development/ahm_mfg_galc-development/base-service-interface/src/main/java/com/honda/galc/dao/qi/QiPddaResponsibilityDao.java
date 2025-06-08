package com.honda.galc.dao.qi;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Interface description</h3> 
 * <p>
 * <code>QiPddaResponibilityDao</code> is a DAO interface to implement database interaction for PddaResponibility.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public interface QiPddaResponsibilityDao extends IDaoService<QiPddaResponsibility, Integer>{

	public List<BigDecimal> findAllModelYear();
	
	public List<String> findAllByModelYear(BigDecimal modelYear);
	
	public List<String> findAllByModelYearAndVMC(String modelYear, String vmc);
	
	public List<String> findAllByModelYearVMCAndProcessNumber(String modelYear, String vmc, String processNo);
	
	public QiPddaResponsibility findByModelYearVMCProcessNoAndUnitNumber(String modelYear, String vmc, String processNo, String unitNo);

	public QiPddaResponsibility findByCompanyPlantDeptModelYearVMCUnitNoPddaLine(QiPddaResponsibility qipddaresponsibility);
	
	public List<BigDecimal> findAllModelYearByPlantSiteDeptLine(String respPlant, String respSite, String respDept,String respLevel1, String pddaLine);
	
	public List<String> findAllVehicleModelCodeByPlantSiteDeptLineModelYear( String respPlant, String respSite, String respDept,String respLevel1, String pddaLine,
			BigDecimal modelYear);
	
	public List<String> findAllProcessNoByPlantSiteDeptLineModelYearVMC( String respPlant, String respSite, String respDept,String respLevel1, String pddaLine,
			BigDecimal modelYear,String vehicleModelCode);
	
	public List<String> findAllUnitNoByPlantSiteDeptLineModelYearVMCProcessNo( String respPlant, String respSite, String respDept,String respLevel1, String pddaLine,
			BigDecimal modelYear,String vehicleModelCode,String processNumber );
	
	public QiPddaResponsibility findByPlantSiteDeptLineModelYearVMCProcessNoUnitNo( String respPlant, String respSite, String respDept,
			String respLevel1, String pddaLine,BigDecimal modelYear,String vehicleModelCode,String processNumber,String unitNumber);
	
	public List<String> findAllRespCompanyByAdminConfirmedFix(short adminConfirmed);
	
	public List<String> findAllPlantsByCompany(String company, short adminConfirmed);
	
	public List<String> findAllProductsByCompanyAndPlant(String plant, String company, short adminConfirmed);
	
	public List<String> findAllDepartmentsByCompanyAndPlantAndProduct(String company, String plant, String product,short adminConfirmed);

	public List<QiPddaResponsibility> findByNewModelYear(BigDecimal modelYear, QiPddaResponsibility oldQiPddaResponsibility);

}
