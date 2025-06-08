package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.MbpnProductType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.MbpnProductOnService;
import com.honda.galc.util.MbpnSpecCodeUtil;
 
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

public class PreProductionLotMbpnOnSnProcessor extends MbpnProductIdProcessor{
	
	public PreProductionLotMbpnOnSnProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}
	
	@Override
	protected List<MbpnProductType> getMbpnProductTypes() {
		
		List<MbpnProductType> mbpnProductTypes = super.getMbpnProductTypes();
		
		String productSpecCode = context.getCurrentPreProductionLot().getProductSpecCode();
		
		String mainNo = MbpnSpecCodeUtil.getMainNo(productSpecCode);
		
		List<MbpnProductType> filteredList = new ArrayList<MbpnProductType>();
		
		for (MbpnProductType type : mbpnProductTypes) {
			if(type.getId().getMainNo().equalsIgnoreCase(mainNo)) filteredList.add(type);
		}
		
		return filteredList;
		 
	}
	
	
	
	@Override
	protected BaseProduct getProductFromServer() {
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(
				product.getProductId());
		if (aproduct == null) {
			DataContainer dc = new DefaultDataContainer();
			dc.setClientID(context.getProcessPointId());
			dc.put(DataContainerTag.PRODUCT_ID.toString(), product.getProductId());
			dc.put(DataContainerTag.PRODUCTION_LOT.toString(),context.getCurrentPreProductionLot().getProductionLot());
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE, context.getCurrentPreProductionLot().getProductSpecCode());
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
					
					if (aproduct.getProductionLot() != null) {
						product.setProductionLot(aproduct.getProductionLot());
						product.setBaseProduct(aproduct);
					}
				} else
					throw new TaskException("Unable to process product " + product.getProductId() + ". No Product Spec specified.");
			}else
				throw new TaskException("Unable to process product. Reply data not setup for the process point");

		}

		else {
			throw new TaskException("Product " +  product.getProductId() + " has been processed on " + aproduct.getLastPassingProcessPointId(), "MBPN Product ON Processor");
		}

		return aproduct;
	}
	
	protected void validateProduct(BaseProduct aproduct) {
		//Check hold status here if required.
		//checkFrameHoldStatus();


		if(property.isCheckProcessedProduct()) {

			//Check cache first then database
			checkProcessedProductOnLocalCache();
			checkProcessedProduct(aproduct.getProductId());
		}

	}
	
	public void checkExpectedProduct(ProductId productId) {
	}

}
