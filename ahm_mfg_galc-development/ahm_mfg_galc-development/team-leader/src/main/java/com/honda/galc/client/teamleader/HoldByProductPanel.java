package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.data.HoldByNonProductType;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.service.utils.ProductTypeUtil;

/** * * 
 * @version 0.1 
 * @author Gangadhararao Gadde 
 * @since Mar 27, 2013
 */
public class HoldByProductPanel extends HoldReleaseProductBasePanel 
{


	private static final long serialVersionUID = 1L;
	
	public HoldByProductPanel(TabbedMainWindow mainWindow) {
		super("Hold Product", KeyEvent.VK_H,mainWindow);	
		initHoldScreenComponents();
	}

	
	public void initHoldScreenComponents() 
	{
		getHoldReleaseInformationLabel().setText("Hold Information");
		ButtonGroup group = new ButtonGroup();
		getHoldReleaseInformationPanel().add(getHoldNowCheckBox(), getHoldNowCheckBox().getName());
		group.add(getHoldNowCheckBox());
		getHoldReleaseInformationPanel().add(getHoldShippingCheckBox(), getHoldShippingCheckBox().getName());
		group.add(getHoldShippingCheckBox());
		getHoldReleaseButton().setText("Hold");
		getSnInput1Panel().setBounds(235, 72, 249, 25);
		getSnInput2Panel().setBounds(553, 72, 249, 25);
		getJLabelUnitsHold().setText("Units to Hold");
		getArrowLabel().setBounds(497, 77, 45, 14);
		getStartLabel().setBounds(234, 42, 59, 20);
		getStopLabel().setBounds(553, 42, 59, 20);
		getHoldReleaseByComboBox().getLabel().setText("Hold By:");
		getMainPanel().add(getCountTextField(), getCountTextField().getName());
		getCountTextField().getComponent().addKeyListener(this);
		initComponents();
		addListeners();
		getHoldReasons();
	}
	
	@Override
	public void addListeners(){
		super.addListeners();
		
		getHoldNowCheckBox().addItemListener(this);
		getHoldShippingCheckBox().addItemListener(this);
	}


	public LabeledTextField getCountTextField() {
		if(countTextField == null){
			countTextField = new LabeledTextField("Count", false);
			countTextField.setUpperCaseField(true);
			countTextField.setBackground(new java.awt.Color(192,192,192));
			countTextField.getLabel().setForeground(java.awt.Color.black);
			countTextField.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			countTextField.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			countTextField.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			countTextField.getComponent().setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			countTextField.getComponent().setBackground(java.awt.Color.white);
			countTextField.getComponent().setDisabledTextColor(java.awt.Color.black);
			countTextField.getComponent().setFont(new java.awt.Font("dialog", 0, 14));
			countTextField.getComponent().setEditable(true);
			countTextField.setBounds(850, 35, 60, 75);		
			countTextField.getComponent().setEnabled(true);		
		}
		return countTextField;
	}

	private javax.swing.JCheckBox getHoldNowCheckBox() {
		if (holdNowCheckBox == null) {
			holdNowCheckBox = new javax.swing.JCheckBox();
			holdNowCheckBox.setName("JCheckBoxHoldNow");
			holdNowCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
			holdNowCheckBox.setText("Dept. Hold");
			holdNowCheckBox.setBounds(382, 63, 97, 22);
			holdNowCheckBox.setEnabled(false);				
		}
		return holdNowCheckBox;
	}

