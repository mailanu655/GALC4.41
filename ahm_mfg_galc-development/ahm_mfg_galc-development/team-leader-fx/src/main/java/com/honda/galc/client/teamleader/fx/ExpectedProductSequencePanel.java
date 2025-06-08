package com.honda.galc.client.teamleader.fx;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.ProductSequencePropertyBean;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedLabeledComboBox;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ExpectedProductSequencePanel extends TabbedPanel {
	
	
	private static final long serialVersionUID = -6228668085284502516L;

	private static final String DELETE_EXPECTED_PRODUCT = "DELETE_EXPECTED_PRODUCT";
	private static final String DELETE = "DELETE";
	private static final String INSERT_BEFORE = "INSERT BEFORE";
	private static final String INSERT_AFTER = "INSERT AFTER";

	private LabeledComboBox<String> processPointComboBox;
	
	private TableView<ExpectedProduct> expectedProductTableView;
	private TableView<ProductSequence> productSequenceTableView;
	private String processPoint;
	private Button refreshExpectedButton;
	private Button refreshProductSequenceButton;
	
	//private ExpectedProductTableModel expectedProductTableModel;
	//private ProductSequenceTableModel productSequenceTableModel;
	ObservableList<ProductSequence> productSequence = FXCollections.observableArrayList();

	
	private int panelHight = 200;
	private int panelWidth = 800;
	private int buttonHeight = 45;
	private int buttonWidth = 150;
	private int startX = 10;
	private int startY = 10;
	private int spaceY = 30;
//	public static final Font FONT = new Font("Dialog", Font.PLAIN, 18);
	private List<String> processPoints;
	

	public ExpectedProductSequencePanel() {
		super("Expected Product Sequence", KeyEvent.VK_E);
		init();
	}
	
	private void init() {
		ProductSequencePropertyBean propertyBean = PropertyService.getPropertyBean(ProductSequencePropertyBean.class);
		String processPointStr = propertyBean.getInProductSequenceId();
		if(!StringUtils.isEmpty(processPointStr)){
			processPoints = Arrays.asList(processPointStr.split(","));
		} else {
			processPoints = new ArrayList<String>();
		}
		onTabSelected();
	}
	

	
	@Override
	public void onTabSelected() {
		// TODO FIX THIS NOT SUPPORTED
		if(isInitialized) {
			return;
		} 
		
		initComponents();
		addEventHandlers();
		isInitialized = true;
		
		showResult();
		
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == processPointComboBox.getControl()){
			processPoint = processPointComboBox.getControl().getSelectionModel().selectedItemProperty().toString();
 		   // processPoint = processPointComboBox.getComponent().getSelectedItem().toString();
			showProductSequence();
		}
		
	}
	
	
//	public void actionPerformed(ActionEvent e) {
//	if(e.getSource() == processPointComboBox.getComponent()){
//		processPoint = processPointComboBox.getComponent().getSelectedItem().toString();
//		showProductSequence();
//		
//	} else if(e.getSource() == refreshExpectedButton){
//		showExpectedProduct();
//	} else if(e.getSource() == refreshProductSequenceButton){
//		showProductSequence();
//	}
//	
//	if(e.getSource() instanceof JMenuItem) {
//		try{
//			JMenuItem menuItem = (JMenuItem)e.getSource();
//			if(menuItem.getName().equals(INSERT_BEFORE)) insertBefore();
//			else if(menuItem.getName().equals(INSERT_AFTER)) insertAfter();
//			else if (menuItem.getName().equals(DELETE)) delete();
//			else if(menuItem.getName().equals(DELETE_EXPECTED_PRODUCT)) deleteExpectedProduct();
//			
//		}catch(Exception ex) {
//			handleException(ex);
//		}
//		
//	}
//}
	
	


	


	private void addEventHandlers() {
	//	expectedProductPane.getTable().addMouseListener(createExpectedProductListener());
	//	productSequencePane.getTable().addMouseListener(createProductSequenceListener());
		processPointComboBox.getControl().setOnAction(this);
	//	refreshExpectedButton.addActionListener(this);
	//	refreshProductSequenceButton.addActionListener(this);
		
	}
