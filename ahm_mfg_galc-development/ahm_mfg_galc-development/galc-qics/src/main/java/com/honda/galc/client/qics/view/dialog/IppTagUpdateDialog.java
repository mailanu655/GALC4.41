package com.honda.galc.client.qics.view.dialog;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.qics.view.screen.IPPTagMainPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IppTagUpdateDialog</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 23, 2017
 */
public class IppTagUpdateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField ippTagNumber;
	private JTextField ippTagNumberInput;

	private JButton cancelButton;
	private JButton updateButton;

	private IPPTag ippTag;

	private IPPTagMainPanel parentPanel;

	public IppTagUpdateDialog(IPPTagMainPanel parentPanel, IPPTag ippTag) {
		this.parentPanel = parentPanel;
		this.ippTag = ippTag;
		setSize(500, 200);
		initComponents();
		initData();
		setResizable(false);
		setModal(true);
		addListeners();
		UiUtils.requestFocus(getIppTagNumberInput());
	}

	// === init == //
	private void initComponents() {
		this.updateButton = UiFactory.createButton("Update", Fonts.DIALOG_BOLD_16, true);
		this.cancelButton = UiFactory.createButton("Cancel", Fonts.DIALOG_BOLD_16, true);
		this.ippTagNumber = UiFactory.createTextField(Fonts.DIALOG_PLAIN_30, TextFieldState.READ_ONLY);
		this.ippTagNumberInput = UiFactory.createTextField(Fonts.DIALOG_PLAIN_30, TextFieldState.EDIT);
		getIppTagNumberInput().setDocument(new LimitedLengthPlainDocument(10));
		setLayout(new MigLayout("insets 10 10 10 10", "[][max][120!]", "[max][max]"));
		add(UiFactory.createLabel("Tag", Fonts.DIALOG_BOLD_16, SwingConstants.RIGHT));
		add(getIppTagNumber(), "width max, height 50!");
		add(getUpdateButton(), "height 50!, grow, wrap");
		add(UiFactory.createLabel("New Tag", Fonts.DIALOG_BOLD_16));
		add(getIppTagNumberInput(), "width max, height 50!");
		add(getCancelButton(), "height 50!, grow");
	}

	protected void initData() {
		getIppTagNumber().setText(getIppTag().getId().getIppTagNo());
	}

	// === event handlers === //
	private void addListeners() {
		getCancelButton().addActionListener(this);
		getUpdateButton().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (e.getSource().equals(getCancelButton())) {
				dispose();
			}
			if (e.getSource().equals(getUpdateButton())) {
				update();
			}
		} finally {
			getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void update() {

		String tagNumber = getIppTagNumberInput().getText();
		tagNumber = StringUtils.trim(tagNumber);
		StringBuilder msg = new StringBuilder();
		if (!getParentPanel().isIppNumberValid(tagNumber, msg)) {
			JOptionPane.showMessageDialog(this, msg, "IPP Tag Update", JOptionPane.WARNING_MESSAGE);
			UiUtils.requestFocus(getIppTagNumberInput());
			return;
		}
		if (isIppTagNumberExist(getIppTag(), tagNumber)) {
			JOptionPane.showMessageDialog(this, "Ipp Tag Number already exist for the same Product and Division !", "IPP Tag Update", JOptionPane.WARNING_MESSAGE);
			UiUtils.requestFocus(getIppTagNumberInput());
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want to update IPPTag ?", "IPPTag Update", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		getDao(IPPTagDao.class).update(getIppTag(), tagNumber);
		getParentPanel().startPanel();
		dispose();
	}

	protected boolean isIppTagNumberExist(IPPTag ippTag, String ippTagNo) {
		List<IPPTag> tags = getParentPanel().getDisplayPane().getItems();
		if (tags == null || tags.isEmpty()) {
			return false;
		}
		IPPTagId newId = new IPPTagId(ippTag.getProductId(), ippTagNo, ippTag.getDivisionId());
		for (IPPTag tag : tags) {
			if (tag == null) {
				continue;
			}
			if (newId.equals(tag.getId())) {
				return true;
			}
		}
		return false;
	}

	// === get/set === //
	protected JButton getCancelButton() {
		return cancelButton;
	}

	protected JButton getUpdateButton() {
		return updateButton;
	}

	protected JTextField getIppTagNumber() {
		return ippTagNumber;
	}

	protected JTextField getIppTagNumberInput() {
		return ippTagNumberInput;
	}

	protected IPPTag getIppTag() {
		return ippTag;
	}

	protected IPPTagMainPanel getParentPanel() {
		return parentPanel;
	}
}
