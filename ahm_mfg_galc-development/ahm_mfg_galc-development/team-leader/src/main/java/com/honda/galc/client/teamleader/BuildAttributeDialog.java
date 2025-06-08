package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>BuildAttributeDialog Class description</h3>
 * <p> BuildAttributeDialog description </p>
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
 * Nov 12, 2010
 *
 *
 */
public class BuildAttributeDialog extends JDialog implements ActionListener {
	
	
	private static final long serialVersionUID = 1L;
	
	private LabeledTextField nameField = new LabeledTextField("Name");
	private LabeledComboBox subIdBox = new LabeledComboBox("Sub Id");
	private LabeledComboBox valueType = new LabeledComboBox("Attribute Type");
	private LabeledComboBox valueField = new LabeledComboBox("Value",true);
	private LabeledTextField descriptionField = new LabeledTextField("Description");
	
	private JButton cancelButton = new JButton("Cancel");
	private JButton saveButton = new JButton("Save");
	private String attribute;
	private List<String> values;
	private int selectionIndex;
	private boolean editAttribute = true;
	
	private String value;
	private String subId;
	private String description;
	
	String[] ValueType={"String", ProductType.MBPN.name()};
	
	public BuildAttributeDialog(MainWindow owner,String attribute,List<String> values,int selectionIndex) {
		this(owner,attribute,values,selectionIndex,false, "");
		
	}	
	
	public BuildAttributeDialog(MainWindow owner,String attribute,List<String> values,int selectionIndex,boolean editAttribute, String subId) {
		super(owner, "Build Attribute",true);
		this.attribute = attribute;
		this.values = values;
		this.selectionIndex = selectionIndex;
		this.description=getDescription();
		this.editAttribute = editAttribute;
		this.subId=subId;
		setSize(600,255);
		initComponents();
		addListeners();
		//setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
	}

	private String getDescription(){
		if(selectionIndex>-1 && selectionIndex<values.size())
			return ServiceFactory.getDao(BuildAttributeDao.class).findfirstByAttributeAndValue(attribute,values.get(selectionIndex)).getAttributeDescription();
		else{
			this.selectionIndex=-1;
			return "";
		}
			
	}
	
