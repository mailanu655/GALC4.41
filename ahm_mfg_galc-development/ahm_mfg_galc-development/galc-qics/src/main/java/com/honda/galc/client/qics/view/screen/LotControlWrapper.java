package com.honda.galc.client.qics.view.screen;

import java.awt.BorderLayout;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.view.IViewManager;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.frame.QicsLotControlFrame;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;

/**
 * 
 * <h3>LotControlWrapper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlWrapper description </p>
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
 * <TD>Apr 27, 2011</TD>
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
 * @since Apr 27, 2011
 */

public class LotControlWrapper extends LotControlPanel {
	private static final long serialVersionUID = 1L;
	private IViewManager viewManager;
	
	public LotControlWrapper(QicsFrame frame) {
		super(frame);
		initialize();
		
	}
	protected void initialize() {
		setLayout(new BorderLayout());
		setSize(getTabPaneWidth(), getTabPaneHeight());
		
		AnnotationProcessor.process(this);
		
		add(getViewManager().getView());

		DataCollectionController.getInstance().getClientContext().setQicsSupport(true);
	}
	
	@Override
	public void startProduct() {
		final ProductId productId = new ProductId(getProductModel().getInputNumber());
		Thread t = new Thread(){
			public void run() {
				Object received = DataCollectionController.getInstance().received(productId);
				System.out.println("result:" + received.getClass().getSimpleName());
				if(received instanceof DataCollectionComplete){
					DataCollectionComplete result = (DataCollectionComplete)received;
					if(DataCollectionComplete.OK.equals(result.getCompleteFlag()))
						setLotControlEnabled(true);
				}
			}
		};
		
		t.start();
	}
	
	public IViewManager getViewManager() {
		if(viewManager == null)
			viewManager = ((QicsLotControlFrame)getQicsFrame()).getViewManager();
		
		return viewManager;
	}
	
	@EventSubscriber(eventClass = Event.class)
	public void processState(Event event) {
		if (!(event.getSource() instanceof DataCollectionState && event.getTarget() instanceof Action)) {
			return;
		}
		DataCollectionState state = (DataCollectionState) event.getSource();
		Action action = (Action) event.getTarget();
		Logger.getLogger().debug("processState:" + state.getClass() + ":" + action.toString());
		if (action == Action.CANCEL || action == Action.SKIP_PRODUCT) {
			fireCancel();
		} else if (action == Action.COMPLETE && state instanceof ProcessProduct) {
			setLotControlEnabled(false);
		} else if (action == Action.INIT && state instanceof ProcessProduct) {
			disableProductId();
	}
	}

	private void disableProductId() {
		viewManager.getView().getTextFieldProdId().setEnabled(false);
		viewManager.getView().getTextFieldProdId().setVisible(false);

	}
	

	private void fireCancel() {
		getQicsFrame().displayIdleView();
		getQicsFrame().sendDataCollectionNotCompleteToPlcIfDefined();
	}

}
