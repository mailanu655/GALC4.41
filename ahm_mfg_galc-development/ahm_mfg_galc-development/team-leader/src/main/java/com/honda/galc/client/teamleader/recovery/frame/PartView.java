package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.FocusManager;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.validator.StringValidator;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.teamleader.recovery.frame.PartDefinition.ValueType;
import com.honda.galc.client.ui.component.DecimalDocument;
import com.honda.galc.client.ui.component.FontSizeHandler;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.entity.product.ProductBuildResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartView</code> is ... .
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
 * @created Jun 17, 2013
 */
public class PartView extends JPanel {

	private static final long serialVersionUID = 1L;

	private int statusFieldWidth;

	private boolean renderStatus;
	private boolean renderValue;
	private boolean editable;

	private JPanel statusPanel;
	private JTextField statusView;
	private JComboBox statusInput;
	private List<JTextField> valueFields;

	private PartDefinition partDefinition;
	private ProductBuildResult buildResult;

	public PartView(PartDefinition partDefinition, boolean renderStatus, boolean renderValue) {
		this.partDefinition = partDefinition;
		this.renderStatus = renderStatus;
		this.renderValue = renderValue;
		this.valueFields = new ArrayList<JTextField>();

		createComponents();
		initLayout();
	}

	public PartView(PartDefinition partDefinition, boolean renderStatus, boolean renderValue, int statusFieldWidth) {
		this.partDefinition = partDefinition;
		this.renderStatus = renderStatus;
		this.renderValue = renderValue;
		this.valueFields = new ArrayList<JTextField>();
		this.statusFieldWidth = statusFieldWidth;
		createComponents();
		initLayout();
	}

	protected void createComponents() {

		this.statusPanel = new JPanel(new CardLayout());
		this.statusView = createStatusField();
		this.statusInput = createStatusComboBox();

		int tokenCount = getPartDefinition().getTokenCount() < 1 ? 1 : getPartDefinition().getTokenCount();
		for (int i = 0; i < tokenCount; i++) {
			JTextField field = createValueField();
			getValueFields().add(field);
			List<Command> validators = new ArrayList<Command>();
			validators.add(new RequiredValidator());
			if (StringUtils.isNotEmpty(getPartDefinition().getValuePattern())) {
				validators.add(new RegExpValidator(StringUtils.trim(getPartDefinition().getValuePattern())));
			}
			if (getPartDefinition().getValues() != null && getPartDefinition().getValues().length > 0 ) {
				validators.add(new StringValidator(getPartDefinition().getValues()));
			}
			String name = String.format("%s (%s)", StringUtils.trimToEmpty(getPartDefinition().getLabel()), getPartDefinition().getName());
			ChainCommand validator = ChainCommand.create(validators, name);
			validator.setShortCircuit(true);
			UiUtils.mapValidator(field, validator);
		}
	}

	protected void initLayout() {
		JPanel tp = new JPanel(new MigLayout("insets 0, gap 0", "max,fill"));
		JPanel cp = new JPanel(new MigLayout("insets 0, gap 0", "max,fill"));
		tp.add(getStatusView(), new CC().height("max"));
		cp.add(getStatusInput(), new CC().height("max"));
		getStatusPanel().add(tp, "view");
		getStatusPanel().add(cp, "input");

		((CardLayout) getStatusPanel().getLayout()).show(getStatusPanel(), "input");
		StringBuilder sb = new StringBuilder();
		if (isRenderStatus()) {
			if (getStatusFieldWidth() > 0) {
				sb.append(String.format("[%s!]", getStatusFieldWidth()));
			} else {
				sb.append(String.format("[max,fill]", getStatusFieldWidth()));
			}
		}
		if (isRenderValue()) {
			if (getValueFields().size() == 1 && getPartDefinition().getLength() < 6 && getStatusFieldWidth() > 0) {
				sb.append(String.format("[:%s:,fill]", getStatusFieldWidth()));
			} else {
				for (int i = 0; i < getValueFields().size(); i++) {
					sb.append("[max,fill]");
				}
			}
		}

		setLayout(new MigLayout("insets 0, gap 0", sb.toString()));
		if (isRenderStatus()) {
			add(getStatusPanel(), new CC().height("max"));
		}
		if (isRenderValue()) {
			for (JTextField tf : getValueFields()) {
				add(tf, new CC().height("max"));
			}
		}
	}

