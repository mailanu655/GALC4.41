package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductIdMaskId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3> MbpnSpecPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Panel for the Teamlead which is use to assign the productId mask to Mbpn</p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * March 05, 2016
 * 
 */

public class MbpnSpecPanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final String CREATE_MASK ="Create Product Id Mask";
	private static final String DELETE_MASK = "Delete Product Id Mask";
	private static final String COPY_MASK = "Copy Product Id Mask";
	private static final String PASTE_MASK = "Paste Product Id Mask";
	
	protected ProcessPointSelectiontPanel processPointPanel;
	protected ProductSpecSelectionBase productSpecSelectionPanel;	
	protected TablePane productIdMaskSelectionPanel;
	protected TablePane productIdMaskPanel;
	
	private ProductIdMaskListTableModel productIdMaskListTableModel;
	private ProductIdMaskTableModel productIdMaskTableModel;
	
	private int startX = 10;
	private int startY = 10;
	private int spaceX = 3;
	private int spaceY = 3;
	private int processPointPanelWidth = 1000;
	private int processPointPanelHeight = 50;
	
	private int midPanelHeight = 250;
	private int lowerPanelHeight = 300;
	
	private Map<String,ProductIdMask> copyCache = new HashMap<String,ProductIdMask>();

	public MbpnSpecPanel(TabbedMainWindow mainWindow) {

		super("MBPN Spec Assignment", KeyEvent.VK_M,mainWindow);
		AnnotationProcessor.process(this);
		
	}
	
	protected void initComponents() {
		
		setLayout(null);
		
		add(createProcessPointSelectionPanel());
		add(createProductSpecSelectionPanel());
		add(createProductIdMaskSelectionPanel());
		add(createProductIdMaskPanel());
		
	}

	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		String siteName = PropertyService.getSiteName();
		if(StringUtils.isEmpty(siteName)){
			MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
		}

		processPointPanel= new ProcessPointSelectiontPanel(siteName);
		processPointPanel.setLocation(startX, startY);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);
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
	
	private TablePane createProductIdMaskSelectionPanel() {
		productIdMaskSelectionPanel = new TablePane("Available Masks",true);
		productIdMaskSelectionPanel.setSize(450,midPanelHeight);
		productIdMaskSelectionPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		productIdMaskSelectionPanel.setLocation(startX + productSpecSelectionPanel.getWidth() + spaceX, startY + processPointPanel.getHeight() + spaceY);
		return productIdMaskSelectionPanel;
	}
	
	private Component createProductIdMaskPanel() {
		productIdMaskPanel = new TablePane("MBPN-ProdSpec Maps",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		productIdMaskPanel.getTable().setRowHeight(20);
		productIdMaskPanel.setBounds(10, productSpecSelectionPanel.getY() + productSpecSelectionPanel.getHeight() + spaceY, processPointPanelWidth, lowerPanelHeight);
		return productIdMaskPanel;
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
		
		productIdMaskSelectionPanel.getTable().addMouseListener(createProductIdMaskListener());
		productIdMaskPanel.getTable().addMouseListener(createProductIdMaskListMouseListener());
		productIdMaskPanel.getTable().addMouseListener(createProductIdMaskListMouseListener());
		productIdMaskPanel.addMouseListener(createProductIdMaskListMouseListener());
				
	}
    
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(processPointPanel.getProcessPointComboBox().getComponent())) processPointSelected();
		if(e.getSource() instanceof JMenuItem) {
			clearErrorMessage();
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				if(menuItem.getName().equals(CREATE_MASK)) saveProductIdMasks();
				else if(menuItem.getName().equals(DELETE_MASK)) deleteProductIdMasks();
				else if(menuItem.getName().equals(COPY_MASK)) copyProductIdMasks();
	        	else if(menuItem.getName().equals(PASTE_MASK)) pasteProductIdMasks();
			}catch(Exception ex) {
				handleException(ex);
			}			
		}
	}
    
	private void copyProductIdMasks() {
		copyCache.clear();
		List<ProductIdMask> ProductIdMaskList = productIdMaskTableModel.getSelectedItems();
		for (ProductIdMask part : ProductIdMaskList) {
			copyCache.put(part.getId().toString(), part);
		}
	}
	
	private void pasteProductIdMasks() {
		List<String> specCodes = ProductSpec.trimWildcard(productSpecSelectionPanel.buildSelectedProductSpecCodes());
		if(specCodes.size() == 0 || copyCache.isEmpty()) return;
		String pp = processPointPanel.getCurrentProcessPointId();
		String productType = processPointPanel.getProductComboBox().getComponent().getSelectedItem().toString();
		List<ProductIdMask> items = new ArrayList<ProductIdMask>();
		
		for(String ymtoc : specCodes) {
			String specCode = ProductSpec.trimWildcard(ymtoc);
			for(ProductIdMask productIdMask : copyCache.values()) {
				
				productIdMask.getId().setProcessPointId(pp);
				productIdMask.getId().setProductType(productType);
				productIdMask.setProductSpecCode(specCode);
				
				// Check if the record exists already
				if(productIdMaskTableModel.findProductIdMask(productIdMask) == null) {
					items.add(productIdMask);
				}
				
			}
		}	
		getDao(ProductIdMaskDao.class).saveAll(items);
		logUserAction(SAVED, items);
		showProductIdMaskResult();
	}
	
	private void deleteProductIdMasks() {
		List<ProductIdMask> productIdMasks =productIdMaskTableModel.getSelectedItems();
		if(productIdMasks.isEmpty()) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this required part?"))
			return;
		getDao(ProductIdMaskDao.class).removeAll(productIdMasks);
		logUserAction(REMOVED, productIdMasks);
		showProductIdMaskResult();		
	}

	private void saveProductIdMasks() {
		List<ProductIdMask> newProductIdMask = createProductIdMasks();
		
		if(newProductIdMask==null) return;
		
		for(ProductIdMask item : newProductIdMask) {
			ProductIdMask productIdMask = getDao(ProductIdMaskDao.class).findByKey(item.getId());
			if(productIdMask == null){
    			getDao(ProductIdMaskDao.class).insert(item);
    			logUserAction(INSERTED, item);
			}
			else{
				JOptionPane.showMessageDialog(this,
					    "Already assigned that Mask "+productIdMask.getId().getProductIdMask()+" to another Spec "+productIdMask.getProductSpecCode()+
					    " Process Point "+productIdMask.getId().getProcessPointId());
			}
		}
    	showProductIdMaskResult();
    	
    }

	private List<ProductIdMask> createProductIdMasks() {

		List<ProductIdMask> productIdMasks = new ArrayList<ProductIdMask>();
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		String productIdMask =productIdMaskListTableModel.getSelectedItem();
		if(StringUtils.isEmpty(productIdMask)){
			productIdMask = JOptionPane.showInputDialog(this, "Input the ProductId Mask");
			if ((productIdMask == null)) {
			    return null;
			}
		}
		for(String productSpecCode : specCodes) {
			//add the new table entity
			ProductIdMask productIdMaskobj = new ProductIdMask();
			ProductIdMaskId id = new ProductIdMaskId();
			String specCode = ProductSpec.trimWildcard(productSpecCode);
			id.setProcessPointId(processPointPanel.getCurrentProcessPointId());
			id.setProductType(processPointPanel.getProductComboBox().getComponent().getSelectedItem().toString());
			id.setProductIdMask(productIdMask);
			productIdMaskobj.setId(id);
			productIdMaskobj.setProductSpecCode(specCode);
		
			if (ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(id.getProductType()))) {
				Mbpn mbpn = getDao(MbpnDao.class).findByKey(specCode);
				if (mbpn == null) {
					JOptionPane.showMessageDialog(this, "No Mbpn define in MBPN table like " + specCode + ". Please select the product spec correctly.");
				} else {
					productIdMasks.add(productIdMaskobj);
				}
			} else {
				productIdMasks.add(productIdMaskobj);
			}
		}
		return productIdMasks;
	}
    
     
    private void processPointSelected() {
		
		initPartSelectionPanel();
	
		showProductIdMaskResult();
		
	}
    
    private void showProductIdMaskResult() {
    	clearErrorMessage();
    	try{
    		ProductIdMask selectedProductIdMask =productIdMaskTableModel == null ? null : productIdMaskTableModel.getSelectedItem();
	    	List<ProductIdMask> ProductIdMask = retrieveSelectedProductIdMask();
	    	productIdMaskTableModel = new ProductIdMaskTableModel(productIdMaskPanel.getTable(),ProductIdMask);
	    	productIdMaskTableModel.pack();
	    	productIdMaskTableModel.addTableModelListener(this);
			
			if(selectedProductIdMask != null) {
				productIdMaskTableModel.selectItem(productIdMaskTableModel.findProductIdMask(selectedProductIdMask));
			}
    	}catch(Exception ex) {
    		handleException(ex);
    	}    	
    }
    
    private void initPartSelectionPanel() {
		List<String> productIdMasks = new ArrayList<String>();
		String productType = processPointPanel.selectedProductType();
		if(productType != null){
				productIdMasks= getDao(ProductIdMaskDao.class).findAllByProductType(productType);
		}

    	productIdMaskListTableModel = new ProductIdMaskListTableModel(productIdMaskSelectionPanel.getTable(),productIdMasks,true);
    	productIdMaskListTableModel.pack();
    	productIdMaskSelectionPanel.addListSelectionListener(this);
    }
    
    private void showCopyPasteDeleteMaskPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		if(productIdMaskTableModel == null) return;
		popupMenu.add(createMenuItem(COPY_MASK,isCopyEnabled()));
		popupMenu.add(createMenuItem(PASTE_MASK,isPasteEnabled()));
		popupMenu.add(createMenuItem(DELETE_MASK, productIdMaskTableModel.getSelectedItem() != null));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    
    private MouseListener createMouseListener(){
		 return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateProductIdMaskPopupMenu(e);
			}
		 });
	}
    
    private MouseListener createProductIdMaskListMouseListener(){
   	 	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCopyPasteDeleteMaskPopupMenu(e);
			}
		 });
   	 	
   	 
	}
    
    private MouseListener createProductIdMaskListener() {
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateProductIdMaskPopupMenu(e);
			}
		 }); 
 	}
    
    protected void showCreateProductIdMaskPopupMenu(MouseEvent e) {
    	JPopupMenu popupMenu = new JPopupMenu();
    	productIdMaskListTableModel = (ProductIdMaskListTableModel)productIdMaskSelectionPanel.getTable().getModel();
		popupMenu.add(createMenuItem(CREATE_MASK, isAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	
	}

    private boolean isAllSelected() {
    	return processPointPanel.isProcessPointSelected() && 
    	productSpecSelectionPanel.isProductSpecSelected() ;
    }
    
    private boolean isCopyEnabled() {
    	return processPointPanel.isProcessPointSelected() && 
    	productIdMaskPanel.getTable().getSelectedRowCount() > 0;
    }
   
	private boolean isPasteEnabled() {
		return productSpecSelectionPanel.isProductSpecSelected() && !copyCache.isEmpty();
	}
 
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		if(e.getSource()== productIdMaskSelectionPanel.getTable().getSelectionModel()) 
			showProductIdMaskResult();
    }

   @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel) &&
				isValidProcessPointSelected()) {
    		showProductIdMaskResult();
    	}
    }
    
    @EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
    public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
       	if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
         	   event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) || 
         	   event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){ 
       		showProductIdMaskResult();
    	}
        	
    }
	    
	private boolean isValidProcessPointSelected() {
		return processPointPanel.getProcessPointComboBox().getComponent().getSelectedIndex() != -1;
	}
	
	
	private List<ProductIdMask> retrieveSelectedProductIdMask() {
    	List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
    	List<ProductIdMask> productIdMask = new ArrayList<ProductIdMask>();
    	List<ProcessPoint> ppList = processPointPanel.getApplicableProcessPoints();
    	List<ProductIdMask> rpList;
    	
    	for(String productSpecCode : specCodes) {
    		if(ppList.size() == 1)
				rpList = getDao(ProductIdMaskDao.class).findAllByProcessPointAndProductSpec(processPointPanel.getCurrentProcessPointId(), productSpecCode);
			else
				rpList = getDao(ProductIdMaskDao.class).findAllByProductSpecCode(productSpecCode);
    		productIdMask.addAll(rpList);
    	}
    	return productIdMask;
    }
	
	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof ProductIdMaskTableModel) {
			ProductIdMaskTableModel model =  (ProductIdMaskTableModel)e.getSource();
			ProductIdMask productIdMask = model.getSelectedItem();
			if(productIdMask == null) return;
			clearErrorMessage();
			try{
				ServiceFactory.getDao(ProductIdMaskDao.class).update(productIdMask);
				logUserAction(UPDATED, productIdMask);
			}catch(Exception ex) {
				handleException(ex);
				model.rollback();
			}			
		}
	}

}