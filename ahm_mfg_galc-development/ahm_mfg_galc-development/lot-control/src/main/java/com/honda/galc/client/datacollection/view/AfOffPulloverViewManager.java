package com.honda.galc.client.datacollection.view;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.entity.enumtype.StrategyType;

public class AfOffPulloverViewManager extends PartLotViewManager {

	public AfOffPulloverViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	/**
	protected void skipNextProduct() {
		List<String> list = new ArrayList<String>();
		list.add(DataCollectionController.getInstance().getState().getProductId());
		DataCollectionController.getInstance().getState().getStateBean().setPullOverProductList(list);
		DataCollectionController.getInstance().getState().getStateBean().getProduct().getPartList().clear();
		
		SkipNextExpectedProductAction action = new SkipNextExpectedProductAction(context, "Skip Product Ok");
		action.doNextVIN();
	}
	*/
	public void partSnNg(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		renderFieldBeanNg(partSerialNumber, state.getCurrentInstallPart().getPartSerialNumber());
		if(state.getCurrentLotControlRule().getStrategy().equals(StrategyType.ENGINE_VIN_CONFIRMATION.name()))
			partSerialNumber.setEditable(false);
		partSerialNumber.requestFocus();

	}
}
