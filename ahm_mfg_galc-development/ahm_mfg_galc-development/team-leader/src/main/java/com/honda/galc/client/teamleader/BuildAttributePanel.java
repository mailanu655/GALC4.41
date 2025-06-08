package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.teamleader.property.BuildAttributeMaintenancePropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.tablemodel.AttributeValueTableModel;
import com.honda.galc.client.ui.tablemodel.BuildAttributeTableModel;
import com.honda.galc.client.ui.tablemodel.ValueTableModel;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;

public class BuildAttributePanel extends TabbedPanel implements ListSelectionListener, ActionListener{

	
	private static final long serialVersionUID = 1L;
	
	private static final String CREATE_BUILD_ATTRIBUTE ="Create Build Attribute";
	private static final String UPDATE_BUILD_ATTRIBUTE ="Update Build Attribute";
	private static final String DELETE_BUILD_ATTRIBUTE ="DELETE Build Attribute";
	private static final String COPY_BUILD_ATTRIBUTE ="COPY Build Attribute";
	private static final String PASTE_BUILD_ATTRIBUTE ="PASTE Build Attribute";
	
	
	private TablePane attributePanel;
	private TablePane attributeValuePanel;
	private ProductSpecSelectionBase productSpecSelectionPanel;
	private TablePane buildAttributePanel;
	private JTextField attribueFilterInput;
	
	private ValueTableModel attributeTableModel;
	private AttributeValueTableModel attributeValueTableModel;
	private BuildAttributeTableModel buildAttributeTableModel;
	
	private List<KeyValue<String, String>> allAttributes; 

	private List<String> attributeNames = new SortedArrayList<String>();
	private List<String> attributeValues = new SortedArrayList<String>();
	private List<BuildAttribute> attributeValueList = new SortedArrayList<BuildAttribute>();
	private List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
	
	private List<BuildAttribute> copyCache = new ArrayList<BuildAttribute>();
		
	private int midPanelHeight = 250;

	public BuildAttributePanel(TabbedMainWindow mainWindow) {
		super("Build Attribute - " + mainWindow.getApplicationPropertyBean().getProductType(), KeyEvent.VK_B,mainWindow);
		AnnotationProcessor.process(this);
		
	}
	
