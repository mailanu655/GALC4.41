package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ProductSequencePropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>ExpectedProductSequencePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ExpectedProductSequencePanel description </p>
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
 * @author Paul Chou
 * Dec 7, 2010
 *
 */
public class ExpectedProductSequencePanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener{
	private static final long serialVersionUID = -6228668085284502516L;

	private static final String DELETE_EXPECTED_PRODUCT = "DELETE_EXPECTED_PRODUCT";
	private static final String DELETE = "DELETE";
	private static final String INSERT_BEFORE = "INSERT BEFORE";
	private static final String INSERT_AFTER = "INSERT AFTER";

	private LabeledComboBox processPointComboBox;
	
	private TablePane expectedProductPane;
	private TablePane productSequencePane;
	private String processPoint;
	private JButton refreshExpectedButton;
	private JButton refreshProductSequenceButton;
	
	private ExpectedProductTableModel expectedProductTableModel;
	private ProductSequenceTableModel productSequenceTableModel;
	
	private int panelHight = 200;
	private int panelWidth = 800;
	private int buttonHight = 45;
	private int buttonWidth = 150;
	private int startX = 10;
	private int startY = 10;
	private int spaceY = 30;
	public static final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	private List<String> processPoints;
	
	
	public ExpectedProductSequencePanel() {
		super("Expected Product Sequence", KeyEvent.VK_E);
		
		init();
	}
	
	
	private void init() {
		
		setSize(1000, 750);
		
		ProductSequencePropertyBean propertyBean = PropertyService.getPropertyBean(ProductSequencePropertyBean.class);
		String processPointStr = propertyBean.getInProductSequenceId();
		if(!StringUtils.isEmpty(processPointStr)){
			processPoints = Arrays.asList(processPointStr.split(","));
		}
		
		onTabSelected();
	}


	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		initComponents();
		addListeners();
		isInitialized = true;
		
