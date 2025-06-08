package com.honda.galc.client.knuckle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.honda.galc.client.ui.OkCancelButtonPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.SubProductTableModel;
import com.honda.galc.entity.product.SubProduct;

/**
 * 
 * <h3>KnuckleSelectionDialog Class description</h3>
 * <p> KnuckleSelectionDialog description </p>
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
 * @author Jeffray Huang<br>
 * Jan 13, 2011
 *
 *
 */

public class KnuckleSelectionDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private SubProduct subProduct = null;
	
	private TablePane subProductPane;
	
	private SubProductTableModel subProductTableModel;
	
	private OkCancelButtonPanel okCancelButtonPanel = new OkCancelButtonPanel();
	
	
	public KnuckleSelectionDialog(JFrame frame, List<SubProduct> subProducts) {
		
		super(frame,true);
		setTitle("Select Knuckle Dialog");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		subProductPane = new TablePane("Knucle Serial Number List");
		subProductTableModel = new SubProductTableModel(subProductPane.getTable(),subProducts);
		
		subProductPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		mainPanel.add(subProductPane,BorderLayout.CENTER);
		mainPanel.add(okCancelButtonPanel,BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
		
		okCancelButtonPanel.getOkButton().addActionListener(this);
		okCancelButtonPanel.getCancelButton().addActionListener(this);
		
		
		pack();
	}


	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == okCancelButtonPanel.getOkButton()) {
			subProduct = subProductTableModel.getSelectedItem();
		}
		
		this.dispose();

	}
	
	public SubProduct getSelectedSubProduct() {
		return subProduct;
	}
	
	
	
}
