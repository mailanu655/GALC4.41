package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCRevisionDaoImpl 
	extends BaseDaoImpl<MCRevision, Long> 
	implements MCRevisionDao {
	
	private static final String FIND_ALL_BY_PDDA_PLATFORM = "SELECT DISTINCT a.* FROM galadm.MC_REV_TBX a " +
										"join galadm.MC_PDDA_CHG_TBX b on a.REV_ID=b.REV_ID " +
										"join vios.PVCFR1 c on b.CHANGE_FORM_ID=c.CHANGE_FORM_ID " + 
										"where c.PLANT_LOC_CODE = ?1 and c.DEPT_CODE = ?2  and c.MODEL_YEAR_DATE = ?3  and c.PROD_SCH_QTY = ?4 and " + 
										"c.PROD_ASM_LINE_NO = ?5 and c.VEHICLE_MODEL_CODE = ?6";
	
	
	private static final String FIND_ALL_BY_PDDA_PLATFORM_WITHCONTROLNUMBER = "SELECT a.REV_DESC, a.REV_STATUS, a.REV_TYPE, c.CONTROL_NO, a.REV_ID, a.ASSOCIATE_NO FROM galadm.MC_REV_TBX a " +
			"join galadm.MC_PDDA_CHG_TBX b on a.REV_ID=b.REV_ID " +
			"join vios.PVCFR1 c on b.CHANGE_FORM_ID=c.CHANGE_FORM_ID " + 
			"where c.PLANT_LOC_CODE = ?1 and c.DEPT_CODE = ?2  and c.MODEL_YEAR_DATE = ?3  and c.PROD_SCH_QTY = ?4 and " + 
			"c.PROD_ASM_LINE_NO = ?5 and c.VEHICLE_MODEL_CODE = ?6 order by a.REV_ID desc";

	

	public Long getMaxRevId() {
		return max("id", Long.class);
		
	}

	@Transactional
	public void setRevisionStatus(long revId, RevisionStatus revisionStatus) {
		MCRevision revision = findByKey(revId);
		if(revision!=null) {
			revision.setStatus(revisionStatus.getRevStatus());
			save(revision);
		}
	}
	@Transactional
	@Override
	public void updateRevisionDescription(long revId, String description,RevisionStatus revisionStatus) {
		MCRevision revision = findByKey(revId);
		if(revision!=null) {
			revision.setStatus(revisionStatus.getRevStatus());
			revision.setDescription(description);
			update(revision);
		}
	}
	@Override
	public List<MCRevision> findAllByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode)
				.put("3", modelYearDate).put("4", prodSchQty)
				.put("5", prodAsmLineNo).put("6", vehicleModelCode);
		return findAllByNativeQuery(FIND_ALL_BY_PDDA_PLATFORM, params, MCRevision.class);
	}
	
	
	@Override
	public List<MCRevisionDto> findAllByPddaPlatformWithControlNumber(String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode)
				.put("3", modelYearDate).put("4", prodSchQty)
				.put("5", prodAsmLineNo).put("6", vehicleModelCode);
		List<MCRevisionDto> list = findAllByNativeQuery(FIND_ALL_BY_PDDA_PLATFORM_WITHCONTROLNUMBER, params, MCRevisionDto.class);
		return list;
	}


}
