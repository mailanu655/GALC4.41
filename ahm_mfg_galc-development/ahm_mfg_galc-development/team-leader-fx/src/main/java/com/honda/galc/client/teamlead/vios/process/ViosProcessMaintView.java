package com.honda.galc.client.teamlead.vios.process;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.vios.AbstractViosTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;

import javafx.scene.Node;
/**
 * <h3>ViosProcessMaintView Class description</h3>
 * <p>
 * View for Vios Process Maintenance
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
 * 
 * </TABLE>
 * 
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class ViosProcessMaintView extends AbstractViosTabbedView<ViosProcessMaintModel, ViosProcessMaintController> {

	private ViosProcessMaintPanel panel;
	
	public ViosProcessMaintView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
		panel = new ViosProcessMaintPanel(this, 0 , null);
		getViosBorderPane().setCenter(panel);
	}

	@Override
	public String getScreenName() {
		return "VIOS Process Maintenance";
	}

	@Override
	public void reload() {
		
	}

	@Override
	public void start() {
		
	}

	@Override
	public Node getTopBody() {
		return null;
	}

	@Override
	public Node getCenterBody() {
		return null;
	}

	@Override
	public void onScreenSelected() {
		panel.onPanelSelected();
	}

}
