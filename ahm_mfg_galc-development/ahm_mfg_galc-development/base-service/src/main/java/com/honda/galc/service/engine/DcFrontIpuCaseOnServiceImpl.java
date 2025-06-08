package com.honda.galc.service.engine;

import com.honda.galc.dao.product.FrontIpuCaseDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.FrontIpuCase;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>DcFrontIpuCaseOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcFrontIpuCaseOnServiceImpl description </p>
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
 * <TD>Kamlesh Maharjan</TD>
 * <TD>Jun 10, 2024</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * @since Jun 10, 2024
 */
public class DcFrontIpuCaseOnServiceImpl extends DiecastOn<FrontIpuCase> implements DcFrontIpuCaseOnService{
	public DcFrontIpuCaseOnServiceImpl() {
		context.setProductType(ProductType.FIPUCASE);
	}

	@Override
	public void saveDiecast(FrontIpuCase frontIpuCase) {
		frontIpuCase.setProductId(frontIpuCase.getDcSerialNumber());
		ServiceFactory.getDao(FrontIpuCaseDao.class).save(frontIpuCase);
		
	}

}
