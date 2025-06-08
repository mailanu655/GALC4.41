package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.GenericDaoService;

/**
 * 
 * <h3>PruneSubProductTask</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PruneSubProductTask description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Oct 30, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Oct 30, 2015
 */
public class PruneSubProductTask extends PruneByProdutionLotTask{

	private String productType;
	private String trackingProcessPointId;
	public PruneSubProductTask(String name) {
		super(name, "SUB_PRODUCT_TBX", "Sub Product");
		init();
	}

	private void init() {
		productType = getPropertyBean().getProductType();
		trackingProcessPointId = getPropertyBean().getTrackingProcessPointId();
	}

	@Override
	public List<ProductionLot> getFinishedProductionLots() {
		return getService(GenericDaoService.class).findIntactSubProductProdLotsForGivenDate(trackingProcessPointId, getStartProductionDate(), productType);
	}

	@Override
	public void pruneProductionResults(ProductionLot productionLot) {
		
		for(TableDefinitions.TableDefinition table : TableDefinitions.subProductTables.getTables()) {
			
			int count = getService(GenericDaoService.class).deleteAllFinishedSubProducts(
					table.getTableName(), productType, productionLot.getProductionLot());
			
			logger.info("deleted " + count + " " + table.getTableName() + " " + 
					 table.getTableDescription() +  " data of production lot " + productionLot.getProductionLot());
		}
	}
	
	
	@Override
	public void pruneProductionLot(ProductionLot productionLot) {
		int count = getService(GenericDaoService.class).deleteProductionLot("SUB_PRODUCT_SHIPPING_TBX",productionLot.getProductionLot());
		putCount("SUB_PRODUCT_SHIPPING_TBX",count);
		logger.info("deleted " + count + " SUB_PRODUCT_SHIPPING_TBX Vanning Schedule of production lot - " + productionLot.getProductionLot());
	    
		super.pruneProductionLot(productionLot);
	}
	
	@Override
	public void postPruneByProductionLot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pruneProductSpecificResults(ProductionLot productionLot) {
		// TODO Auto-generated method stub
		
	}

}
