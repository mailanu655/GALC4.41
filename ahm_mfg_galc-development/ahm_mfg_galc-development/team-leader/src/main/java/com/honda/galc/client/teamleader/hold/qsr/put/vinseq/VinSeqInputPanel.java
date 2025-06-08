package com.honda.galc.client.teamleader.hold.qsr.put.vinseq;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.UpperCaseDocument;

public class VinSeqInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;
	
	private LabeledComboBox lineElement;
	
	private JLabel startAfSeqLabel;
	private JLabel endAfSeqLabel;
	private JLabel startVinSeqLabel;
	private JLabel endVinSeqLabel;
	private JLabel startProductionLotLabel;
	private JLabel endProductionLotLabel;
	
	private JTextField productInput;
	private JTextField startAfSeqInput;
	private JTextField endAfSeqInput;
	private JTextField startVinSeqInput;
	private JTextField endVinSeqInput;
	private JTextField startProductionLotInput;
	private JTextField endProductionLotInput;
	
	private JCheckBox includeShipScrapInput;
	
	private LabeledListBox modelYearListElement;
	private LabeledListBox modelCodeListElement;
	private LabeledListBox modelTypeListElement;
	private LabeledListBox modelDestListElement;
	
	private JButton vinButton;
	private JButton selectButton;
	private JButton clearButton;
	
	public VinSeqInputPanel(VinSeqPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	protected void initView() {
		super.initView();
		remove(getDepartmentElement());
		remove(getProductTypeElement());
		add(getProductTypePanel());	
		add(getSpecSelectionPanel(),"gap left 20, spany 2");
		add(getButtonPanel(),"spany 2, wrap");
		add(getRangeSelectionPanel());
	}
	
	@Override
	protected void mapActions() {
		super.mapActions();
	}
	
	protected JPanel getProductTypePanel() {
		JPanel productTypePanel = new JPanel(new MigLayout("insets 0, gap 0"));
		productTypePanel.add(getLineElement(), "width 150:160:160");
		productTypePanel.add(getDepartmentElement(), "width 170:190:190");
		productTypePanel.add(getProductTypeElement(), "width 180::");
		return productTypePanel;
	}
	
	protected JPanel getSpecSelectionPanel() {
		JPanel specSelectionPanel = new JPanel(new MigLayout("insets 0, gap 0"));
		specSelectionPanel.add(getModelYearListElement(), "width 50:150:150, grow");
		specSelectionPanel.add(getModelCodeListElement(), "width 50:150:150, grow");
		specSelectionPanel.add(getModelTypeListElement(), "width 50:150:150, grow");
		specSelectionPanel.add(getModelDestListElement(), "width 50:150:150, grow");
		return specSelectionPanel;
	}

	protected JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(getCommandButton(), "grow, wrap");
		buttonPanel.add(getClearButton(), "grow");
		return buttonPanel;
	}
	
	protected JPanel getRangeSelectionPanel() {
		JPanel rangeSelectionPanel = new JPanel(new MigLayout("insets 0","[]20[]",""));
		rangeSelectionPanel.add(getLeftRangeSelectionPanel());
		rangeSelectionPanel.add(getRightRangeSelectionPanel());
		return rangeSelectionPanel;
	}
	
	protected JPanel getLeftRangeSelectionPanel() {
		JPanel leftRangePanel = new JPanel(new MigLayout("insets 0","[65][100][fill][100]",""));
		leftRangePanel.add(getVinButton(),"al right, grow");
		leftRangePanel.add(getProductInput(),"grow, span, wrap");
		leftRangePanel.add(getStartAfSeqLabel(),"al right");
		leftRangePanel.add(getStartAfSeqInput(),"grow");
		leftRangePanel.add(getEndAfSeqLabel());
		leftRangePanel.add(getEndAfSeqInput(),"grow, wrap");
		leftRangePanel.add(getStartVinSeqLabel(),"al right");
		leftRangePanel.add(getStartVinSeqInput(),"grow");
		leftRangePanel.add(getEndVinSeqLabel());
		leftRangePanel.add(getEndVinSeqInput(),"grow");
		return leftRangePanel;
	}
	
	protected JPanel getRightRangeSelectionPanel() {
		JPanel rightRangePanel = new JPanel(new MigLayout("insets 0","[85][100]",""));
		rightRangePanel.add(getStartProductionLotLabel(),"al right");
		rightRangePanel.add(getStartProductionLotInput(),"grow, wrap");
		rightRangePanel.add(getEndProductionLotLabel(),"al right");
		rightRangePanel.add(getEndProductionLotInput(),"grow, wrap");
		rightRangePanel.add(getIncludeShipScrapInput(),"span");
		return rightRangePanel;
	}

	protected LabeledComboBox getLineElement() {
		if (lineElement == null) {
			lineElement = new LabeledComboBox("Plan");
			lineElement.setSize(190, 50);
			lineElement.setFont(Fonts.DIALOG_BOLD_12);
		}
		return lineElement;
	}

	protected LabeledListBox getModelYearListElement() {
		if (modelYearListElement == null) modelYearListElement = new LabeledListBox("Year");
		return modelYearListElement;
	}
	
	protected LabeledListBox getModelCodeListElement() {
		if (modelCodeListElement == null) modelCodeListElement = new LabeledListBox("Model");
		return modelCodeListElement;
	}

	protected LabeledListBox getModelTypeListElement() {
		if (modelTypeListElement == null) modelTypeListElement = new LabeledListBox("Type");
		return modelTypeListElement;
	}

	protected LabeledListBox getModelDestListElement() {
		if (modelDestListElement == null) modelDestListElement = new LabeledListBox("Dest");
		return modelDestListElement;
	}
	
	public JLabel getStartAfSeqLabel() {
		if (startAfSeqLabel == null) startAfSeqLabel = new JLabel("AF On Seq");
		return startAfSeqLabel;
	}
	
	public JLabel getEndAfSeqLabel() {
		if (endAfSeqLabel == null) endAfSeqLabel = new JLabel("-");
		return endAfSeqLabel;
	}
	
	public JLabel getStartVinSeqLabel() {
		if (startVinSeqLabel == null) startVinSeqLabel = new JLabel("VIN Seq");
		return startVinSeqLabel;
	}
	
	public JLabel getEndVinSeqLabel() {
		if (endVinSeqLabel == null) endVinSeqLabel = new JLabel("-");
		return endVinSeqLabel;
	}
	
	public JLabel getStartProductionLotLabel() {
		if (startProductionLotLabel == null) startProductionLotLabel = new JLabel("From Prod Lot");
		return startProductionLotLabel;
	}
	
	public JLabel getEndProductionLotLabel() {
		if (endProductionLotLabel == null) endProductionLotLabel = new JLabel("To Prod Lot");
		return endProductionLotLabel;
	}
	
	public JTextField getProductInput() {
		if (productInput == null) productInput = createInputField(20);
		return productInput;
	}
	public JTextField getStartAfSeqInput() {
		if (startAfSeqInput == null) startAfSeqInput = createInputField(20);
		return startAfSeqInput;
	}
	
	public JTextField getEndAfSeqInput() {
		if (endAfSeqInput == null) endAfSeqInput = createInputField(20);
		return endAfSeqInput;
	}
	
	public JTextField getStartVinSeqInput() {
		if (startVinSeqInput == null) startVinSeqInput = createInputField(20);
		return startVinSeqInput;
	}
	
	public JTextField getEndVinSeqInput() {
		if (endVinSeqInput == null) endVinSeqInput = createInputField(20);
		return endVinSeqInput;
	}
	
	public JTextField getStartProductionLotInput() {
		if (startProductionLotInput == null) startProductionLotInput = createInputField(40);
		return startProductionLotInput;
	}
	
	public JTextField getEndProductionLotInput() {
		if (endProductionLotInput == null) endProductionLotInput = createInputField(30);
		return endProductionLotInput;
	}
	
	private JTextField createInputField(int maxLength) {
		Document document = new UpperCaseDocument(maxLength);
		JTextField inputField = new JTextField();
		inputField.setDocument(document);
		inputField.setFont(Fonts.DIALOG_BOLD_12);
		return inputField;
	}
	
	protected JCheckBox getIncludeShipScrapInput() {
		if (includeShipScrapInput == null) includeShipScrapInput = new JCheckBox("Include All Shipped/Scrapped");
		return includeShipScrapInput;
	}

	@Override
	public JButton getCommandButton() {
		if (selectButton == null) selectButton = createButton("Select", false, Fonts.DIALOG_BOLD_16);
		return selectButton;
	}
	
	public JButton getClearButton() {
		if (clearButton == null) clearButton = createButton("Clear", true, Fonts.DIALOG_BOLD_16);
		return clearButton;
	}
	
	public JButton getVinButton() {
		if (vinButton == null) vinButton = createButton("VIN", false, Fonts.DIALOG_BOLD_12);
		return vinButton;
	}
	
	private JButton createButton(String title, Boolean isEnabled, Font font) {
		JButton button = new JButton(title);
		button.setEnabled(isEnabled);
		button.setFont(font);
		return button;
	}
	
	public void setInputEnabled(boolean enabled) {
		getVinButton().setEnabled(enabled);
		getProductInput().setEnabled(enabled);
		getStartAfSeqInput().setEnabled(enabled);
		getEndAfSeqInput().setEnabled(enabled);
		getStartVinSeqInput().setEnabled(enabled);
		getEndVinSeqInput().setEnabled(enabled);
		getStartProductionLotInput().setEnabled(enabled);
		getEndProductionLotInput().setEnabled(enabled);
	}

	public void resetInput() {
		getProductInput().setText("");
		getStartAfSeqInput().setText("");
		getEndAfSeqInput().setText("");
		getStartVinSeqInput().setText("");
		getEndVinSeqInput().setText("");
		getStartProductionLotInput().setText("");
		getEndProductionLotInput().setText("");
		getModelYearListElement().getComponent().clearSelection();
		getModelCodeListElement().getComponent().clearSelection();
		getModelTypeListElement().getComponent().clearSelection();
		getModelDestListElement().getComponent().clearSelection();
	}
	
	public void showFilterInput(boolean visible) {
		getStartAfSeqLabel().setVisible(visible);
		getStartAfSeqInput().setVisible(visible);
		getEndAfSeqLabel().setVisible(visible);
		getEndAfSeqInput().setVisible(visible);
		getStartVinSeqLabel().setVisible(visible);
		getStartVinSeqInput().setVisible(visible);
		getEndVinSeqLabel().setVisible(visible);
		getEndVinSeqInput().setVisible(visible);
		getStartProductionLotLabel().setVisible(visible);
		getStartProductionLotInput().setVisible(visible);
		getEndProductionLotLabel().setVisible(visible);
		getEndProductionLotInput().setVisible(visible);
		getModelYearListElement().setVisible(visible);
		getModelCodeListElement().setVisible(visible);
		getModelTypeListElement().setVisible(visible);
		getModelDestListElement().setVisible(visible);
		getIncludeShipScrapInput().setVisible(visible);
	}
}