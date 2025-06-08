package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.GenericDaoService;

/**
 * 
 * <h3>PruneProductTask Class description</h3>
 * <p> PruneProductTask description </p>
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
 * @author Jeffray Huang<br>
 * May 30, 2012
 *
 *
 */
public class PruneFrameTask extends PruneByProdutionLotTask{

	
	public PruneFrameTask(String name) {
		super(name,"GAL141TBX","Frame");
	}

	@Override
	public List<ProductionLot> getFinishedProductionLots() {
		return getService(GenericDaoService.class).
			findFinishedFrameProdLots(getProcessLocation(), getShippingPPID(), getStartProductionDate());
	}

	@Override
	public void pruneProductSpecificResults(ProductionLot productionLot) {
		
	}
	
	@Override
	protected void pruneByProductionDate() {

		deleteProductionResultByProductionDate("GAL260TBX", "QICS Station Result");
		super.pruneByProductionDate();
	}
	
	@Override
	public void postPruneByProductionLot() {
		
	}


}
