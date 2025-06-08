package com.honda.galc.client.teamleader.hold.qsr.put.dunnage;

import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.NumberInputPanel;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldDunnagePanel</code> is ... .
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
 * @created Apr 30, 2013
 */
public class HoldDunnagePanel extends HoldProductPanel {

	private static final long serialVersionUID = 1L;

	public HoldDunnagePanel(QsrMaintenanceFrame mainWindow) {
		super("Hold By Dunnage", KeyEvent.VK_U, mainWindow);
	}

	// === factory methods === //
	@Override
	protected NumberInputPanel createInputPanel() {
		NumberInputPanel panel = new NumberInputPanel(this) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Division> selectDivisions() {
				return Config.getDunnageDivisions();
			}

			@Override
			protected List<ProductType> selectProductTypes(Division division) {
				return Config.getInstance().getDiecastProductTypes(division);
			}
		};
		panel.getNumberLabel().setText("Dunnage");
		int length = PropertyService.getPropertyBean(DunnagePropertyBean.class).getDunnageNumberLength();
		((UpperCaseDocument) panel.getNumberTextField().getDocument()).setMaxLength(length);
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
		super.mapActions();
		getInputPanel().getNumberTextField().addActionListener(new SelectProductsAction(this));
	}

	@Override
	public NumberInputPanel getInputPanel() {
		return (NumberInputPanel) super.getInputPanel();
	}
}