		showResult();
		
	}

	private void addListeners() {
		expectedProductPane.getTable().addMouseListener(createExpectedProductListener());
		productSequencePane.getTable().addMouseListener(createProductSequenceListener());
		processPointComboBox.getComponent().addActionListener(this);
		refreshExpectedButton.addActionListener(this);
		refreshProductSequenceButton.addActionListener(this);
		
	}


	private MouseListener createProductSequenceListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showProductSequencePopupMenu(e);
			}
		 });  
	}


	protected void showProductSequencePopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		productSequenceTableModel = (ProductSequenceTableModel)productSequencePane.getTable().getModel();
		popupMenu.add(createMenuItem(INSERT_BEFORE, true));
		popupMenu.add(createMenuItem(INSERT_AFTER, true));
		popupMenu.add(createMenuItem(DELETE, true));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}


	private MouseListener createExpectedProductListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showExpectedProductPopupMenu(e);
			}
		 });  
	}


	protected void showExpectedProductPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		expectedProductTableModel = (ExpectedProductTableModel)expectedProductPane.getTable().getModel();
		popupMenu.add(createMenuItem(DELETE_EXPECTED_PRODUCT, true));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}


	private void initComponents() {
		setLayout(null);
		
		add(createExpectedProductPane());
		add(createRefreshExpectedButton());
		add(createProcessPointComboBox());
	    add(createProductSequencePane());
	    add(createRefreshProductSequenceButton());
				
	}
	
	

	private void showResult() {

		try{
			showExpectedProduct();
		}catch(Exception ex) {
			handleException(ex);
		}
	}


	private void showExpectedProduct() {
		ExpectedProduct selectedExpectedProduct = expectedProductTableModel == null ? null : expectedProductTableModel.getSelectedItem();
		List<ExpectedProduct> expectedProducts = getDao(ExpectedProductDao.class).findAllOrderByProcessPoint();
		expectedProductTableModel = new ExpectedProductTableModel(expectedProductPane.getTable(),expectedProducts);
		expectedProductTableModel.addTableModelListener(this);
		expectedProductPane.addListSelectionListener(this); 

		if(selectedExpectedProduct != null) {
			expectedProductTableModel.selectItem(expectedProductTableModel.findExpectedProduct(selectedExpectedProduct));
		}
		
	}


	private void showProductSequence() {
		ProductSequence selectedproductSequence = productSequenceTableModel == null ? null : productSequenceTableModel.getSelectedItem();
		List<ProductSequence> productSequences = getDao(ProductSequenceDao.class).findAll(processPoint);
		productSequenceTableModel = new ProductSequenceTableModel(productSequencePane.getTable(),productSequences);
		productSequenceTableModel.addTableModelListener(this);
		productSequencePane.addListSelectionListener(this); 

		if(selectedproductSequence != null) {
			productSequenceTableModel.selectItem(productSequenceTableModel.findProductSequence(selectedproductSequence));
		}
		
	}


	public void valueChanged(ListSelectionEvent e) {
	}
	


	public void tableChanged(TableModelEvent e) {
		if(e.getSource() instanceof ProductSequenceTableModel) {
			saveProductSequence(e);
			
		} if(e.getSource() instanceof ExpectedProductTableModel){
			saveExpected(e);
		}
		
	}


	private void saveProductSequence(TableModelEvent e) {
		ProductSequenceTableModel model = (ProductSequenceTableModel)e.getSource();
		ProductSequence sequence = model.getSelectedItem();
		if(sequence == null) return;
		
		try{
			ServiceFactory.getDao(ProductSequenceDao.class).save(sequence);
			logUserAction(SAVED, sequence);
			//showProductSequence();
		}catch(Exception ex) {
			model.rollback();
			handleException(ex);
		}
	}


	private void saveExpected(TableModelEvent e) {
		ExpectedProductTableModel model = (ExpectedProductTableModel)e.getSource();
		ExpectedProduct expectedProduct = model.getSelectedItem();
		if(expectedProduct == null) return;
		
		try{
			ServiceFactory.getDao(ExpectedProductDao.class).save(expectedProduct);
			logUserAction(SAVED, expectedProduct);
		}catch(Exception ex) {
			model.rollback();
			handleException(ex);
		}
	}
	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == processPointComboBox.getComponent()){
			processPoint = processPointComboBox.getComponent().getSelectedItem().toString();
			showProductSequence();
			
		} else if(e.getSource() == refreshExpectedButton){
			showExpectedProduct();
		} else if(e.getSource() == refreshProductSequenceButton){
			showProductSequence();
		}
		
		if(e.getSource() instanceof JMenuItem) {
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if(menuItem.getName().equals(INSERT_BEFORE)) insertBefore();
				else if(menuItem.getName().equals(INSERT_AFTER)) insertAfter();
				else if (menuItem.getName().equals(DELETE)) delete();
				else if(menuItem.getName().equals(DELETE_EXPECTED_PRODUCT)) deleteExpectedProduct();
				
			}catch(Exception ex) {
				handleException(ex);
			}
			
		}
	}
	
	
	private void insertBefore() {
		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
		int index = productSequencePane.getTable().getSelectedRow();
		ProductSequence newItem = new ProductSequence();
		ProductSequenceId id = new ProductSequenceId("", selectedItem.getId().getProcessPointId());
		newItem.setId(id);
		long time;
		if(index == 0)
			newItem.setReferenceTimestamp(new Timestamp(selectedItem.getReferenceTimestamp().getTime() -50000));
		else if(index > 0 && index < (productSequencePane.getTable().getRowCount())){
            ProductSequence otherItem = productSequenceTableModel.getItem(index -1);
            ProductSequence currentItem = productSequenceTableModel.getItem(index);
            if(Math.abs(currentItem.getReferenceTimestamp().getTime() - otherItem.getReferenceTimestamp().getTime())/2 < 0)
            {
                handleException(new TaskException("There is no space between the selected items to insert."));
                return;
            }
            time = (currentItem.getReferenceTimestamp().getTime() + otherItem.getReferenceTimestamp().getTime())/2;
			newItem.setReferenceTimestamp(new Timestamp(time));
		}
		
		productSequenceTableModel.insertRow(productSequencePane.getTable().getSelectedRow(), newItem);
		productSequenceTableModel.selectItem(newItem);
		productSequenceTableModel.setEditableCell(productSequencePane.getTable().getSelectedRow(), 2);
	}



	private void insertAfter() {
		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
		int index = productSequencePane.getTable().getSelectedRow();
		ProductSequence newItem = new ProductSequence();
		ProductSequenceId id = new ProductSequenceId("", selectedItem.getId().getProcessPointId());
		newItem.setId(id);

		if(index == (productSequencePane.getTable().getRowCount() -1)) //last item
			newItem.setReferenceTimestamp(new Timestamp(selectedItem.getReferenceTimestamp().getTime() +50000));
		else if(index > 0 && index < (productSequencePane.getTable().getRowCount() -1)){
            ProductSequence otherItem = productSequenceTableModel.getItem(index +1);
            ProductSequence currentItem = productSequenceTableModel.getItem(index);
            if(Math.abs(currentItem.getReferenceTimestamp().getTime() - otherItem.getReferenceTimestamp().getTime())/2 < 0)
            {
                handleException(new TaskException("There is no space between the selected items to insert."));
                return;
            }
            long time = (currentItem.getReferenceTimestamp().getTime() + otherItem.getReferenceTimestamp().getTime())/2;
			newItem.setReferenceTimestamp(new Timestamp(time));
			
		}
		
		productSequenceTableModel.insertRow(productSequencePane.getTable().getSelectedRow()+1, newItem);
		productSequenceTableModel.selectItem(newItem);
		productSequenceTableModel.setEditableCell(productSequencePane.getTable().getSelectedRow(), 2);
		
	}


	private void delete() {
		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete the selected product in sequence?"))
			return;
		getDao(ProductSequenceDao.class).remove(selectedItem);
		logUserAction(REMOVED, selectedItem);
		expectedProductTableModel = null;
		showProductSequence();
	}

	private void deleteExpectedProduct() {
		ExpectedProduct selectedItem = expectedProductTableModel.getSelectedItem();
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete the selected product?"))
			return;
		getDao(ExpectedProductDao.class).remove(selectedItem);
		logUserAction(REMOVED, selectedItem);
		expectedProductTableModel = null;
		showExpectedProduct();
	}


	private TablePane createExpectedProductPane(){
		if(expectedProductPane == null){
			expectedProductPane = new TablePane("Expected Product");
			expectedProductPane.setSize(panelWidth,panelHight);
			expectedProductPane.getTable().setRowHeight(20);
			expectedProductPane.getTable().setFont(FONT);
			expectedProductPane.setBounds(startX, startY, panelWidth, panelHight);
		}
		return expectedProductPane;
	}
	
	private Component createRefreshExpectedButton() {
		if(refreshExpectedButton == null){
			refreshExpectedButton = new JButton("Refresh");
			refreshExpectedButton.setFont(FONT);
			refreshExpectedButton.setSize(buttonWidth,buttonHight);
			Rectangle bounds = expectedProductPane.getBounds();
			refreshExpectedButton.setBounds(bounds.x + bounds.width + 10, bounds.y, buttonWidth, buttonHight);
			
		}
		return refreshExpectedButton;
	}
	
	private TablePane createProductSequencePane(){
		if(productSequencePane == null){
			productSequencePane = new TablePane("Product Sequence");
			productSequencePane.setSize(panelWidth,(int)(panelHight*1.5));
			productSequencePane.getTable().setRowHeight(20);
			productSequencePane.getTable().setFont(FONT);
			Rectangle bounds = processPointComboBox.getBounds();
			productSequencePane.setLocation((int)bounds.getX(), (int)(bounds.getY() + bounds.getHeight() + 5));
		}
		return productSequencePane;
	}
	
	private Component createRefreshProductSequenceButton() {
		if(refreshProductSequenceButton == null){
			refreshProductSequenceButton = new JButton("Refresh");
			refreshProductSequenceButton.setFont(FONT);
			refreshProductSequenceButton.setSize(buttonWidth,buttonHight);
			Rectangle bounds = productSequencePane.getBounds();
			refreshProductSequenceButton.setBounds(bounds.x + bounds.width + 10, bounds.y, buttonWidth, buttonHight);
		}
		return refreshProductSequenceButton;
	}
	
	private LabeledComboBox createProcessPointComboBox() {
		if(processPointComboBox == null){
			processPointComboBox = new LabeledComboBox("Process Point", true);
            ComboBoxModel<String> model = new ComboBoxModel<String>(processPoints);
            processPointComboBox.getComponent().setModel(model);
            processPointComboBox.getComponent().setFont(FONT);
            Rectangle bounds = expectedProductPane.getBounds();
            processPointComboBox.setBounds((int)bounds.getX(), (int)(bounds.getY() + bounds.getHeight() + spaceY), 150,20);
            processPointComboBox.setSize(550, 50);
            
		}
		return processPointComboBox;
	}

}
