package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FrameMTOCMasterSpecDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameMTOCMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>FrameMTOCMasterSpecDaoImpl.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameMTOCMasterSpecDaoImpl.java description </p>
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
 * <TD>PB</TD>
 * <TD>September 11, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kenneth Gibson
 * @created September 11, 2014
 */
public class FrameMTOCMasterSpecDaoImpl extends BaseDaoImpl<FrameMTOCMasterSpec,FrameMTOCMasterSpecId> 
		implements FrameMTOCMasterSpecDao {

	public FrameMTOCMasterSpec getFrameMTOCMasterSpec(FrameSpecDto selectedFrameSpecDto) {
		
		Parameters parameters = Parameters.with("id.modelYearCode", selectedFrameSpecDto.getModelYearCode())
				.put("id.modelCode", selectedFrameSpecDto.getModelCode()).put("id.modelOptionCode", selectedFrameSpecDto.getModelOptionCode())
				.put("id.extColorCode", selectedFrameSpecDto.getExtColorCode());
		
		List<FrameMTOCMasterSpec> frameMTOCMasterSpecList = findAll(parameters);
		if(frameMTOCMasterSpecList.size() >= 1) {
			FrameMTOCMasterSpec frameMTOCMasterSpec = frameMTOCMasterSpecList.get(0);
			return frameMTOCMasterSpec;
		}
		return null;
		
	}
}
