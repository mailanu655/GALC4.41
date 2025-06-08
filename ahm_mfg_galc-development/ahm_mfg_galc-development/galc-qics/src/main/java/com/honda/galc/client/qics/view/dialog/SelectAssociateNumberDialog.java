package com.honda.galc.client.qics.view.dialog;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.honda.galc.client.qics.validator.QicsValidator;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Specialization of <code>SelectOptionDialog</code> for selection/input
 * associate number.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
public class SelectAssociateNumberDialog extends SelectOptionDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox dataComboBox;

	public SelectAssociateNumberDialog(JFrame frame, String title, boolean editable) {
		super(frame, title, true);
	}

	@Override
	protected void initialize(boolean editable) {
		setInputLength(QicsValidator.MAX_INPUT_ASSOCIATE_NUMBER_LENGTH);
		super.initialize(editable);
	}

	public void loadComboBox(Object[] items) {
		ComboBoxModel model = null;
		if (items == null) {
			model = new DefaultComboBoxModel();
		} else {
			model = new DefaultComboBoxModel(items);
		}
		getDataComboBox().setModel(model);
	}

	@Override
	protected JComboBox getDataComboBox() {
		if (dataComboBox == null) {
			dataComboBox = super.getDataComboBox();
			dataComboBox.setName("associateNumberComboBox");
			JTextComponent editor = (JTextComponent) dataComboBox.getEditor().getEditorComponent();
			Document document = null;
			if (getInputLength() > 0) {
				document = new LimitedLengthPlainDocument(getInputLength());
			} else {
				document = new PlainDocument();
			}
			editor.setDocument(document);
		}
		return dataComboBox;
	}

	@Override
	protected List<String> validate(Object value) {
		return QicsValidator.validateAssociateNumber(value);
	}

}