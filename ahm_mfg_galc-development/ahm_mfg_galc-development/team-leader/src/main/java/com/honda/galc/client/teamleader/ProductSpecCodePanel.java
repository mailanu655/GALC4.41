package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
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
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProductSpecCodeSelectionEvent;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ProductSpecCodePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpecCodePanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 31, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 31, 2012
 */
public class ProductSpecCodePanel extends TabbedPanel implements ListSelectionListener, TableModelListener, ActionListener{
	private static final int SELECTION_PANEL_HIGHT = 60;

	private static final long serialVersionUID = 1L;
	
	private TablePane productSpecTablePane;
	private ProductSpecCodeTableModel productSpecTableModel;
	private Color defaultSelectBackground;
	private JButton refreshButton;
	private int midPanelHeight = 500;
	private ProductSpecCodeSelectionPanel specSelectionPanel;
	private List<ProductSpecCode> specs = new ArrayList<ProductSpecCode>();
	
	private static String DELETE = "DELETE";
	private static String CREATE = "CREATE";
	private static String COPY = "COPY";
	private static String PAST = "PAST";
	private ProductSpecCode copy;
	
	
	
	public ProductSpecCodePanel() {
		super("Product Spec", KeyEvent.VK_F);
		AnnotationProcessor.process(this);
	}
	
	public ProductSpecCodePanel(TabbedMainWindow mainWindow) {
		super("Product Spec", KeyEvent.VK_F, mainWindow);
		AnnotationProcessor.process(this);
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) return;

		initComponents();
		addListeners();
		isInitialized = true;
		defaultSelectBackground = productSpecTablePane.getTable().getSelectionBackground();

