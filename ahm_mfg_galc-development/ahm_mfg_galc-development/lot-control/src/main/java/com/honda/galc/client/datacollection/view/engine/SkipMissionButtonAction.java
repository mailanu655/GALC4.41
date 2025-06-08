package com.honda.galc.client.datacollection.view.engine;

import java.awt.event.ActionEvent;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.strategy.MissionInstall;
import com.honda.galc.client.datacollection.view.action.SkipPartButtonAction;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.net.Request;

/**
 * 
 * <h3>SkipMissionButtonAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SkipMissionButtonAction description </p>
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
 * Mar 10, 2011
 *
 */
public class SkipMissionButtonAction extends SkipPartButtonAction{
	
	public SkipMissionButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	private static final long serialVersionUID = 1L;

	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().stopRepeatedSound();

		if(getProperty().isNeedAuthorizedUserToSkipPart() && !login()) return;

		logInfo();

		if(isSkipMissingRequiredPart()){
			
			DataCollectionController.getInstance().getFsm().message(new Message(
					MissionInstall.SKIP_MISSING_REQUIRED_PART, "", MessageType.INFO));
			
		} else if(isMissionPart()){

			if(MessageDialog.confirm(null, "Do you really want to skip the current mission?")){
				
				Logger.getLogger().info("Skip mission for product ",
				DataCollectionController.getInstance().getState().getProductId());

				skipProduct(e);
			}

		} else 
			skipCurrentPart();
	}
	
	private void skipProduct(ActionEvent e) {
		try {
			
			EventBus.publish(new SkippedProduct(new SkippedProductId(
					DataCollectionController.getInstance().getState().getProductId(),
					context.getProcessPointId())));
			
			runInSeparateThread(new Request("skipProduct"));
			
		} catch (java.lang.Throwable t) {
			handleException(t);
		}

	}

	private boolean isSkipMissingRequiredPart(){
		DataCollectionState state = DataCollectionController.getInstance().getState();
		return (state.getProduct().getMissingRequiredPartList() != null &&
				state.getProduct().getMissingRequiredPartList().size() > 0 &&
				state.getCurrentPartIndex() == 0);
	}
		
		
	private boolean isMissionPart() {
		DataCollectionState state = DataCollectionController.getInstance().getState();
		return (getProperty().getMissionPartName().equals(state.getCurrentPartName().trim()) || 
				getProperty().getMissionTypePartName().equals(state.getCurrentPartName().trim()));
	}
}
