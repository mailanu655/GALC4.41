package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IViosDao;
import com.honda.galc.vios.dto.PddaPlatformDto;
/**
 * <h3>MCViosMasterPlatformDao Class description</h3>
 * <p>
 * Interface for MCViosMasterPlatformDaoImpl
 * </p>
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public interface MCViosMasterPlatformDao extends IDaoService<MCViosMasterPlatform, String>, IViosDao<MCViosMasterPlatform> {
	
	public List<MCViosMasterPlatform> findAllPlatforms();
	
	public void saveEntity(MCViosMasterPlatform mcopsPlatform);
	
	public MCViosMasterPlatform removeAndInsert(MCViosMasterPlatform oldPlatform, MCViosMasterPlatform newPlatform);
	
	public List<String> findAllPlants();
	
	public List<String> findAllDeptBy(String plantCode);
	
	public List<BigDecimal> findAllModelYearBy(String plantCode, String dept);
	
	public List<BigDecimal> findAllProdQtyBy(String plantCode, String dept, BigDecimal modelYearDate);
	
	public List<String> findAllLineNoBy(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty);
	
	public List<String> findAllVMCBy(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty, String lineNo);
	
	public List<PddaPlatformDto> findAllPlatformsByPlantLocCodeAndDeptCode(String plantLocCode, String deptCode);
	
	public List<PddaPlatformDto> findAllActivePlatformBy(String processPointId, BigDecimal modelYearDate);
	
}
