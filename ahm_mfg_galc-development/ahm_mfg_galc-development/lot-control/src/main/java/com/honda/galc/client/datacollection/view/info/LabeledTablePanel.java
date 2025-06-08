package com.honda.galc.client.datacollection.view.info;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.honda.galc.client.common.component.PropertyTableModel;
import com.honda.galc.client.ui.component.TablePane;
/**
 * 
 * <h3>LabeledScrollTablePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LabeledScrollTablePanel description </p>
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
 * May 31, 2010
 *
 */
public class LabeledTablePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private TablePane tablePanel;
	private JLabel label;
	private String title;

	public LabeledTablePanel() {
		super();
		initialize();
	}

	public LabeledTablePanel(String title) {
		this.title = title;
		initialize();
	}

	private void initialize() {
		 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		 add(getLabel());
		 add(getTablePanel());

	}

	public TablePane getTablePanel() {

		if(tablePanel == null){
			tablePanel = new TablePane();
			tablePanel.setPreferredSize(new Dimension(LotControlSystemInfo.WIN_WIDTH,LotControlSystemInfo.WIN_HIGHT));
			new PropertyTableModel(null, tablePanel.getTable());
		}
		return tablePanel;
	}

	public JLabel getLabel() {
		if(label == null){
			label = new JLabel(title);
		}
		return label;
	}



}
