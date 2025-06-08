package com.honda.galc.client.product.process.engine.bearing.select.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.controller.listener.ActionAdapter;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.process.engine.bearing.select.controller.BearingSelectController;
import com.honda.galc.client.product.process.engine.bearing.select.controller.UiState;
import com.honda.galc.client.product.process.engine.bearing.select.model.BearingSelectModel;
import com.honda.galc.client.product.process.engine.bearing.select.validator.BearingSelectValidator;
import com.honda.galc.client.product.process.view.ProcessView;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingSelectPanel</code> is ... .
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
public class BearingSelectPanel extends ApplicationMainPanel implements ProcessView {

	private static final long serialVersionUID = 1L;
	public static final String EDIT_ACTION_LABEL = "Edit";
	public static final String RECOVER_ACTION_LABEL = "Recover";
	public static final String EDIT_ACTION_COMMAND = "edit";
	public static final String RECOVER_ACTION_COMMAND = "recover";

	private BearingSelectController controller;

	private JButton mcbEditButton;
	private JButton crankSnSelectButton;
	private JButton crankSnEditButton;

	private JCheckBox crankSnCollectCheckBox;
	private JCheckBox conrodSnCollectCheckBox;

	private Map<Integer, JButton> conrodSnSelectButtons;
	private JButton conrodSnEditButton;

	private JButton blockMeasurementsEditButton;
	private JButton crankMainMeasurementsEditButton;
	private JButton crankConrodMeasurementsEditButton;
	private JButton conrodMeasurementsEditButton;

	private JButton doneButton;

	// === model input === //
	private JTextField mcbTextField;
	private JTextField crankSnTextField;
	private Map<Integer, JTextField> conrodSnTextFields;

	private Map<Integer, JTextField> blockMeasurementTextFields;
	private Map<Integer, JTextField> crankMainMeasurementTextFields;

	private Map<Integer, JTextField> crankConrodMeasurementTextFields;
	private Map<Integer, JTextField> conrodMeasurementTextFields;

	public BearingSelectPanel(MainWindow window) {
		super(window);

		this.conrodSnSelectButtons = new HashMap<Integer, JButton>();
		this.conrodSnTextFields = new HashMap<Integer, JTextField>();

		this.blockMeasurementTextFields = new HashMap<Integer, JTextField>();
		this.crankMainMeasurementTextFields = new HashMap<Integer, JTextField>();
		this.crankConrodMeasurementTextFields = new HashMap<Integer, JTextField>();
		this.conrodMeasurementTextFields = new HashMap<Integer, JTextField>();
		this.controller = new BearingSelectController(this);

		initView();
		mapActions();
		getController().setValidator(new BearingSelectValidator(getController()));
	}

	protected void initView() {

		setName("BearingSelectPanel");
		setLayout(new MigLayout("insets 0 5 0 5", "[grow,fill]", ""));

		createBlockSection();
		createCrankSnSection();
		createConrodSnSection();
		createMeasurementSection();
	}

