package com.honda.galc.client.datacollection.observer.knuckles;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.IExpectedProductManager;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>LotControlKnucklesPersistenceManagerExt</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlKnucklesPersistenceManagerExt description </p>
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
 * @author Paul Chou
 * Dec 22, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlKnucklesPersistenceManagerExt extends LotControlKnucklesPersistenceManager{

	
	public LotControlKnucklesPersistenceManagerExt(ClientContext context) {
		super(context);
	}

	@Override
	public IExpectedProductManager getExpectedProductManger() {
		if(expectedProductManger == null){
			if(isKnucklesOn())
				expectedProductManger = new KnucklesOnSequenceManager(context);
			else
				expectedProductManger = new KnucklesSequenceManager(context);
		}
		
		return expectedProductManger;
	}

	
	
	private boolean isKnucklesOn() {
		return context.getProcessPointId().equals(context.getProperty().getOnProcessPointId());
	}


	@Override
	public void saveCompleteData(ProcessProduct state) {
		
		savePartLot(state);
		expectedProductManger.updatePreProductionLot();
		expectedProductManger.updateProductSequence(state);
		
		if(context.isOnLine())
			trackProduct(state);
		
		if(context.getProperty().isSaveSkippedProduct()){
			trackSkippedProduct(state);
		}
		
		saveExpectedProduct(state);
		
		if(state.getProduct() == null) {
			Logger.getLogger().warn("Failed to save collected data for product id is null");
			return; 
		}
		
		if(state.getProduct().getPartList().size() > 0){
			Logger.getLogger().debug("PersistentManager save InstalledPart on Server");
			saveCollectedData(state);
		}

	}
	

	@Override
	protected void saveExpectedProduct(ProcessProduct state) {
		if(context.isOnLine() && context.isCheckExpectedProductId()){
			if(isKnucklesOn())
				getExpectedProductManger().saveNextExpectedProduct(state);
			else if(!isSkippedProduct(state))
				getExpectedProductManger().saveNextExpectedProduct(state);
				
		}
		else if(!context.isOnLine())
			state.setExpectedProductId(null);
		
		saveLastExpected(state);
	}
}
