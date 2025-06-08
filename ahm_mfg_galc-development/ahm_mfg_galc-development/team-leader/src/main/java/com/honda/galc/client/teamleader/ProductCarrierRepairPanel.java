package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.model.ProductCarrierResultModel;
import com.honda.galc.client.teamleader.property.ProductCarrierPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.entity.product.BaseProduct;

import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

public class ProductCarrierRepairPanel extends ApplicationMainPanel implements ActionListener, ListSelectionListener{
	
	private static final long serialVersionUID = 1L;
	private static final String CREATE_BUILD_ATTRIBUTE ="ADD PRODUCT IN RACK";
	private static final String DELETE_BUILD_ATTRIBUTE ="DELETE PRODUCT FROM RACK";
	private Font labelFont = new Font("dialog", 0, 18);
	private Font fieldFont = new Font("dialog", 0, 36);
	
	private JPanel buttonPanel;
	private JPanel rackPanel;
	private TablePane productCarrierPanel;
	private JButton resetButton;
	private JButton completeButton;
	private JLabel rackLabel;
	private UpperCaseFieldBean rackIdField;
	
	private int selectedIndex;
	
	private ProductCarrierPropertyBean property;
	protected BaseProduct product;
	protected ProductSpec productSpec;
	private List<ProductCarrier> productCarriers=new ArrayList<ProductCarrier>();

	private ProductCarrierResultModel productCarrierResultModel;
	private boolean showMenu;
	
	
	public ProductCarrierRepairPanel(MainWindow mainWin) {
		super(mainWin);
		initialize();
	}
	
	private void initialize() {		
		try {
			property = PropertyService.getPropertyBean(ProductCarrierPropertyBean.class, 
					getMainWindow().getApplication().getApplicationId());
					
			initComponents();
			
			getMainWindow().addWindowListener(new WindowAdapter() {			
				public void windowOpened(WindowEvent e){				
					getRackIdField().requestFocus();						
				}
			});			
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to start EngineToRackMarriagePanel");
		}
	}
	
