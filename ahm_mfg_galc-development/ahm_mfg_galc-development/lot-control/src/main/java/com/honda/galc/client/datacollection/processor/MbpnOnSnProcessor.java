package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.MbpnProductOnService;
 
/**
 * 
 * <h3>MbpnOnSnProcessor</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Processor for Mbpn On SN processer which insert the product id if not in the MBPN_PRODUCT_TBX along with
 * product spec code mapped for the product in PRODUCT_ID_MASK_TBX. Property CHECK_PRODUCT_SPEC has to be true 
 * in order to check PRODUCT_ID_MASK_TBX table.
 * </p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * March 05, 2016
 * 
 */

public class MbpnOnSnProcessor extends MBPNProcessor{
	
	public MbpnOnSnProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	@Override
	protected BaseProduct getProductFromServer() {
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(
				product.getProductId());
		if (aproduct == null) {
			DataContainer dc = new DefaultDataContainer();
			dc.setClientID(context.getProcessPointId());
			dc.put(DataContainerTag.PRODUCT_ID.toString(), product.getProductId());

			DataContainer dc1 = new DefaultDataContainer();

			dc1 = ServiceFactory.getService(MbpnProductOnService.class).execute(dc);
			if (dc1.containsKey(DataContainerTag.ERROR_CODE)) {
				Logger.getLogger().info(
						"Error occur at MBPN OnService Process due to "
								+ dc1.getString(DataContainerTag.ERROR_CODE));
			}
			Logger.getLogger().info("MBPN OnService Process Successful");
			if (dc1.containsKey(DataContainerTag.DATA_COLLECTION_COMPLETE)){
				if (dc1.getString(TagNames.DATA_COLLECTION_COMPLETE.name())
						.equalsIgnoreCase(LineSideContainerValue.COMPLETE)) {
					aproduct = context.getDbManager().confirmProductOnServer(product.getProductId());
					product.setProductSpec(aproduct.getProductSpecCode());
					
					if (aproduct.getProductionLot() != null)
						product.setProductionLot(aproduct.getProductionLot());
				} else
					throw new TaskException("Unable to process product " + product.getProductId() + ". No Product Spec specified.");
			}else
				throw new TaskException("Unable to process product. Reply data not setup for the process point");

		}

		else
			product.setProductSpec(aproduct.getProductSpecCode());

		return aproduct;
	}

}
