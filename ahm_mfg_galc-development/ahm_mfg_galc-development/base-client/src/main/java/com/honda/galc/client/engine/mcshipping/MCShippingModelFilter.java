package com.honda.galc.client.engine.mcshipping;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

public class MCShippingModelFilter extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static MCShippingModelFilter instance;
	private LabeledComboBox prodTypeComboBox;
	private ObjectTablePane<BuildAttribute> blockedModelsTable;
	private ObjectTablePane<ProductSpecCode> allowedModelsTable;
	private JPanel buttonsPanel;
	private JButton addButton;
	private JButton removeButton;
	private JButton doneButton = this.createButton("Done", Fonts.DIALOG_BOLD_24);
	List<BuildAttribute> buildAttributes;
	List<ProductSpecCode> specCodes;
	List<ProductSpecCode> allowedSpecCodes;
	private static String buildAttributeKey= "";
	
	public static MCShippingModelFilter getInstance(String authGroup, String buildAttributeKey){
		if (!isAccessPermitted(authGroup)) return null;
		setBuildAttributeKey(buildAttributeKey);
		if (instance == null)
			instance = new MCShippingModelFilter();
		else 
			instance.setVisible(true);
		
		return instance;
	}
	
	private static void setBuildAttributeKey(String buildAttributeKey) {
		MCShippingModelFilter.buildAttributeKey = buildAttributeKey;
	}

	private MCShippingModelFilter() {
		super();
		this.setSize(800,400);
		this.initComponents();
		this.addListeners();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setTitle("MC Shipping Model Filter");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	protected void initComponents() {
		setLayout(new MigLayout("insets 1", "[grow,fill]"));
		add(this.createProdTypeComboBox(),"wrap, span2, gapleft 10, gaptop 10, gapbottom 30");
		add(this.createBlockedModelsTable(),"grow, gapleft 10");
		add(this.createButtonsPanel(),"grow");
		add(this.createAllowedModelsTable(),"grow, gapright 10, wrap");
		add(this.doneButton, "span 3, center, width 150:150:150, wrap, gapy 10");
		this.reloadTables();
	}
	
	protected LabeledComboBox createProdTypeComboBox(){
		prodTypeComboBox =  new LabeledComboBox("Product Type: ");
		prodTypeComboBox.setFont(Fonts.DIALOG_BOLD_24);
		prodTypeComboBox.setLabelPreferredWidth(200);
		prodTypeComboBox.setInsets(0, 10, 0, 10);
		prodTypeComboBox.getComponent().setEnabled(true);
		prodTypeComboBox.getComponent().insertItemAt(ProductType.BLOCK.getProductName(), 0);
		prodTypeComboBox.getComponent().insertItemAt(ProductType.HEAD.getProductName(), 1);
		prodTypeComboBox.getComponent().insertItemAt(ProductType.CONROD.getProductName(), 2);
		prodTypeComboBox.getComponent().insertItemAt(ProductType.CRANKSHAFT.getProductName(), 3);
		return prodTypeComboBox;
	}
	
	protected ObjectTablePane<BuildAttribute> createBlockedModelsTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Spec Code", "id.productSpecCode");
		mapping.put("Product Type", "productType");		
		this.blockedModelsTable = new ObjectTablePane<BuildAttribute>(mapping.get(), true, true);
		this.blockedModelsTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.blockedModelsTable.getTable().setName("BlockedModelsTable");
		this.blockedModelsTable.getTable().setRowHeight(20);
		this.blockedModelsTable.getTable().setFont(Fonts.DIALOG_PLAIN_20);
		this.blockedModelsTable.setBorder(new TitledBorder(null, "Blocked Models", 1, 0, Fonts.DIALOG_BOLD_20));
		return this.blockedModelsTable;
	}
	
	protected ObjectTablePane<ProductSpecCode> createAllowedModelsTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Spec Code", "id.productSpecCode");
		mapping.put("Product Type", "id.productType");		
		this.allowedModelsTable = new ObjectTablePane<ProductSpecCode>(mapping.get(), true, true);
		this.allowedModelsTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.allowedModelsTable.getTable().setName("AllowedModelsTable");
		this.allowedModelsTable.getTable().setRowHeight(20);
		this.allowedModelsTable.getTable().setFont(Fonts.DIALOG_PLAIN_20);
		this.allowedModelsTable.setBorder(new TitledBorder(null, "Allowed Models", 1, 0, Fonts.DIALOG_BOLD_20));
		return this.allowedModelsTable;
	}
	
	protected JPanel createButtonsPanel(){
		this.addButton = this.createButton(" << ", Fonts.DIALOG_BOLD_24);
		this.removeButton = this.createButton(" >> ", Fonts.DIALOG_BOLD_24);
		this.buttonsPanel = new JPanel();
		this.buttonsPanel.setLayout(new MigLayout("center, center, wrap"));
		this.buttonsPanel.add(this.addButton, "width 80:80:80, gapy 5");
		this.buttonsPanel.add(this.removeButton, "width 80:80:80, gapy 5");
		return this.buttonsPanel;
	}
	
	protected JButton createButton(String label,Font font) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setMargin(new Insets(0,0,0,0));
		button.setFont(font);
		return button;
	}
	
	private void addListeners() {
		this.prodTypeComboBox.getComponent().addActionListener(this);
		this.addButton.addActionListener(this);
		this.removeButton.addActionListener(this);
		this.doneButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.prodTypeComboBox.getComponent())) loadData();
		else if(e.getSource().equals(this.addButton)) addModel();
		else if(e.getSource().equals(this.removeButton)) removeModel();
		else if(e.getSource().equals(this.doneButton))this.dispose();
	}
	
	public void loadData(){
		String prodType = (String)this.prodTypeComboBox.getComponent().getSelectedItem();
		this.buildAttributes = new SortedArrayList<BuildAttribute>();
		this.allowedSpecCodes = new SortedArrayList<ProductSpecCode>();
		this.specCodes = new SortedArrayList<ProductSpecCode>();
		this.buildAttributes.addAll(ServiceFactory.getDao(BuildAttributeDao.class).findAllMatchBuildAttributes(buildAttributeKey));
		this.specCodes.addAll(ServiceFactory.getDao(ProductSpecCodeDao.class).findAllProductSpecCodesOnly(prodType.toUpperCase()));
		ArrayList <BuildAttribute> tmp = new SortedArrayList<BuildAttribute>();
		for (BuildAttribute buildAttr : buildAttributes){
			if (buildAttr.getProductType().equalsIgnoreCase(prodType))
				tmp.add(buildAttr);
		}
		buildAttributes = tmp;
		for (BuildAttribute buildAttr : buildAttributes){
			ProductSpecCode specCode;
			if ((specCode = this.findSpecCode(buildAttr.getProductSpecCode(), buildAttr.getProductType(), this.specCodes)) != null){
				this.specCodes.remove(specCode);
				this.allowedSpecCodes.add(specCode);
			}
		}
		reloadTables();
	}
	
	public void reloadTables(){
		this.blockedModelsTable.reloadData(this.buildAttributes);
		this.allowedModelsTable.reloadData(this.specCodes);
	}
	
	private void addModel(){
		ProductSpecCode specCode;
		if ((specCode=this.allowedModelsTable.getSelectedItem()) == null) return;
		BuildAttribute attr = new BuildAttribute();
		BuildAttributeId attrId = new BuildAttributeId();
		String productType = specCode.getId().getProductType().toUpperCase().trim();
		attrId.setAttribute(buildAttributeKey+ productType);
		attrId.setProductSpecCode(specCode.getId().getProductSpecCode());
		attr.setId(attrId);
		attr.setProductType(specCode.getId().getProductType());
		ServiceFactory.getDao(BuildAttributeDao.class).save(attr);
		this.specCodes.remove(specCode);
		this.allowedSpecCodes.add(specCode);
		this.buildAttributes.add(attr);
		reloadTables();
	}
	
	private void removeModel(){
		BuildAttribute att;
		if ((att=this.blockedModelsTable.getSelectedItem()) == null) return;
		ProductSpecCode specCode;
		if ((specCode = this.findSpecCode(att.getProductSpecCode(), att.getProductType(), this.allowedSpecCodes)) != null){
			ServiceFactory.getDao(BuildAttributeDao.class).remove(att);
			this.buildAttributes.remove(att);
			this.allowedSpecCodes.remove(specCode);
			this.specCodes.add(specCode);
			reloadTables();
		}
	}
	
	private ProductSpecCode findSpecCode(String productSpecCode, String productType, List<ProductSpecCode> list){
		for (ProductSpecCode record : list){
			String argSpecCode = productSpecCode.trim();
			String argProdType = productType.trim();
			String specSpecCode = record.getId().getProductSpecCode().trim();	
			String specProdType = record.getId().getProductType().trim();
			
			if (argSpecCode.equals(specSpecCode) && argProdType.equals(specProdType)){
				return record;
			}
		}
		return null;
	}
	
	private static Boolean isAccessPermitted(String authGroup){
		if(LoginDialog.login() == com.honda.galc.enumtype.LoginStatus.OK){
			if(ClientMain.getInstance().getAccessControlManager().isAuthorized(authGroup)) return true;
			JOptionPane.showMessageDialog(null, "Terminating application! \nYou have no access permission of default application of this terminal",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
}
