package com.honda.galc.service.datacollection.work;

import java.util.List;

import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
/**
 * 
 * <h3>DataCollection</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollection description </p>
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
 * <TD>Mar 12, 2014</TD>
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
 * @since Mar 12, 2014
 */
public class DataCollection extends CollectorWork{

	protected List<ProductBuildResult> buildResults = null;
	
	public DataCollection(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
	}

	@Override
	void doWork() throws Exception {
		context.updateDevice();
		collector.collectData();
	}

}