	public void initComponents() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setMaximumSize(getPreferredSize());
		add(getRackPanel());
		add(getProductCarrierPanel());
		add(getButtonPanel());
		addListener();
		reset();
	}
	
	private void addListener() {
		 getRackIdField().addActionListener(this);
		 getResetButton().addActionListener(this);
		 getCompleteButton().addActionListener(this);
		 
		 getProductCarrierPanel().addMouseListener(createProductCarrierMouseListener());
		 getProductCarrierPanel().getTable().addMouseListener(createProductCarrierMouseListener());
		 getProductCarrierPanel().addListSelectionListener(this);
		 getProductCarrierTable().getSelectionModel().addListSelectionListener(this);
	}
	
	private MouseListener createProductCarrierMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showOptionPopupMenu(e);
			}
		 });  
	}
	
	private void showOptionPopupMenu(MouseEvent e) {
		if(showMenu){
		int rowCount = getProductCarrierPanel().getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		
		popupMenu.add(createMenuItem(CREATE_BUILD_ATTRIBUTE,!StringUtils.isEmpty(getRackIdField().getText())));
		popupMenu.add(createMenuItem(DELETE_BUILD_ATTRIBUTE,(rowCount > 0 && property.isAllowDelete())));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	protected JMenuItem createMenuItem(String name,boolean enabled) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setName(name);
		menuItem.addActionListener(this);
		menuItem.setEnabled(enabled);
		return menuItem;
	}
	
	private Component getRackPanel(){
		if(rackPanel == null){
			rackPanel = new JPanel();
			rackPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
			rackPanel.add(getRackLabel());
			rackPanel.add(getRackIdField());
		}
		return rackPanel;
	}
	
	private TablePane getProductCarrierPanel() {
		
		if(productCarrierPanel == null){
			productCarrierPanel = new TablePane("Product Carrier Panel");
			productCarrierResultModel=getProductCarrierResultModel();
		}
		return productCarrierPanel;
	}
	
	private JTable getProductCarrierTable(){
		return getProductCarrierPanel().getTable();
	}

	private ProductCarrierResultModel getProductCarrierResultModel(){
		if(productCarrierResultModel==null){
			productCarrierResultModel=new ProductCarrierResultModel(null, productCarrierPanel.getTable());
		}
		return productCarrierResultModel;
	}
	
	private Component getButtonPanel() {
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 120, 5));
			buttonPanel.add(getResetButton());	
			buttonPanel.add(getCompleteButton());	
		}
		return buttonPanel;
	}
		
	private JButton getResetButton() {
		if(resetButton == null){
			resetButton = new JButton("Reset");
			resetButton.setName("Reset");
		}
		return resetButton;
	}
	
	private JButton getCompleteButton() {
		if(completeButton == null){
			completeButton = new JButton("Complete");
			completeButton.setName("Complete");
		}
		return completeButton;
	}
	
	private JLabel getRackLabel() {
		if(rackLabel == null){
			rackLabel = new JLabel("Carrier ID", JLabel.TRAILING);
			rackLabel.setName("RackId");
			rackLabel.setFont(labelFont);
			rackLabel.setPreferredSize(new Dimension(130,45));
		}
		return rackLabel;
	}

	private UpperCaseFieldBean getRackIdField() {
		if(rackIdField == null){
			rackIdField = new UpperCaseFieldBean();
			rackIdField.setName("RackIdField");
			rackIdField.setFont(fieldFont);
			rackIdField.setColumns(12);
			rackIdField.setPreferredSize(new Dimension(830,45));
			rackIdField.setEditable(false);
			rackIdField.setEnabled(false);			
		}
		return rackIdField;
	}
	
	private ProductCarrierDao getProductCarrierDao(){
		return ServiceFactory.getDao(ProductCarrierDao.class);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()){
			return;
		}
		
    	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    	if (!lsm.isSelectionEmpty()) {
            selectedIndex = lsm.getMinSelectionIndex();
            ProductCarrierResultModel model = (ProductCarrierResultModel)getProductCarrierTable().getModel();
            ProductCarrier productCarrier = model.getItem(selectedIndex);
            getLogger().debug("Product : "+productCarrier.getId().getProductId()+" is selected");
        }
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==  getRackIdField()) loadProductCarrierResult();
		else if(e.getSource() == getResetButton()) reset();
		else if(e.getSource() == getCompleteButton()) rackReady();
		
		if(e.getSource() instanceof JMenuItem) {
	       	 Exception exception = null;
	       	 try{
		        	 JMenuItem menuItem = (JMenuItem)e.getSource();
		        	 if(menuItem.getName().equals(CREATE_BUILD_ATTRIBUTE)) addProductCarrier();
		        	 if(menuItem.getName().equals(DELETE_BUILD_ATTRIBUTE)) deleteProductCarrier();
       	 
	      	 }catch(Exception ex) {
	       		 exception = ex;
	       	 }
	       	 handleException(exception);
	        }
	}	

	private void loadProductCarrierResult() {
		showMenu=true;
		getMainWindow().clearMessage();
		String carrierId =getRackIdField().getText().trim();
		getRackIdField().setText(carrierId);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			checkRackId(carrierId);
			getLogger().info("Loading Products for Rack ID: "+carrierId);
			
			productCarriers=getProductCarrierDao().findEnginesInCarrier(carrierId);
			getProductCarrierResultModel().refresh(productCarriers);
			int i=1;
			for(ProductCarrier pc:productCarriers){
				getLogger().info("Product "+ i + ": "+pc.getId().getProductId());
				i++;
			}
			enableButtons();
		}catch(Exception e){
			setError(e.toString());
			getProductCarrierResultModel().refresh(null);
			disableButtons();
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void checkRackId(String rackId) {
		if(!isNumberValid(rackId)){			
			throw new TaskException("Invalid rack id length:" + rackId.length() +". Expected length: " + property.getCarrierIdLength());
		}
		if(!isRightMask(rackId)){
			throw new TaskException("Invalid rack id. Expected Mask:" + property.getCarrierIdMask());
		}
		getLogger().info("Valid Rack ID: "+rackId);
	}
	
	private boolean isNumberValid(String rackId){
		if(property.getCarrierIdLength()<=0) return true;
		else return rackId.length()==property.getCarrierIdLength()?true:false;
	}
	
	private boolean isRightMask(String rackId){
		String mask=property.getCarrierIdMask();
		return CommonPartUtility.verification(rackId, mask, PropertyService.getPartMaskWildcardFormat());		
	}
	
	private void addProductCarrier(){
		getLogger().info("Add Function selected.");
		String carrierId =getRackIdField().getText().trim();
		ProductCarrierDialog dialog = new ProductCarrierDialog(this.getMainWindow(),carrierId,getMainWindow(),property);
    	dialog.setLocationRelativeTo(this);
    	dialog.setVisible(true);
    	
    	loadProductCarrierResult();
	}

	private void deleteProductCarrier() {
		getLogger().info("Delete Function selected.");
		List<ProductCarrier> productCarrierlists = getProductCarrierResultModel().getSelectedItems();
		if(productCarrierlists.isEmpty()) return;
		
		if(!MessageDialog.confirm(this, "Are you sure to delete the selected Product?")) return;
		getProductCarrierDao().removeAll(productCarrierlists);
		for(ProductCarrier pc:productCarrierlists){
			getLogger().info("The selected Product "+pc.getId().getProductId()+" is deleted");
		}
		
		loadProductCarrierResult();
		MessageDialog.showInfo(this, "The selected Product is deleted");	
	}

	private void rackReady() {
		String ppid = property.getReadyToShipTrackingProcessPoint().trim();
		getLogger().info("Complete Button Pressed.");
		for(ProductCarrier productCarrier:productCarriers){
			try{
				ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(property.getProductType());
				BaseProduct product = (BaseProduct)productDao.findBySn(productCarrier.getId().getProductId());
				getService(TrackingService.class).track(product,ppid);
				getLogger().info("Tracking status updated for product= "+product.getProductId()+", process point= "+ppid);
			}catch(Exception e){
				setError(e.toString());
			}
		}
		getLogger().info("Rack ready for ship.");
		reset();
		
	}
	
	private void disableButtons() {
		getResetButton().setEnabled(false);
		getCompleteButton().setEnabled(false);
	}
	
	private void enableButtons(){
		getResetButton().setEnabled(true);
		getCompleteButton().setEnabled(true);
	}
	
	private void reset() {
		getMainWindow().clearMessage();
		showMenu=false;
		getRackIdField().setText(new Text(""));
		getRackIdField().setEditable(true);
		getRackIdField().setEnabled(true);	
		disableButtons();
		getRackIdField().requestFocus();
		getProductCarrierResultModel().refresh(null);
	}
	
	private void setError(String msg) {
		showMenu=false;
		getLogger().error("Exception due to: "+ msg);
		getMainWindow().getStatusMessagePanel().setErrorMessageArea(msg);		
	}
	
}
