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
import com.honda.galc.client.ui.tablemodel.PreProductionLotTableModel;
import com.honda.galc.entity.product.PreProductionLot;

/**
 * 
 * <h3>KnuckleKdLotSelectionDialog Class description</h3>
 * <p> KnuckleKdLotSelectionDialog description </p>
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

public class KnuckleKdLotSelectionDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private PreProductionLot selectedLot = null;
	
	private TablePane preProductionLotPane;
	
	private PreProductionLotTableModel preProductionLotTableModel;
	
	private OkCancelButtonPanel okCancelButtonPanel = new OkCancelButtonPanel();
	
	
	public KnuckleKdLotSelectionDialog(JFrame frame, List<PreProductionLot> preProdLots) {
		
		super(frame,true);
		setTitle("Select Knuckle Dialog");
		setSize(600, 200);
	    
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		preProductionLotPane = new TablePane("Knucle Kd Lot List");
		preProductionLotPane.setPreferredWidth(550);
		preProductionLotTableModel = new PreProductionLotTableModel(preProductionLotPane.getTable(),preProdLots);
		preProductionLotTableModel.pack();
		preProductionLotPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		mainPanel.add(preProductionLotPane,BorderLayout.CENTER);
		mainPanel.add(okCancelButtonPanel,BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
		
		okCancelButtonPanel.getOkButton().addActionListener(this);
		okCancelButtonPanel.getCancelButton().addActionListener(this);
		
		
		pack();
	}


	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == okCancelButtonPanel.getOkButton()) {
			selectedLot = preProductionLotTableModel.getSelectedItem();
		}
		
		this.dispose();

	}
	
	public PreProductionLot getSelectedLot() {
		return selectedLot;
	}
	
	
	
}
