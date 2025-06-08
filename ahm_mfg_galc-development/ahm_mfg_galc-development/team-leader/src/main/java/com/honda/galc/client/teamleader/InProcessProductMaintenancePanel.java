package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.teamleader.property.InProcessProductMaintPanelPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.InProcessProductMaintenanceDTO;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;



/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 25, 2016
 */


public  class InProcessProductMaintenancePanel extends TabbedPanel 
{

	private ObjectTablePane<InProcessProductMaintenanceDTO> inProcessProductMaintTable=null;
	private LabeledComboBox lineComboBox = null;
	private LabeledComboBox divComboBox = null;
	private JButton productIdButton= null;
	private JButton insertBeforeButton= null;
	private JButton insertAfterButton= null;
	private JButton deleteButton= null;
	private JButton refreshButton= null;
	private JTextField productIdTextField= null;
	private final static String INSERT_BEFORE = "InsertBefore";
	private final static String INSERT_AFTER = "InsertAfter";
	private final static String DELETE = "Delete";
	private static final long serialVersionUID = 1L;
	private ProductType productType;
	private ProductDao<?> productDao;
	private InProcessProductDao inProcessProductDao;
	private DivisionDao divisionDao;
	private LineDao lineDao;
	private InProcessProductMaintPanelPropertyBean propertyBean;
	private Map<String, String> divisionLineMapping;
	boolean isFrame=false;
	private ProductCarrierDao productCarrierDao;
	private ProcessPointDao processPointDao;
	private ProductResultDao productResultDao;