	private void initComponents() {
		
		setLayout(new BorderLayout());
		initNameField();
		initSubIdBox();
		initTypeField();
		initValueField();
		initDescriptionField();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,0));
		panel.add(nameField);
		panel.add(subIdBox);
		panel.add(valueType);
		panel.add(valueField);
		panel.add(descriptionField);
		
		add(panel,BorderLayout.NORTH);
		add(createButtonPanel(),BorderLayout.CENTER);
		
		
	}
	
	private void addListeners() {
		valueType.getComponent().addActionListener(this);
		valueField.getComponent().addActionListener(this);
		subIdBox.getComponent().addActionListener(this);
		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);	
	}
		
	private void initNameField() {
		
		nameField.getLabel().setPreferredSize(new Dimension(50, 25));
		nameField.setFont(new Font("Dialog", Font.BOLD, 16));
		if(attribute != null) nameField.getComponent().setText(attribute);
		nameField.getComponent().setEditable(editAttribute|| attribute== null);
		
		subIdBox.getComponent().setEnabled(editAttribute);
		
		this.attribute = null;
		
	}
	
	private void initSubIdBox() {
		subIdBox.getComponent().setFont(Fonts.DIALOG_BOLD_16);
		subIdBox.getLabel().setPreferredSize(new Dimension(50, 25));
		subIdBox.getComponent().setEnabled(isEnableSubId());

		if(isEnableSubId()){
			ComboBoxModel<String> model = new ComboBoxModel<String>(Product.getSubIds());
			int subIdIndex = Arrays.asList(Product.getSubIds()).indexOf(subId);
			subIdBox.getComponent().setModel(model);
			subIdBox.getComponent().setSelectedIndex(subIdIndex);
			
		}
	}
	
	private boolean isEnableSubId() {
		return getProductType().getSubIds().size() > 0;
	}

	private void initTypeField() {
		valueType.getLabel().setPreferredSize(new Dimension(110, 25));
		valueType.setFont(new Font("Dialog", Font.BOLD, 16));
		valueType.getComponent().setModel(new ComboBoxModel<String>(ValueType));
		valueType.getComponent().setSelectedIndex(0);
		
	}

	private void initValueField() {
		valueField.getLabel().setPreferredSize(new Dimension(50, 25));
		valueField.setFont(new Font("Dialog", Font.BOLD, 16));
		valueField.setModel(new ComboBoxModel<String>(values), selectionIndex);
		valueField.getComponent().setEditable(true);
		
	}
	
	private void initDescriptionField() {
		descriptionField.getLabel().setPreferredSize(new Dimension(50, 25));
		descriptionField.setFont(new Font("Dialog", Font.BOLD, 16));
		if(description != null) descriptionField.getComponent().setText(description);
		descriptionField.getComponent().setEditable(true);
				
	}
	
	private JPanel createButtonPanel() {
		
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 18));
		saveButton.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		ViewUtil.setInsets(panel, 0, 10, 0, 10);
		panel.add(Box.createHorizontalStrut(200));
		JPanel buttonPanel = new JPanel();
		
		// alignButtonWidths();
		buttonPanel.setLayout(new GridLayout(1,0,10,10));
		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);
		ViewUtil.setInsets(buttonPanel, 10, 10, 10, 10);
		panel.add(buttonPanel);
		
		return panel;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public String getSubId() {
		return subId;
	}
	
	public String getAttributeValue() {
		return value;
	}
	
	public String getAttributeDescription() {
		return description;
	}
	
	private void cancelButtonSelected() {
		this.attribute = null;
		this.setVisible(false);
		this.dispose();
	}
	
	private void saveButtonSelected() {
		this.attribute = StringUtils.trim(nameField.getComponent().getText());
		this.value = StringUtils.trim((String)valueField.getComponent().getSelectedItem());
		this.description = StringUtils.trim(descriptionField.getComponent().getText());
		try{
			BuildAttribute buildAttribute=ServiceFactory.getDao(BuildAttributeDao.class).findfirstByAttributeAndValue(attribute,value);
			if(buildAttribute!=null){
				if(StringUtils.trimToEmpty(buildAttribute.getAttributeDescription()).equals(StringUtils.trimToEmpty(this.description))){
					this.setVisible(false);
					this.dispose();
				}else{
					if(MessageDialog.confirm(this, "Are you sure you want to update the description of the attribute value?")){
						ServiceFactory.getDao(BuildAttributeDao.class).updateDescription(attribute, value, description);
						this.setVisible(false);
						this.dispose();						
					}else{
						descriptionField.getComponent().setText(buildAttribute.getAttributeDescription());
					}
				}
			}else{
				ServiceFactory.getDao(BuildAttributeDao.class).updateDescription(attribute, value, description);
				this.setVisible(false);
				this.dispose();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			Logger.getLogger().info("Error: " + ex);
		}
	}
	
	private ProductType getProductType() {
		return ((MainWindow)getOwner()).getProductType();
	}

	private void changeDescription(ActionEvent e) {
		this.attribute = nameField.getComponent().getText();
		this.value = (String)valueField.getComponent().getSelectedItem();
		try{
			BuildAttribute buildAttribute=ServiceFactory.getDao(BuildAttributeDao.class).findfirstByAttributeAndValue(attribute,value);
			if(buildAttribute!=null){
				this.description=buildAttribute.getAttributeDescription();
				}
			else this.description=null;
				descriptionField.getComponent().setText(description);
		}catch(Exception ex){
			ex.printStackTrace();
			Logger.getLogger().info("Error: " + ex);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelButton) cancelButtonSelected();
		else if(e.getSource() == saveButton) saveButtonSelected();
		else if(e.getSource() == subIdBox.getComponent()) subIdChanged(e);
		else if(e.getSource() == valueType.getComponent()) valueTypeChanged(e);
		else if(e.getSource()==valueField.getComponent()) changeDescription(e);
	}

	private void subIdChanged(ActionEvent e) {
		if(isEnableSubId()) this.subId = (String)subIdBox.getComponent().getSelectedItem();
	}
	
	private void valueTypeChanged(ActionEvent e) {
		List<String> stringValues =  new ArrayList<String>(values);
		if (valueType.getComponent().getSelectedItem().equals(ProductType.MBPN.name())) {
			List<Mbpn> mbpnSpecs = loadMbpnSpecs();
			if (mbpnSpecs != null) {
				for(int i = 0; i < mbpnSpecs.size(); i++)
				{
					Mbpn mbpn = mbpnSpecs.get(i);
					stringValues.add(mbpn.getProductSpecCode());
				}
				valueField.setModel(new ComboBoxModel<String>(stringValues), selectionIndex);
			}
		} else valueField.setModel(new ComboBoxModel<String>(values), selectionIndex);;
	}

	private List<Mbpn> loadMbpnSpecs() {
		List<Mbpn> mbpnSpecs;
		try{
			mbpnSpecs = getDao(MbpnDao.class).findAllProductSpecCodesOnly(getProductType().name());
		}catch(Exception e){
			Logger.getLogger().warn(e, "failed to load MBPN Spec.");
			return null;
		}
		return mbpnSpecs;
	}
	
}
