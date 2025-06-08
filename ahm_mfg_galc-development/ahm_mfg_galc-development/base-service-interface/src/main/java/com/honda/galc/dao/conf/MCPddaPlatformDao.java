package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dto.PddaDetailDto;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.service.IDaoService;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCPddaPlatformDao extends IDaoService<MCPddaPlatform, Integer> {

	public List<MCPddaPlatform> getPDDAPlatformForOperation(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo);
	
	public MCPddaPlatform getPDDAPlatformForOperation(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId);
	
	public MCPddaPlatform getAprvdPDDAPlatformExceptRev(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId);
	
	public List<MCPddaPlatform> getAllAprvdPDDAPlatformExceptRev(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId);
	
	public MCPddaPlatform getLatestAprvdPDDAPlatform(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo);

	public List<MCPddaPlatform> getAllActivePlatforms();
	
	public List<MCPddaPlatform> getAprvdPDDAPlatforms();
	public List<MCPddaPlatform> getAprvdPDDAPlatformsforMatrix(long revId);
	
	public List<MCPddaPlatform> getPlatformsForRevision(long revId);
	
	public List<Long> getUnapprovedOldRevisions(long revisionId);
	
	public List<PddaDetailDto> findPlatformsByStructureRevision(long structureRev);
	
	public List<String> findAllPlants();
	
	public List<String> findAllDeptBy(String plantCode);
	
	public List<Long> findAllModelYearBy(String plantCode, String dept);
	
	public List<Long> findAllProdQtyBy(String plantCode, String dept, BigDecimal modelYearDate);
	
	public List<String> findAllLineNo(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty);
	
	public List<String> findAllVMC(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty, String lineNo);
	
	public List<PddaPlatformDto> findAllPlatformsByPlantLocCodeAndDeptCode(String plantLocCode, String deptCode);
	
	public List<MCPddaPlatform> findAllByPddaPlatform(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode);
	
}
