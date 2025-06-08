package com.honda.galc.client.teamleader.hold.qsr.release;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.ExportAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupDefectDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupScanReleaseDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupScanScrapDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.RemoveSelectedProductsAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.SelectByListAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.UnreleaseAction;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.report.TableReport;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScanReleasePanel</code> is ... .
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
public class ScanReleasePanel extends ReleasePanel {

	private static final long serialVersionUID = 1L;
	private String productId;
	private JButton searchButton;
	
	public ScanReleasePanel(QsrMaintenanceFrame mainWindow) {
		super(mainWindow,"Release By Scan");
	}
	
	@Override
	protected void initView() {
		super.initView();
		this.getInputPanel().setLayout(new MigLayout("insets 3","[210,fill][210,fill]30[]2[][grow,fill]","center"));
		this.getInputPanel().add(this.getSearchButton(),2);
	}

	@Override
	protected ReleaseNumberInputPanel createInputPanel() {
		ReleaseNumberInputPanel panel = new ReleaseNumberInputPanel(this);
		panel.getNumberLabel().setText("");
		return panel;
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
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean selected = getProductPanel().getSelectedValue() != null;
					boolean listed = getProductPanel().getTable().getRowCount() > 0;
					getProductPopupMenu().getSubElements()[0].getComponent().setEnabled(selected);
					getProductPopupMenu().getSubElements()[1].getComponent().setEnabled(listed);
					getProductPopupMenu().getSubElements()[2].getComponent().setEnabled(selected);
					getProductPopupMenu().getSubElements()[3].getComponent().setEnabled(selected);
					if(disableScrapMenuItem())
					{
						getProductPopupMenu().getSubElements()[4].getComponent().setEnabled(false);
					}else
					{
						getProductPopupMenu().getSubElements()[4].getComponent().setEnabled(selected);
						
					}
					getProductPopupMenu().getSubElements()[5].getComponent().setEnabled(selected);
					getProductPopupMenu().getSubElements()[6].getComponent().setEnabled(selected);
					getProductPopupMenu().getSubElements()[7].getComponent().setEnabled(listed);
					getProductPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		getProductPanel().addMouseListener(mouseListener);
		getProductPanel().getTable().addMouseListener(mouseListener);
		getInputPanel().getNumberTextField().addActionListener(new ScanReleaseProductAction(this));
		this.searchButton.addActionListener(this);
		getInputPanel().getProductTypeComboBox().addActionListener(this);
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
	
	@Override
	protected JPopupMenu createProductPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add("Remove Selected Products").addActionListener(new RemoveSelectedProductsAction(this));
		popup.add("Select By List").addActionListener(new SelectByListAction(this));
		popup.addSeparator();
		popup.add("Release Selected").addActionListener(new PopupScanReleaseDialogAction(this));
		popup.add("Inline Defect").addActionListener(new PopupDefectDialogAction(this));
		popup.add("Scrap Selected").addActionListener(new PopupScanScrapDialogAction(this));
		popup.addSeparator();
		popup.add("Unrelease Selected").addActionListener(new UnreleaseAction(this));
		popup.addSeparator();
		popup.add("Export Selected").addActionListener(new ExportAction(this, false));
		popup.add("Export All").addActionListener(new ExportAction(this, true));
		return popup;
	}
	
	@Override
	public ReleaseNumberInputPanel getInputPanel() {
		return (ReleaseNumberInputPanel) super.getInputPanel();
	}
	
	@Override
	public TableReport getReport() {
		TableReport report = addReport();
		ProductType productType = getProductType();
		String fileName = String.format("%s-RELEASE-%s.xlsx", productType, productId);
		String headerLine = String.format("%s-RELEASE-%s-%s", productType, "PRODUCT", productId);
		String sheetName = String.format("%s-RELEASE-%s", productType, productId);
		report.setFileName(fileName);
		report.setTitle(headerLine);
		report.setReportName(sheetName);
		return report;
	}
	
	private TableReport addReport() {
		ProductType productType = getProductType();
		TableReport report = getReports().get(productType);
		if (report != null) {
			return report;
		}
		report = createDefaultReport();
		getReports().put(productType, report);
		return report;
	}

	public void setProductId(String productId) {
		this.productId = productId; 
	}
	
	private JButton getSearchButton() {
		if (this.searchButton == null) {
			this.searchButton = new JButton("Product ID");
			this.searchButton.setPreferredSize(new Dimension(100,31));
			this.searchButton.setEnabled(false);
		}
		return this.searchButton;
	}
	
}
