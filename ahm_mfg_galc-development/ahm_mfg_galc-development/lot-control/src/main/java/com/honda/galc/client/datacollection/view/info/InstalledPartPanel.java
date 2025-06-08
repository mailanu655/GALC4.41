package com.honda.galc.client.datacollection.view.info;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.honda.galc.client.common.data.InstalledPartTableModel;
import com.honda.galc.client.common.data.MeasurementTableModel;
import com.honda.galc.client.ui.component.TablePane;
/**
 * 
 * <h3>InstalledPartPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartPanel description </p>
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
 * Jun 1, 2010
 *
 */
public class InstalledPartPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private TablePane installedPartTablePanel;
	private TablePane measurementTablePanel;
	private Dimension preferredSize = new Dimension(450, 250);;

	public InstalledPartPanel() {
		super();
		initialize();
	}

	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Installed Part:"));
		add(getInstalledPartTablePanel());
		add(new JLabel("Measurement:"));
		add(getMeasurementTablePanel());
	}
	
	public TablePane getInstalledPartTablePanel() {
		if(installedPartTablePanel == null){
			installedPartTablePanel = new TablePane();
			installedPartTablePanel.setPreferredSize(preferredSize );
			new InstalledPartTableModel(null, installedPartTablePanel.getTable());
		}
		return installedPartTablePanel;
	}

	public TablePane getMeasurementTablePanel() {
		if(measurementTablePanel == null){
			measurementTablePanel = new TablePane();
			measurementTablePanel.setPreferredSize(preferredSize);
			new MeasurementTableModel(null, measurementTablePanel.getTable());
		}
		return measurementTablePanel;
	}
	
	

}