//
//
//	private MouseListener createProductSequenceListener() {
//		return new PopupMenuMouseAdapter(new IPopupMenu() {
//			public void showPopupMenu(MouseEvent e) {
//				showProductSequencePopupMenu(e);
//			}
//		 });  
//	}
//
//
//	protected void showProductSequencePopupMenu(MouseEvent e) {
//		JPopupMenu popupMenu = new JPopupMenu();
//		productSequenceTableModel = (ProductSequenceTableModel)productSequencePane.getTable().getModel();
//		popupMenu.add(createMenuItem(INSERT_BEFORE, true));
//		popupMenu.add(createMenuItem(INSERT_AFTER, true));
//		popupMenu.add(createMenuItem(DELETE, true));
//		
//		popupMenu.show(e.getComponent(), e.getX(), e.getY());
//		
//	}
//
//
//	private MouseListener createExpectedProductListener() {
//		return new PopupMenuMouseAdapter(new IPopupMenu() {
//			public void showPopupMenu(MouseEvent e) {
//				showExpectedProductPopupMenu(e);
//			}
//		 });  
//	}
//
//
//	protected void showExpectedProductPopupMenu(MouseEvent e) {
//		JPopupMenu popupMenu = new JPopupMenu();
//		expectedProductTableModel = (ExpectedProductTableModel)expectedProductPane.getTable().getModel();
//		popupMenu.add(createMenuItem(DELETE_EXPECTED_PRODUCT, true));
//		popupMenu.show(e.getComponent(), e.getX(), e.getY());
//		
//	}
//
//
	private void initComponents() {
		// create gridpane.
		GridPane gridPane = new GridPane();
		gridPane.setGridLinesVisible(true); /* set to true to debug layout */
		gridPane.setVgap(20);
		gridPane.setHgap(20);
		gridPane.setPadding(new Insets(20,20,20,20));
	    ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(80);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(20);
	    gridPane.getColumnConstraints().addAll(column1,column2);
		
		gridPane.add(createExpectedProductPane(),0,0);
		gridPane.add(createRefreshProductSequenceButton(),1,0);
		gridPane.add(createProcessPointComboBox(),0,1);
		gridPane.add(createProductSequencePane(),0,2);
		gridPane.add(createRefreshExpectedButton(),1,2);
		setCenter(gridPane);
	}
	
	

	private void showResult() {
		try{
			showExpectedProduct();
		}catch(Exception ex) {
			handleException(ex);
		}
	}


	// http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
	
	private void showExpectedProduct() {
	//	ExpectedProduct selectedExpectedProduct = expectedProductTableModel == null ? null : expectedProductTableModel.getSelectedItem();
		
		final ObservableList<ExpectedProduct> expectedProducts = FXCollections.observableArrayList(getDao(ExpectedProductDao.class).findAll());
		
		expectedProductTableView.setEditable(true);
        Callback<TableColumn<ExpectedProduct,String>, TableCell<ExpectedProduct,String>> cellFactory =
             new Callback<TableColumn<ExpectedProduct,String>, TableCell<ExpectedProduct,String>>() {
                 public TableCell<ExpectedProduct,String> call(TableColumn<ExpectedProduct,String> p) {
                    return new CellEditor<ExpectedProduct>();
                 }
             };
 		
	    TableColumn<ExpectedProduct,Integer> rowNumber = UiFactory.createTableColumn(ExpectedProduct.class, Integer.class);
	    rowNumber.setText("Row #");
        rowNumber.setCellValueFactory(new Callback<CellDataFeatures<ExpectedProduct, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(CellDataFeatures<ExpectedProduct, Integer> p) {
            	return new ReadOnlyObjectWrapper<Integer>(expectedProducts.indexOf(p.getValue()) + 1);
            }
         });
		
	    TableColumn<ExpectedProduct,String> processPointCol = UiFactory.createTableColumn(ExpectedProduct.class, String.class);
        processPointCol.setText("Process Point Id");
        processPointCol.setCellValueFactory(new PropertyValueFactory<ExpectedProduct,String>("processPointId"));
        TableColumn<ExpectedProduct,String> productId = UiFactory.createTableColumn(ExpectedProduct.class, String.class);
        productId.setText("Product Id");
        productId.setCellValueFactory(new PropertyValueFactory<ExpectedProduct,String>("productId"));
		
        productId.setCellFactory(cellFactory);
        productId.setOnEditCommit(
            new EventHandler<CellEditEvent<ExpectedProduct, String>>() {
                public void handle(CellEditEvent<ExpectedProduct, String> evt) {
                	ExpectedProduct expectedProduct =(ExpectedProduct) evt.getTableView().getItems().get(evt.getTablePosition().getRow());
                	expectedProduct.setProductId(evt.getNewValue());
            		saveExpectedProduct(expectedProduct);
                }
             }
        );       

        expectedProductTableView.setItems(expectedProducts);
        expectedProductTableView.getColumns().addAll(rowNumber, processPointCol, productId);
      //  tableView.setMaxHeight(400);
		
	//	expectedProductTableModel = new ExpectedProductTableModel(expectedProductPane.getTable(),expectedProducts);
	//	expectedProductTableModel.addTableModelListener(this);
	//	expectedProductPane.addListSelectionListener(this); 

	//	if(selectedExpectedProduct != null) {
	//		expectedProductTableModel.selectItem(expectedProductTableModel.findExpectedProduct(selectedExpectedProduct));
	//	}
		
	}


    private void initializeProductSeqTableView() {
    	
    	
    }

	
	private void showProductSequence() {
	//	ProductSequence selectedproductSequence = productSequenceTableModel == null ? null : productSequenceTableModel.getSelectedItem();
	//			List<ProductSequence> productSequences = getDao(ProductSequenceDao.class).findAll(processPoint);
				
		final ObservableList<ProductSequence>  productSequence = FXCollections.observableArrayList(getDao(ProductSequenceDao.class).findAll());
		
		
		productSequenceTableView.setEditable(true);
        Callback<TableColumn<ProductSequence,String>, TableCell<ProductSequence,String>> cellFactory =
             new Callback<TableColumn<ProductSequence,String>, TableCell<ProductSequence,String>>() {
                 public TableCell<ProductSequence,String> call(TableColumn<ProductSequence,String> p) {
                    return new CellEditor<ProductSequence>();
                 }
             };
         
		
	    TableColumn<ProductSequence,Integer> rowNumber = UiFactory.createTableColumn(ProductSequence.class,Integer.class);
	    rowNumber.setText("Row #");
        rowNumber.setCellValueFactory(new Callback<CellDataFeatures<ProductSequence, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(CellDataFeatures<ProductSequence, Integer> p) {
            	return new ReadOnlyObjectWrapper<Integer>(productSequence.indexOf(p.getValue()) + 1);
            }
         });

		
	    TableColumn<ProductSequence,String> processPointCol = UiFactory.createTableColumn(ProductSequence.class, String.class);
        processPointCol.setText("Process Point Id");
        processPointCol.setCellValueFactory(new Callback<CellDataFeatures<ProductSequence, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ProductSequence, String> p) {
            	return new ReadOnlyObjectWrapper<String>(p.getValue().getId().getProcessPointId());
            }
         });
      
        
        TableColumn<ProductSequence,String> productIdCol = UiFactory.createTableColumn(ProductSequence.class, String.class);
        productIdCol.setText("Product Id");
        productIdCol.setCellValueFactory(new Callback<CellDataFeatures<ProductSequence, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ProductSequence, String> p) {
            	return new ReadOnlyObjectWrapper<String>(p.getValue().getId().getProductId());
            }
         });
        productIdCol.setCellFactory(cellFactory);
        productIdCol.setOnEditCommit(
            new EventHandler<CellEditEvent<ProductSequence, String>>() {
                public void handle(CellEditEvent<ProductSequence, String> evt) {
                	ProductSequence productSequence =(ProductSequence) evt.getTableView().getItems().get(evt.getTablePosition().getRow());
                	productSequence.getId().setProductId(evt.getNewValue());
            		saveProductSequence(productSequence);
                }
             }
        ); 
        
       

        TableColumn<ProductSequence,String> dateCol = UiFactory.createTableColumn(ProductSequence.class, String.class);
        dateCol.setText("Date");
        dateCol.setCellValueFactory(new Callback<CellDataFeatures<ProductSequence, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ProductSequence, String> p) {
            	return new ReadOnlyObjectWrapper<String>( String.format ("%1tF",p.getValue().getReferenceTimestamp()));
            }
         });
        

        productSequenceTableView.setItems(productSequence);
        productSequenceTableView.getColumns().clear();
        productSequenceTableView.getColumns().addAll(rowNumber,processPointCol, productIdCol, dateCol);		
        

				
