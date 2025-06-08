package com.honda.galc.client.datacollection.view.knuckles;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.view.DataCollectionPanel;
import com.honda.galc.client.datacollection.view.PartLotViewManager;
import com.honda.galc.client.ui.component.Text;
/**
 * 
 * <h3>KnuckleViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnuckleViewManager description </p>
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
 * Nov 29, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class KnuckleOnViewManager extends PartLotViewManager {

	public KnuckleOnViewManager(ClientContext clientContext) {
		super(clientContext);
	}
	
	
	@Override
	protected DataCollectionPanel createDataCollectionPanel(
			DefaultViewProperty property) {
		if(view == null){
			view =  new ProductOnPanel(property, 
					viewManagerProperty.getMainWindowWidth(), 
					viewManagerProperty.getMainWindowHeight());
		}
		return view;
	}
	
	public ProductOnPanel getView(){
		return (ProductOnPanel) view;
	}
	
	@Override
	protected void initConnections() throws Exception {
		super.initConnections();
	}
	
	@Override
	protected void refreshScreen() {
		super.refreshScreen();
		view.getButton(3).setVisible(true);
	}


	@Override
	public void refreshScreen(int refreshingDelay) {
		super.refreshScreen(refreshingDelay);
		context.setRemake(false);
		
		view.getButton(2).setEnabled(true);
		view.getButton(2).setVisible(true);
	}
	
	
	@Override
	public void partVisibleControl(boolean eneble, boolean visible) {
		super.partVisibleControl(eneble, visible);
		view.getButton(3).setVisible(!visible);
	}
	

	@Override
	public void productIdOk(ProcessProduct state) {
		super.productIdOk(state);
		
		getView().getTextFieldSubId().getRender().renderField(new Text(state.getProduct().getSubId(),true));
		
		view.getButton(2).setEnabled(false);
		view.getButton(2).setVisible(false);
	}
	

	@Override
	public void initProductId(ProcessProduct state) {
		super.initProductId(state);
		getView().getPreProductionLotScreen().setData(context);
		getView().getKdLotField().setText(context.getCurrentPreProductionLot()==null ? "" : context.getCurrentPreProductionLot().getKdLot());
	}
	
	

}
