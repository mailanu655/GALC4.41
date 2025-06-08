package com.honda.galc.client.teamleader.mbpn;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.datacollection.view.info.LabeledTablePanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductIdNumberDef;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>MbpnTypeManagementPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnTypePanel description </p>
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
 * <TD>May 26, 2017</TD>
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
 * @since May 26, 2017
 */
public class MbpnTypeManagementPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabeledTablePanel mbpnTypePanel;
	private JPanel mbpnButtonPanel;
	private JPanel productNumberDefPanel;
	private JButton removeButton;
	private JButton newButton;
	private JButton updateButton;
	private JButton saveButton;
	private LabeledTablePanel productNoDefTablePanel;
	private LabeledTablePanel assignedProductNoDefTablePanel;
	private JPanel productNumberDefButtonPanel;
	private JButton prodDefAssignButton;
	private JButton prodDefDeassignButton;
	private Dimension screenDimension;
	private JPanel productNumberMgtPanel;
	
	private MainWindow window;
	enum ProductIdNumberDefEvent {ADD, REMOVE};
	
	public MbpnTypeManagementPanel(MainWindow window, MbpnTypeTreePanel mbpnTypeTree) {
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.window = window;
		init();
	}

	private void init() {
		try {
			initComponents();
		} catch (Exception e) {
			handleException(e);
		}
	}


	private void initComponents() {
		setLayout(new MigLayout());
		add(getMbpnTypePanel(),"grow, push, height 50:100:,wrap");
		add(getMbpnButtonPanel(),"growx, gap bottom 20, wrap");
		add(getProductNumberDefPanel(),"grow, push");
	}

	public LabeledTablePanel getMbpnTypePanel() {
		if(mbpnTypePanel == null){
			mbpnTypePanel = new LabeledTablePanel();
			mbpnTypePanel.getTablePanel().getTable().setRowHeight(20);
			mbpnTypePanel.setPreferredSize(new Dimension((int)screenDimension.getWidth()/3, (int)screenDimension.getHeight()/10));
			new MbpnTableModel(null, mbpnTypePanel.getTablePanel().getTable(), new String[]{}, new String[]{} , new String[]{}, true);
		}	
		return mbpnTypePanel;
	}

	public void setMbpnTypePanel(LabeledTablePanel mbpnTypePanel) {
		this.mbpnTypePanel = mbpnTypePanel;
	}

	public JPanel getMbpnButtonPanel() {
		if(mbpnButtonPanel == null){
			mbpnButtonPanel = new JPanel();
			mbpnButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			mbpnButtonPanel.add(getRemoveButton());
			mbpnButtonPanel.add(getNewButton());
			mbpnButtonPanel.add(getUpdateButton());
			mbpnButtonPanel.add(getSaveButton());
		}
		return mbpnButtonPanel;
	}

	public JButton getRemoveButton() {
		if(removeButton == null)
			removeButton = new JButton("Remove");
		return removeButton;
	}

	public void setRemoveButton(JButton removeButton) {
		this.removeButton = removeButton;
	}

	public JButton getSaveButton() {
		if(saveButton == null){
			saveButton = new JButton("Save");
		}
		return saveButton;
	}

	public JButton getUpdateButton() {
		if(updateButton == null)
			updateButton = new JButton("Update");
		return updateButton;
	}

	public JButton getNewButton() {
		if(newButton == null)
			newButton = new JButton("Add");
		return newButton;
	}

	public void setMbpnButtonPanel(JPanel mbpnButtonPanel) {
		this.mbpnButtonPanel = mbpnButtonPanel;
	}

	public JPanel getProductNumberDefPanel() {
		if(productNumberDefPanel == null){
			productNumberDefPanel = new JPanel();
			TitledBorder border = new TitledBorder("Product Number Definitions");
			productNumberDefPanel.setBorder(border);
			productNumberDefPanel.setLayout(new MigLayout());
			productNumberDefPanel.add(getProductNumberMgtPanel(),"grow, spanx");
		}
		return productNumberDefPanel;
	}
	
	private JPanel getProductNumberMgtPanel() {
		if(productNumberMgtPanel == null){
			productNumberMgtPanel= new JPanel(new MigLayout());
			productNumberDefPanel.add(getProductNoDefTablePanel(),"grow, push");
			productNumberDefPanel.add(getProductNoDefButtonPanel(),"pushx");
			productNumberDefPanel.add(getAssignedProductNoDefTablePanel(),"grow, push");
		}
		return productNumberMgtPanel;
	}
	
	public void setProductNumberDefPanel(JPanel productNumberDefPanel) {
		this.productNumberDefPanel = productNumberDefPanel;
	}
	
	public LabeledTablePanel getAssignedProductNoDefTablePanel() {
		if (this.assignedProductNoDefTablePanel == null){
			this.assignedProductNoDefTablePanel = new LabeledTablePanel("Assigned Definitions");
			this.assignedProductNoDefTablePanel.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			new ProductNumberDefTableModel(new ArrayList<ProductIdNumberDef>(), assignedProductNoDefTablePanel.getTablePanel().getTable(), false);
		}
		return assignedProductNoDefTablePanel;
	}

	private Component getProductNoDefButtonPanel() {
		if(productNumberDefButtonPanel == null){
			productNumberDefButtonPanel = new JPanel();
			productNumberDefButtonPanel.setLayout(new BoxLayout(productNumberDefButtonPanel, BoxLayout.Y_AXIS));
			productNumberDefButtonPanel.add(getProdDefAssignButton());
			productNumberDefButtonPanel.add(new JSeparator());
			productNumberDefButtonPanel.add(getProdDefDeassignButton());
		}
		return productNumberDefButtonPanel;
	}

	public JButton getProdDefDeassignButton() {
		if(prodDefDeassignButton == null)
			prodDefDeassignButton  = new JButton("<html><h2><b>&#8592<b></h2></html>");
		return prodDefDeassignButton;
	}

	public JButton getProdDefAssignButton() {
		if(prodDefAssignButton ==  null)
			prodDefAssignButton = new JButton("<html><h2><b>&#8594<b></h2></html>");
		return prodDefAssignButton;
	}

	public LabeledTablePanel getProductNoDefTablePanel() {
		if (this.productNoDefTablePanel == null) {
			this.productNoDefTablePanel = new LabeledTablePanel("Unassigned Definitions");
			this.productNoDefTablePanel.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			this.productNoDefTablePanel.getTablePanel().getTable().setRowHeight(20);
		}
		return this.productNoDefTablePanel;
	}

	protected void handleException (Exception e) {
		if(e != null) {
			Logger.getLogger().error(e, "unexpected exception occurs: " + e.getMessage());
			this.window.setMessage(e.getMessage());
		} else {
			window.clearMessage();
		}
	}
}