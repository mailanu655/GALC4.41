package com.honda.galc.client.linesidemonitor.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.honda.galc.client.ui.ApplicationMainWindow;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.ViewUtil;

@Deprecated
public class LineSideMonitorLegacyPanel extends LineSideMonitorPanel {

	public LineSideMonitorLegacyPanel(MainWindow window) {
		super(window);
	}

	public LineSideMonitorLegacyPanel(ApplicationMainWindow window) {
		super(window);
	}

	public LineSideMonitorLegacyPanel(LineSideMonitorWindow window) {
		super(window);
	}

	@Override
	protected void initComponents() {
		boolean viewportLocked = getPropertyBean().getViewportRowCount() > -1;
		setLayout(new GridBagLayout());
		if (getPropertyBean().isWidescreen()) {
			ViewUtil.setGridBagConstraints(this, createTablePane(), 0,1,null,null,viewportLocked ? GridBagConstraints.HORIZONTAL : GridBagConstraints.BOTH,null,null,null,GridBagConstraints.PAGE_START,getPropertyBean().getTablePaneWeight(),1.0);
			if (getPropertyBean().isInfoVisible()) ViewUtil.setGridBagConstraints(this, createInfoPane(), 0,0,null,null,GridBagConstraints.HORIZONTAL,null,null,null,GridBagConstraints.PAGE_START,getPropertyBean().getTablePaneWeight(),getPropertyBean().getInfoPaneWeight());
			if (getPropertyBean().isAllowDataCollection()) ViewUtil.setGridBagConstraints(this, createDataCollectionPane(), 1,0,null,2,GridBagConstraints.BOTH,null,null,null,GridBagConstraints.FIRST_LINE_START,getPropertyBean().getDataCollectionPaneWeight(),1.0);
		} else {
			ViewUtil.setGridBagConstraints(this, createTablePane(), 0,1,null,null,viewportLocked ? GridBagConstraints.HORIZONTAL : GridBagConstraints.BOTH,null,null,null,GridBagConstraints.PAGE_START,1.0,viewportLocked ? 0.0 : getPropertyBean().getTablePaneWeight());
			if (getPropertyBean().isInfoVisible()) ViewUtil.setGridBagConstraints(this, createInfoPane(), 0,0,null,null,GridBagConstraints.HORIZONTAL,null,null,null,GridBagConstraints.PAGE_START,1.0,getPropertyBean().getInfoPaneWeight());
			if (getPropertyBean().isAllowDataCollection()) ViewUtil.setGridBagConstraints(this, createDataCollectionPane(), 0,2,null,null,GridBagConstraints.BOTH,null,null,null,GridBagConstraints.PAGE_START,1.0,viewportLocked ? 1.0 : getPropertyBean().getDataCollectionPaneWeight());
		}
	}

}