	protected void initComponents() {
		
		setLayout(new GridLayout(1, 1));
		
		JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createProductSpecSelectionPanel(), createUpperRightPanel());
		topPanel.setDividerLocation(500);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, getExistingBuildAttributePanel());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		add(splitPane);
		
	}

	private JPanel createProductSpecSelectionPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		productSpecSelectionPanel = createProductSpecPanel();
		productSpecSelectionPanel.setSize(350,midPanelHeight);
		panel.add(productSpecSelectionPanel,BorderLayout.CENTER);
		return panel;
	}

	private ProductSpecSelectionBase createProductSpecPanel() {
		if(isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel(getApplicationProductTypeName());
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel(getApplicationProductTypeName());
		
		return productSpecSelectionPanel;
	}
	
	private JPanel createUpperRightPanel() {
		JPanel layoutPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));
		layoutPanel.add(createAttributePanel(), "height : max, width : max");
		layoutPanel.add(createAttributeValuePanel(), "height : max, width : max, span 1 2, wrap");
		layoutPanel.add(createAttributeFilterPanel());
		return layoutPanel;
	}
	
	private Component createAttributePanel() {
		attributePanel = new TablePane("Attribute List",true);
		attributeTableModel = new ValueTableModel(attributeNames,"Attribute Name",attributePanel.getTable());
		
		return attributePanel;
	}

	protected Component createAttributeFilterPanel() {
		this.attribueFilterInput = new JTextField();
		getAttribueFilterInput().setDocument(new UpperCaseDocument(32));
		getAttribueFilterInput().setFont(Fonts.DIALOG_BOLD_12);
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","5[][]2",""));
		panel.add(new JLabel("Attr Filter:"));
		panel.add(getAttribueFilterInput(),"width : max");
		return panel;
	}
	
	private Component createAttributeValuePanel() {
		attributeValuePanel = new TablePane("Attribute Value List",true);
		attributeValueTableModel=new AttributeValueTableModel(attributeValueList,attributeValuePanel.getTable());
		
		return attributeValuePanel;
	}

	private Component getExistingBuildAttributePanel() {
		buildAttributePanel = new TablePane("Build Attribute List",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		buildAttributeTableModel = new BuildAttributeTableModel(buildAttributePanel.getTable(),isProductSpec(),buildAttributes);
		
		return buildAttributePanel;
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		
		initComponents();
		loadData();
		addListeners();
		isInitialized = true;
	}
		
	private void loadData() {
		
		allAttributes = ServiceFactory.getDao(BuildAttributeDao.class).findAllDistinctAttributes(getProductTypeString());
		Set<String> attributeNameSet = new HashSet<String>();
		for(KeyValue<String,String> keyValue : allAttributes) {
			attributeNameSet.add(keyValue.getKey());
		}
		String attribute = attributeTableModel.getSelectedItem();
		attributeNames = new SortedArrayList<String>(attributeNameSet);
		attributeTableModel.refresh(attributeNames);
		attributeTableModel.selectItem(attribute);
	}
	
	private void addListeners() {
		MouseListener attributeMouseListner = createAttributeMouseListener();
		
		for(LabeledListBox lbox : productSpecSelectionPanel.getColumnBoxsList()){
			lbox.getComponent().addMouseListener(attributeMouseListner);
		}
		
		attributePanel.getTable().addMouseListener(attributeMouseListner);
		attributeValuePanel.getTable().addMouseListener(attributeMouseListner);
		buildAttributePanel.getTable().addMouseListener(createBuildAttributeMouseListener());
		buildAttributePanel.addMouseListener(createBuildAttributeMouseListener());
		attributePanel.addListSelectionListener(this);
		buildAttributePanel.addListSelectionListener(this);
		getAttribueFilterInput().getDocument().addDocumentListener(createAttributeFilterListener());
	}
	
    @EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
    public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
           event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
           event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
        	   showBuildAttributeResult();
    	} 
    }
        
    public void actionPerformed(ActionEvent e) {
    	
    	if(e.getSource() instanceof JMenuItem) {
       	 Exception exception = null;
       	 try{
	        	 JMenuItem menuItem = (JMenuItem)e.getSource();
        		 logUserAction("selected menu item: " + menuItem.getName());
	        	 if(menuItem.getName().equals(CREATE_BUILD_ATTRIBUTE)) createBuildAttribute();
	        	 if(menuItem.getName().equals(UPDATE_BUILD_ATTRIBUTE)) updateBuildAttribute();
	        	 if(menuItem.getName().equals(DELETE_BUILD_ATTRIBUTE)) deleteBuildAttributes();
	        	 if(menuItem.getName().equals(COPY_BUILD_ATTRIBUTE)) copyBuildAttributes();
	        	 if(menuItem.getName().equals(PASTE_BUILD_ATTRIBUTE)) pasteBuildAttributes();
	        	 
      	 }catch(Exception ex) {
       		 exception = ex;
       	 }
       	 handleException(exception);
        }
         
    }
    
	private void createBuildAttribute() {
    	String attribute = attributeTableModel.getSelectedItem();
		int selectionIndex = attributeValuePanel.getTable().getSelectedRow();
    	BuildAttributeDialog dialog = new BuildAttributeDialog(this.getMainWindow(),attribute,attributeValues,selectionIndex);
    	dialog.setLocationRelativeTo(this);
    	dialog.setVisible(true);
    	String newAttribute = dialog.getAttribute();
    	String attributeValue = dialog.getAttributeValue();
    	String subId = dialog.getSubId();
    	String attributeDescription = dialog.getAttributeDescription();
    	if(!StringUtils.isEmpty(newAttribute)) {    	
    		List<String> prodSpecs = ProductSpec.trimWildcard(productSpecSelectionPanel.buildSelectedProductSpecCodes());
    		List<BuildAttribute> items = new ArrayList<BuildAttribute>();
    		for(String spec : prodSpecs) {
    			BuildAttribute buildAttribute = new BuildAttribute(spec,newAttribute,attributeValue,attributeDescription);
    			buildAttribute.setProductType(getProductTypeString());
    			if(!StringUtils.isEmpty(subId)) buildAttribute.getId().setSubId(subId);
    			buildAttribute.setUpdateUser(getUserName());
    			items.add(buildAttribute);
    		}
    		getDao(BuildAttributeDao.class).saveAll(items);
   			logUserAction(SAVED, items);
   			for(BuildAttribute item:items) {
   	   			AuditLoggerUtil.logAuditInfo(null,item,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
   	   		}
    		loadData();
    		
    		attributeSelected();
    		
    	}
	}

	private void updateBuildAttribute() {
		BuildAttribute buildAttribute = buildAttributeTableModel.getSelectedItem();
		if(buildAttribute == null) return;
		
		String attribute = buildAttribute.getId().getAttribute();
		List<String> attrValues = getAttributeValues(attribute);
		
		int selectionIndex = attrValues.indexOf(buildAttribute.getAttributeValue());
		
    	BuildAttributeDialog dialog = new BuildAttributeDialog(this.getMainWindow(),attribute, attrValues,selectionIndex,false, buildAttribute.getId().getSubId());
    	
    	dialog.setLocationRelativeTo(this);
    	dialog.setVisible(true);
    	
    	if(StringUtils.isEmpty(dialog.getAttribute())) return;
    	
    	String attributeValue = dialog.getAttributeValue();
    	String subId = dialog.getSubId();
    	String attributeDescription = dialog.getAttributeDescription();
    	
    	if(StringUtils.equals(attributeValue, buildAttribute.getAttributeValue()) 
    			&& StringUtils.equals(attributeDescription, buildAttribute.getAttributeDescription())
    			&& StringUtils.equals(subId, buildAttribute.getId().getSubId())) return;
    	
    	getDao(BuildAttributeDao.class).updateBuildAttribute(buildAttribute, attributeValue, subId, attributeDescription, getUserName());
    	logUserAction(UPDATED, buildAttribute);
    	BuildAttribute oldAttribute = getDao(BuildAttributeDao.class).findByKey(buildAttribute.getId());
    	AuditLoggerUtil.logAuditInfo( buildAttribute,oldAttribute,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
	
    	loadData();
    		
    	attributeSelected(); 		
		
	}


	private void deleteBuildAttributes() {

		List<BuildAttribute> buildAttributes = buildAttributeTableModel.getSelectedItems();
		if(buildAttributes.isEmpty()) return;
		
		if(!MessageDialog.confirm(this, "Are you sure to delete the selected build attributes?")) return;
		getDao(BuildAttributeDao.class).removeAll(buildAttributes);
		logUserAction(REMOVED, buildAttributes);
		for(BuildAttribute buildAttribute: buildAttributes) {
			AuditLoggerUtil.logAuditInfo(buildAttribute,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		loadData();
		attributeSelected();
		MessageDialog.showInfo(this, "The selected build attributes are deleted");
	}

	private void copyBuildAttributes() {
		copyCache.clear();
		copyCache.addAll(buildAttributeTableModel.getSelectedItems());
	}

	private void pasteBuildAttributes() {
		
		List<String> mtos = ProductSpec.trimWildcard(productSpecSelectionPanel.buildSelectedProductSpecCodes());
		if(mtos.size() == 0 || copyCache.isEmpty()) return;
		
		List<BuildAttribute> items = new ArrayList<BuildAttribute>();
		List<Boolean> saveFlag = new ArrayList<Boolean>();
		for(String mto : mtos) {
			
			for(BuildAttribute ba: copyCache) {
				BuildAttribute item = new BuildAttribute(mto,ba.getId().getAttribute(), ba.getId().getSubId(),
						ba.getAttributeValue(), ba.getAttributeDescription());

				item.setProductType(getProductTypeString());
				item.setUpdateUser(getUserName());
				if(!contains(item)) {
					items.add(item);
					if(!mto.equals(ba.getId().getProductSpecCode())) {
						saveFlag.add(true);
					}
				}
			}
		
		}
		
		if (removeDuplicatedAttributes(items)){
			if (!MessageDialog.confirm(this, "There are repeated Attribute Values in your copy selection," +
					"\n just the first occurrence of those duplicated will be saved.\n Do you want to continue?")) {
				copyCache.clear();
				showBuildAttributeResult();
				return;
			}
		}
		
		if(!items.isEmpty())getDao(BuildAttributeDao.class).saveAll(items);
		logUserAction(SAVED, items);
		if(!items.isEmpty()) {
			for(int i=0; i<items.size();i++) {
				if(saveFlag.get(i).equals(true)) {
					AuditLoggerUtil.logAuditInfo(null,items.get(i),"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
				}
			}
		}
		
		copyCache.clear();
		
		showBuildAttributeResult();
		
	}
	
	private boolean removeDuplicatedAttributes(List<BuildAttribute> buildAttributes){		
		Set<BuildAttribute> uniqueBuildAttributes = new HashSet<BuildAttribute>();
		for (BuildAttribute ba: buildAttributes) {
			// Using a Set, it forces to avoid duplicated values
			uniqueBuildAttributes.add(ba);
		}
		// we compare final Set size against List size, if different there were duplicated values
		if (uniqueBuildAttributes.size() != buildAttributes.size()){
			buildAttributes.clear();
			buildAttributes.addAll(uniqueBuildAttributes);
			return true;
		}
		return false;
	}

	private boolean contains(BuildAttribute buildAttribute) {
		
		if(buildAttribute == null) return false;
		for(BuildAttribute item : buildAttributes) {
			if(item.getId().equals(buildAttribute.getId()) && item.getAttributeValue().equals(buildAttribute.getAttributeValue())) return true;
		}
		return false;
	}
	
	protected void filterAttributes() {
		String filter = getAttribueFilterInput().getText();
		attributePanel.clearSelection();
		attributeNames = filterAttributes(filter, allAttributes);
		attributeTableModel.refresh(attributeNames);
	}
	
	protected List<String> filterAttributes(String filter, List<KeyValue<String, String>> attributeNames) {
		List<String> filteredList = new SortedArrayList<String>();
		if (attributeNames == null || attributeNames.isEmpty()) {
			return filteredList;
		}
		Set<String> attributeNameSet = new HashSet<String>();
		if (StringUtils.isEmpty(filter)) {
			for(KeyValue<String,String> keyValue : attributeNames) {
				attributeNameSet.add(keyValue.getKey());
			}
		} else {
			for(KeyValue<String,String> keyValue : attributeNames) {
				String key = keyValue.getKey();
				if (key == null) {
					continue;
				}
				if (key.toUpperCase().contains(filter)) {
					attributeNameSet.add(key);
				}
			}			
		}
		filteredList.addAll(attributeNameSet);
		return filteredList;
	}
	
	private MouseListener createAttributeMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showAttributePopupMenu(e);
			}
		 });  
	}
	
	private MouseListener createBuildAttributeMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showBuildAttributePopupMenu(e);
			}
		 });  
	}
    
	protected DocumentListener createAttributeFilterListener() {
		DocumentListener listner = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void changedUpdate(DocumentEvent e) {
				filterAttributes();
			}
		};
		return listner;
	}
    private void showAttributePopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		boolean aFlag = containsAllProductSpecCodes(ProductSpec.trimWildcard(specCodes));

		boolean flag = productSpecSelectionPanel.isProductSpecSelected()
			&& (!aFlag ||  getSelectedAttribute() == null);
		
		popupMenu.add(createMenuItem(CREATE_BUILD_ATTRIBUTE,flag));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private boolean containsAllProductSpecCodes(List<String> specCodes) {
    	for(String specCode : specCodes) {
    		if(!containsProductSpecCode(specCode)) return false;
    	}
    	return true;
    }
    
    private boolean containsProductSpecCode(String specCode) {
    	for(BuildAttribute buildAttribute : buildAttributes) {
    		if(buildAttribute.getId().getProductSpecCode().equals(specCode.trim())) return true;
    	}
    	return false;
    }
    
	private void showBuildAttributePopupMenu(MouseEvent e) {
		int rowCount = buildAttributePanel.getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(UPDATE_BUILD_ATTRIBUTE,rowCount == 1));
		popupMenu.add(createMenuItem(DELETE_BUILD_ATTRIBUTE,rowCount > 0));
		popupMenu.add(createMenuItem(COPY_BUILD_ATTRIBUTE,productSpecSelectionPanel.isProductSpecSelected() && rowCount > 0));
		popupMenu.add(createMenuItem(PASTE_BUILD_ATTRIBUTE,isPasteEnabled()));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private boolean isPasteEnabled() {
		
		if(copyCache.isEmpty()) return false;
		return true;
		
	}

	public void valueChanged(ListSelectionEvent e) {
	
		if(e.getValueIsAdjusting()) return;
		if(e.getSource()==attributePanel.getTable().getSelectionModel())
			attributeSelected();		
	}

	private String getSelectedAttribute() {
		return attributeTableModel.getSelectedItem();
	}

	private void attributeSelected() {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String attribute = attributeTableModel.getSelectedItem();
			reloadAttributeValues(attribute);
			showBuildAttributeResult();
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	private void showBuildAttributeResult() {
		try {
			clearErrorMessage();
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String attribute = attributeTableModel.getSelectedItem();
			attributeValuePanel.getTable().updateUI();

			List<String> productSpecCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
			buildAttributes.clear();
			BuildAttributeDao dao = ServiceFactory.getDao(BuildAttributeDao.class);
			BuildAttributeMaintenancePropertyBean properties = PropertyService.getPropertyBean(BuildAttributeMaintenancePropertyBean.class, getApplicationId());

			int maxSize = properties.getBuildAttributeResultsetMaxSize();
			String productType = getProductTypeString();
			for (String specCode : productSpecCodes) {
				if (maxSize > 0) {
					long count = dao.count(specCode, attribute, productType);
					if (count > maxSize) {
						String msg = "Resultset: %s exceeds max size : %s for: %s, please select additional criteria.";
						msg = String.format(msg, count, maxSize, specCode);
						setErrorMessage(msg);
						break;
					}
				}
				buildAttributes.addAll(dao.findAllMatchId(specCode, attribute, productType));
			}
			buildAttributeTableModel.refresh(buildAttributes);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	private void reloadAttributeValues(String attribute) {
		
		attributeValues.clear();
		attributeValueList.clear();
		if(attribute == null) return;

		for(KeyValue<String,String> keyValue : allAttributes) {
			if(attribute.equals(keyValue.getKey())) 
			{
				attributeValues.add(keyValue.getValue());
				attributeValueList.add(ServiceFactory.getDao(BuildAttributeDao.class).findfirstByAttributeAndValue(keyValue.getKey(),keyValue.getValue()));
			}
		}
		attributeValueTableModel.refresh(attributeValueList);
	}
	
	private List<String> getAttributeValues(String attribute) {
		
		List<String> values = new ArrayList<String>();
		
		if(attribute == null) return values;
		for(KeyValue<String,String> keyValue : allAttributes) {
			if(attribute.equals(keyValue.getKey())) values.add(keyValue.getValue());
		}
		
		return values;
	}

	public JTextField getAttribueFilterInput() {
		return attribueFilterInput;
	}

	protected List<KeyValue<String, String>> getAllAttributes() {
		return allAttributes;
	}
}
