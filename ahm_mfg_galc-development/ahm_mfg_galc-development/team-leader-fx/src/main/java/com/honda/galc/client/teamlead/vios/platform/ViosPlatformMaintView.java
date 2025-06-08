package com.honda.galc.client.teamlead.vios.platform;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.vios.AbstractViosTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;

import javafx.scene.Node;
/**
 * <h3>ViosPlatformMaintView Class description</h3>
 * <p>
 * View for Vios Platform Maintenance
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
public class ViosPlatformMaintView extends AbstractViosTabbedView<ViosPlatformMaintModel, ViosPlatformMaintController> {
	
	private ViosPlatformMaintPanel panel;
	
	public ViosPlatformMaintView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
		panel = new ViosPlatformMaintPanel(this, true);
		getViosBorderPane().setCenter(panel);
	}

	@Override
	public String getScreenName() {
		return "VIOS Platform Maintenance";
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
		panel.loadData();
		panel.onPanelSelected();
	}

}