	protected JComboBox createStatusComboBox() {
		JComboBox comp = new JComboBox();
		comp.setRequestFocusEnabled(true);
		comp.setEditable(false);
		ComboBoxModel model = new DefaultComboBoxModel(BuildAttributeStatus.values());
		comp.setModel(model);
		comp.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				setHorizontalAlignment(JLabel.CENTER);
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});

		comp.addComponentListener(new FontSizeHandler(0.9f));
		return comp;
	}

	protected JTextField createStatusField() {
		JTextField comp = new JTextField();
		comp.setHorizontalAlignment(JTextField.CENTER);
		comp.setRequestFocusEnabled(false);
		TextFieldState.DISABLED.setState(comp);
		comp.addComponentListener(new FontSizeHandler());
		return comp;
	}

	protected JTextField createValueField() {
		JTextField comp = new JTextField();
		comp.setText("");
		comp.setRequestFocusEnabled(true);
		if (getPartDefinition().getLength() == 1) {
			comp.setHorizontalAlignment(JTextField.CENTER);
		}
		Document document = new UpperCaseDocument();
		if (getPartDefinition() == null) {
		} else if (getPartDefinition().isValueDecimal())
			document = new DecimalDocument(getPartDefinition().getLength());
		else if (getPartDefinition().isValueNumeric())
			document = new NumericDocument(getPartDefinition().getLength());
		else if (ValueType.STRING.equals(getPartDefinition().getValueType()) && getPartDefinition().getLength() > 0) {
			document = new UpperCaseDocument(getPartDefinition().getLength());
		} else {
			document = new UpperCaseDocument(30);
		}
		comp.setDocument(document);
		addListeners(comp);
		TextFieldState.DISABLED.setState(comp);
		return comp;
	}

	protected void addListeners(final JTextField comp) {
		comp.addComponentListener(new FontSizeHandler());
		FocusListener fl = new FocusListener() {
			public void focusGained(FocusEvent e) {
				comp.selectAll();
			}

			public void focusLost(FocusEvent e) {
			}
		};
		comp.addFocusListener(fl);
		DocumentListener dl = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updated();
			}

			public void changedUpdate(DocumentEvent e) {
				updated();
			}

			public void removeUpdate(DocumentEvent e) {
				updated();
			}

			protected void updated() {
				if (TextFieldState.ERROR.isInState(comp)) {
					TextFieldState.EDIT.setState(comp);
				}
			}
		};
		comp.getDocument().addDocumentListener(dl);

		if (getPartDefinition().getLength() != 1) {
			return;
		}
		DocumentListener dl1 = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updated();
				if (e.getLength() > 0) {
					FocusManager fm = FocusManager.getCurrentManager();
					FocusTraversalPolicy policy = fm.getDefaultFocusTraversalPolicy();
					Component component = policy.getComponentAfter(PartView.this, comp);
					if (component != null) {
						component.requestFocus();
					}
				}
			}

			public void changedUpdate(DocumentEvent e) {
			}

			public void removeUpdate(DocumentEvent e) {
			}

			protected void updated() {
			}
		};
		comp.getDocument().addDocumentListener(dl1);
	}

	// === controlling api === //
	public void setIdleMode() {
		((CardLayout) getStatusPanel().getLayout()).show(getStatusPanel(), "view");
		getStatusView().setText("");
		TextFieldState.DISABLED.setState(getStatusView());
		getStatusInput().setEnabled(false);
		UiUtils.setText(getValueFields(), "");
		UiUtils.setState(getValueFields(), TextFieldState.DISABLED);
	}

	public void setReadOnlyMode() {
		((CardLayout) getStatusPanel().getLayout()).show(getStatusPanel(), "view");
		TextFieldState state = TextFieldState.ERROR_READ_ONLY;
		BuildAttributeStatus status = BuildAttributeStatus.FAILED;

		if (getBuildResult() != null && getBuildResult().isStatusOk()) {
			state = TextFieldState.READ_ONLY;
			status = BuildAttributeStatus.OK;
		}
		if (getBuildResult() != null) {
			getStatusView().setText(status.getLabel());
		}
		getStatusInput().setSelectedItem(status);
		getStatusInput().setEnabled(false);
		state.setState(getStatusView());
		UiUtils.setState(getValueFields(), state);

		if (getBuildResult() != null && getBuildResult().getResultValue() != null) {
			if (getValueFields().size() == 1) {
				getValueFields().get(0).setText(getBuildResult().getResultValue());
			} else if (getValueFields().size() > 1) {
				String[] values = getBuildResult().getResultValue().trim().split(getPartDefinition().getDelimiter());
				for (int i = 0; i < values.length && i < getValueFields().size(); i++) {
					getValueFields().get(i).setText(values[i].trim());
				}
			}
		}
	}

	public void setEditMode() {
		((CardLayout) getStatusPanel().getLayout()).show(getStatusPanel(), "input");
		TextFieldState state = TextFieldState.EDIT;
		UiUtils.setState(getValueFields(), state);
		UiUtils.setText(getValueFields(), "");
		getStatusInput().setEnabled(true);
	}

	protected JComponent getFirstFocusableComponent() {
		if (isRenderStatus() && getStatusInput().isEnabled()) {
			return getStatusInput();
		}
		for (JTextField tf : getValueFields()) {
			if (tf.isEnabled() && tf.isEditable()) {
				return tf;
			}
		}
		return null;
	}

	protected JTextField getFirstTextFieldInState(TextFieldState state) {
		for (JTextField tf : getValueFields()) {
			if (state.isInState(tf)) {
				return tf;
			}
		}
		return null;
	}

	public String validateInput() {
		if (isRenderStatus() && BuildAttributeStatus.FAILED.equals(getStatusInput().getSelectedItem())) {
			return null;
		}
		if (isRenderValue()) {
			for (JTextField tf : getValueFields()) {
				String str = tf.getText().trim();
				tf.setText(str);
			}
			for (JTextField tf : getValueFields()) {
				List<String> msgs = UiUtils.validate(tf);
				if (msgs != null && !msgs.isEmpty()) {
					TextFieldState.ERROR.setState(tf);
					return StringUtils.join(msgs, "\n");
				}
			}
		}
		return null;
	}

	// === config api === //
	protected boolean isRenderStatus() {
		return renderStatus;
	}

	protected boolean isRenderStatusOnly() {
		return isRenderStatus() && !isRenderValue();
	}

	protected boolean isRenderValue() {
		return renderValue;
	}

	protected boolean isRenderValueOnly() {
		return isRenderValue() && !isRenderStatus();
	}

	// === get/set === //
	protected PartDefinition getPartDefinition() {
		return partDefinition;
	}

	protected List<JTextField> getValueFields() {
		return valueFields;
	}

	protected JPanel getStatusPanel() {
		return statusPanel;
	}

	protected JTextField getStatusView() {
		return statusView;
	}

	protected JComboBox getStatusInput() {
		return statusInput;
	}

	public ProductBuildResult getBuildResult() {
		return buildResult;
	}

	public void setBuildResult(ProductBuildResult buildResult) {
		this.buildResult = buildResult;
	}

	public boolean isEditable() {
		return editable;
	}

	protected int getStatusFieldWidth() {
		return statusFieldWidth;
	}

	protected void setStatusFieldWidth(int statusFieldWidth) {
		this.statusFieldWidth = statusFieldWidth;
	}
}
