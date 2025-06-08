package com.honda.galc.dao.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCRevisionDao
	extends IDaoService<MCRevision, Long>{
	
	public Long getMaxRevId();
	
	public void setRevisionStatus(long revId, RevisionStatus revisionStatus);
	
	public List<MCRevision> findAllByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
	
	public List<MCRevisionDto> findAllByPddaPlatformWithControlNumber(String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode);
	
	public void updateRevisionDescription(long revId, String description,RevisionStatus revisionStatus);
	

}
