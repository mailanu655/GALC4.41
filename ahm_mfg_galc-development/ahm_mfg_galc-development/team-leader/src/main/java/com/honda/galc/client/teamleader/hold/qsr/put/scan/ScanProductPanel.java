package com.honda.galc.client.teamleader.hold.qsr.put.scan;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.NumberInputPanel;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScanProductPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ScanProductPanel extends HoldProductPanel {
	private JButton searchButton;
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initView() {
		super.initView();
		this.getInputPanel().setLayout(new MigLayout("insets 3","[210,fill][210,fill]30[]2[][grow,fill]","center"));
		this.getInputPanel().add(this.getSearchButton(),2);
	}
	
	public ScanProductPanel(QsrMaintenanceFrame mainWindow) {
		super("Hold By Scan Part", KeyEvent.VK_S, mainWindow);
	}

	// === factory methods === //
	@Override
	protected NumberInputPanel createInputPanel() {
		NumberInputPanel panel = new NumberInputPanel(this);
		panel.getNumberLabel().setText("");
		return panel;
	}
	
	private JButton getSearchButton() {
		if (this.searchButton == null) {
			this.searchButton = new JButton("Product ID");
			this.searchButton.setPreferredSize(new Dimension(100,31));
			this.searchButton.setEnabled(false);
		}
		return this.searchButton;
	}

	@Override
	public void onTabSelected() {
		if (getInputPanel().getNumberTextField().isEnabled() && getInputPanel().getNumberTextField().isEditable()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getInputPanel().getNumberTextField().requestFocus();

				}
			});
		}
	}

	@Override
	protected void mapActions() {
		super.mapActions();
		this.searchButton.addActionListener(this);
		getInputPanel().getProductTypeComboBox().addActionListener(this);
		getInputPanel().getNumberTextField().addActionListener(new ScanProductAction(this));
	}

	@Override
	public NumberInputPanel getInputPanel() {
		return (NumberInputPanel) super.getInputPanel();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource().equals(this.getSearchButton())) jButtonSearchActionPerformed();
		else if(e.getSource().equals(this.getInputPanel().getProductTypeComboBox())) enableSearchButton();
	}
	
	private void enableSearchButton() {
		if (this.getInputPanel().getProductTypeComboBox().getSelectedItem() != null)
			this.getSearchButton().setEnabled(true);
		else 
			this.getSearchButton().setEnabled(false);
	}
	
	public void jButtonSearchActionPerformed() {
		ProductType productType = this.getProductType();
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getMainWindow(), this.getProductType().toString(), ProductNumberDef.getProductNumberDef(productType).get(0).getName());
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		String productId = manualProductEntry.getResultProductId();
		if (StringUtils.isBlank(productId)) return;
		this.getInputPanel().getNumberTextField().setText(productId);
		this.getInputPanel().getNumberTextField().requestFocus();
	}
}