	public void mapActions() {

		final BearingSelectListener actionListener = new BearingSelectListener(this);

		getMcbEditButton().addActionListener(actionListener);
		getMcbTextField().addActionListener(actionListener);

		getMcbTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getMcbTextField()));
		getCrankSnTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getCrankSnTextField()));
		for (JTextField tf : getConrodSnTextFields().values()) {
			tf.getDocument().addDocumentListener(new InputNumberChangeListener(this, tf));
			tf.addActionListener(actionListener);
		}
		for (JTextField tf : getBlockMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf, getBlockMeasurementsEditButton(), getModel().getProperty().isBlockMeasurementsEditable());
			tf.addFocusListener(ml);
		}
		for (JTextField tf : getCrankMainMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf, getCrankMainMeasurementsEditButton(), getController().isCrankMainMeasurementsEditable());
			tf.addFocusListener(ml);
		}
		for (JTextField tf : getCrankConrodMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf, getCrankConrodMeasurementsEditButton(), getController().isCrankConrodMeasurementsEditable());
			tf.addFocusListener(ml);
		}
		for (JTextField tf : getConrodMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf, getConrodMeasurementsEditButton(), getController().isConrodMeasurementsEditable());
			tf.addFocusListener(ml);
		}

		getCrankSnTextField().addActionListener(actionListener);
		getCrankSnEditButton().addActionListener(actionListener);
		getCrankSnCollectCheckBox().addItemListener(actionListener);
		getConrodSnCollectCheckBox().addItemListener(actionListener);
		getConrodSnEditButton().addActionListener(actionListener);
		getBlockMeasurementsEditButton().addActionListener(actionListener);
		getCrankMainMeasurementsEditButton().addActionListener(actionListener);
		getCrankConrodMeasurementsEditButton().addActionListener(actionListener);
		getConrodMeasurementsEditButton().addActionListener(actionListener);

		BaseListener<BearingSelectPanel> doneAction = new BaseListener<BearingSelectPanel>(this) {
			@Override
			protected void executeActionPerformed(ActionEvent ae) {
				getController().finish();
			}
		};
		getDoneButton().setAction(new ActionAdapter<BearingSelectPanel>("Done", KeyEvent.VK_D, doneAction));
		getDoneButton().getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "done");
		getDoneButton().getActionMap().put("done", getDoneButton().getAction());
	}

	protected void createBlockSection() {
		this.mcbTextField = UiFactory.getInfoSmall().createTextField(getModel().getBlockInputNumberMaxLength(), TextFieldState.READ_ONLY);
		this.mcbEditButton = UiFactory.getInput().createButton(EDIT_ACTION_LABEL, false);
		getMcbTextField().setName("blockSnTextField");
		getMcbEditButton().setActionCommand(EDIT_ACTION_COMMAND);
		String columnLayout = String.format("[right][grow,fill][%s!,fill]2", getFixedColumnWidth());
		JPanel panel = new JPanel(new MigLayout("insets 5", columnLayout));
		panel.add(UiFactory.getInfoSmall().createLabel("Block " + getModel().getProperty().getInstalledBlockPartNameSnType().name()));
		panel.add(getMcbTextField());
		panel.add(getMcbEditButton(), "grow");
		add(panel, "wrap 0");
	}

	protected void createCrankSnSection() {
		UiFactory.addSeparator(this, "Traceability");

		this.crankSnTextField = UiFactory.getInput().createTextField(getModel().getProperty().getCrankSnLength(), TextFieldState.EDIT);
		this.crankSnSelectButton = UiFactory.getInput().createButton("Crank SN", false);
		this.crankSnEditButton = UiFactory.getInput().createButton("Edit", false);
		this.crankSnCollectCheckBox = new JCheckBox("Crank Auto");
		this.conrodSnCollectCheckBox = new JCheckBox("Conrods Auto");
		getCrankSnTextField().setName("crankSnTextField");
		int fl = getFixedColumnWidth();
		String columnLayout = String.format("[%s!,fill][max,fill][][%s!,fill]2", fl, fl, fl);
		JPanel panel = new JPanel(new MigLayout("insets 0", columnLayout));
		panel.add(getCrankSnSelectButton(), "grow");
		panel.add(getCrankSnTextField(), "grow");
		panel.add(getCrankSnCollectCheckBox());
		panel.add(getCrankSnEditButton(), "grow");

		add(panel, "wrap");
	}

	protected void createConrodSnSection() {

		this.conrodSnEditButton = UiFactory.getInput().createButton("Edit", false);
		String columnLayout = String.format("[%s!,fill][max,fill]20[%s,fill][max,fill][%s!,fill]2", getFixedColumnWidth(), getFixedColumnWidth(), getFixedColumnWidth());
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0", columnLayout));
		UiFactory.addSeparator(panel, "");

		List<Integer> conrodIx = getModel().getConrodIxDisplaySequence();
		for (int i = 0; i < conrodIx.size(); i++) {
			int ix = conrodIx.get(i);
			String label = String.format("Conrod SN.%s", ix);
			JButton button = UiFactory.getInput().createButton(label, false);
			JTextField field = createConrodSnTextField();
			field.setName(String.format("conrodSnTextField.%s", ix));
			panel.add(button, "grow");
			if (i % 2 == 1) {
				panel.add(field, "wrap");
			} else {
				panel.add(field);
			}
			getConrodSnSelectButtons().put(ix, button);
			getConrodSnTextFields().put(ix, field);
		}
		panel.add(getConrodSnCollectCheckBox(), new CC().cell(4, 1).span(1, 1).grow());
		panel.add(getConrodSnEditButton(), new CC().cell(4, 2).span(1, 1).grow());

		add(panel, "wrap 5");
	}

	public void createMeasurementSection() {

		this.blockMeasurementsEditButton = UiFactory.getInput().createButton("Edit");

		UiFactory.addSeparator(this, "Rankings");
		String constraintToken = "[max,fill]";
		StringBuilder mainLayout = new StringBuilder();
		for (int i = 0; i < getModel().getMainBearingCount(); i++) {
			mainLayout.append(constraintToken);
		}
		String str = mainLayout.toString();
		StringBuilder doubleMainLayout = new StringBuilder(str).append(str);
		int w = getFixedColumnWidth() / 2;

		String layoutConstraints = "insets 0 0 0 0";
		String columnConstraints = String.format("[%s!,fill][%s!,fill]%s[%s!,fill]5", w, w, doubleMainLayout, getFixedColumnWidth());

		StringBuilder layoutConrod = new StringBuilder();
		for (int i = 0; i < getModel().getConrodCount(); i++) {
			layoutConrod.append(constraintToken);
		}

		MigLayout layout = new MigLayout(layoutConstraints, columnConstraints, "");
		JPanel panel = new JPanel(layout);

		int startColIx = 2;
		int sectionStartRowIx = 0;

		addBlockMeasurements(panel, startColIx, sectionStartRowIx, layoutConstraints);
		UiFactory.addSeparator(panel, "");

		sectionStartRowIx = 3;
		addCrankJournalMeasurements(panel, startColIx, sectionStartRowIx, layoutConstraints);

		addCrankConrodMeasurements(panel, sectionStartRowIx, layoutConstraints, layoutConrod.toString());
		UiFactory.addSeparator(panel, "");

		sectionStartRowIx = 8;
		addConrodMeasurements(panel, sectionStartRowIx, layoutConstraints, layoutConrod.toString());

		add(panel, "wrap 5");
	}

	protected void addBlockMeasurements(JPanel panel, int startColIx, int sectionStartRowIx, String layoutConstraints) {
		List<Integer> blockIx = getModel().getBlockMainIxDisplaySequence();
		panel.add(UiFactory.getInfoSmall().createLabel("Block", JLabel.CENTER), new CC().cell(0, 1).span(2, 1).height("max"));
		for (int i = 0; i < blockIx.size(); i++) {
			int bearingIx = blockIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;
			JLabel mainIxLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);
			JTextField blockMain = UiFactory.createBearingMeasurementTextField(getModel().getConrodCount());
			blockMain.setName(String.format("blockMainTextField.%s", bearingIx));
			panel.add(mainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1));
			panel.add(blockMain, new CC().cell(colIx, rowIx++).span(2, 1).height("max"));
			getBlockMeasurementTextFields().put(bearingIx, blockMain);
		}
		panel.add(getBlockMeasurementsEditButton(), new CC().cell(startColIx + getModel().getMainBearingCount() * 2, sectionStartRowIx + 1).grow().wrap());
	}

	protected void addCrankJournalMeasurements(JPanel panel, int startColIx, int sectionStartRowIx, String layoutConstraints) {
		this.crankMainMeasurementsEditButton = UiFactory.getInput().createButton("Edit");
		this.crankConrodMeasurementsEditButton = UiFactory.getInput().createButton("Edit");

		List<Integer> crankMainIx = getModel().getCrankMainIxDisplaySequence();

		panel.add(UiFactory.getInfoSmall().createLabel("Crank", JLabel.CENTER), new CC().cell(0, sectionStartRowIx + 1).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Journal", JLabel.CENTER), new CC().cell(1, sectionStartRowIx + 1).height("max"));
		panel.add(UiFactory.getInput().createLabel("Cons", JLabel.CENTER), new CC().cell(1, sectionStartRowIx + 2).height("max"));

		for (int i = 0; i < crankMainIx.size(); i++) {
			int bearingIx = crankMainIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;
			JLabel mainIxLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);
			JTextField crankMain = UiFactory.createBearingMeasurementTextField(getModel().getConrodCount());
			crankMain.setName(String.format("crankMainTextField.%s", bearingIx));
			panel.add(mainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1));
			panel.add(crankMain, new CC().cell(colIx, rowIx++).span(2, 1).height("max"));
			getCrankMainMeasurementTextFields().put(bearingIx, crankMain);
		}
		panel.add(getCrankMainMeasurementsEditButton(), new CC().cell(startColIx + getModel().getMainBearingCount() * 2, sectionStartRowIx + 1).grow().wrap());
	}

	protected void addCrankConrodMeasurements(JPanel panel, int sectionStartRowIx, String layoutConstraints, String layoutConrod) {
		List<Integer> crankConrodIx = getModel().getCrankConrodIxDisplaySequence();
		JPanel crankConPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		JPanel crankConLabelPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		for (int i = 0; i < crankConrodIx.size(); i++) {
			int bearingIx = crankConrodIx.get(i);
			JTextField crankCon = UiFactory.createBearingMeasurementTextField(getModel().getConrodCount());
			crankCon.setName(String.format("crankConTextField.%s", bearingIx));
			JLabel crankConIxLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);
			crankConPanel.add(crankCon, new CC().cell(i, 0).height("max"));
			crankConLabelPanel.add(crankConIxLabel, new CC().cell(i, 0).height("max"));
			getCrankConrodMeasurementTextFields().put(bearingIx, crankCon);
		}

		panel.add(crankConPanel, new CC().span(getModel().getMainBearingCount() * 2 - 2, 1).cell(3, sectionStartRowIx + 2));
		panel.add(getCrankConrodMeasurementsEditButton(), new CC().cell(getModel().getMainBearingCount() * 2 + 2, sectionStartRowIx + 2).grow());
		panel.add(crankConLabelPanel, new CC().span(getModel().getMainBearingCount() * 2 - 2, 1).cell(3, sectionStartRowIx + 3).wrap());
	}

	protected void addConrodMeasurements(JPanel panel, int sectionStartRowIx, String layoutConstraints, String layoutConrod) {
		this.conrodMeasurementsEditButton = UiFactory.getInput().createButton("Edit");
		this.doneButton = UiFactory.getInfo().createButton("");
		getDoneButton().setName("doneButton");
		List<Integer> conrodIx = getModel().getConrodIxDisplaySequence();
		panel.add(UiFactory.getInfoSmall().createLabel("Conrod", JLabel.CENTER), new CC().cell(0, sectionStartRowIx + 0).span(2, 1));
		JPanel conrodPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod, ""));
		JPanel conrodLabelPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod, ""));
		for (int i = 0; i < conrodIx.size(); i++) {
			int bearingIx = conrodIx.get(i);
			JTextField conrod = UiFactory.createBearingMeasurementTextField(getModel().getConrodCount());
			conrod.setName(String.format("conrodTextField.%s", bearingIx));
			JLabel conrodIxLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);
			conrodPanel.add(conrod, new CC().cell(i, 0).height("max"));
			conrodLabelPanel.add(conrodIxLabel, new CC().cell(i, 0));
			getConrodMeasurementTextFields().put(bearingIx, conrod);
		}

		panel.add(conrodLabelPanel, new CC().span(getModel().getMainBearingCount() * 2 - 2, 1).cell(3, sectionStartRowIx + 1));
		panel.add(conrodPanel, new CC().span(getModel().getMainBearingCount() * 2 - 2, 1).cell(3, sectionStartRowIx + 0));

		panel.add(getConrodMeasurementsEditButton(), new CC().cell(getModel().getMainBearingCount() * 2 + 2, sectionStartRowIx + 0).grow().height("max"));
		panel.add(getDoneButton(), new CC().cell(getModel().getMainBearingCount() * 2 + 2, sectionStartRowIx + 1));
	}

	protected JTextField createConrodSnTextField() {
		UiFactory factory = UiFactory.getInput();
		if (getModel().getConrodCount() < 5) {
			factory = UiFactory.getInput();
		} else if (getModel().getConrodCount() > 6) {
			factory = UiFactory.getInputSmall();
		}
		return factory.createTextField(getModel().getProperty().getConrodSnLength(), TextFieldState.EDIT);
	}

	public JTextField getMcbTextField() {
		return mcbTextField;
	}

	public JTextField getCrankSnTextField() {
		return crankSnTextField;
	}

	public Map<Integer, JTextField> getBlockMeasurementTextFields() {
		return blockMeasurementTextFields;
	}

	public List<JTextField> getBlockMeasurementTextFieldsAsList() {
		return UiUtils.toList(getModel().getBlockMainIxDisplaySequence(), getBlockMeasurementTextFields());
	}

	public Map<Integer, JTextField> getCrankMainMeasurementTextFields() {
		return crankMainMeasurementTextFields;
	}

	public List<JTextField> getCrankMainMeasurementTextFieldsAsList() {
		return UiUtils.toList(getModel().getCrankMainIxDisplaySequence(), getCrankMainMeasurementTextFields());
	}

	public Map<Integer, JTextField> getCrankConrodMeasurementTextFields() {
		return crankConrodMeasurementTextFields;
	}

	public List<JTextField> getCrankConrodMeasurementTextFieldsAsList() {
		return UiUtils.toList(getModel().getCrankConrodIxDisplaySequence(), getCrankConrodMeasurementTextFields());
	}

	public Map<Integer, JTextField> getConrodMeasurementTextFields() {
		return conrodMeasurementTextFields;
	}

	public List<JTextField> getConrodMeasurementTextFieldsAsList() {
		return UiUtils.toList(getModel().getConrodIxDisplaySequence(), getConrodMeasurementTextFields());
	}

	public JButton getCrankSnSelectButton() {
		return crankSnSelectButton;
	}

	public JButton getCrankSnEditButton() {
		return crankSnEditButton;
	}

	public Map<Integer, JButton> getConrodSnSelectButtons() {
		return conrodSnSelectButtons;
	}

	public Map<Integer, JTextField> getConrodSnTextFields() {
		return conrodSnTextFields;
	}

	public List<JTextField> getConrodSnTextFieldsAsList() {
		return UiUtils.toList(getModel().getConrodIxDisplaySequence(), getConrodSnTextFields());
	}

	public BearingSelectController getController() {
		return controller;
	}

	public JButton getDoneButton() {
		return doneButton;
	}

	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	public JButton getConrodMeasurementsEditButton() {
		return conrodMeasurementsEditButton;
	}

	public JButton getCrankConrodMeasurementsEditButton() {
		return crankConrodMeasurementsEditButton;
	}

	public JButton getBlockMeasurementsEditButton() {
		return blockMeasurementsEditButton;
	}

	public JButton getCrankMainMeasurementsEditButton() {
		return crankMainMeasurementsEditButton;
	}

	public JCheckBox getCrankSnCollectCheckBox() {
		return crankSnCollectCheckBox;
	}

	public JCheckBox getConrodSnCollectCheckBox() {
		return conrodSnCollectCheckBox;
	}

	protected int getFixedColumnWidth() {
		return 115;
	}

	public JButton getConrodSnEditButton() {
		return conrodSnEditButton;
	}

	@Override
	public void requestFocus() {
		getController().requestFocus();
		UiUtils.requestFocus(getController().getFirstFocusableComponent());
	}

	public JButton getMcbEditButton() {
		return mcbEditButton;
	}

	public BearingSelectModel getModel() {
		return getController().getModel();
	}

	// === ui listeners === //
	public class BearingSelectListener extends BaseListener<BearingSelectPanel> implements ActionListener, ItemListener {

		public BearingSelectListener(BearingSelectPanel parentPanel) {
			super(parentPanel);

		}

		@Override
		protected void executeActionPerformed(ActionEvent ae) {

			getController().preExecute();

			if (getView().getMcbEditButton().equals(ae.getSource()) && BearingSelectPanel.EDIT_ACTION_COMMAND.equals(ae.getActionCommand())) {
				editMcb();
			} else if (getView().getMcbEditButton().equals(ae.getSource()) && BearingSelectPanel.RECOVER_ACTION_COMMAND.equals(ae.getActionCommand())) {
				getController().recoverBlock();
			}

			else if (getView().getMcbTextField().equals(ae.getSource())) {
				getController().recoverBlock();
			}

			else if (getView().getCrankSnTextField().equals(ae.getSource())) {
				getController().processCrankSn();
			} else if (getView().getCrankSnEditButton().equals(ae.getSource())) {
				editCrankSnNumber();
			}

			else if (getView().getConrodSnTextFields().values().contains(ae.getSource())) {
				getController().processConrodSn((JTextField) ae.getSource());
			} else if (getView().getConrodSnEditButton().equals(ae.getSource())) {
				editConrodSnNumber();
			}

			else if (getView().getBlockMeasurementsEditButton().equals(ae.getSource())) {
				editMeasurements(getView().getBlockMeasurementTextFieldsAsList(), getView().getBlockMeasurementsEditButton());
			} else if (getView().getCrankMainMeasurementsEditButton().equals(ae.getSource())) {
				editMeasurements(getView().getCrankMainMeasurementTextFieldsAsList(), getView().getCrankMainMeasurementsEditButton());
			} else if (getView().getCrankConrodMeasurementsEditButton().equals(ae.getSource())) {
				editMeasurements(getView().getCrankConrodMeasurementTextFieldsAsList(), getView().getCrankConrodMeasurementsEditButton());
			} else if (getView().getConrodMeasurementsEditButton().equals(ae.getSource())) {
				editMeasurements(getView().getConrodMeasurementTextFieldsAsList(), getView().getConrodMeasurementsEditButton());
			}

			getController().postExecute();
		}

		@Override
		protected void executeItemStateChanged(ItemEvent ie) {

			if (ie.getSource() instanceof Component) {
				if (!((Component) ie.getSource()).isEnabled()) {
					return;
				}
			}

			getController().preExecute();
			if (getView().getCrankSnCollectCheckBox().equals(ie.getItem())) {
				if (ItemEvent.SELECTED == ie.getStateChange()) {
					crankSnCollectChecked();
				} else if (ItemEvent.DESELECTED == ie.getStateChange()) {
					crankSnCollectUnchecked();
				}
			} else if (getView().getConrodSnCollectCheckBox().equals(ie.getItem())) {
				if (ItemEvent.SELECTED == ie.getStateChange()) {
					conrodSnCollectChecked();
				} else if (ItemEvent.DESELECTED == ie.getStateChange()) {
					conrodSnCollectUnchecked();
				}
			}
			getController().postExecute();
		}

		// === simple events handling === //
		protected void editMcb() {
			TextFieldState.EDIT.setState(getView().getMcbTextField());
			getController().setFocusComponent(getView().getMcbTextField());
			getView().getMcbEditButton().setText(BearingSelectPanel.RECOVER_ACTION_LABEL);
			getView().getMcbEditButton().setActionCommand(BearingSelectPanel.RECOVER_ACTION_COMMAND);
		}

		protected void editCrankSnNumber() {
			getView().getCrankSnEditButton().setEnabled(false);
			TextFieldState.EDIT.setState(getView().getCrankSnTextField());
			getController().setFocusComponent(getView().getCrankSnTextField());
			UiState.initCrankMeasurementTextFieldsForCrankSnEdit(getView());
		}

		protected void crankSnCollectChecked() {
			TextFieldState.EDIT.setState(getView().getCrankSnTextField());
			getView().getCrankSnEditButton().setEnabled(false);
		}

		protected void crankSnCollectUnchecked() {
			getView().getCrankSnTextField().setText("");
			TextFieldState.DISABLED.setState(getView().getCrankSnTextField());
			getView().getCrankSnEditButton().setEnabled(false);
		}

		protected void conrodSnCollectChecked() {
			UiUtils.setState(getView().getConrodSnTextFields().values(), TextFieldState.EDIT);
			getView().getConrodSnEditButton().setEnabled(false);
		}

		protected void conrodSnCollectUnchecked() {
			UiUtils.setText(getView().getConrodSnTextFields().values(), "");
			UiUtils.setState(getView().getConrodSnTextFields().values(), TextFieldState.DISABLED);
			getView().getConrodSnEditButton().setEnabled(false);
		}

		protected void editConrodSnNumber() {
			getView().getConrodSnEditButton().setEnabled(false);

			UiUtils.setText(getView().getConrodMeasurementTextFields().values(), "");
			UiUtils.setState(getView().getConrodMeasurementTextFields().values(), TextFieldState.EDIT);

			UiUtils.setState(getView().getConrodSnTextFields().values(), TextFieldState.EDIT);
			getController().setFocusComponent(getView().getConrodSnTextFields().values().iterator().next());
		}

		protected void editMeasurements(List<JTextField> fields, JButton button) {
			if (fields == null) {
				return;
			}
			UiUtils.setState(fields, TextFieldState.EDIT);
			if (button != null) {
				button.setEnabled(false);
			}
			getController().setFocusComponent(fields.get(0));
		}
	}

	public class MeasurementListener extends InputNumberChangeListener implements FocusListener {

		private JButton button;
		private boolean buttonToggable;

		public MeasurementListener(BearingSelectPanel view, JTextField textField, JButton button, boolean buttonToggable) {
			super(view, textField);
			this.button = button;
			this.buttonToggable = buttonToggable;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			getController().preExecute();

			super.processChange();
			if (e.getLength() > 0) {
				getController().validate(getTextField());
				if (getController().isErrorExists()) {
					getController().setFocusComponent(getTextField());
				} else {
					TextFieldState.READ_ONLY.setState(getTextField());
					getController().setFocusComponent(getController().getNextFocusableTextField(getTextField()));
					if (getButton() != null && isButtonToggable() && !getButton().isEnabled()) {
						getButton().setEnabled(true);
					}
				}
			}
			getController().postExecute();
		}

		public void focusGained(FocusEvent e) {
			getTextField().selectAll();
			getTextField().getDocument().addDocumentListener(this);
		}

		public void focusLost(FocusEvent e) {
			getTextField().getDocument().removeDocumentListener(this);
		}

		// === get/set === //
		protected BearingSelectValidator getValidator() {
			return getController().getValidator();
		}

		protected JButton getButton() {
			return button;
		}

		protected boolean isButtonToggable() {
			return buttonToggable;
		}
	}
}
