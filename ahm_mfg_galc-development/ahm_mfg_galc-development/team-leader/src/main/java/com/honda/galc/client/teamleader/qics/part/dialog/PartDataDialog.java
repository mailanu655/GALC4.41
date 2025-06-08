package com.honda.galc.client.teamleader.qics.part.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.SecondaryPart;

/**
 * 
 * <h3>PartDataDialog Class description</h3>
 * <p> PartDataDialog description </p>
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
 * Oct 31, 2011
 *
 *
 */
public class PartDataDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private LabeledUpperCaseTextField nameField;
	private LabeledUpperCaseTextField descriptionField;
	private LabeledUpperCaseTextField descriptionLongField;
	private LabeledComboBox modelCodeElement;
	private String currentModelCode;
	
	private String name;
	private String description;
	private String descriptionLong;
	private List<String> modelCodes;
	
	private JButton createButton;
	private JButton cancelButton;
	
	private boolean dataCreated = false;

	public PartDataDialog(JFrame frame, String partDataName) {
		this(frame,partDataName,null,null,null);
	}

	public PartDataDialog(JFrame frame, String partDataName, String name,
			String description, String descriptionLong) {
		super(frame, partDataName, true);
		this.name = name;
		this.description = description;
		this.descriptionLong = descriptionLong;
		initComponents();
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	//part group
	public PartDataDialog(JFrame frame, String partDataName, List<String> modelCodes) {
		super(frame, partDataName, true);
		this.modelCodes = modelCodes;
		initComponents();
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	
	//Defect group
	public PartDataDialog(JFrame frame, String partDataName, String name,
			String description, String descriptionLong,
			String currentModelCode, List<String> modelCodes) {
		super(frame, partDataName, true);
		this.name = name;
		this.description = description;
		this.descriptionLong = descriptionLong;
		this.modelCodes = new ArrayList<String>();
		this.modelCodes.addAll(modelCodes);
		this.currentModelCode = currentModelCode;
		initComponents();
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	protected void initComponents() {
		if ((getTitle().trim().equals("Part Group"))||(getTitle().trim().equals("Defect Group"))){
			setSize(600, 330);
		}else{
			setSize(600, 240);	
		}
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		JPanel nameDescriptionPanel = new JPanel(new BorderLayout());
		nameDescriptionPanel.add(createNameField(),BorderLayout.CENTER);
		nameDescriptionPanel.add(createDescriptionField(),BorderLayout.EAST);
		if ((getTitle().trim().equals("Part Group"))||(getTitle().trim().equals("Defect Group")))
		{
			modelCodeElement = createModelCodeElement();
			nameDescriptionPanel.add(getModelCodeElement(),BorderLayout.SOUTH);
		}
		panel.add(nameDescriptionPanel);
		panel.add(createDescriptionLongField());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(createCancelButton());
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(createCreateButton());
		panel.add(buttonPanel);
		panel.add(Box.createVerticalStrut(10));
		getContentPane().add(panel);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		addEventListeners();

	}
	
	private void addEventListeners() {
		cancelButton.addActionListener(this);
		createButton.addActionListener(this);
		
	}

	private LabeledUpperCaseTextField createNameField() {
		nameField = new LabeledUpperCaseTextField("Name",false);
		nameField.getComponent().setText(name);
		nameField.setFont(Fonts.DIALOG_BOLD_16);
		return nameField;
	}
	
	protected LabeledComboBox createModelCodeElement() {
		LabeledComboBox modelCodeElement = new LabeledComboBox("Model Code", false);
		modelCodeElement.setSize(190, 50);
		if(getTitle().trim().equals("Defect Group")){
			modelCodes.add(0, currentModelCode);
			LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
			linkedHashSet.addAll(modelCodes);
			modelCodes.clear();
			modelCodes.addAll(linkedHashSet);
				modelCodes.add(0, " ");
				modelCodeElement.setModel(modelCodes, 1);
		}
		else if(getTitle().trim().equals("Part Group")){
			modelCodeElement.setModel(modelCodes, -1);	
		}
		modelCodeElement.setFont(Fonts.DIALOG_BOLD_16);
		return modelCodeElement;
	}

	public LabeledComboBox getModelCodeElement() {
		return modelCodeElement;
	}
	
	public JComboBox getModelCodeComboBox() {
		return getModelCodeElement().getComponent();
	}
	
	
	private LabeledUpperCaseTextField createDescriptionField() {
		descriptionField = new LabeledUpperCaseTextField("Description",false);
		descriptionField.getComponent().setText(description);
		descriptionField.setFont(Fonts.DIALOG_BOLD_16);
		return descriptionField;
	}
	
	private LabeledUpperCaseTextField createDescriptionLongField() {
		descriptionLongField = new LabeledUpperCaseTextField("Description Long",false);
		descriptionLongField.getComponent().setText(descriptionLong);
		descriptionLongField.setFont(Fonts.DIALOG_BOLD_16);
		return descriptionLongField;
	}

	protected JButton createCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(Fonts.DIALOG_PLAIN_18);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		return cancelButton;
	}

	protected JButton createCreateButton() {
		createButton = new JButton("Create");
		createButton.setFont(Fonts.DIALOG_PLAIN_18);
		createButton.setMnemonic(KeyEvent.VK_R);
		return createButton;
	}

	// === handlers === //

	// === action mappings ===//
	protected void mapActions() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public boolean isDataCreated() {
		return dataCreated;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == createButton) {
			/* 
			 * Part Group, Inspection Part, Inspection Part Location, Defect Group, Defect Type & 
			 * Secondary Part Name, Description, Long Description length validation 
			 * */
			boolean validationFlag = true;
			String validationError = "";
			int nameFieldLength = 0;
			if (getTitle().trim().equals("Inspection Part")) {
				nameFieldLength = 32;
			} else if (getTitle().trim().equals("Defect Type")) {
				nameFieldLength = 45;
			} else {
				nameFieldLength = 20;
			}
			if (nameField.getComponent().getText().trim().length() > nameFieldLength) {
				validationError = getTitle().trim() + " Name should not exceed "+ nameFieldLength +" characters";
				validationFlag = false;
			} else if (nameField.getComponent().getText().trim().length() == 0) {
				validationError = getTitle().trim() + " Name should not be blank";
				validationFlag = false;
			}
			if (descriptionField.getComponent().getText().trim().length() > 10) {
				validationError = validationError
						+ " \n"
						+ getTitle().trim() + " Description should not exceed 10 characters";
				validationFlag = false;
			}
			if (descriptionLongField.getComponent().getText().trim().length() > 30) {
				validationError = validationError
						+ " \n"
						+ getTitle().trim() + " Long Description should not exceed 30 characters";
				validationFlag = false;
			}

			if (!validationFlag) {
				MessageDialog.showError(validationError);
				return;
			}
			dataCreated = true;
		}
		dispose();
	}
	
	public PartGroup getPartGroup() {
		PartGroup partGroup = new PartGroup();
		partGroup.setPartGroupName(nameField.getComponent().getText().trim());
		partGroup.setPartGroupDescriptionShort(descriptionField.getComponent().getText().trim());
		partGroup.setPartGroupDescriptionLong(descriptionLongField.getComponent().getText().trim());
		if (getModelCodeComboBox().getSelectedItem() != null)
		partGroup.setModelCode(getModelCodeComboBox().getSelectedItem().toString().trim());
		return partGroup;
	}
	
	public InspectionPart getInspectionPart() {
		InspectionPart inspectionPart = new InspectionPart();
		inspectionPart.setInspectionPartName(nameField.getComponent().getText().trim());
		inspectionPart.setInspectionPartDescShort(descriptionField.getComponent().getText().trim());
		inspectionPart.setInspectionPartDescLong(descriptionLongField.getComponent().getText().trim());
		return inspectionPart;
	}
	
	public InspectionPartLocation getPartLocation() {
		InspectionPartLocation partLocation = new InspectionPartLocation();
		partLocation.setInspectionPartLocationName(nameField.getComponent().getText().trim());
		partLocation.setInspectionPartLocDescShort(descriptionField.getComponent().getText().trim());
		partLocation.setInspectionPartLocDescLong(descriptionLongField.getComponent().getText().trim());
		return partLocation;
	}
	
	public DefectGroup getDefectGroup(String imageName) {
		DefectGroup defectGroup = new DefectGroup();
		defectGroup.setDefectGroupName(nameField.getComponent().getText().trim());
		defectGroup.setDefectGroupDescriptionShort(descriptionField.getComponent().getText().trim());
		defectGroup.setDefectGroupDescriptionLong(descriptionLongField.getComponent().getText().trim());
		defectGroup.setImageName(imageName);
		if (getModelCodeComboBox().getSelectedItem() != null)
			if(getModelCodeComboBox().getSelectedItem().toString().trim().length() == 0)
				defectGroup.setModelCode(null);
			else
				defectGroup.setModelCode(getModelCodeComboBox().getSelectedItem().toString().trim());
		return defectGroup;
	}
	
	public DefectType getDefectType() {
		DefectType defectType = new DefectType();
		defectType.setDefectTypeName(nameField.getComponent().getText().trim());
		defectType.setDefectTypeDescriptionShort(descriptionField.getComponent().getText().trim());
		defectType.setDefectTypeDescriptionLong(descriptionLongField.getComponent().getText().trim());
		return defectType;
	}
	
	public SecondaryPart getSecondaryPart() {
		SecondaryPart secondaryPart = new SecondaryPart();
		secondaryPart.setSecondaryPartName(nameField.getComponent().getText().trim());
		secondaryPart.setSecondaryPartDescShort(descriptionField.getComponent().getText().trim());
		secondaryPart.setSecondaryPartDescLong(descriptionLongField.getComponent().getText().trim());
		return secondaryPart;
	}
	
}