	private javax.swing.JCheckBox getHoldShippingCheckBox() {
		if (holdShippingCheckBox == null) {
			holdShippingCheckBox = new javax.swing.JCheckBox();
			holdShippingCheckBox.setName("JCheckBoxHoldShipping");
			holdShippingCheckBox.setFont(new java.awt.Font("dialog", 0, 14));
			holdShippingCheckBox.setText("Hold At Shipping");
			holdShippingCheckBox.setBounds(541, 63, 166, 22);
			holdShippingCheckBox.setEnabled(false);
		}
		return holdShippingCheckBox;
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getHoldReleaseButton()) 		
			holdButtonActionPerformed(e);			
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 			
			holdByComboBoxActionPerformed(e);			
		if (e.getSource() == getAddButton()) 			
			addButtonActionPerformed(e);			
		if (e.getSource() == getSnInput1Panel().getSNTextField()) 			
			snInputPanel1ActionPerformed(e);			
		if (e.getSource() == getSnInput2Panel().getSNTextField()) 			
			snInputPanel2ActionPerformed(e);			
		if (e.getSource() == getExcludeButton()) 			
			excludeButtonActionPerformed(e);			
		if (e.getSource() == getCancelButton()) 			
			cancelButtonActionPerformed(e);			
		if (e.getSource() == getSnInput1Panel().getTextFieldSN()) 						
			confirmSN(getSnInput1Panel().getTextFieldSN().getText());							
		if (e.getSource() == getSnInput2Panel().getTextFieldSN())			
			confirmSN(getSnInput2Panel().getTextFieldSN().getText());				

	}
	
	@Override
	public void focusLost(java.awt.event.FocusEvent e) {
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 
			FocusLost(e);
		if (e.getSource() == getSnInput1Panel().getSNTextField()) 
			FocusLost(e);
		if (e.getSource() == getSnInput2Panel().getSNTextField()) 
			FocusLost(e);
	}
	
	@Override
	public void itemStateChanged(java.awt.event.ItemEvent e) {
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 
			holdByComboBoxItemStateChanged(e);
		if (e.getSource() == getHoldReleaseReasonComboBox().getComponent()) 
			holdReasonComboBoxItemStateChanged(e);
		if (e.getSource() == getHoldNowCheckBox() || e.getSource() == getHoldShippingCheckBox())
			holdInfoCheckboxTypeStateChanged(e);
	}
	
	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		if (e.getSource() == getPhoneExtTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getPagerTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getAssociateNameTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getCountTextField().getComponent()) 
			countDataChanged(null);
	}



	private void changeHoldView(){

		setErrorMessage(null);
		getSnInput1Panel().getTextFieldSN().setBackground(Color.white);
		getSnInput2Panel().getTextFieldSN().setBackground(Color.white);
		getCountTextField().getComponent().setBackground(Color.white);   
		if (getHoldReleaseByComboBox().getComponent().getSelectedIndex() < 1)
		{
			setErrorMessage("Select the required hold type");
			changeViewHoldDisabled();
			return;
		}
		else if(getSnInput1Panel().getTextFieldSN().getText().equals(""))
		{
			setErrorMessage("Start Product not selected");
			changeViewHoldDisabled();
			return;
		}else if(getSnInput2Panel().getTextFieldSN().getText().equals(""))
		{
			setErrorMessage("Stop Product not selected");
			changeViewHoldDisabled();
			return;
		}else if(getCountTextField().getComponent().getText().equals(""))
		{
			setErrorMessage("Count not entered");
			changeViewHoldDisabled();
			return;
		}else if(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
		{
			if(getSnInput1Panel().getTextFieldSN().getText().length()< 4 || getSnInput1Panel().getTextFieldSN().getText().length()< 4)
			{
				setErrorMessage("Please enter AfOn Sequence Number in 4 digit format");
				changeViewHoldDisabled();
				return;
			}
		}
		
		boolean checkCountResult; 
		try{
			checkCountResult = checkCount();
		}catch(Exception ex){
			setErrorMessage("Please verify the format for the Start and Stop product.");
			changeViewHoldDisabled();
			return;
		}

		if (checkCountResult) 
		{
			if (checkYear()) 
			{
				if (!checkHoldModelTypes())
				{
					if(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName())||getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
					{						
						JOptionPane.showMessageDialog(getMainWindow(), "Based on selected criteria we found Products that span across multiple model types.Please use the Exclude button to exclude a product from the list", "Validation Error", JOptionPane.ERROR_MESSAGE);						      										
						changeViewHoldEnabled();
					}     			
					else
					{   							
						changeViewHoldDisabled();
						setErrorMessage("Error - Product ID Range Spans across Multiple Models.");
						getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
						getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
						getCountTextField().getComponent().setBackground(Color.red);
					}
				}
				else 
				{
					changeViewHoldEnabled();
					if (!getHoldReleaseReasonComboBox().getComponent().isEnabled())
					{
						getHoldReleaseReasonComboBox().getComponent().setEnabled(true);
					}                
					getHoldReleaseReasonComboBox().getComponent().requestFocus();

				}
			} else 
			{
				changeViewHoldDisabled();
				setErrorMessage("First Part of Start/Stop not the same");
				getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
				getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
			}
		} else 
		{
			changeViewHoldDisabled();
			if(countCheck ==1 )
			{
				countCheck = 0;				
				JOptionPane.showMessageDialog(getMainWindow(), "ERROR: CANNOT HOLD MORE THAN 100 VINS \n Click OK and Modify your Input", "Validation Error", JOptionPane.ERROR_MESSAGE);
			}else 
			{				
				setErrorMessage("Error - Count does not match difference.");
				getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
				getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
			}			
			getCountTextField().getComponent().setBackground(Color.red);
		}
		
		if ( getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName()) 
				&& !checkFrameProductsAllowed()){
			setErrorMessage("One or more products are shipped and is not allowed to hold them. "
							+ "List of products already shipped: " + productIDsDisallowedShipped.toString());
			changeViewHoldDisabled();
			return;
		}

	}

	private DefaultTableModel createHoldModel()
	{
		defaultTableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		defaultTableModel.addColumn("Product ID");
		defaultTableModel.addColumn("MTOC");
		defaultTableModel.addColumn("Production Date");
		defaultTableModel.addColumn("Production Lot");
		return defaultTableModel;

	}

	private boolean checkHoldModelTypes() {
		try {
			List<Object[]> productsList=null;
			if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
			{				
				productsList = getDao(FrameDao.class).getProductsWithinAfOnSeqRange(Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText()), Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText()));

			}else
			{
				ProductType selectedProductType=ProductNumberDef.getProductNumberDefs(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString()).get(0).getProductType();				
				productsList = ProductTypeUtil.getProductDao(selectedProductType).getProductsWithinRange(getSnInput1Panel().getTextFieldSN().getText(), getSnInput2Panel().getTextFieldSN().getText());			
			}
			boolean uniqueModel=true;
			defaultTableModel = createHoldModel();

			for (Object[] productData : productsList)
			{
				Vector<String> product = new Vector<String>();
				String mtoc=null;
				if(productData[0]!=null)
				{
					product.add((String)productData[0]);

				}
				if(productData[1]!=null)
				{
					if(mtoc!=null&&!(uniqueModel==false))
					{
						if(!(mtoc.equals((String)productData[1])))
						{
							uniqueModel=false;
						}
					}
					mtoc=(String)productData[1];
					product.add((String)productData[1]);	
				}
				if(productData[2]!=null)
				{
					product.add((String)productData[2]);	

				}
				if(productData[3]!=null)
				{
					product.add((String)productData[3]);	

				}
				defaultTableModel.addRow(product);
			}															

			getProductsListTable().setModel(defaultTableModel);
			getProductsListTable().repaint();
			getProductsListScrollPane().repaint();

			if(!uniqueModel)
			{
				return false;
			}	

		}catch (Exception e) {
			handleException(e);
			return false;
		} 
		return true;
	}

	private void changeViewHoldDisabled() {

		changeViewHoldReleaseDisabled();
		getHoldNowCheckBox().setSelected(false);
		getHoldNowCheckBox().setEnabled(false);

		getHoldShippingCheckBox().setSelected(false);
		getHoldShippingCheckBox().setEnabled(false);
		

	}

	private void changeViewHoldEnabled() {
		
		changeViewHoldReleaseEnabled();		  
		getHoldNowCheckBox().setEnabled(true);
		getHoldShippingCheckBox().setEnabled(true);

	}

	public boolean checkCount() {

		int intStarChar = 0;
		int intStopChar = 17;


		if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.ENGINE).get(0).getName())) {
			intStarChar = 8;
			intStopChar = 12;

		} else if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName())) {
			intStarChar = 11;
			intStopChar = 17;
		}if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName())) {
			intStarChar = 1;
			intStopChar = 4;
		}

		if((Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				- Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				+ 1 ) != (Integer.parseInt(getCountTextField().getComponent().getText()))){

			return false;

		}
		else{

			if(Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
					- Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
					+ 1 > 100){
				countCheck = 1;
				return false;
			}
			else if(Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
					- Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
					+ 1	<= 0){
				return false;
			}
			else{
				return true;
			}

		}

	}



	public void getHoldReasons() {
		try{		
			List<Object[]> list = getDao(HoldResultDao.class).getHoldReasons();

			if (list.size() > 0) {
				for (Object[] array : list) {
					if(array[0]!=null)
					{
						getHoldReleaseReasonComboBox().getComponent().addItem( ((String) array[0]).trim() );
					}
				}
			}
		}
		catch(Exception e){
			handleException(e);
		}
	}


	public void initializeHoldView() {

		initializeView();
		getHoldNowCheckBox().setEnabled(false);
		getHoldShippingCheckBox().setEnabled(false);    
		getCountTextField().getComponent().setText("");	 
	}

	public void holdButtonActionPerformed(java.awt.event.ActionEvent actionEvent) {

		if (!checkHoldType()){
			setErrorMessage("Please select either Dept. Hold or Hold at Shipping check box");
			return;
		}
		
		if(checkText())
		{
			int ret = JOptionPane.showConfirmDialog(getMainWindow(),"Place the Products on Dept. Hold?", "Are you sure ?",  JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) 
			{
				saveHoldData();
				getHoldReleaseButton().setEnabled(false);
			}			
		}else
		{
			setErrorMessage("Please enter the Associate details");
		}
	}

	public void holdByComboBoxActionPerformed(java.awt.event.ActionEvent actionEvent) 
	{
		holdReleaseByComboBoxActionPerformed();
		changeViewHoldDisabled();		
	}


	public void holdByComboBoxItemStateChanged(java.awt.event.ItemEvent itemEvent) {
		initializeHoldView();

	}


	public void holdReasonComboBoxItemStateChanged(java.awt.event.ItemEvent itemEvent) {
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());

	}
	
	public void holdInfoCheckboxTypeStateChanged(java.awt.event.ItemEvent itemEvent) {
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());
	}

	public void addButtonActionPerformed(java.awt.event.ActionEvent actionEvent){	
		defaultTableModel=new DefaultTableModel();
		getProductsListTable().setModel(defaultTableModel);
		getProductsListTable().repaint();
		changeHoldView();
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());

	}



	public void resetHoldScreen()
	{	
	    resetScreen();
		getHoldNowCheckBox().setEnabled(false);
		getHoldShippingCheckBox().setEnabled(false);
		getCountTextField().getComponent().setText("");	  
	}
	
	public void cancelButtonActionPerformed(java.awt.event.ActionEvent actionEvent){	

		resetHoldScreen();
	}

	

	public void countDataChanged(java.awt.AWTEvent e) {
		if((getSnInput1Panel().getTextFieldSN().getText().trim().length()>0) && (getSnInput2Panel().getTextFieldSN().getText().trim().length()>0))
			getAddButton().setEnabled(true);

	}
	
	@Override
	public void associateDataChanged(java.awt.AWTEvent e) {
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());
	}
	
	public boolean checkHoldType(){
		return getHoldNowCheckBox().isSelected() || getHoldShippingCheckBox().isSelected();
	}



	public boolean saveHoldData() {
		try{		

			setErrorMessage(null);
			Vector data = defaultTableModel.getDataVector();
			for (int i = 0; i < getProductsListTable().getRowCount(); i++)
			{
				Vector row = (Vector) data.elementAt(i);
				String productId =(String)row.get(0);
				HoldResultId id=new HoldResultId();
				HoldResult holdResult=new HoldResult();
				id.setProductId(productId.trim());
				Date currentDate=new Date();
				id.setActualTimestamp(new Timestamp(currentDate.getTime()));
				if(getHoldNowCheckBox().isSelected())
					id.setHoldType(0);
				else if(getHoldShippingCheckBox().isSelected())
					id.setHoldType(1);
				else
				{
					setErrorMessage("Please select either Dept. Hold or Hold at Shipping check box");
					return false ;
				}
				if(getHoldReleaseReasonComboBox().getComponent().getSelectedItem().toString().trim().length()==0)
				{
					setErrorMessage("Please enter or select the Hold Reason");
					return false ;
				}
				holdResult.setId(id);
				holdResult.setHoldAssociateName(getAssociateNameTextField().getComponent().getText());
				holdResult.setHoldAssociatePager(getPagerTextField().getComponent().getText());
				holdResult.setHoldAssociatePhone(getPhoneExtTextField().getComponent().getText());
				holdResult.setHoldAssociateNo(getAssociateNoTextField().getComponent().getText());
				holdResult.setHoldReason(getHoldReleaseReasonComboBox().getComponent().getSelectedItem().toString().trim().toUpperCase());
				holdResult.setReleaseFlag((short)0);
				if(row.get(1)!=null)
				{
					holdResult.setProductSpecCode(row.get(1).toString());
				}
				if(row.get(2)!=null)
				{
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(row.get(2).toString());
					holdResult.setProductionDate(new java.sql.Date(date.getTime()));
				}			
				if(row.get(3)!=null)
				{
					holdResult.setProductionLot(row.get(3).toString());	
				}
				holdResult.setLotHoldStatus(0);
				getDao(HoldResultDao.class).save(holdResult);
				logUserAction(SAVED, holdResult);

			}
			resetHoldScreen();
			return true;
		}
		catch(Exception e){
			handleException(e);
			return false;
		}

	}

	public void snInputPanel1ActionPerformed(java.awt.event.ActionEvent e) {
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());	

	}

	public void snInputPanel2ActionPerformed(java.awt.event.ActionEvent e) {
		getHoldReleaseButton().setEnabled(checkText() && checkHoldType());

	}
}