	public InProcessProductMaintenancePanel(TabbedMainWindow mainWindow) {
		super("InProcessProduct Maintenance Panel", KeyEvent.VK_A,mainWindow);	
		initView();	
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) {
			return;
		} 
	}

	public void initView(){
		try{
			propertyBean=PropertyService.getPropertyBean(InProcessProductMaintPanelPropertyBean.class,getApplicationId());
			if(getProductType().equals(ProductType.FRAME))
				isFrame=true;
			setInitComponents();
			initConnections();
			setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
			getLogger().error("An error occurred in the InProcessProduct Maintenance Panel"+e.getMessage());
		}
	}

	protected void initConnections() throws java.lang.Exception {
		getDivComboBox().getComponent().addActionListener(this);;
		getLineComboBox().getComponent().addActionListener(this);;
		getProductButton().addActionListener(this);	
		getRefreshButton().addActionListener(this);
		getDeleteButton().addActionListener(this);
		getInsertBeforeButton().addActionListener(this);
		getInsertAfterButton().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)  {
		if (e.getSource().equals(getDivComboBox().getComponent()))
		{
			loadLineData();		 
		}else if(e.getSource().equals(getLineComboBox().getComponent()))
		{
			loadData();		 
		}else if(e.getSource().equals(getProductButton()))
		{
			clearErrorMessage();
			ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(getMainWindow(), getProductType().name(),ProductNumberDef.getProductNumberDef(getProductType()).get(0).getName());
			manualProductEntryDialog.setModal(true);
			manualProductEntryDialog.setVisible(true);
			String productId =manualProductEntryDialog.getResultProductId();
			if (!(productId == null || StringUtils.isEmpty(productId))) {
				getProductTxtBox().setText(productId);
				getProductTxtBox().requestFocusInWindow();
			} 
		}else if (e.getSource().equals(getDeleteButton()))
			deleteInProcessProduct();		
		else if (e.getSource().equals(getRefreshButton())) 
			refresh();
		else if (e.getSource().equals(getInsertAfterButton())) 
			insertBeforeAfterInProcessProduct(INSERT_AFTER);
		else if (e.getSource().equals(getInsertBeforeButton())) 
			insertBeforeAfterInProcessProduct(INSERT_BEFORE);
	}

	protected void setInitComponents() {				
				setLayout(new MigLayout("insets 20 20 20 20", "[grow,fill]"));				
				add(getDivComboBox());	
				add(getLineComboBox(),"wrap");
				add(getInProcessProductMaintTable(),"span,gapbottom 25");
				add(getProductButton(),"w 100!,gapbottom 25");
				add(getProductTxtBox(),"wrap,gapbottom 25");
				add(getRefreshButton(),"w 125!");
				add(getDeleteButton(),"w 125!");
				add(getInsertBeforeButton(),"w 125!");
				add(getInsertAfterButton(),"w 125! ");			
	}

	private JButton getRefreshButton() {		
		if(refreshButton==null)
		{
			refreshButton = new javax.swing.JButton();
			refreshButton.setName("getRefreshButton");
			refreshButton.setText("Refresh");
			refreshButton.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return refreshButton;
	}

	private JButton getDeleteButton() {		
		if(deleteButton==null)
		{
			deleteButton = new javax.swing.JButton();
			deleteButton.setName("getDeleteButton");
			deleteButton.setText("Delete");
			deleteButton.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return deleteButton;
	}

	private JButton getInsertAfterButton() {		
		if(insertAfterButton==null)
		{
			insertAfterButton = new javax.swing.JButton();
			insertAfterButton.setName("getInsertAfterButton");
			insertAfterButton.setText("Insert After");
			insertAfterButton.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return insertAfterButton;
	}

	private JButton getInsertBeforeButton() {		
		if(insertBeforeButton==null)
		{
			insertBeforeButton = new javax.swing.JButton();
			insertBeforeButton.setName("getInsertBeforeButton");
			insertBeforeButton.setText("Insert Before");
			insertBeforeButton.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return insertBeforeButton;
	}

	private JTextField  getProductTxtBox() {
		if(productIdTextField==null)
		{
			productIdTextField = new JTextField();
			productIdTextField.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 18));
			productIdTextField.setDocument(new AsciiDocument(17));
			productIdTextField.setEditable(false);
		}
		return productIdTextField;
	}

	private JButton getProductButton() {
		if(productIdButton==null)
		{
			productIdButton = new javax.swing.JButton();
			productIdButton.setName("getProductButton");
			productIdButton.setText(ProductNumberDef.getProductNumberDef(getProductType()).get(0).getName());
			productIdButton.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return productIdButton;
	}

	private LabeledComboBox getLineComboBox() {
		if(lineComboBox == null){
			lineComboBox = new LabeledComboBox("Line", true);				
			lineComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			lineComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lineComboBox.getLabel().setFont(new java.awt.Font("dialog", Font.BOLD, 16));
			resetLineComboBoxItems();
		}		
		return lineComboBox;
	}

	private LabeledComboBox getDivComboBox() {		
		if(divComboBox == null){
			divComboBox = new LabeledComboBox("Division", true);				
			divComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			divComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			divComboBox.getLabel().setFont(new java.awt.Font("dialog", Font.BOLD, 16));
			loadDivisionData();
		}		
		return divComboBox;
	}

	private void loadDivisionData() {
		if(getDivisionLineMapping()!=null)
		{
			getDivComboBox().getComponent().addItem("Please Select");		
			for(Entry<String, String> entry : getDivisionLineMapping().entrySet()) {	
				Division division=getDivisionDao().findByKey(entry.getValue());
				if(division==null)
				{
					setErrorMessage("Incorrect Division setup in LINE_DIVISION_MAPPING property");
					return;
				}
				String divStr=division.getDivisionName()+"("+division.getDivisionId()+")";
				if(((DefaultComboBoxModel)getDivComboBox().getComponent().getModel()).getIndexOf(divStr) == -1)
				{					
					getDivComboBox().getComponent().addItem(divStr);				
				}
			}
		}else
		{
			List<Division> divisionList=getDivisionDao().findAll();
			getDivComboBox().getComponent().addItem("Please Select");		
			for(Division division:divisionList)
			{
				String divisionStr=division.getDivisionName()+"("+division.getDivisionId()+")";
				getDivComboBox().getComponent().addItem(divisionStr);
			}
		}		
		getDivComboBox().getComponent().setSelectedIndex(0);
		clearErrorMessage();
	}

	private void refresh() {
		resetLineComboBoxItems();
		getDivComboBox().getComponent().setSelectedIndex(0);
		resetInProcessProductMaintTable();
	}

	private void resetInProcessProductMaintTable()
	{
		getInProcessProductMaintTable().getItems().clear();
		getInProcessProductMaintTable().clearSelection();
		getInProcessProductMaintTable().refresh();
		clearErrorMessage();
		getProductTxtBox().setText("");
		getProductTxtBox().requestFocus();
	}

	private void loadLineData() {
		List<Line> lineList=new ArrayList<Line>();
		clearErrorMessage();
		String selectedDivComboBox=getDivComboBox().getComponent().getSelectedItem().toString().trim();
		resetLineComboBoxItems();
		if(selectedDivComboBox.equals("Please Select"))
			return;
		String divId= selectedDivComboBox.substring(selectedDivComboBox.indexOf("(") + 1, selectedDivComboBox.indexOf(")"));
		if(getDivisionLineMapping()!=null)
		{
			for(Entry<String, String> entry : getDivisionLineMapping().entrySet()) {
				if(entry.getValue().equals(divId))
				{
					Line line=getLineDao().findByKey(entry.getKey());
					if(line==null)
					{
						setErrorMessage("Incorrect Line setup in LINE_DIVISION_MAPPING property");
						return;
					}else
					{
						lineList.add(line);
					}			    	 
				}
			}
		}else
		{			
			Division division=getDao(DivisionDao.class).findByDivisionId(divId);
			lineList=getLineDao().findAllByDivisionId(division, true);
		}
		if(lineList.size()==0)
		{
			setErrorMessage("No lines found for selected division");
			return;
		}
		for(Line line:lineList)
		{
			String lineStr=line.getLineName()+"("+line.getLineId()+")";
			getLineComboBox().getComponent().addItem(lineStr);
		}
	}

	private ObjectTablePane<InProcessProductMaintenanceDTO> getInProcessProductMaintTable() {
		if (inProcessProductMaintTable == null) {
			try {
				List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
				mappings.add(new ColumnMapping("Line Id", "lineId"));
				mappings.add(new ColumnMapping("Line Name", "lineName"));
				mappings.add(new ColumnMapping("Product ID", "productId"));
				mappings.add(new ColumnMapping("Next Product ID", "nextProductId"));
				if(isFrame)
				    mappings.add(new ColumnMapping("Seq No#", "afOnSequenceNumber"));
				inProcessProductMaintTable = new ObjectTablePane<InProcessProductMaintenanceDTO>(mappings);
				inProcessProductMaintTable.setBounds(30, 75, 900, 400);
				inProcessProductMaintTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error("An error occurred in the InProcessProduct Maintenance Panel"+e.getMessage());
			}
		}
		return inProcessProductMaintTable;
	}

	public boolean verifyInsertedProductId() {
		try {
			setErrorMessage(null);
			String insertProductId = getProductTxtBox().getText().trim();			
			if (insertProductId.equals("") == true) {
				setErrorMessage("Please enter the Product Id to be inserted"); 
				return false;			
			}
			else if (insertProductId.trim().length() != ProductNumberDef.getProductNumberDef(getProductType()).get(0).getLength()) {
				setErrorMessage("Length of Product Id is less than "+ProductNumberDef.getProductNumberDef(getProductType()).get(0).getLength()+" characters"); 
				return false;
			}
			for (int i = 0; getInProcessProductMaintTable().getItems().size() > i; i++) {
				String currentProductId =  ((InProcessProductMaintenanceDTO)getInProcessProductMaintTable().getItems().get(i)).getProductId() ;
				if (currentProductId.equals(insertProductId) == true) {
					setErrorMessage("Product ID already exists in the InProcess Product Maintenance table"); 
					return false;
				}
			}			
			return true;
		}
		catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	public void loadData()
	{
		try {
			resetInProcessProductMaintTable();
			if (getLineComboBox().getComponent().getItemCount() <= 0) {					
				setErrorMessage("Line Id List is empty");					
				return ;
			}	
			String lineStr=getLineComboBox().getComponent().getSelectedItem().toString().trim(); 
			String divisionStr=getDivComboBox().getComponent().getSelectedItem().toString().trim(); 
			if (lineStr.equals("Please Select")) {					
				return ;
			}
			String lineId= lineStr.substring(lineStr.indexOf("(") + 1, lineStr.indexOf(")"));
			String divisionId= divisionStr.substring(divisionStr.indexOf("(") + 1, divisionStr.indexOf(")"));
			List<InProcessProductMaintenanceDTO> inProcessProductList = getInProcessProductDao().findSeqListByDivIdLineId(divisionId,lineId,isFrame);	

			if(inProcessProductList.size()==0)
			{
				setErrorMessage("InProcess Product Data not found for selected division and Line");
				return;
			}else
			{
				getInProcessProductMaintTable().getItems().addAll(inProcessProductList);
			}
		}
		catch (Exception e) {
			handleException(e);
		}
	}

	public void resetLineComboBoxItems()
	{		
		getLineComboBox().getComponent().removeAllItems();
		getLineComboBox().getComponent().addItem("Please Select");
		getLineComboBox().getComponent().setSelectedIndex(0);		
	}

	public void insertBeforeAfterInProcessProduct(String insertType) {
		try {
			clearErrorMessage();
			if(!verifySelectedData(insertType))
				return;
			String currentProductId =getInProcessProductMaintTable().getSelectedItem().getProductId(); 
			String currentLineIdStr = getLineComboBox().getComponent().getSelectedItem().toString(); 
			String currentLineId=currentLineIdStr.substring(currentLineIdStr.indexOf("(") + 1, currentLineIdStr.indexOf(")"));
			String insertProductId = getProductTxtBox().getText().trim();
			if(currentProductId.equals(insertProductId))
			{
				setErrorMessage("Product to be inserted is same as the selected Product ");
				return;
			}
			insertInProcessProduct( currentProductId , insertProductId, currentLineId,insertType);
			resetInProcessProductMaintTable();
			loadData();
		}
		catch (Exception e) {
			handleException(e);
		}
	}

	public void insertInProcessProduct(String currentProductId ,String insertProductId,String currentLineId,String insertType)
	{
		try {			
			String currentNextProductId = null;
			InProcessProduct currentInProcessProduct =getInProcessProductDao().findByKey(currentProductId) ;
			InProcessProduct insertInProcessProduct = getInProcessProductDao().findByKey(insertProductId);
			String selectedDivComboBox=getDivComboBox().getComponent().getSelectedItem().toString().trim();
			
			//destination division
			String divisionId= selectedDivComboBox.substring(selectedDivComboBox.indexOf("(") + 1, selectedDivComboBox.indexOf(")"));
			
			String insertLineId = null, originDivision = null;
			
			//a line only has 1 tracking process point
			ProcessPoint destinationTrackingProcessPoint = getProcessPointDao().findTrackingPointsByLine(currentLineId).get(0);
			
			//get the history for the product from the destination tracking process point
			ProductResult result = new ProductResult(insertProductId, destinationTrackingProcessPoint.getProcessPointId());
			List<ProductResult> insertProductHistory = getProductResultDao().findByProductAndProcessPoint(result);
			
			if (insertProductHistory.size() == 0) {
				MessageDialog.showError("Cannot insert product " + insertProductId + " into " + currentLineId + ". Product history does not exist.");
				return;
			}
			
			if(insertInProcessProduct==null)
			{
				insertInProcessProduct =createInProcessProduct(insertProductId, currentLineId);	
			}
			else
			{				
				insertLineId =insertInProcessProduct.getLineId();
				
				//get origin division if product is part of another sequence
				originDivision = getLineDao().findByKey(insertLineId).getDivisionId();
				
				//get the linked list for the origin line
				List<InProcessProductMaintenanceDTO> insertLineIdInProcessProductList = getInProcessProductDao().findSeqListByDivIdLineId(originDivision,insertLineId,isFrame);
				String insertNextProductId = insertInProcessProduct.getNextProductId();
				for(InProcessProductMaintenanceDTO dto:insertLineIdInProcessProductList)
				{
					String tempNextProductId = dto.getNextProductId();
					if ( insertProductId.equals(tempNextProductId)) {
						InProcessProduct tempInProcessProduct=getInProcessProductDao().findByKey(dto.getProductId());
						tempInProcessProduct.setNextProductId(insertNextProductId);
						getInProcessProductDao().save(tempInProcessProduct);	
						logUserAction(SAVED, tempInProcessProduct);
						break;
					}
				}
			}
			if (insertType.equals(INSERT_AFTER) == true) {
				String confirmMsg = "";
				if (originDivision == null && insertLineId == null) {
					confirmMsg = "Are you sure that you want to insert the product " + insertProductId  + " after " + currentProductId +" ?";
				} else {
					confirmMsg = "Are you sure that you want to insert the product " + insertProductId  + " (Division: " + originDivision.trim() + " | Line: " + insertLineId.trim() + ") after " + currentProductId +" ?";
				}
				if(!MessageDialog.confirm(this, confirmMsg)) 
					return;
				currentNextProductId = currentInProcessProduct.getNextProductId();
				insertInProcessProduct.setNextProductId(currentNextProductId);
				currentInProcessProduct.setNextProductId(insertProductId);
				insertInProcessProduct.setLineId(currentLineId);
				getInProcessProductDao().save(currentInProcessProduct);
				logUserAction(SAVED, currentInProcessProduct);
				getInProcessProductDao().save(insertInProcessProduct);
				logUserAction(SAVED, insertInProcessProduct);
			}else if (insertType.equals(INSERT_BEFORE) == true) {
				String confirmMsg = "";
				if (originDivision == null && insertLineId == null) {
					confirmMsg = "Are you sure that you want to insert the product " + insertProductId  + " before " + currentProductId +" ?";
				} else {
					confirmMsg = "Are you sure that you want to insert the product " + insertProductId  + " (Division: " + originDivision.trim() + " | Line: " + insertLineId.trim() + ") before " + currentProductId +" ?";
				}
				if(!MessageDialog.confirm(this, confirmMsg)) 
					return;
				List<InProcessProductMaintenanceDTO> currentLineIdInProcessProductList = getInProcessProductDao().findSeqListByDivIdLineId(divisionId,currentLineId,isFrame);				
				for (InProcessProductMaintenanceDTO dto:currentLineIdInProcessProductList) {
					String tempNextProductId = dto.getNextProductId();
					if (tempNextProductId!=null && tempNextProductId.equals(currentProductId) == true) {
						InProcessProduct tempInProcessProduct=getInProcessProductDao().findByKey(dto.getProductId());
						tempInProcessProduct.setNextProductId(insertProductId);
						insertInProcessProduct.setNextProductId(tempNextProductId.toString());
						insertInProcessProduct.setLineId(currentLineId);
						getInProcessProductDao().save(insertInProcessProduct);
						logUserAction(SAVED, insertInProcessProduct);
						getInProcessProductDao().save(tempInProcessProduct);
						logUserAction(SAVED, tempInProcessProduct);
						break;
					}
				}
			}
			
			//Update product tracking status
			getProductDao().updateNextTracking(insertProductId, currentLineId);
			
		}
		catch (Exception e) {
			handleException(e);
		}
	}

	private InProcessProduct createInProcessProduct(String insertProductId, String currentLineId) {
		InProcessProduct insertProcessProduct = new InProcessProduct();
		try {
			BaseProduct baseProduct = getProductDao().findByKey(insertProductId);
			insertProcessProduct.setProductId(insertProductId);
			insertProcessProduct.setProductSpecCode(baseProduct.getProductSpecCode());	
			insertProcessProduct.setProductionLot(baseProduct.getProductionLot());	
			if (baseProduct instanceof Product)
			{
			   insertProcessProduct.setPlanOffDate(new java.sql.Date(((Product)baseProduct).getPlanOffDate().getTime()));	
			}
			insertProcessProduct.setLastPassingProcessPointId(baseProduct.getLastPassingProcessPointId());	
			insertProcessProduct.setLineId(currentLineId);				
		} catch (Exception e) {
			handleException(e);
		}	
		return insertProcessProduct;
	}

	public void deleteInProcessProduct( )
	{		
		try {
			clearErrorMessage();
			
			String currentProductId=null;
			String currentLineId=null;
			String currentNextProductId = null;
			if(!verifySelectedData(DELETE))
				return;
			currentProductId=getInProcessProductMaintTable().getSelectedItem().getProductId();
			Integer seqNumber = getInProcessProductMaintTable().getSelectedItem().getAfOnSequenceNumber();
			String currentLineIdStr=getLineComboBox().getComponent().getSelectedItem().toString();		
			currentLineId=currentLineIdStr.substring(currentLineIdStr.indexOf("(") + 1, currentLineIdStr.indexOf(")"));
			
			if(!MessageDialog.confirm(this, "Are you sure that you want to delete the product "+ currentProductId +" from "+currentLineIdStr+" ?")) 
				return;
			InProcessProduct currentInProcessProduct = getInProcessProductDao().findByKey(currentProductId);
			currentNextProductId = currentInProcessProduct.getNextProductId();
			updateExpectedProduct(currentProductId);
			String selectedDivComboBox=getDivComboBox().getComponent().getSelectedItem().toString().trim();
			String divisionId= selectedDivComboBox.substring(selectedDivComboBox.indexOf("(") + 1, selectedDivComboBox.indexOf(")"));
			List<InProcessProductMaintenanceDTO> currentLineIdInProcessProductList =getInProcessProductDao().findSeqListByDivIdLineId(divisionId,currentLineId,isFrame);			
			for (InProcessProductMaintenanceDTO dto:currentLineIdInProcessProductList) {
				String strNextProductID = dto.getNextProductId();
				if (strNextProductID != null && strNextProductID.equals(currentProductId) == true) {
					InProcessProduct tempInProcessProduct=getInProcessProductDao().findByKey(dto.getProductId());
					tempInProcessProduct.setNextProductId(currentNextProductId);	
					getInProcessProductDao().save(tempInProcessProduct);
					logUserAction(SAVED, tempInProcessProduct);
				}
			}
			getInProcessProductDao().removeByKey(currentProductId);
			//update ProductCarrier
			if(propertyBean.isUpdateProductCarrier())
			updateProductCarrier(currentProductId,seqNumber, currentLineId);
			logUserAction("removed InProcessProduct by key: " + currentProductId);
			resetInProcessProductMaintTable();
			loadData();
		}
		catch (Exception e) {
			handleException(e);
		}
	}

	private void updateProductCarrier(String currentProductId,Integer seqNumber,String currLineId) {
		String seqString =StringUtil.padLeft(seqNumber.toString(),5,'0');
		List<ProductCarrier> productCarrierList = getProductCarrierDao().findAll(currentProductId, seqString);
		BaseProduct product = ProductTypeUtil.getProductDao(getProductType().getProductName()).findBySn(currentProductId);
		String prodLot = product.getProductionLot().trim();
		String lineNumber = prodLot.substring(5, 6);
		
		for(ProductCarrier productCarrier:productCarrierList) {
			
			ProcessPoint processPoint = getProcessPointDao().findById(productCarrier.getProcessPointId());
			if(processPoint!= null && StringUtils.equalsIgnoreCase(processPoint.getLineId(),currLineId)) {
				
				ProductCarrierId id = new ProductCarrierId();
				id.setCarrierId(productCarrier.getId().getCarrierId());
				id.setOnTimestamp(new Timestamp(productCarrier.getId().getOnTimestamp().getTime()));
				id.setProductId(productCarrier.getId().getProductId());
				
				String emptyProductId = "EMPTY" + lineNumber + DateFormatUtils.format(productCarrier.getId().getOnTimestamp(), "yyMMdd") + StringUtil.padLeft(seqNumber.toString(),5,'0');
				ProductCarrier emptyCarrier = productCarrier;
				emptyCarrier.getId().setProductId(emptyProductId);
				getProductCarrierDao().save(emptyCarrier);
				
				logUserAction("updated productCarrier by key: " + currentProductId);
				
				getProductCarrierDao().removeByKey(id);
			}
		}
		
		
		
	}

	private boolean verifySelectedData(String type) {
		if(getDivComboBox().getComponent().getSelectedIndex()<=0)
		{
			setErrorMessage("Please select a Division from Division Combo Box list");
			return false;
		}
		else if(getLineComboBox().getComponent().getSelectedIndex()<=0)
		{
			setErrorMessage("Please select a line from Line Combo Box list");
			return false;
		}
		else if(getInProcessProductMaintTable().getTable().getSelectedRow()<0)
		{
			setErrorMessage("Please select a Product from InProcess Product Mainteance Table List");
			return false;
		}else if(type.equals(INSERT_BEFORE) && getInProcessProductMaintTable().getTable().getSelectedRow()==0)
		{
			setErrorMessage("Product cannot before the first record in the list");
			return false;
		}
		else if((type.equals(INSERT_BEFORE)|| type.equals(INSERT_AFTER)) && getProductTxtBox().getText().trim().length()<=0)
		{
			setErrorMessage("Please select a Product to be inserted ");
			return false;
		}else if((type.equals(INSERT_BEFORE)|| type.equals(INSERT_AFTER)) && !verifyInsertedProductId())
			return false;
		return true;
	}

	public void updateExpectedProduct(String currentProductId)
	{
		try {
			ExpectedProductDao expectedProductDao=getDao(ExpectedProductDao.class);					
			InProcessProduct inProcessProduct = getInProcessProductDao().findByKey(currentProductId);
			String currentNextProductId = inProcessProduct.getNextProductId();
			String replacedNextProductId = null;
			if (currentNextProductId == null) {
				replacedNextProductId = "";
			}
			else {
				replacedNextProductId = currentNextProductId;
			}				
			List<ExpectedProduct> expectedProductList = expectedProductDao.findAll();
			for (ExpectedProduct expectedProduct:expectedProductList) {
				String productId = expectedProduct.getProductId();
				if (currentProductId.equals(productId)) {
					expectedProduct.setProductId(replacedNextProductId);
					expectedProductDao.save(expectedProduct);
					logUserAction(SAVED, expectedProduct);
				}
			}						
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
	public ProductType getProductType() {
		if(productType == null){
			productType = ProductTypeCatalog.getProductType(propertyBean.getProductType());			
		}
		return productType;
	}
	
	public Map<String, String> getDivisionLineMapping() {
		if(divisionLineMapping==null)
		{
			divisionLineMapping=propertyBean.getLineDivisionMapping();
		}
		return divisionLineMapping;
	}
	
	private ProductDao<?> getProductDao() {
		if (productDao == null) 
			productDao = ProductTypeUtil.getProductDao(getProductType());
		return productDao;
	}
	
	private InProcessProductDao getInProcessProductDao()
	{
		if(inProcessProductDao==null)
		{
			inProcessProductDao=getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	private DivisionDao getDivisionDao()
	{
		if(divisionDao==null)
		{
			divisionDao=getDao(DivisionDao.class);
		}
		return divisionDao;
	}
	
	private LineDao getLineDao()
	{
		if(lineDao==null)
		{
			lineDao=getDao(LineDao.class);
		}
		return lineDao;
	}
	
	private ProductCarrierDao getProductCarrierDao()
	{
		if(productCarrierDao==null)
		{
			productCarrierDao=getDao(ProductCarrierDao.class);
		}
		return productCarrierDao;
	}
	
	private ProcessPointDao getProcessPointDao()
	{
		if(processPointDao==null)
		{
			processPointDao=getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	private ProductResultDao getProductResultDao() {
		if (productResultDao == null) {
			productResultDao = getDao(ProductResultDao.class);
		}
		return productResultDao;
	}
}