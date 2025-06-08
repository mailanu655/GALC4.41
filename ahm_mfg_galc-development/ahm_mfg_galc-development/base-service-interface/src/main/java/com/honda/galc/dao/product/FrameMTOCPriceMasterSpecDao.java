package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>FrameMTOCPriceMasterSpecDao.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameMTOCPriceMasterSpecDao.java description </p>
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
 * <TD>August 1, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kenneth Gibson
 * @created August 1, 2014
 */
public interface FrameMTOCPriceMasterSpecDao extends IDaoService<FrameMTOCPriceMasterSpec, FrameMTOCPriceMasterSpecId>{
	
	public FrameMTOCPriceMasterSpec findByProductIdAndSpecCode(String vin, String processPoint60A);
    public String getMTOCPrice(String mtoc, Integer effectiveDate);
    /**
     * Get Price for current production date
     * @param productSpecCode
     * @param dateStr 
     * @return
     */
	public String getPriceForProductionDate(String productSpecCode, String dateStr);
	public FrameMTOCPriceMasterSpec getFrameMTOCPriceMasterSpec(FrameSpecDto selectedFrameSpecDto);

}