//		productSequenceTableModel = new ProductSequenceTableModel(productSequencePane.getTable(),productSequences);
//		productSequenceTableModel.addTableModelListener(this);
//		productSequencePane.addListSelectionListener(this); 
//
//		if(selectedproductSequence != null) {
//			productSequenceTableModel.selectItem(productSequenceTableModel.findProductSequence(selectedproductSequence));
//		}
//		
    }

//	public void valueChanged(ListSelectionEvent e) {
//	}
//	
//
//
//	public void tableChanged(TableModelEvent e) {
//		if(e.getSource() instanceof ProductSequenceTableModel) {
//			saveProductSequence(e);
//			
//		} if(e.getSource() instanceof ExpectedProductTableModel){
//			saveExpected(e);
//		}
//		
//	}
//
//
	private void saveProductSequence(ProductSequence productSequence) {
		if(productSequence == null) return;
		
		try{
			ServiceFactory.getDao(ProductSequenceDao.class).save(productSequence);
		}catch(Exception ex) {
			handleException(ex);
		}
	}
	
	private void saveExpectedProduct(ExpectedProduct expectedProduct) {
		if(expectedProduct == null) return;
		try{
			ServiceFactory.getDao(ExpectedProductDao.class).save(expectedProduct);
		}catch(Exception ex) {
			handleException(ex);
		}
	}
