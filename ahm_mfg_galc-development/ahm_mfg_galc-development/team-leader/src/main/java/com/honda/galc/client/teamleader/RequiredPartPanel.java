package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.entity.product.RequiredPartId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

public class RequiredPartPanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final String CREATE_REQUIRED_PART ="Create Required Parts";
	private static final String DELETE_PART = "Delete Required Parts";
	private static final String COPY_PART = "Copy Required Parts";
	private static final String PASTE_PART = "Paste Required Parts";
	
	protected ProcessPointSelectiontPanel processPointPanel;
	protected ProductSpecSelectionBase productSpecSelectionPanel;
	protected TablePane partSelectionPanel;
	protected TablePane requiredPartPanel;
	private JTextField partFilterInput;
	
	private PartNameTableModel partNameTableModel;
	private RequiredPartTableModel requiredPartTableModel;
	
	private int startX = 10;
	private int startY = 10;
	private int spaceX = 3;
	private int spaceY = 3;
	private int processPointPanelWidth = 992;
	private int processPointPanelHeight = 50;
	
	private int midPanelHeight = 250;
	private int lowerPanelHeight = 300;
	
	private String currentProductType;
	private String defaultProductType;
	private List<PartName> partNames = new ArrayList<PartName>();
	
	private Map<String,RequiredPart> copyCache = new HashMap<String,RequiredPart>();
	
	public RequiredPartPanel(TabbedMainWindow mainWindow) {

		super("Required Parts", KeyEvent.VK_R,mainWindow);
		AnnotationProcessor.process(this);
		
	}
	
	protected void initComponents() {
		
		setLayout(null);
		
		add(createProcessPointSelectionPanel());
		add(createProductSpecSelectionPanel());
		add(createPartSelectionPanel());
		add(createRequiredPartPanel());
		
	}

	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)){
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}

		processPointPanel= new ProcessPointSelectiontPanel(siteName);
		processPointPanel.setLocation(startX, startY);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);
		//processPointPanel.register(ProcessPointSelectionEvent.DepartmentSelected, this);
		return processPointPanel;
	}
	
	private ProductSpecSelectionBase createProductSpecSelectionPanel() {
		if(isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel(getApplicationProductTypeName());
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel(getApplicationProductTypeName());
		
		productSpecSelectionPanel.setSize(550,midPanelHeight);
		productSpecSelectionPanel.setLocation(startX, startY + processPointPanel.getHeight() + spaceY);
		return productSpecSelectionPanel;
	}
	
	private Component createPartSelectionPanel() {
		JPanel layoutPanel = new JPanel(new MigLayout("insets 0, gap 0", "[][]",""));
		layoutPanel.setSize(440,midPanelHeight);
		layoutPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		layoutPanel.setLocation(startX + productSpecSelectionPanel.getWidth() + spaceX, startY + processPointPanel.getHeight() + spaceY);		
		partSelectionPanel = new TablePane("Available Parts",true);
    	partNameTableModel = new PartNameTableModel(partSelectionPanel.getTable(),partNames,true);
		layoutPanel.add(partSelectionPanel, "height : max, width : max, wrap");
		layoutPanel.add(createPartFilterPanel());
		return layoutPanel;
	}

	protected Component createPartFilterPanel() {
		this.partFilterInput = new JTextField();
		getPartFilterInput().setDocument(new UpperCaseDocument(32));
		getPartFilterInput().setFont(Fonts.DIALOG_BOLD_12);
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0","5[][]2",""));
		panel.add(new JLabel("Part Filter:"));
		panel.add(getPartFilterInput(),"width : max");
		return panel;
	}
	
	private Component createRequiredPartPanel() {
		requiredPartPanel = new TablePane("Required Parts",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		requiredPartPanel.getTable().setRowHeight(20);
		requiredPartPanel.setBounds(10, productSpecSelectionPanel.getY() + productSpecSelectionPanel.getHeight() + spaceY, processPointPanelWidth, lowerPanelHeight);
		return requiredPartPanel;
	}
	
	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		initComponents();
		productSpecSelectionPanel.registerProcessPointSelectionPanel(processPointPanel);
		addListeners();
		isInitialized = true;
	}
	
	private void addListeners() {
		
		MouseListener listener = createMouseListener();
		
		for(LabeledListBox lbox : productSpecSelectionPanel.getColumnBoxsList()){
			lbox.getComponent().addMouseListener(listener);
		}
		
		processPointPanel.getProcessPointComboBox().getComponent().addActionListener(this);
		
		partSelectionPanel.getTable().addMouseListener(createPartsListener());
		requiredPartPanel.getTable().addMouseListener(createRequiredPartListMouseListener());
		requiredPartPanel.getTable().addMouseListener(createRequiredPartListMouseListener());
		requiredPartPanel.addMouseListener(createRequiredPartListMouseListener());
		
    	partSelectionPanel.addListSelectionListener(this);
    	getPartFilterInput().getDocument().addDocumentListener(createPartFilterListener());
	}

    
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(processPointPanel.getProcessPointComboBox().getComponent())) processPointSelected();
		if(e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if(menuItem.getName().equals(CREATE_REQUIRED_PART)) saveRequiredParts();
				else if(menuItem.getName().equals(DELETE_PART)) deleteRequiredParts();
				else if(menuItem.getName().equals(COPY_PART)) copyRequiredParts();
	        	else if(menuItem.getName().equals(PASTE_PART)) pasteRequiredParts();
			}catch(Exception ex) {
				exception = ex;
			}
			if(exception != null) handleException(exception);
		}

	}
    
	private void copyRequiredParts() {
		copyCache.clear();
		List<RequiredPart> requiredPartList = requiredPartTableModel.getSelectedItems();
		for (RequiredPart part : requiredPartList) {
			copyCache.put(part.getId().toString(), part);
		}
	}
	
	private void pasteRequiredParts() {
		List<String> specCodes = ProductSpec.trimWildcard(productSpecSelectionPanel.buildSelectedProductSpecCodes());
		if(specCodes.size() == 0 || copyCache.isEmpty()) return;
		String pp = processPointPanel.getCurrentProcessPointId();
		List<RequiredPart> items = new ArrayList<RequiredPart>();
		
		for(String ymtoc : specCodes) {
			String specCode = ProductSpec.trimWildcard(ymtoc);
			for(RequiredPart part : copyCache.values()) {
				
				part.getId().setProcessPointId(pp);
				part.getId().setProductSpecCode(specCode);
				if(!isMbpnProduct()){
					// Set new selected YMTOC
					String modelYearCode = ProductSpec.extractModelYearCode(specCode);
					part.getId().setModelYearCode(StringUtils.isEmpty(modelYearCode) ? ProductSpec.WILDCARD : modelYearCode);
					String modelCode = ProductSpec.extractModelCode(specCode);
					part.getId().setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
					String modelTypeCode = ProductSpec.extractModelTypeCode(specCode);
					part.getId().setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
					String modelOptionCode = ProductSpec.extractModelOptionCode(specCode);
					part.getId().setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
					String extColorCode = ProductSpec.extractExtColorCode(specCode);
					part.getId().setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
					String intColorCode = ProductSpec.extractIntColorCode(specCode);
					part.getId().setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
				}
				
				// Check if the required part exists already
				if(requiredPartTableModel.findRequiredPart(part) == null) {
					items.add(part);
				}
				
			}
		}	
		getDao(RequiredPartDao.class).saveAll(items);
		logUserAction(SAVED, items);
		for(RequiredPart item: items) {
			AuditLoggerUtil.logAuditInfo(null,item,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		showRequiredPartResult();
	}
	private void deleteRequiredParts() {
		List<RequiredPart> requiredParts =requiredPartTableModel.getSelectedItems();
		if(requiredParts.isEmpty()) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this required part?"))
			return;
		getDao(RequiredPartDao.class).removeAll(requiredParts);
		logUserAction(REMOVED, requiredParts);
		for(RequiredPart requiredPart: requiredParts) {
			AuditLoggerUtil.logAuditInfo(requiredPart,null,"delete", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
		}
		showRequiredPartResult();
		
	}

	private void saveRequiredParts() {
		List<RequiredPart> newRequiredParts = createRequiredParts();
		
		if(newRequiredParts.isEmpty()) return;
		
		for(RequiredPart item : newRequiredParts) {
			RequiredPart requiredPart = requiredPartTableModel.findRequiredPart(item);
			if(requiredPart == null){
				item.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
    			getDao(RequiredPartDao.class).insert(item);
    			logUserAction(INSERTED, item);
    			AuditLoggerUtil.logAuditInfo(null,item,"save", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");
			}
		}
    	showRequiredPartResult();
    	
    }

	private List<RequiredPart> createRequiredParts() {

		List<RequiredPart> requiredParts = new ArrayList<RequiredPart>();
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();

		for(String productSpecCode : specCodes) {
			RequiredPart newRequiredPart = new RequiredPart();
			RequiredPartId id = new RequiredPartId();
			String specCode = ProductSpec.trimWildcard(productSpecCode);
			id.setPartName(partNameTableModel.getSelectedItem().getPartName());
			id.setProcessPointId(processPointPanel.getCurrentProcessPointId());
			id.setProductSpecCode(specCode);
			newRequiredPart.setId(id);
			if(!isMbpnProduct()){//backward compatible
				String modelYearCode = ProductSpec.extractModelYearCode(specCode);
				id.setModelYearCode(StringUtils.isEmpty(modelYearCode) ? ProductSpec.WILDCARD : modelYearCode);
				String modelCode = ProductSpec.extractModelCode(specCode);
				id.setModelCode(StringUtils.isEmpty(modelCode) ? ProductSpec.WILDCARD : modelCode);
				String modelTypeCode = ProductSpec.extractModelTypeCode(specCode);
				id.setModelTypeCode(StringUtils.isEmpty(modelTypeCode) ? ProductSpec.WILDCARD : modelTypeCode);
				String modelOptionCode = ProductSpec.extractModelOptionCode(specCode);
				id.setModelOptionCode(StringUtils.isEmpty(modelOptionCode) ? ProductSpec.WILDCARD : modelOptionCode);
				String extColorCode = ProductSpec.extractExtColorCode(specCode);
				id.setExtColorCode(StringUtils.isEmpty(extColorCode) ? ProductSpec.WILDCARD : extColorCode);
				String intColorCode = ProductSpec.extractIntColorCode(specCode);
				id.setIntColorCode(StringUtils.isEmpty(intColorCode) ? ProductSpec.WILDCARD : intColorCode);
			}
			requiredParts.add(newRequiredPart);

		}
		return requiredParts;
	}
    
     
    private void processPointSelected() {
		
		initPartSelectionPanel();
	
		showRequiredPartResult();
		
	}
    
    private void showRequiredPartResult() {
    	Exception exception = null;
    	try{
    		RequiredPart selectedRequiredPart =requiredPartTableModel == null ? null : requiredPartTableModel.getSelectedItem();
	    	List<RequiredPart> requiredParts = retrieveSelectedRequiredParts();
			requiredPartTableModel = new RequiredPartTableModel(requiredPartPanel.getTable(),requiredParts);
			requiredPartTableModel.pack();
			requiredPartTableModel.addTableModelListener(this);
			
			if(selectedRequiredPart != null) {
				requiredPartTableModel.selectItem(requiredPartTableModel.findRequiredPart(selectedRequiredPart));
			}
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	handleException(exception);
    }
    
    private void initPartSelectionPanel() {
		String productType = processPointPanel.selectedProductType();
		if (productType == null) {
			getPartNames().clear();
		} else if (productType.equalsIgnoreCase(currentProductType)) {
			return;
		} else {
			partNames = getDao(PartNameDao.class).findAllByProductType(productType);
		}
		currentProductType = productType;
		defaultProductType = PropertyService.getPropertyBean(SystemPropertyBean.class).getProductType();
		filterParts();
    }
    
    private void showCopyPasteDeletePartPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		if(requiredPartTableModel == null) return;
		popupMenu.add(createMenuItem(COPY_PART,isCopyEnabled()));
		popupMenu.add(createMenuItem(PASTE_PART,isPasteEnabled()));
		popupMenu.add(createMenuItem(DELETE_PART, requiredPartTableModel.getSelectedItem() != null));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private MouseListener createMouseListener(){
		 return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRequiredPartPopupMenu(e);
			}
		 });
	}
    
    private MouseListener createRequiredPartListMouseListener(){
   	 	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCopyPasteDeletePartPopupMenu(e);
			}
		 });
	}
    
    private MouseListener createPartsListener() {
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRequiredPartPopupMenu(e);
			}
		 }); 
 	}
    
	protected DocumentListener createPartFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				filterParts();
			}

			public void removeUpdate(DocumentEvent e) {
				filterParts();
			}

			public void changedUpdate(DocumentEvent e) {
				filterParts();
			}
		};
		return listener;
	}
    
    protected void showCreateRequiredPartPopupMenu(MouseEvent e) {
    	JPopupMenu popupMenu = new JPopupMenu();
    	partNameTableModel = (PartNameTableModel)partSelectionPanel.getTable().getModel();
		popupMenu.add(createMenuItem(CREATE_REQUIRED_PART, isAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}

    private boolean isAllSelected() {
    	return processPointPanel.isProcessPointSelected() && 
    	productSpecSelectionPanel.isProductSpecSelected() &&
    	partNameTableModel.getSelectedItem() != null;
    }
    
    private boolean isCopyEnabled() {
    	return processPointPanel.isProcessPointSelected() && 
    	requiredPartPanel.getTable().getSelectedRowCount() > 0;
    }
   
	private boolean isPasteEnabled() {
		return productSpecSelectionPanel.isProductSpecSelected() && !copyCache.isEmpty();
	}
 
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		if(e.getSource()== partSelectionPanel.getTable().getSelectionModel()) 
			showRequiredPartResult();
    }

   @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel) &&
				isValidProcessPointSelected()) {
    		showRequiredPartResult();
    	}
    }
    
    @EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
    public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
       	if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
         	   event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
         	   event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
       		showRequiredPartResult();
    	}
        	
    }
	    
	private boolean isValidProcessPointSelected() {
		return processPointPanel.getProcessPointComboBox().getComponent().getSelectedIndex() != -1;
	}
	
	
	private List<RequiredPart> retrieveSelectedRequiredParts() {
    	
    	List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
    	List<RequiredPart> requiredParts = new ArrayList<RequiredPart>();
    	List<ProcessPoint> ppList = processPointPanel.getApplicableProcessPoints();
    	List<RequiredPart> rpList;
    	String productType = processPointPanel.selectedProductType();
    	
    	for(String productSpecCode : specCodes) {
    		String specCode = ProductSpec.trimWildcard(productSpecCode);
    		RequiredPartId id = new RequiredPartId();
			
    		if(ppList.size() == 1) {
		    	id.setProcessPointId(ppList.get(0).getProcessPointId());
    		}
    		
    		id.setProductSpecCode(StringUtils.trim(productSpecCode));
			PartName partName = partNameTableModel ==null? null : partNameTableModel.getSelectedItem();
			if(partName != null) id.setPartName(partName.getPartName());
			if(!isMbpnProduct()){
				id.setModelYearCode(ProductSpec.extractModelYearCode(specCode));
				id.setModelCode(ProductSpec.extractModelCode(specCode));
				id.setModelTypeCode(ProductSpec.extractModelTypeCode(specCode));
				id.setModelOptionCode(ProductSpec.extractModelOptionCode(specCode));
				id.setExtColorCode(ProductSpec.extractExtColorCode(specCode));
				id.setIntColorCode(ProductSpec.extractIntColorCode(specCode));
			}

			if(ppList.size() == 1) {
				rpList = getDao(RequiredPartDao.class).findAllById(id, productType);
			} else {
				if(defaultProductType.equals(productType)) {
					rpList = getDao(RequiredPartDao.class).findAllByIdAndDefaultProductType(id, processPointPanel.selectedDivision());
				} else {
					rpList = getDao(RequiredPartDao.class).findAllByIdAndProductType(id, processPointPanel.selectedDivision(), productType);
				}
			}
			requiredParts.addAll(rpList);
    	}
    	
    	return requiredParts;
    	
    }
	
	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof RequiredPartTableModel) {
			RequiredPartTableModel model =  (RequiredPartTableModel)e.getSource();
			RequiredPart requiredPart = model.getSelectedItem();
			if(requiredPart == null) return;
			Exception exception = null;
			try{
				RequiredPart oldPart = ServiceFactory.getDao(RequiredPartDao.class).findByKey(requiredPart.getId());
				ServiceFactory.getDao(RequiredPartDao.class).update(requiredPart);
				logUserAction(UPDATED, requiredPart);			
				AuditLoggerUtil.logAuditInfo(oldPart,requiredPart,"update", getScreenName(), getUserName().toUpperCase(),"GALC", "GALC_Maintenance");

			}catch(Exception ex) {
				exception = ex;
				model.rollback();
			}
			handleException(exception);
			
		}
	}
	
	protected void filterParts() {
		String filter = getPartFilterInput().getText();
		List<PartName> filteredList = filterParts(filter, getPartNames());
		partSelectionPanel.clearSelection();
		partNameTableModel.refresh(filteredList);
	}
	
	protected List<PartName> filterParts(String filter, List<PartName> partNames) {
		List<PartName> filteredList = new ArrayList<PartName>();
		if (getPartNames() == null || getPartNames().isEmpty()) {
			return filteredList;
		}
		if (StringUtils.isEmpty(filter)) {
			filteredList.addAll(partNames);
			return filteredList;
		}
		
		for (PartName pn : getPartNames()) {
			String name = pn.getPartName();
			if (name == null) {
				continue;
			}
			if (name.toUpperCase().contains(filter)) {
				filteredList.add(pn);
			}
		}
		return filteredList;
	}
	
	protected List<PartName> getPartNames() {
		return partNames;
	}

	protected JTextField getPartFilterInput() {
		return partFilterInput;
	}
}
