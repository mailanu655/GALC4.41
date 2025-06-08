package com.honda.galc.dao.product;

import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameMTOCMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>FrameMTOCMasterSpecDao.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameMTOCMasterSpecDao.java description </p>
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
public interface FrameMTOCMasterSpecDao extends IDaoService<FrameMTOCMasterSpec, FrameMTOCMasterSpecId>{

	public FrameMTOCMasterSpec getFrameMTOCMasterSpec(FrameSpecDto selectedFrameSpecDto);

}