//	
//
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == processPointComboBox.getComponent()){
//			processPoint = processPointComboBox.getComponent().getSelectedItem().toString();
//			showProductSequence();
//			
//		} else if(e.getSource() == refreshExpectedButton){
//			showExpectedProduct();
//		} else if(e.getSource() == refreshProductSequenceButton){
//			showProductSequence();
//		}
//		
//		if(e.getSource() instanceof JMenuItem) {
//			try{
//				JMenuItem menuItem = (JMenuItem)e.getSource();
//				if(menuItem.getName().equals(INSERT_BEFORE)) insertBefore();
//				else if(menuItem.getName().equals(INSERT_AFTER)) insertAfter();
//				else if (menuItem.getName().equals(DELETE)) delete();
//				else if(menuItem.getName().equals(DELETE_EXPECTED_PRODUCT)) deleteExpectedProduct();
//				
//			}catch(Exception ex) {
//				handleException(ex);
//			}
//			
//		}
//	}
//	
//	
//	private void insertBefore() {
//		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
//		int index = productSequencePane.getTable().getSelectedRow();
//		ProductSequence newItem = new ProductSequence();
//		ProductSequenceId id = new ProductSequenceId("", selectedItem.getId().getProcessPointId());
//		newItem.setId(id);
//		long time;
//		if(index == 0)
//			newItem.setReferenceTimestamp(new Timestamp(selectedItem.getReferenceTimestamp().getTime() -50000));
//		else if(index > 0 && index < (productSequencePane.getTable().getRowCount() -1)){
//            ProductSequence otherItem = productSequenceTableModel.getItem(index -1);
//            ProductSequence currentItem = productSequenceTableModel.getItem(index);
//            if(Math.abs(currentItem.getReferenceTimestamp().getTime() - otherItem.getReferenceTimestamp().getTime())/2 < 0)
//            {
//                handleException(new TaskException("There is no space between the selected items to insert."));
//                return;
//            }
//            time = (currentItem.getReferenceTimestamp().getTime() + otherItem.getReferenceTimestamp().getTime())/2;
//			newItem.setReferenceTimestamp(new Timestamp(time));
//		}
//		
//		productSequenceTableModel.insertRow(productSequencePane.getTable().getSelectedRow(), newItem);
//		productSequenceTableModel.selectItem(newItem);
//		productSequenceTableModel.setEditableCell(productSequencePane.getTable().getSelectedRow(), 2);
//	}
//
//
//
//	private void insertAfter() {
//		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
//		int index = productSequencePane.getTable().getSelectedRow();
//		ProductSequence newItem = new ProductSequence();
//		ProductSequenceId id = new ProductSequenceId("", selectedItem.getId().getProcessPointId());
//		newItem.setId(id);
//
//		if(index == (productSequencePane.getTable().getRowCount() -1)) //last item
//			newItem.setReferenceTimestamp(new Timestamp(selectedItem.getReferenceTimestamp().getTime() +50000));
//		else if(index > 0 && index < (productSequencePane.getTable().getRowCount() -1)){
//            ProductSequence otherItem = productSequenceTableModel.getItem(index +1);
//            ProductSequence currentItem = productSequenceTableModel.getItem(index);
//            if(Math.abs(currentItem.getReferenceTimestamp().getTime() - otherItem.getReferenceTimestamp().getTime())/2 < 0)
//            {
//                handleException(new TaskException("There is no space between the selected items to insert."));
//                return;
//            }
//            long time = (currentItem.getReferenceTimestamp().getTime() + otherItem.getReferenceTimestamp().getTime())/2;
//			newItem.setReferenceTimestamp(new Timestamp(time));
//			
//		}
//		
//		productSequenceTableModel.insertRow(productSequencePane.getTable().getSelectedRow()+1, newItem);
//		productSequenceTableModel.selectItem(newItem);
//		productSequenceTableModel.setEditableCell(productSequencePane.getTable().getSelectedRow(), 2);
//		
//	}
//
//
//	private void delete() {
//		ProductSequence selectedItem = productSequenceTableModel.getSelectedItem();
//		if(!MessageDialog.confirm(this, "Are you sure that you want to delete the selected product in sequence?"))
//			return;
//		getDao(ProductSequenceDao.class).remove(selectedItem);
//		expectedProductTableModel = null;
//		showProductSequence();
//	}
//
//	private void deleteExpectedProduct() {
//		ExpectedProduct selectedItem = expectedProductTableModel.getSelectedItem();
//		if(!MessageDialog.confirm(this, "Are you sure that you want to delete the selected product?"))
//			return;
//		getDao(ExpectedProductDao.class).remove(selectedItem);
//		expectedProductTableModel = null;
//		showExpectedProduct();
//	}
//
	
	private TableView<ExpectedProduct> createExpectedProductPane(){
		if(expectedProductTableView == null){
			expectedProductTableView = UiFactory.createTableView(ExpectedProduct.class);
//			expectedProductPane = new TablePane("Expected Product");
//			expectedProductPane.setSize(panelWidth,panelHight);
//			expectedProductPane.getTable().setRowHeight(20);
//			expectedProductPane.getTable().setFont(FONT);
//			expectedProductPane.setBounds(startX, startY, panelWidth, panelHight);
		}
		return expectedProductTableView;
	}
	

	
	
	private Button createRefreshExpectedButton() {
		if(refreshExpectedButton == null){
			refreshExpectedButton = UiFactory.createButton("Refresh");
		}
		return refreshExpectedButton;
	}

	private TableView<ProductSequence> createProductSequencePane(){
		if(productSequenceTableView == null){
			productSequenceTableView = UiFactory.createTableView(ProductSequence.class);
//			productSequencePane.setSize(panelWidth,(int)(panelHight*1.5));
//			productSequencePane.getTable().setRowHeight(20);
//			productSequencePane.getTable().setFont(FONT);
//			Rectangle bounds = processPointComboBox.getBounds();
//			productSequencePane.setLocation((int)bounds.getX(), (int)(bounds.getY() + bounds.getHeight() + 5));
		}
		return productSequenceTableView;
	}
	
	private Button createRefreshProductSequenceButton() {
		if(refreshProductSequenceButton == null){
			refreshProductSequenceButton = UiFactory.createButton("Refresh");
//			refreshProductSequenceButton.setMinSize(buttonWidth,buttonHeight);
		}
		return refreshProductSequenceButton;
	}
	
	private LabeledComboBox<String> createProcessPointComboBox() {
		if(processPointComboBox == null){
			processPointComboBox = new LoggedLabeledComboBox<String>("Process Point", true);
            processPointComboBox.setItems(processPoints);
     //       processPointComboBox.getComponent().setFont(FONT);
          //  Rectangle bounds = expectedProductPane.getBounds();
           // processPointComboBox.setBounds((int)bounds.getX(), (int)(bounds.getY() + bounds.getHeight() + spaceY), 150,20);
            processPointComboBox.setMinSize(550, 50);
		}
		return processPointComboBox;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}