		showProductSpecCode();

	}

	private void initComponents() {
		setLayout(null);

		add(getSpecSelectionPanel());
		add(createProductSpecTablePane());
		add(createRefreshButton());
		
	}

	private TablePane createProductSpecTablePane() {
		productSpecTablePane = new TablePane("Product Spec");
		productSpecTablePane.setSize(1024,midPanelHeight);
		productSpecTablePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		productSpecTablePane.setLocation(-10, SELECTION_PANEL_HIGHT +1);
		return productSpecTablePane;
	}

	private Component createRefreshButton() {
		if(refreshButton == null){
			refreshButton = new JButton("Refresh");
			refreshButton.setFont(new Font("Dialog", Font.PLAIN, 18));
			refreshButton.setBounds(380, midPanelHeight +10 +SELECTION_PANEL_HIGHT, 120, 30);
		}
		return refreshButton;
	}

	
	private void addListeners() {
		productSpecTablePane.getTable().addMouseListener(createProducSpecListener());
		productSpecTablePane.addMouseListener(createProducSpecListener());
		productSpecTablePane.addListSelectionListener(this);
		refreshButton.addActionListener(this);
	}
	
	public ProductSpecCodeSelectionPanel getSpecSelectionPanel() {
		if(specSelectionPanel == null){
			specSelectionPanel = new ProductSpecCodeSelectionPanel();
			specSelectionPanel.setSize(1024, SELECTION_PANEL_HIGHT);
		}
		return specSelectionPanel;
	}

	private MouseListener createProducSpecListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showEditPopupMenu(e);
			}
		 }); 
	}
	
	protected void showEditPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		productSpecTableModel = (ProductSpecCodeTableModel)productSpecTablePane.getTable().getModel();
		popupMenu.add(createMenuItem(CREATE, isEnableCreate()));
		popupMenu.add(createMenuItem(COPY, isSelectedRow()));
		popupMenu.add(createMenuItem(PAST, copy != null));
		
		ProductSpecCode spec = productSpecTableModel.getSelectedItem();
		String name = DELETE + (spec == null ? "" : " " + spec.getId().getProductSpecCode());
		
		popupMenu.add(createMenuItem(name, isSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private boolean isSelectedRow() {
		return productSpecTableModel.getSelectedItem() != null;
	}

	private boolean isEnableCreate() {
		//Create is enabled when at lease model and year is provided
		//return !StringUtils.isEmpty(getProductType()) && !StringUtils.isEmpty(getModelYear());
		return !StringUtils.isEmpty(getProductType());
	}

	private boolean isSelected() {
		return productSpecTablePane.getTable().getSelectedRow() >= 0;
	}
	
	public void showProductSpecCode(List<ProductSpecCode> specs) {
		this.specs = specs;
		showProductSpecCode();
	}
	
	@EventSubscriber(eventClass=ProductSpecCodeSelectionEvent.class)
	public void showProductSpecCode(ProductSpecCodeSelectionEvent event) {
		if(event.isSource(specSelectionPanel)){
			specs = specSelectionPanel.getSelectedProductSepecCode();
		}
		showProductSpecCode();
	}
	
	private void showProductSpecCode() {
		Exception exception = null;
		try{
			ProductSpecCode selectedItem = productSpecTableModel == null ? null : productSpecTableModel.getSelectedItem();
			
			productSpecTablePane.getTable().setSelectionBackground(defaultSelectBackground);
			productSpecTableModel = new ProductSpecCodeTableModel(productSpecTablePane.getTable(),specs);
			productSpecTableModel.addTableModelListener(this);

			selectedItem = findItem(specs, selectedItem);
			if(selectedItem != null) {
				productSpecTableModel.selectItem(productSpecTableModel.findItem(selectedItem));
			}
		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
		
	}
	

	private ProductSpecCode findItem(List<ProductSpecCode> specs, ProductSpecCode selectedItem) {
		if(selectedItem == null) return null;

		for(ProductSpecCode item : specs){
			if(item.getId().equals(selectedItem.getId()))
				return item;
		}
		return null;
	}


	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem) {
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				if(menuItem.getName().equals(CREATE)) create();
				if(menuItem.getName().startsWith(DELETE)) delete();
				if(menuItem.getName().startsWith(COPY)) copy();
				if(menuItem.getName().startsWith(PAST)) past();
			}catch(Exception ex) {
				handleException(ex);
			}
		} else if(e.getSource() == refreshButton){
			refresh();
		} 
		
	}


	private void past() {
		copy.setModelYearCode(specSelectionPanel.getModelYearCode());
		copy.setModelCode(specSelectionPanel.getModelCode());
		copy.setModelTypeCode(specSelectionPanel.getModelTypeCode());
		copy.setModelOptionCode(specSelectionPanel.getModelOptionCode());
		copy.setExtColorCode(specSelectionPanel.getExtColorCode());
		copy.setIntColorCode(specSelectionPanel.getIntColorCode());
		copy.getId().setProductType(getProductType());
		copy.getId().setProductSpecCode(copy.generateProductSpecCode());

		
		ProductSpecCodeDao dao = getDao(ProductSpecCodeDao.class);
		
		if(dao.findByKey(copy.getId()) != null){
			MessageDialog.showError(this,"Failed to add new product spec code for duplicated key:" + 
					copy.getId().getProductType() + "," + copy.getId().getProductSpecCode());
			return;
		}
		
		ProductSpecCode newSpecCode = dao.insert(copy);
		logUserAction(INSERTED, copy);
		productSpecTableModel.insertRow(0,newSpecCode);
		productSpecTableModel.selectItem(newSpecCode);
		
		copy = null;
	}

	private void copy() {
		copy = productSpecTableModel.getSelectedItem();
		
	}

	private void refresh() {
		List<ProductSpecCode> selectedProductSepecCode = specSelectionPanel.getSelectedProductSepecCode(true);
		showProductSpecCode(selectedProductSepecCode);
	}

	private void create() {
		ProductSpecCode specCode = new ProductSpecCode();
		ProductSpecCodeId id = new ProductSpecCodeId();
		
		specCode.setModelYearCode(specSelectionPanel.getModelYearCode());
		specCode.setModelCode(specSelectionPanel.getModelCode());
		specCode.setModelTypeCode(specSelectionPanel.getModelTypeCode());
		specCode.setModelOptionCode(specSelectionPanel.getModelOptionCode());
		specCode.setExtColorCode(specSelectionPanel.getExtColorCode());
		specCode.setIntColorCode(specSelectionPanel.getIntColorCode());
		id.setProductType(getProductType());
		id.setProductSpecCode(specCode.generateProductSpecCode());
		specCode.setId(id);
		
		ProductSpecCodeDao dao = getDao(ProductSpecCodeDao.class);
		
		if(dao.findByKey(specCode.getId()) != null){
			MessageDialog.showError(this,"Failed to add new product spec code for duplicated key:" + 
					specCode.getId().getProductType() + "," + specCode.getId().getProductSpecCode());
			return;
		}
		
		ProductSpecCode newSpecCode = dao.insert(specCode);
		logUserAction(INSERTED, specCode);
		productSpecTableModel.insertRow(0,newSpecCode);
		productSpecTableModel.selectItem(newSpecCode);
	}
	

	public String getProductType() {
		return specSelectionPanel.getProductType();
	}
	
	public String getModelYear() {
		return specSelectionPanel.getModelYearCode();
	}
	

	private void delete() {
		ProductSpecCode spec = productSpecTableModel.getSelectedItem();
		if(spec == null) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete this product spec code?"))
				return;
		ServiceFactory.getDao(ProductSpecCodeDao.class).remove(spec);
		logUserAction(REMOVED, spec);
		productSpecTableModel.remove(spec);
	}

	public void tableChanged(TableModelEvent e) {
		ProductSpecCodeTableModel model = (ProductSpecCodeTableModel)e.getSource();
		Exception exception = null;
		try{
			ProductSpecCode prodSpec = model.getSelectedItem();
			if(prodSpec == null) return;
			if(e.getType()==TableModelEvent.UPDATE){
				ProductSpecCodeDef validateResult = prodSpec.validate();
				if(validateResult != null){
					if(validateResult == ProductSpecCodeDef.YEAR && prodSpec.getModelYearCode() == null)
						MessageDialog.showError(this,"Please input model year code.");
					else if(validateResult == ProductSpecCodeDef.MODEL && StringUtils.isEmpty(prodSpec.getModelCode()))
						MessageDialog.showError(this,"Please input model code.");
					else {
						MessageDialog.showError(this,"Invalid input: " + validateResult.toString() + " data.");
						model.rollback();
					}
					return;
				}							
								
				//update product spec code if needed
				String productSpecCode = StringUtils.trim(prodSpec.generateProductSpecCode());
				if(!StringUtils.equals(productSpecCode, StringUtils.trim(prodSpec.getId().getProductSpecCode()))){
					ProductSpecCodeDao dao = ServiceFactory.getDao(ProductSpecCodeDao.class);
					
					//validate if new assigned product spec code already exist 
					if(dao.findByProductSpecCode(productSpecCode, prodSpec.getId().getProductType()) != null){
						MessageDialog.showError(this,"Failed to update product spec code due to duplicated key:" + 
								prodSpec.getId().getProductType() + "," + productSpecCode);
						model.rollback();
						return;
					}					
					
					//update only 'product spec code' unique column
					dao.updateProductSpecCode(prodSpec, productSpecCode);
					
					//update the rest of values on record
					prodSpec.getId().setProductSpecCode(productSpecCode);
					dao.update(prodSpec);
					logUserAction(UPDATED, prodSpec);
					
					productSpecTableModel.fireTableDataChanged();
				}
			}
			
			productSpecTableModel.selectItem(productSpecTableModel.findItem(prodSpec));
		}catch(Exception ex) {
			exception = ex;
			model.rollback();
		}
		handleException(exception);

	}
	
	

}
